package com.bridgecanada.prismatic.feed;

import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.prismatic.data.PrismaticFeed;

/**
 * User: bridge
 * Date: 09/05/13
 */
public interface IPrismaticFeed {

    /**
     * Get the feed asynchronously, which will return a new id.
     * @param successCallback
     * @param failureCallback
     */
    //void GetFeed(IHttpResultCallback<PrismaticFeed> successCallback, IHttpResultCallback<HttpError> failureCallback);

    /**
     * Use the id from the other call to get the feed asynchronously, within the given range first-last (indexed from 0)
     * @param feedStrategy the url plus whatever other params we are using to access the feed
     * @param successCallback
     * @param failureCallback
     */
    void GetFeed(IFeedStrategy feedStrategy, IHttpResultCallback<com.bridgecanada.prismatic.data.PrismaticFeed> successCallback, IHttpResultCallback<HttpError> failureCallback);

}
