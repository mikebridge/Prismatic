package com.bridgecanada.prismatic.search;

import com.bridgecanada.prismatic.data.SearchResults;

/**
 * User: bridge
 */
public interface ISearchResultListener {
    void HandleSearchResult(String searchString, SearchResults results);
}
