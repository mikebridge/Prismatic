package com.bridgecanada.prismatic.data;

/**
 * User: bridge
 * Date: 06/10/13
 */
public class SearchFeed implements ISearchResultDisplay{

    private String img;
    private String key;
    private String title;
    private String type;
    private ActivitySource source;

    public SearchFeed() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public ActivitySource getSource() {
        return source;
    }

    public void setSource(ActivitySource source) {
        this.source = source;
    }

    @Override
    public String getDisplayText() {
        return getTitle();
    }

}
