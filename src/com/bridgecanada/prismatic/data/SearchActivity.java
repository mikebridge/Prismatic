package com.bridgecanada.prismatic.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * User: bridge
 * Date: 06/10/13
 */
public class SearchActivity {

    private String displayKey;
    private String img;
    private String name;
    private long key;
    private ActivitySource source;
    private String title;
    private String type;

    public SearchActivity() {
    }
    @JsonProperty("display-key")
    public String getDisplayKey() {
        return displayKey;
    }

    @JsonProperty("display-key")
    public void setDisplayKey(String displayKey) {
        this.displayKey = displayKey;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActivitySource getSource() {
        return source;
    }

    public void setSource(ActivitySource source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }
}
