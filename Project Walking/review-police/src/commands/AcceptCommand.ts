import { CommandInteraction, Interaction, InteractionReplyOptions, MessageEmbed } from "discord.js";
import { userMention } from '@discordjs/builders'
import { SlashCommandBuilder } from '@discordjs/builders'
import { CommandExecutor } from "../interfaces/Command";
import { PullRequest } from "../model/PullRequest";
import { GitHubApi, ReviewRequest } from "../core/GitHubApi";
import { UserMapper } from "../util/UserMapper";

function getBuilder(): SlashCommandBuilder {
    const builder: SlashCommandBuilder = new SlashCommandBuilder()
    builder.setName('accept')
    builder.setDescription('Accept the review request for a particular PR in GitHub')

    builder.addIntegerOption(option => option.setName('pr-number').setDescription('The number for the pull request that you want to accept the review request for: #num').setRequired(true));

    return builder;
}

async function getRemainingReviewers(username: string, pullRequestNumber: number): Promise<Array<ReviewRequest>> {
    const pullRequest: PullRequest = await GitHubApi.getPullRequest(pullRequestNumber)

    const reviewers: Array<ReviewRequest> = new Array()

    pullRequest.reviewRequests.forEach(reviewRequest => {
        if (reviewRequest.reviewer != username) {
            reviewers.push(reviewRequest)
        }
    })

    return reviewers
}

async function deleteRemainingReviewers(username: string, pullRequestNumber: number): Promise<boolean> {
    const reviewers: Array<ReviewRequest> = await getRemainingReviewers(username, pullRequestNumber)

    return await GitHubApi.deleteReviewers(pullRequestNumber, reviewers.map(x => x.reviewer))
}

export const executor: CommandExecutor = async (client, interaction: CommandInteraction) => {
    const pullRequestNumber = interaction.options.getInteger('pr-number')
    const user = interaction.user
    const username = UserMapper.getGithubUsername(user.id)

    const reviewRequests: Array<ReviewRequest> = await GitHubApi.getAllReviewRequests(username)
    const pullRequest: PullRequest = await GitHubApi.getPullRequest(pullRequestNumber)

    if (!pullRequest) {
        const reply: InteractionReplyOptions = { embeds: [new MessageEmbed().setTitle('Review Police court duty').setDescription(`I don't know what you're going on about... Come on, get on out of here! The pull request with number ${pullRequestNumber} does not exist!`)] }
        interaction.reply(reply)
        return;
    }

    let reviewRequested = false
    let deleteSuccessfull = false
    let success = false
    let alreadyAssigned = false

    let remainingReviewers: Array<ReviewRequest> = new Array()

    for (let i = 0; i < reviewRequests.length; i++) {
        if (reviewRequests[i].pullNumber == pullRequestNumber) {
            reviewRequested = true;
            break;
        }
    }

    if (reviewRequested) {
        remainingReviewers = await getRemainingReviewers(username, pullRequestNumber)

        console.log(`Remaining reviewer count: ${remainingReviewers.length}`)

        if (remainingReviewers.length > 1) {
            deleteSuccessfull = await deleteRemainingReviewers(username, pullRequestNumber)
        }
        else {
            alreadyAssigned = true
        }
    }

    success = reviewRequested && deleteSuccessfull
    let description = ""

    if (alreadyAssigned) {
        description = "Trying to work double shifts, eh? You have already accepted this request review, try another one."
    } else if (success) {
        description = ""

        remainingReviewers.forEach(reviewRequest => {
            let discordId = UserMapper.getDiscordId(reviewRequest.reviewer)
            description += `${userMention(discordId)} `
        })

        description = description.trim()

        let userDiscordId = UserMapper.getDiscordId(username)

        description += `: ${userMention(userDiscordId)} has bailed you out of review duty. I'll catch you next time 🚓`
    } else if (!reviewRequested) {
        description = "Trying to bail your friends out of review duty, huh? Not gonna happen this time. Your review has not been requested for this pull request, try another one."
    } else if (!deleteSuccessfull && reviewRequested) {
        description = "Your friends could not be bailed out of review duty this time. Something went wrong when trying to remove other reviewers from the pull request. Try again or do it manually in GitHub."
    }

    const reply: InteractionReplyOptions = { embeds: [new MessageEmbed().setTitle("Review Police: Court Duty").setDescription(description).setURL(pullRequest.url)] }
    interaction.reply(reply)
}

export const builder: SlashCommandBuilder = getBuilder()

export const name: string = 'accept'