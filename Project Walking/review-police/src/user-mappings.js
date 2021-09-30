const fs = require('fs')
const path = require('path')

module.exports.mappings = JSON.parse(fs.readFileSync(path.join(__dirname, '../account-mappings.json'), 'utf-8')).mappings

module.exports.getGitHubUsername = function (discordId) {
    let returnVal = null;

    this.mappings.forEach(mapping => {
        if (mapping.discordId.trim() == discordId.trim()) {
            returnVal = mapping.githubId
        }
    })

    return returnVal
}

module.exports.getDiscordId = function (githubUsername) {
    let returnVal = null

    this.mappings.forEach(mapping => {
        if (mapping.githubId == githubUsername) {
            returnVal = mapping.discordId
        }
    })

    return returnVal
}