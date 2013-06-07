package com.bridgecanada.prismatic.feed;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 28/05/13
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IFeedStrategy {

    public String getFeedUrl();

    public String getUrlPath();

    public IFeedStrategy next(String feedId, long start, long first, long last);


}
