const { bold, italic, strikethrough, underscore, spoiler, quote, blockQuote, hyperlink, hideLinkEmbed, userMention } = require('@discordjs/builders');
const { MessageEmbed } = require('discord.js')
const gitHubPullApi = require('../api/github_pull')

module.exports.showAll = async function (username) {
    console.log(username)
    let reviewRequests = await gitHubPullApi.getReviewRequests(username)
    return formatReviewRequests(reviewRequests, 'all')
}

module.exports.showAccepted = async function (username) {
    let reviewRequests = await gitHubPullApi.getAcceptedReviewRequests(username)
    return formatReviewRequests(reviewRequests, 'accepted')
}

module.exports.showUnaccepted = async function (username) {
    let reviewRequests = await gitHubPullApi.getUnacceptedReviewRequests(username)
    return formatReviewRequests(reviewRequests, 'unaccepted')
}

function formatReviewRequests(reviewRequests, formatType) {
    //const boldString = bold(string);
    //const italicString = italic(string);
    //const strikethroughString = strikethrough(string);
    //const underscoreString = underscore(string);
    //const spoilerString = spoiler(string);
    //const quoteString = quote(string);
    //const blockquoteString = blockQuote(string);

    let formattedMessages = [];

    if (reviewRequests.length === 0) {
        let formattedMessage = "You are free to leave, for now... You have no "

        if (formatType === 'accepted' || formatType === 'unaccepted') {
            formattedMessage += formatType
        }

        formattedMessage += " review requests. I will catch you later 🚓"

        formattedMessages.push(new MessageEmbed().setTitle('Show review requests').setDescription(formattedMessage))

        return formattedMessages;
    }

    reviewRequests.forEach(reviewRequest => {
        formattedMessages.push({ name: `#${reviewRequest['number']}: ${reviewRequest['title']}`, value: hyperlink('link', reviewRequest['html_url']) })
    })

    return [new MessageEmbed().setTitle('Show review requests').addFields(formattedMessages)]
}