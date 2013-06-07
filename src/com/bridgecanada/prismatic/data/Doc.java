package com.bridgecanada.prismatic.data;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

/**
 * User: bridge
 * Date: 21/04/13
 */
public class Doc {

    //title: "The Atlantic",
    //url: "http://feeds.feedburner.com/TheAtlantic",
    //image: "http://imagecdn.getprismatic.com/pub/16/4886764575945213146.ico",
    //highres-image: "http://imagecdn.getprismatic.com/pub/big/4886764575945213146.png"

    private long id;
    private String title;
    private String url;
    private String image;
    private String highresImage;

    private String start;
    private Author author;
    private long date;
    private Feed feed;
    private String text;
    private Img img;
    private PrismaticActivity prismaticActivity;

    public String getText() {
        return text;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setText(String text) {
        this.text = text;
    }



    public String getTitle() {
        return title;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUrl(String url) {
        this.url = url;
    }

//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getHighresImage() {
//        return highresImage;
//    }
//
//    public void setHighresImage(String highresImage) {
//        this.highresImage = highresImage;
//    }

    public Img getImg() {
        return img;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setImg(Img img) {
        this.img = img;
    }

    public String getStart() {
        return start;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setStart(String start) {
        this.start = start;
    }

    public Author getAuthor() {
        return author;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setAuthor(Author author) {
        this.author = author;
    }

    public long getDate() {
        return date;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setDate(long date) {
        this.date = date;
    }

    public Feed getFeed() {
        return feed;
    }

    public PrismaticActivity getPrismaticActivity() {
        return prismaticActivity;
    }

    @SuppressWarnings("UnusedDeclaration")
    @JsonProperty("prismatic-activity")
    public void setPrismaticActivity(PrismaticActivity prismaticActivity) {
        this.prismaticActivity = prismaticActivity;
    }

    public long getId() {
        return id;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setId(long id) {
        this.id = id;
    }

}
