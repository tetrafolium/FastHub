package com.fastaccess.provider.rest;

import androidx.annotation.NonNull;

import com.fastaccess.data.dao.types.IssueState;

/**
 * Created by Kosh on 23 Mar 2017, 7:26 PM
 */

public class RepoQueryProvider {
    @NonNull public static String getIssuesPullRequestQuery(final @NonNull String owner, final @NonNull String repo,
            final @NonNull IssueState issueState, final boolean isPr) {
        return "+" + "type:" + (isPr ? "pr" : "issue")
               + "+" + "repo:" + owner + "/"
               + repo + "+" + "is:" + issueState.name();
    }

    @NonNull public static String getMyIssuesPullRequestQuery(final @NonNull String username, final @NonNull IssueState issueState, final boolean isPr) {
        return "type:" + (isPr ? "pr" : "issue")
               + "+" + "author:" + username
               + "+is:" + issueState.name();
    }

    @NonNull public static String getAssigned(final @NonNull String username, final @NonNull IssueState issueState, final boolean isPr) {
        return "type:" + (isPr ? "pr" : "issue")
               + "+" + "assignee:" + username
               + "+is:" + issueState.name();
    }

    @NonNull public static String getMentioned(final @NonNull String username, final @NonNull IssueState issueState, final boolean isPr) {
        return "type:" + (isPr ? "pr" : "issue")
               + "+" + "mentions:" + username
               + "+is:" + issueState.name();
    }

    @NonNull public static String getReviewRequests(final @NonNull String username, final @NonNull IssueState issueState) {
        return "type:pr"
               + "+" + "review-requested:" + username
               + "+is:" + issueState.name();
    }

    public static String getParticipated(final @NonNull String username, final @NonNull IssueState issueState, final boolean isPr) {
        return "type:" + (isPr ? "pr" : "issue")
               + "+" + "involves:" + username
               + "+is:" + issueState.name();
    }
}
