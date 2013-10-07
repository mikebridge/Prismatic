package com.bridgecanada.prismatic.data;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

/**
 * User: bridge
 * Date: 06/10/13
 */
public class SearchResults {

    private ArrayList<SearchActivity> activities;

    private ArrayList<SearchTopic> topics;

    private ArrayList<SearchFeed> feeds;

    @JsonProperty("activity")
    public ArrayList<SearchActivity> getActivities() {
        return activities;
    }

    @JsonProperty("activity")
    public void setActivities(ArrayList<SearchActivity> activities) {
        this.activities = activities;
    }

    @JsonProperty("topic")
    public ArrayList<SearchTopic> getTopics() {
        return topics;
    }

    @JsonProperty("topic")
    public void setTopics(ArrayList<SearchTopic> topics) {
        this.topics = topics;
    }

    @JsonProperty("feed")
    public ArrayList<SearchFeed> getFeeds() {
        return feeds;
    }

    @JsonProperty("feed")
    public void setFeeds(ArrayList<SearchFeed> feeds) {
        this.feeds = feeds;
    }



}
