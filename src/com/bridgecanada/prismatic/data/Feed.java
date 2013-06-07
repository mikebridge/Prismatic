package com.bridgecanada.prismatic.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * User: bridge
 * Date: 23/05/13
 */
public class Feed {

    private String url;
    private String title;
    private String highResImage;
    private String image;

    public String getUrl() {
        return url;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getHighResImage() {
        return highResImage;
    }

    @SuppressWarnings("UnusedDeclaration")
    @JsonProperty("highres-image")
    public void setHighResImage(String highResImage) {
        this.highResImage = highResImage;
    }

    public String getImage() {
        return image;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setImage(String image) {
        this.image = image;
    }


}
