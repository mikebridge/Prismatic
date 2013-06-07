package com.bridgecanada.prismatic.data;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/04/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */


// SEE: http://api.getprismatic.com/news/home?api-version=1.0&subpage=true&last-feed-id=G__201447_47597875&first-article-idx=5&last-article-idx=10&_=1366575962308

public class CardData {


    private long id;
    private String text;
    private long date; // todo:fix the date
    private PrismaticActivity prismaticActivity;
    private Author author;
    private String title;
    private String url;
    private String feedName;
    private String feedUrl;
    private String feedImage;
    private String feedHighResImage;

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }


    //private ArrayList<HomeInterest> homeInterests;
    //private ArrayList<PrismaticShares> ...
    private ArrayList<String> topics;

    private Image img;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public PrismaticActivity getPrismaticActivity() {
        return prismaticActivity;
    }

    public void setPrismaticActivity(PrismaticActivity prismaticActivity) {
        this.prismaticActivity = prismaticActivity;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }


    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public String getFeedImage() {
        return feedImage;
    }

    public void setFeedImage(String feedImage) {
        this.feedImage = feedImage;
    }

    public String getFeedHighResImage() {
        return feedHighResImage;
    }

    public void setFeedHighResImage(String feedHighResImage) {
        this.feedHighResImage = feedHighResImage;
    }
}
