import fs from 'fs'
import path from 'path'
import { GitHubApi } from '../core/GitHubApi';

interface UserMap {
    discordId: string,
    githubId: string
}

export abstract class UserMapper {
    private static usermaps: Array<UserMap> = new Array()

    static getGithubUsername(discordId: string): string {
        let returnVal: string = "";

        UserMapper.usermaps.forEach((mapping: UserMap) => {
            if (mapping.discordId.trim() == discordId.trim()) {
                returnVal = mapping.githubId
            }
        })

        return returnVal
    }

    static getDiscordId(gitHubUsername: string): string {
        let returnVal: string = ""

        UserMapper.usermaps.forEach((mapping: UserMap) => {
            if (mapping.githubId == gitHubUsername) {
                returnVal = mapping.discordId
            }
        })

        return returnVal
    }

    static readMaps(): void {
        let maps: Array<UserMap> = JSON.parse(fs.readFileSync(path.join(__dirname, '../../account-mappings.json'), 'utf-8')).mappings

        UserMapper.usermaps = maps
    }
}