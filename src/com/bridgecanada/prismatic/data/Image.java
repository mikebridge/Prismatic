package com.bridgecanada.prismatic.data;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/04/13
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Image {

    // TODO: size, etc.
    private String url;
    private int width;
    private int height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
