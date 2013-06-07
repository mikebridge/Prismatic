package com.bridgecanada.prismatic.feed;

import com.bridgecanada.prismatic.di.ApiBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.ApiVersionAnnotation;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 28/05/13
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class SavedFeedStrategy implements IFeedStrategy {

    private String _baseUrl;
    private final String _apiVersionId;
    private long _start;

    private String _path = "/news/saved";


    public interface SavedFeedStrategyFactory {
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
    public SavedFeedStrategy(
            @Assisted("start") long start,
            @ApiBaseUrlAnnotation String baseUrl,
            @ApiVersionAnnotation String apiVersionId) {

        _start = start;
        _baseUrl = baseUrl;
        _apiVersionId = apiVersionId;
    }

    /* Get the URL that assigns a new ID
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
        return new SavedFeedStrategy(start, this._baseUrl, this._apiVersionId);
    }

}
