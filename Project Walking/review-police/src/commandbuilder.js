const { SlashCommandBuilder } = require('@discordjs/builders');
const { REST } = require('@discordjs/rest');
const { Routes } = require('discord-api-types/v9');

module.exports.buildCommands = function () {
    return [buildShowCommand(), buildAcceptCommand()].map(command => command.toJSON())
}

function buildShowCommand() {
    return new SlashCommandBuilder().setName('show').setDescription('Show open PRs for which you have been assigned as a reviewer').addUserOption(option => option.setName('user').setDescription('Show open PRs for which the specified user has been assigned as a reviewer')).addStringOption(option => option.addChoice('All', 'all').addChoice('Accepted', 'accepted').addChoice('Unaccepted', 'unaccepted').setName('type').setDescription('Use \'All\', \'Unaccepted\' or \'Accepted\' to show the corresponding review requests (defaults to \'All\').'))
}

function buildAcceptCommand() {
    return new SlashCommandBuilder().setName('accept').setDescription('Accept the review request for a particular PR in GitHub').addIntegerOption(option => option.setName('pr-number').setDescription('The number for the pull request that you want to accept the review request for: #num').setRequired(true))
}

