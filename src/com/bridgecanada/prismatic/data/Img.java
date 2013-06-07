package com.bridgecanada.prismatic.data;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 20/05/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Img {
    private Size size;
    private String url;

    public Img() {
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public class Size {

        private int width;
        private int height;

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
}
