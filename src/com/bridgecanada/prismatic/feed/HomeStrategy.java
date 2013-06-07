package com.bridgecanada.prismatic.feed;

import com.bridgecanada.prismatic.di.ApiBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.ApiVersionAnnotation;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;


import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 28/05/13
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomeStrategy implements IFeedStrategy {

    //http://api.getprismatic.com/news/home?callback=jQuery183012413264252245426_1369186496420&api-version=1.0&subpage=true&last-feed-id=XXXXXXXC&first-article-idx=0&last-article-idx=5&_=1369186498737

    private String _baseUrl;
    private final String _apiVersionId;
    private String _feedId;
    private final long _first;
    private final long _last;

    private String _homePath = "/news/home";


    public interface HomeStrategyFactory {
        public IFeedStrategy create(
                @Assisted("feedId") String feedId,
                @Assisted("firstArticleIdx") long firstArticleIdx,
                @Assisted("lastArticleIdx") long lastArticleIdx);
    }

    // see; https://code.google.com/p/google-guice/wiki/AssistedInject
    @Inject
    public HomeStrategy(@Assisted("feedId") String feedId,
                        @Assisted("firstArticleIdx") long firstArticleIdx,
                        @Assisted("lastArticleIdx") long lastArticleIdx,
                        @ApiBaseUrlAnnotation String baseUrl,
                        @ApiVersionAnnotation String apiVersionId) {

        if (feedId == null) {
            throw new NullPointerException("Invalid feedId (null)");
        }
        if (baseUrl == null) {
            throw new NullPointerException("Invalid baseUrl (null)");
        }
        if (apiVersionId == null) {
            throw new NullPointerException("Invalid apiVersionId (null)");
        }

        _feedId = feedId;
        _first = firstArticleIdx;
        _last = lastArticleIdx;
        _baseUrl = baseUrl;
        _apiVersionId = apiVersionId;

    }


    @Override
    public String getUrlPath() {
        return _homePath;
    }

    /**
     * Get the URL that assigns a new ID
     * @return
     */
    @Override
    public String getFeedUrl() {


            return _baseUrl + _homePath    + "?last-feed-id=" + _feedId
                                       + "&first-article-idx=" + _first
                                       + "&last-article-idx=" + _last
                                       + "&api-version="+_apiVersionId;
    }

    @Override
    public IFeedStrategy next(String feedId, long start, long first, long last) {
        return new HomeStrategy(feedId, first, last, this._baseUrl, this._apiVersionId);
    }

}