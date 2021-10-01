import { Channel, Client, Collection, Intents, Interaction, Message, MessageEmbed, TextChannel } from "discord.js";
import { promisify } from "util";
import { Command } from "../interfaces/Command";
import { Config } from "../interfaces/Config";
import glob from 'glob'
import { Event } from "../interfaces/Event";
import { REST } from "@discordjs/rest";
import { Routes } from "discord-api-types/rest/v9";
import { json } from "stream/consumers";

const globPromise = promisify(glob)

export class DiscordBot extends Client {
    private config: Config;
    private commands: Collection<String, Command> = new Collection()
    private events: Collection<String, Event> = new Collection()

    public constructor(config: Config) {
        super({ intents: [Intents.FLAGS.GUILDS] });

        this.config = config;
    }

    public async start(): Promise<void> {
        await this.setupCommands();
        await this.initEventHandlers();
        await this.login(this.config.discordToken);
    }

    /**
     * Sends a message to the specified channel in the configuration as MessageEmbeds
     * @param embeds The MessageEmbeds to send
     * @returns The message that was sent, as a Message object
     */
    public async sendMessage(embeds: Array<MessageEmbed>): Promise<Message> {
        const channel: Channel = await this.channels.fetch(this.config.discordChannelId)

        if (channel.isText()) {
            const textChannel = channel as TextChannel
            const message: Message = await textChannel.send({ embeds: embeds })
            return message;
        }

        return null;
    }

    /**
     * Loads all the event handlers and sets up the event hooks for each event handler
     */
    private async initEventHandlers(): Promise<void> {
        const eventFiles: string[] = await globPromise(`${__dirname}/../events/**/*{.ts,.js}`)
        await Promise.all(eventFiles.map(async (value: string) => {
            const file: Event = await import(value)
            this.events.set(file.name, file)
            this.on(file.name, file.executor.bind(null, this))
        }))
    }

    private async setupCommands(): Promise<void> {
        const commandFiles: string[] = await globPromise(`${__dirname}/../commands/**/*{.ts,.js}`)
        const jsonCommands: Array<any> = new Array()

        await Promise.all(commandFiles.map(async (value: string) => {
            const file: Command = await import(value)
            this.commands.set(file.name, file)
            jsonCommands.push(file.builder.toJSON())
        }))

        console.log(jsonCommands)

        await this.registerSlashCommands(jsonCommands)
    }

    private async registerSlashCommands(commands: Array<string>): Promise<void> {
        const rest = new REST({ version: '9' }).setToken(this.config.discordToken);
        try {
            await rest.put(Routes.applicationGuildCommands(this.config.discordClientId, this.config.discordGuildId), { body: commands })
            console.log('Successfully registered application commands.')
        } catch (e) {
            console.error(e);
        }
    }
}
