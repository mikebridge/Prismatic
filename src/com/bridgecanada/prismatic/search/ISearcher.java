package com.bridgecanada.prismatic.search;

import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.prismatic.data.SearchResults;

/**
 * User: bridge
 * Date: 06/10/13
 */
public interface ISearcher {

    void AsyncSearch( String searchString );


    void addSeachResultListener(ISearchResultListener listener);


}
