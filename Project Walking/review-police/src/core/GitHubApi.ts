import { createAppAuth } from "@octokit/auth-app";
import { Octokit } from "@octokit/core";
import { Config } from "../interfaces/Config";
import { PullRequest } from "../model/PullRequest";
import { ReviewRequest } from "../model/ReviewRequest";

export abstract class GitHubApi {
    private static repoOwner = 'Erik142'
    private static repoName = 'Team-Apricot'

    private static octokit: Octokit
    private static config: Config

    static setConfig(config: Config) {
        GitHubApi.config = config;
    }

    private static async setup(): Promise<void> {
        if (GitHubApi.octokit) {
            return;
        }

        GitHubApi.octokit = new Octokit({
            authStrategy: createAppAuth,
            auth: {
                appId: 141501,
                privateKey: this.config.gitHubPrivateRSAKey.replace(/\\n/g, '\n'),
                installationId: this.config.gitHubInstallationId
            },
        });
    }

    static async getPullRequests(): Promise<Array<PullRequest>> {
        await GitHubApi.setup()

        const response = await GitHubApi.octokit.request('GET /repos/{owner}/{repo}/pulls', {
            owner: GitHubApi.repoOwner,
            repo: GitHubApi.repoName,
            state: 'open'
        });

        const pullRequests: Array<PullRequest> = new Array()

        response['data'].forEach((singleResponse: any) => {
            pullRequests.push(this.getPullRequestFromResponse(singleResponse))
        })

        return pullRequests;
    }

    static async getPullRequest(pullNumber: number): Promise<PullRequest> {
        await GitHubApi.setup()

        let pullRequest: PullRequest = undefined;

        try {
            const response = await GitHubApi.octokit.request('GET /repos/{owner}/{repo}/pulls/{pull_number}', {
                owner: GitHubApi.repoOwner,
                repo: GitHubApi.repoName,
                pull_number: pullNumber
            });

            if (response.status == 200) {
                pullRequest = this.getPullRequestFromResponse(response['data']);
            }
        } catch {

        }

        return pullRequest;
    }

    static async getAllReviewRequests(username: string): Promise<Array<ReviewRequest>> {
        await GitHubApi.setup()

        let pullRequests: Array<PullRequest> = await GitHubApi.getPullRequests()
        let reviewRequests: Array<ReviewRequest> = []

        pullRequests.forEach(pullRequest => {
            let reviews: Array<ReviewRequest> = pullRequest.reviewRequests

            reviews.forEach((reviewRequest: ReviewRequest) => {
                if (reviewRequest.reviewer == username) {
                    reviewRequests.push(reviewRequest)
                }
            })
        })

        return reviewRequests
    }

    static async getAcceptedReviewRequests(username: string): Promise<Array<ReviewRequest>> {
        await GitHubApi.setup()

        let pullRequests: Array<PullRequest> = await GitHubApi.getPullRequests()
        let reviewRequests: Array<ReviewRequest> = []

        pullRequests.forEach(pullRequest => {
            const reviews: Array<ReviewRequest> = pullRequest.reviewRequests

            if (reviews.length == 1) {
                if (reviews[0].reviewer == username) {
                    reviewRequests.push(reviews[0])
                }
            }
        })

        return reviewRequests
    }

    static async getUnacceptedReviewRequests(username: string): Promise<Array<ReviewRequest>> {
        await GitHubApi.setup()

        let pullRequests: Array<PullRequest> = await GitHubApi.getPullRequests()
        let reviewRequests: Array<ReviewRequest> = []

        pullRequests.forEach(pullRequest => {
            let reviews: Array<ReviewRequest> = pullRequest.reviewRequests

            if (reviews.length > 1) {
                reviews.forEach((reviewRequest: ReviewRequest) => {
                    if (reviewRequest.reviewer == username) {
                        reviewRequests.push(reviewRequest)
                    }
                })
            }
        })

        return reviewRequests
    }

    static async deleteReviewers(pullRequestNumber: number, reviewers: string[]): Promise<boolean> {
        await GitHubApi.setup()

        let response = await GitHubApi.octokit.request('DELETE /repos/{owner}/{repo}/pulls/{pull_number}/requested_reviewers', {
            owner: GitHubApi.repoOwner,
            repo: GitHubApi.repoName,
            pull_number: pullRequestNumber,
            reviewers: reviewers
        })

        return response['status'] == 200
    }

    private static getPullRequestFromResponse(response: any): PullRequest {
        let pullRequest: PullRequest = {
            title: response['title'] as string,
            url: response['html_url'] as string,
            number: response['number'] as number,
            status: response['status'] as string,
            reviewRequests: GitHubApi.getReviewRequestsFromResponse(response)
        }

        return pullRequest;
    }

    private static getReviewRequestsFromResponse(response: any): Array<ReviewRequest> {
        const reviewRequests: Array<ReviewRequest> = new Array()

        let reviewers: Array<any> = response['requested_reviewers']

        for (let i = 0; i < reviewers.length; i++) {
            let reviewRequest: ReviewRequest = {
                pullNumber: response['number'] as number,
                title: response['title'] as string,
                url: response['html_url'] as string,
                reviewer: reviewers[i]['login'] as string
            }

            reviewRequests.push(reviewRequest)
        }

        return reviewRequests;
    }
}