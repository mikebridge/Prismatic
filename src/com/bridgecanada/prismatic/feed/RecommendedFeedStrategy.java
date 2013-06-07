package com.bridgecanada.prismatic.feed;

import com.bridgecanada.prismatic.di.ApiBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.ApiVersionAnnotation;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * User: bridge
 * Date: 28/05/13
 */
public class RecommendedFeedStrategy implements IFeedStrategy {
    //http://api.getprismatic.com/news/read?callback=jQuery183012413264252245426_1369186496420&api-version=1.0&subpage=true&last-feed-id=XXXXXXXC&first-article-idx=0&last-article-idx=5&_=1369186498737

    private String _baseUrl;
    private final String _apiVersionId;
    private long _start;

    private String _path = "/news/recommended";


    public interface RecommendedFeedStrategyFactory {
        public IFeedStrategy create(@Assisted("start") long start);
    }

    // see; https://code.google.com/p/google-guice/wiki/AssistedInject

    /**
     *
     * @param start the start parameter.  If it is zero, don't pass it as a parameter
     * @param baseUrl
     * @param apiVersionId
     */
    @Inject
    public RecommendedFeedStrategy(
            @Assisted("start") long start,
            @ApiBaseUrlAnnotation String baseUrl,
            @ApiVersionAnnotation String apiVersionId) {

        _start = start;
        _baseUrl = baseUrl;
        _apiVersionId = apiVersionId;
    }




    /**
     * Get the URL that assigns a new ID
     * @return
     */
    @Override
    public String getFeedUrl() {

        String url = _baseUrl + _path    + "?api-version="+_apiVersionId;

        if (_start > 0) {
            return url+"&start="+_start;
        }

        return url;

    }
    @Override
    public String getUrlPath() {
        return _path;
    }

    @Override
    public IFeedStrategy next(String feedId, long start, long next, long last) {
        return new RecommendedFeedStrategy(start, this._baseUrl, this._apiVersionId);
    }

}
