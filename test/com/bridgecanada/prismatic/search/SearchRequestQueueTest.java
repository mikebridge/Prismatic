package com.bridgecanada.prismatic.search;

import com.bridgecanada.prismatic.data.SearchResults;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.fail;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItems;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * User: bridge
 * Date: 06/10/13
 */
public class SearchRequestQueueTest {


    @Test
    public void testSearchCallsAsyncSearch() throws Exception {

        // Arrange
        String searchString = "testit";
        ISearcher searcher = mock(ISearcher.class);
        SearchRequestQueue requestQueue = new SearchRequestQueue(searcher);

        // Act
        requestQueue.Search(searchString);

        // Assert
        verify(searcher).AsyncSearch(searchString);
    }

    @Test
    public void testSearchTwiceWithoutResultCallsAsyncSearchOnce() throws Exception {

        // Arrange
        String searchString1 = "testit1";
        String searchString2 = "testit2";
        ISearcher searcher = mock(ISearcher.class);
        SearchRequestQueue requestQueue = new SearchRequestQueue(searcher);

        // Act
        requestQueue.Search(searchString1);
        requestQueue.Search(searchString2);

        // Assert
        verify(searcher, times(1)).AsyncSearch(searchString1);
        verify(searcher, times(0)).AsyncSearch(searchString2);
    }

    @Test
    public void testNewSearchAfterFirstComplete() throws Exception {

        // Arrange
        final ArrayList<String> searches = new ArrayList<String>();
        String searchString1 = "testit1";
        String searchString2 = "testit2";
        TestSearcher searcher = new TestSearcher();
        searcher.addSeachResultListener(new ISearchResultListener() {
            @Override
            public void HandleSearchResult(String str, SearchResults results) {
                searches.add(str);
            }
        });
        SearchRequestQueue requestQueue = new SearchRequestQueue(searcher);

        // Act
        requestQueue.Search(searchString1);
        searcher.simulateResult(searchString1);
        requestQueue.Search(searchString2);
        searcher.simulateResult(searchString2);

        // Assert
        assertThat(searches.size(), equalTo(2));
    }


    @Test
    public void testIntermediateSearchesDropped() throws Exception {

        // Arrange
        final ArrayList<String> searches = new ArrayList<String>();
        String searchString1 = "testit1";
        String searchString2 = "testit2";
        String searchString3 = "testit3";
        String searchString4 = "testit4";
        TestSearcher searcher = new TestSearcher();
        searcher.addSeachResultListener(new ISearchResultListener() {
            @Override
            public void HandleSearchResult(String searchString, SearchResults results) {
                searches.add(searchString);
            }
        });
        SearchRequestQueue requestQueue = new SearchRequestQueue(searcher);

        // Act
        requestQueue.Search(searchString1);
        requestQueue.Search(searchString2);
        requestQueue.Search(searchString3);
        searcher.simulateResult(searchString1);
        searcher.simulateResult(searchString4);

        // Assert
        assertThat(searches.size(), equalTo(2));
        assertThat(searches, hasItems(searchString1, searchString4));
    }



    // communicates a successful search when simulateResult called
    private class TestSearcher implements ISearcher {

        List<ISearchResultListener> listeners = new ArrayList<ISearchResultListener>();

        public void simulateResult(String searchResult) {
            for(ISearchResultListener listener: listeners) {
                listener.HandleSearchResult(searchResult, new SearchResults());
            }
        }

        @Override
        public void AsyncSearch(String searchString) {


        }

        @Override
        public void addSeachResultListener(ISearchResultListener listener) {
            listeners.add(listener);
        }
    }
}

