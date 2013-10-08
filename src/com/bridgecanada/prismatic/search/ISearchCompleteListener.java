package com.bridgecanada.prismatic.search;

import com.bridgecanada.prismatic.data.SearchResults;

/**
 * User: bridge
 * Date: 07/10/13
 */
public interface ISearchCompleteListener {

    void HandleSearchResult(String searchString, SearchResults results);

}
