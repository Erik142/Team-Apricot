const { createAppAuth } = require("@octokit/auth-app");
const { Octokit } = require("@octokit/core");

let octokit = null;

module.exports.initialize = async function () {
    octokit = new Octokit({
        authStrategy: createAppAuth,
        auth: {
            appId: 141501,
            privateKey: process.env.GH_PRIVATE_KEY.replace(/\\n/g, '\n'),
            installationId: process.env.GH_INSTALLATION_ID
        },
    });
}

module.exports.getPullRequests = async function () {

    response = await octokit.request('GET /repos/{owner}/{repo}/pulls', {
        owner: 'Erik142',
        repo: 'Team-Apricot',
        state: 'open'
    });

    return response['data']
}

module.exports.getPullRequest = async function (pullNumber) {
    try {
        response = await octokit.request('GET /repos/{owner}/{repo}/pulls/{pull_number}', {
            owner: 'Erik142',
            repo: 'Team-Apricot',
            pull_number: pullNumber
        });

        if (response.status == 200) {
            return response['data'];
        }
        else {
            return null
        }
    } catch {
        return null
    }
}

module.exports.getReviewRequests = async function (username) {
    let pullRequests = await this.getPullRequests()

    let reviewRequests = []

    pullRequests.forEach(pullRequest => {
        let reviewers = pullRequest['requested_reviewers']

        reviewers.forEach(reviewer => {
            if (reviewer['login'] == username) {
                reviewRequests.push(pullRequest)
            }
        })
    })

    return reviewRequests
}

module.exports.deleteReviewers = async function (reviewers, pullRequestNumber) {
    let response = await octokit.request('DELETE /repos/{owner}/{repo}/pulls/{pull_number}/requested_reviewers', {
        owner: 'Erik142',
        repo: 'Team-Apricot',
        pull_number: pullRequestNumber,
        reviewers: reviewers
    })

    return response['status'] == 200
}

module.exports.getUnacceptedReviewRequests = async function (username) {
    let pullRequests = await this.getPullRequests()

    let reviewRequests = []

    pullRequests.forEach(pullRequest => {
        let reviewers = pullRequest['requested_reviewers']

        if (reviewers.length > 1) {
            reviewers.forEach(reviewer => {
                if (reviewer['login'] == username) {
                    reviewRequests.push(pullRequest)
                }
            })
        }
    })

    return reviewRequests
}

module.exports.getAcceptedReviewRequests = async function (username) {
    let pullRequests = await this.getPullRequests()

    let reviewRequests = []

    pullRequests.forEach(pullRequest => {
        let reviewers = pullRequest['requested_reviewers']

        if (reviewers.length == 1) {
            reviewers.forEach(reviewer => {
                if (reviewer['login'] == username) {
                    reviewRequests.push(pullRequest)
                }
            })
        }
    })

    return reviewRequests
}