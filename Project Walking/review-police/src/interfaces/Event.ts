import { DiscordBot } from "../core/DiscordBot";

export interface EventExecutor {
    (bot: DiscordBot, ...args: any[]): Promise<void>
}

export interface Event {
    name: string,
    executor: EventExecutor
}