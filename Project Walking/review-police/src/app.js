require('dotenv').config()
const fs = require('fs')
const path = require('path')
const { Client, Intents } = require('discord.js');
const { REST } = require('@discordjs/rest');
const { Routes } = require('discord-api-types/v9');
const CommandBuilder = require('./commandbuilder')
const GitHubApi = require('./api/github_pull')
const acceptCommand = require('./commands/accept');
const accept = require('./commands/accept');
const show = require('./commands/show')
const usermappings = require('./user-mappings')

const client = new Client({ intents: [Intents.FLAGS.GUILDS] });

token = process.env.DISCORD_TOKEN
clientId = process.env.DISCORD_CLIENT_ID
guildId = process.env.DISCORD_GUILD_ID


client.on('ready', () => {
    console.log(`Logged in as ${client.user.tag}!`);
});

client.on('interactionCreate', async interaction => {
    if (interaction.isCommand()) {
        let responses = [];
        let isEphemeral = false;
        let user = interaction.user
        let githubUsername

        switch (interaction.commandName) {
            case 'show':
                isEphemeral = true
                let type = interaction.options.getString('type')

                if (!type) {
                    type = 'all'
                }

                if (interaction.options.getUser('user')) {
                    user = interaction.options.getUser('user')
                    isEphemeral = false
                }

                githubUsername = usermappings.getGitHubUsername(user.id)

                switch (type) {
                    case 'all':
                        responses = await show.showAll(githubUsername)
                        break;
                    case 'accepted':
                        responses = await show.showAccepted(githubUsername)
                        break;
                    case 'unaccepted':
                        responses = await show.showUnaccepted(githubUsername)
                        break;
                }
                console.log('Response: ' + response)
                break;
            case 'accept':
                let pullRequestNumber = interaction.options.getInteger('pr-number')
                githubUsername = usermappings.getGitHubUsername(user.id)

                responses = await accept(githubUsername, pullRequestNumber)
                break;
        }

        if (responses.length > 0) {
            interaction.reply({ embeds: responses, ephemeral: isEphemeral })
        }
    }
});

client.login(token).then(result => {
    const rest = new REST({ version: '9' }).setToken(token);

    commands = CommandBuilder.buildCommands()

    rest.put(Routes.applicationGuildCommands(clientId, guildId), { body: commands })
        .then(() => console.log('Successfully registered application commands.'))
        .catch(console.error);
});


GitHubApi.initialize().then(status => {
    console.log("GitHub app initialized")
});