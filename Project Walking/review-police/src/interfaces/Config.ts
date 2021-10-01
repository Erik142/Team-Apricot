export interface Config {
    discordToken: string,
    discordChannelId: string,
    discordClientId: string,
    discordGuildId: string,
    gitHubPrivateRSAKey: string,
    gitHubInstallationId: string,
    gitHubSecret: string,
    gitHubProductOwner: string,
    smeeIoUrl: string,
    appMode: GitHubWebHookMode
}