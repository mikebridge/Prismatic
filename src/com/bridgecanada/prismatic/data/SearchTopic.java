package com.bridgecanada.prismatic.data;

/**
 * User: bridge
 */
public class SearchTopic implements ISearchResultDisplay {

    private String key;
    private String type;
    private ActivitySource source;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ActivitySource getSource() {
        return source;
    }

    public void setSource(ActivitySource source) {
        this.source = source;
    }

    @Override
    public String getDisplayText() {
        return getKey();
    }
}
