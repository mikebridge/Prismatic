package com.bridgecanada.prismatic.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * User: bridge
 * Date: 20/05/13
 */
public class QueryParams {

    private long firstArticleIdx;
    private long lastArticleIdx;
    private String lastFeedId;
    private boolean subpage = true;
    private long _start;

    @JsonProperty("first-article-idx")
    public long getFirstArticleIdx() {
        return firstArticleIdx;
    }

    @SuppressWarnings("UnusedDeclaration")
    @JsonProperty("first-article-idx")
    public void setFirstArticleId(long firstArticleIdx) {
        this.firstArticleIdx = firstArticleIdx;
    }

    @JsonProperty("last-article-idx")
    public long getLastArticleIdx() {
        return lastArticleIdx;
    }

    @SuppressWarnings("UnusedDeclaration")
    @JsonProperty("last-article-idx")
    public void setLastArticleIdx(long lastArticleIdx) {
        this.lastArticleIdx = lastArticleIdx;
    }

    @JsonProperty("last-feed-id")
    public String getLastFeedId() {
        return lastFeedId;
    }

    @SuppressWarnings("UnusedDeclaration")
    @JsonProperty("last-feed-id")
    public void setLastFeedId(String lastFeedId) {
        this.lastFeedId = lastFeedId;
    }

    public boolean isSubpage() {
        return subpage;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setSubpage(boolean subpage) {
        this.subpage = subpage;
    }

    public long getStart() {
        return _start;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setStart(long value) {
        _start = value;
    }
}
