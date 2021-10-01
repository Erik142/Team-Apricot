import { Collection, Interaction } from "discord.js";
import { DiscordBot } from "../../core/DiscordBot";
import { SlashCommandHandler } from "../../core/SlashCommandHandler";
import { Command } from "../../interfaces/Command";
import { EventExecutor } from "../../interfaces/Event";
import { SelectMenuResponse } from "../../interfaces/SelectMenuResponse";
import { CommandFileReader } from "../../util/CommandFileReader";
import { SelectMenuResponseReader } from "../../util/SelectMenuResponseReader";

let commands: Collection<String, Command>
let selectMenuResponders: Collection<String, SelectMenuResponse>
let slashCommandHandler: SlashCommandHandler

export const executor: EventExecutor = async (client: DiscordBot, interaction: Interaction) => {
    if (!interaction.isCommand() && !interaction.isSelectMenu()) {
        return;
    }

    if (interaction.isCommand()) {
        if (!commands) {
            const commandFileReader = new CommandFileReader()
            await commandFileReader.setupCommands()
            commands = commandFileReader.getCommands()
        }

        if (!slashCommandHandler) {
            slashCommandHandler = new SlashCommandHandler(commands)
        }

        slashCommandHandler.handle(client, interaction)
    } else if (interaction.isSelectMenu()) {
        if (!selectMenuResponders) {
            const selectMenuResponseReader = new SelectMenuResponseReader()
            await selectMenuResponseReader.setupSelectMenuResponses()
            selectMenuResponders = selectMenuResponseReader.getCommands()
        }

        const selectMenuResponse: SelectMenuResponse = selectMenuResponders.get(interaction.customId)
        selectMenuResponse.executor(client, interaction);
    }
}

export const name: string = 'interactionCreate'