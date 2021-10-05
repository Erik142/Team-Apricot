import { DiscordBot } from './core/DiscordBot'
import { Config } from './interfaces/Config'
import { UserMapper } from './util/UserMapper'
import { GitHubApi } from './core/GitHubApi'
import { GitHubWebHooks } from './core/GitHubWebHooks'
import { EnvConfig } from './core/EnvConfig'

let config: Config = EnvConfig.getConfig()
GitHubApi.setConfig(config)
UserMapper.readMaps()

const bot: DiscordBot = new DiscordBot(config)
bot.start()

const githubWebHooks = new GitHubWebHooks()