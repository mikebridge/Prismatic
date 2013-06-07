package com.bridgecanada.prismatic.feed;

import android.util.Log;
import com.bridgecanada.net.*;
import com.bridgecanada.prismatic.LoginFailureResult;
import com.bridgecanada.prismatic.di.ApiBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.ApiVersionAnnotation;
import com.bridgecanada.prismatic.di.AuthBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.UserAgentAnnotation;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 *
 * Send the huge "data" response when a user clicks on something.
 *
 * User: bridge
 * Date: 29/05/13
 */

public class EventDispatchService extends DispatchServiceBase {

    //http://api.getprismatic.com/event/dispatch?api-version=1.0
    //"/Api/Auth/Login";
    public static final String PATH = "/event/dispatch";

    public static final String USER_PARM = "handle";
    public static final String PASSWD_PARM = "password";

    private String _articleId;
    private final IFeedCache _feedCache;
    private String _baseApiUrl;

    @Inject
    /*
     * @param sourceUri uri of the source page e.g. /news/home
     * @param targetUri uri of the target eg.g /2013/05/29/blah
     * @param targetUrl  url of target e.g. http://readwrite.com/2013/05/29/blah
     */
    public EventDispatchService(
            @Assisted("articleId") String articleId,
            PersistentCookieStore cookieStore,
            IFeedCache feedCache,
            @ApiVersionAnnotation String apiVersion,
            @ApiBaseUrlAnnotation String baseApiUrl,
            @UserAgentAnnotation String userAgent) {

        super(apiVersion, cookieStore, userAgent);

        if (baseApiUrl == null ) {
            throw new IllegalArgumentException("baseApiUrl must not be null");
        }

        if (articleId == null ) {
            throw new IllegalArgumentException("articleId must not be null");
        }

        if (feedCache == null ) {
            throw new IllegalArgumentException("feedCache must not be null");
        }

        _articleId = articleId;

        _baseApiUrl = baseApiUrl;
        _feedCache = feedCache;
    }


    @Override
    protected String getUrl() {

        String url = _baseApiUrl + PATH + "?api-version="+_apiVersion;
        return url;
    }

    @Override
    public JsonNode createBody() throws IOException {

        String json = _feedCache.load(_articleId);
        if (json == null) {
            return null;
        }
        return new ObjectMapper().readTree(json);

    }
}
