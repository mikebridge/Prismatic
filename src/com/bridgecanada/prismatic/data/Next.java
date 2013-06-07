package com.bridgecanada.prismatic.data;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 20/05/13
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Next {

    private int remainingCount;

    private QueryParams queryParams;

    @JsonProperty("query-params")
    public QueryParams getQueryParams() {
        return queryParams;
    }

    @SuppressWarnings("UnusedDeclaration")
    @JsonProperty("query-params")
    public void setQueryParams(QueryParams queryParams) {
        this.queryParams = queryParams;
    }

    @JsonProperty("remaining-count")
    public int getRemainingCount() {
        return remainingCount;
    }

    @JsonProperty("remaining-count")
    public void setRemainingCount(int remainingCount) {
        this.remainingCount = remainingCount;
    }
}
