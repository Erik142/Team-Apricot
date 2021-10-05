require('dotenv').config()
import { Config } from "../interfaces/Config";

export abstract class EnvConfig {
    static getConfig(): Config {
        const config: Config = {
            discordClientId: process.env.DISCORD_CLIENT_ID,
            discordGuildId: process.env.DISCORD_GUILD_ID,
            discordToken: process.env.DISCORD_TOKEN,
            gitHubInstallationId: process.env.GH_INSTALLATION_ID,
            gitHubPrivateRSAKey: process.env.GH_PRIVATE_KEY
        }

        return config;
    }
}