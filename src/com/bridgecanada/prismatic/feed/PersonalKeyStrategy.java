package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.prismatic.data.*;
import com.bridgecanada.prismatic.data.PrismaticFeed;
import com.bridgecanada.prismatic.di.ApiBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.ApiVersionAnnotation;
import com.google.inject.Inject;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 28/05/13
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersonalKeyStrategy implements IFeedStrategy {


    //http://api.getprismatic.com/news/personal/personalkey?rand=XXXXXX&callback=prismatic.feedPromise.fulfill&api-version=1.0

    private String _baseUrl;
    private String _privateKeyPath = "/news/personal/personalkey";
    private final String _apiVersionId;

    public interface PersonalKeyStrategyFactory {
        public IFeedStrategy create();
    }

    @Inject
    public PersonalKeyStrategy(@ApiBaseUrlAnnotation String baseUrl, @ApiVersionAnnotation String apiVersionId) {

        _baseUrl = baseUrl;
        _apiVersionId = apiVersionId;

    }
    /**
     * Get the URL that assigns a new ID
     * @return
     */
    @Override
    public String getFeedUrl() {
        return _baseUrl + _privateKeyPath + "?rand="+ (new Random()).nextInt(Integer.MAX_VALUE)
                                              + "&api-version="+_apiVersionId;
    }

    @Override
    public String getUrlPath() {
        return _privateKeyPath;
    }

    @Override
    public IFeedStrategy next(String feedId, long start, long first, long last) {
        return new HomeStrategy(feedId, first, last, this._baseUrl, this._apiVersionId);
    }


}
