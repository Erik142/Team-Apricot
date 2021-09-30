import { Collection, Interaction } from "discord.js";
import { DiscordBot } from "../../core/DiscordBot";
import { SlashCommandHandler } from "../../core/SlashCommandHandler";
import { Command } from "../../interfaces/Command";
import { EventExecutor } from "../../interfaces/Event";
import { CommandFileReader } from "../../util/CommandFileReader";

let commands: Collection<String, Command>
let slashCommandHandler: SlashCommandHandler

export const executor: EventExecutor = async (client: DiscordBot, interaction: Interaction) => {
    if (!interaction.isCommand()) {
        return;
    }

    if (!commands) {
        const commandFileReader = new CommandFileReader()
        await commandFileReader.setupCommands()
        commands = commandFileReader.getCommands()
    }

    if (!slashCommandHandler) {
        slashCommandHandler = new SlashCommandHandler(commands)
    }

    slashCommandHandler.handle(client, interaction)
}

export const name: string = 'interactionCreate'