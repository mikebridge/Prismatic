package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.PersistentCookieStore;
import com.bridgecanada.prismatic.di.ApiBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.ApiVersionAnnotation;
import com.bridgecanada.prismatic.di.AuthBaseUrlAnnotation;
import com.bridgecanada.prismatic.di.UserAgentAnnotation;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;

/**
 * Get the cached information for this article and log it with the server
 * User: bridge
 * Date: 05/06/13
 */
public class AuthEventPublicDispatch extends DispatchServiceBase {

    String _baseAuthUrl;

    private final String _sourceUri;
    private final String _targetUri;
    private final String _targetUrl;


    public static final String PATH = "/auth/event_public_dispatch"
            +"?ignore=true"
            +"&whitelist_url=http%3A%2F%2Fwww.getprismatic.com%2Fnews%2Fhome"
            +"&soon_url=http%3A%2F%2Fwww.getprismatic.com%2Fwelcome"
            +"&create_url=http%3A%2F%2Fwww.getprismatic.com%2Fcreateaccount"
            +"&resetpassword_url=http%3A%2F%2Fwww.getprismatic.com%2Fresetpassword";

    @Inject
    public AuthEventPublicDispatch(
            @Assisted("sourceUri") String sourceUri,
            @Assisted("targetUri") String targetUri,
            @Assisted("targetUrl") String targetUrl,

            PersistentCookieStore cookieStore,

            @ApiVersionAnnotation String apiVersion,
            @AuthBaseUrlAnnotation String baseAuthUrl,
            @UserAgentAnnotation String userAgent
    ) {
        super(apiVersion, cookieStore, userAgent);

        if (baseAuthUrl == null ) {
            throw new IllegalArgumentException("baseApiUrl must not be null");
        }
        if (sourceUri == null ) {
            throw new IllegalArgumentException("sourceUri must not be null");
        }
        if (targetUri == null ) {
            throw new IllegalArgumentException("targetUri must not be null");
        }
        if (targetUrl == null ) {
            throw new IllegalArgumentException("targetUrl must not be null");
        }
        _sourceUri = sourceUri;
        _targetUri = targetUri;
        _targetUrl = targetUrl;

        _baseAuthUrl = baseAuthUrl;


    }


    @Override
    /**

     * @return
     */
    public JsonNode createBody() {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        //String k = "browser";
        //String v = _userAgent;
        //ObjectNode node = mapper.createObjectNode();
        rootNode.put("browser", _userAgent);
        rootNode.put("category", "link-click");

        ObjectNode page = mapper.createObjectNode();
        page.put("referer", "");
        page.put("search", "");
        page.put("uri", _sourceUri);
        rootNode.put("page", page );

        ObjectNode target = mapper.createObjectNode();
        target.put("search", "");
        target.put("uri", _targetUri);
        target.put("url", _targetUrl);
        rootNode.put("target", target);

        rootNode.put("type", "login");

        return rootNode;

    }
    @Override
    protected String getUrl() {

            String url = _baseAuthUrl + PATH + "?api-version="+_apiVersion;

            return url;
    }
}

