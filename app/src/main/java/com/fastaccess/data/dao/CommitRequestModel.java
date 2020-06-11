package com.fastaccess.data.dao;

/**
 * Created by kosh on 31/08/2017.
 */

public class CommitRequestModel {

    private String message;
    private String content;
    private String sha;
    private String branch;

    public CommitRequestModel(final String message, final String content, final String sha, final String branch) {
        this.message = message;
        this.content = content;
        this.sha = sha;
        this.branch = branch;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(final String sha) {
        this.sha = sha;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(final String branch) {
        this.branch = branch;
    }
}
