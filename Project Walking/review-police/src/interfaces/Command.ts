import { DiscordBot } from '../core/DiscordBot'
import { CommandInteraction, Interaction, Message } from 'discord.js'
import { SlashCommandBuilder, } from '@discordjs/builders'

export interface CommandExecutor {
    (client: DiscordBot, interaction: CommandInteraction): Promise<void>
}

export interface Command {
    name: String,
    builder: SlashCommandBuilder,
    executor: CommandExecutor
}