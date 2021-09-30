import { Collection, CommandInteraction, Interaction, Message } from "discord.js";
import { Command } from "../interfaces/Command";
import { DiscordBot } from "./DiscordBot";

export class SlashCommandHandler {
    private interactions: Collection<String, Command> = new Collection();

    public constructor(interactions: Collection<String, Command>) {
        this.interactions = interactions;
    }

    public handle(client: DiscordBot, interaction: CommandInteraction) {
        const command: Command = this.interactions.get(interaction.commandName) as Command;
        command.executor(client, interaction)
    }
}