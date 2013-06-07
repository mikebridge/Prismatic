package com.bridgecanada.prismatic.feed;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 28/05/13
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FeedStrategyFactory {
    public IFeedStrategy create(String _feedId, int firstArticleIdx, int lastArticleIdx);
}
