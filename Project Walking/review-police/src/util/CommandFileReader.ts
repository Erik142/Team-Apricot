import { Collection } from "discord.js"
import glob from "glob"
import { promisify } from "util"
import { Command } from "../interfaces/Command"

const globPromise = promisify(glob)

export class CommandFileReader {
    private commands: Collection<String, Command> = new Collection()

    async setupCommands(): Promise<void> {
        const commandFiles: string[] = await globPromise(`${__dirname}/../commands/**/*{.ts,.js}`)

        await Promise.all(commandFiles.map(async (value: string) => {
            const file: Command = await import(value);
            this.commands.set(file.name, file);
        }))
    }

    getCommands(): Collection<String, Command> {
        return this.commands;
    }
}