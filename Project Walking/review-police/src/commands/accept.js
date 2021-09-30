const { bold, italic, strikethrough, underscore, spoiler, quote, blockQuote, hyperlink, hideLinkEmbed, userMention } = require('@discordjs/builders');
const { MessageEmbed } = require('discord.js')
const gitHubPullApi = require('../api/github_pull')
const usermappings = require('../user-mappings')

module.exports = async function (username, pullRequestNumber) {
    let reviewRequests = await gitHubPullApi.getReviewRequests(username)
    let pullRequest = await gitHubPullApi.getPullRequest(pullRequestNumber)

    if (!pullRequest) {
        return [new MessageEmbed().setTitle('Review Police court duty').setDescription(`I don't know what you're going on about... Come on, get on out of here! The pull request with number ${pullRequestNumber} does not exist!`)]
    }

    let reviewRequested = false
    let deleteSuccessfull = false
    let success = false
    let alreadyAssigned = false

    remainingReviewers = []

    for (i = 0; i < reviewRequests.length; i++) {
        if (reviewRequests[i]['number'] == pullRequestNumber) {
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
            console.log("Is already assigned")
            alreadyAssigned = true
        }
    }

    success = reviewRequested && deleteSuccessfull
    let description = ""

    if (alreadyAssigned) {
        description = "Trying to work double shifts, eh? You have already accepted this request review, try another one."
        console.log("Correct if clause")
    } else if (success) {
        description = ""

        remainingReviewers.forEach(reviewer => {
            let discordId = usermappings.getDiscordId(reviewer)
            description += `${userMention(discordId)} `
        })

        description = description.trim()

        let userDiscordId = usermappings.getDiscordId(username)

        description += `: ${userMention(userDiscordId)} has bailed you out of review duty. I'll catch you next time 🚓`
    } else if (!reviewRequested) {
        description = "Trying to bail your friends out of review duty, huh? Not gonna happen this time. Your review has not been requested for this pull request, try another one."
    } else if (!deleteSuccessfull && reviewRequested) {
        description = "Your friends could not be bailed out of review duty this time. Something went wrong when trying to remove other reviewers from the pull request. Try again or do it manually in GitHub."
    }

    return [new MessageEmbed().setTitle("Review Police: Court Duty").setDescription(description).setURL(pullRequest['url'])]
}

async function getRemainingReviewers(username, pullRequestNumber) {
    let pullRequest = await gitHubPullApi.getPullRequest(pullRequestNumber)

    let reviewers = []

    pullRequest['requested_reviewers'].forEach(reviewer => {
        if (reviewer['login'] != username) {
            reviewers.push(reviewer['login'])
        }
    })

    return reviewers
}

async function deleteRemainingReviewers(username, pullRequestNumber) {
    let reviewers = await getRemainingReviewers(username, pullRequestNumber)

    return await gitHubPullApi.deleteReviewers(reviewers, pullRequestNumber)
}