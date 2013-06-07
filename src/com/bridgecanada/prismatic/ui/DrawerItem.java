package com.bridgecanada.prismatic.ui;

import com.bridgecanada.prismatic.feed.IFeedStrategy;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 27/05/13
 * Time: 11:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class DrawerItem {

    private String title;
    private String tag;
    private String image;// ?
    private IFeedStrategy _feedStrategy;

    public String getTitle() {
        return title;
    }

    public DrawerItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public DrawerItem setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getImage() {
        return image;
    }

    public DrawerItem setImage(String image) {
        this.image = image;
        return this;
    }

    public IFeedStrategy getFeedStrategy() {
        return _feedStrategy;
    }

    public DrawerItem setFeedStrategy(IFeedStrategy feedStrategy) {
        _feedStrategy = feedStrategy;
        return this;
    }


}
