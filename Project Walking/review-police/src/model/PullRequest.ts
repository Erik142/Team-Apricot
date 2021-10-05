import { ReviewRequest } from "./ReviewRequest";

export interface PullRequest {
    title: string,
    url: string
    number: number,
    status: string,
    reviewRequests: Array<ReviewRequest>
}