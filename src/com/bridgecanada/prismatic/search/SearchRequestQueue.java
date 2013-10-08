package com.bridgecanada.prismatic.search;

import com.bridgecanada.prismatic.data.SearchResults;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: bridge
 * There are at most two searches in the queue.  One is active and incomplete, and
 * the other is pending.  Any new searches replace the pending query before it can be
 * begun.
 */
public class SearchRequestQueue {

    List<ISearchCompleteListener> listeners = new ArrayList<ISearchCompleteListener>();

    private ConcurrentLinkedQueue<String> _queue = new ConcurrentLinkedQueue<String>();

    private boolean _searchActive = false;

    private ISearcher _searcher;

    @Inject
    public SearchRequestQueue(ISearcher searcher) {
        if (searcher == null) {
            throw new IllegalArgumentException("searcher");
        }
        _searcher = searcher;
        _searcher.addSeachResultListener(new SearchResultHandler());
    }

    public void Search(String search) {
        _queue.add(search);
        doNextSearchIfNotSearching();
    }


    private synchronized void doNextSearchIfNotSearching() {

        if (_searchActive) {
            //System.out.println("doing nothing... "+ search);
            return; // do nothing
        } else {
            String nextSearch = popUntilLastElement();
            if (nextSearch != null) {
                launchSearch(nextSearch);
            }
        }

    }

    public void addSeachResultListener(ISearchCompleteListener listener) {
        listeners.add(listener);
    }

    private void tellListeners(String searchString, SearchResults results) {
        for (ISearchCompleteListener listener : listeners) {
            listener.HandleSearchResult(searchString, results);
        }
    }


    private void launchSearch(String searchString) {
        //System.out.println("launching search..." + searchString);
        _searchActive = true;
        _searcher.AsyncSearch(searchString);
    }

    private String popUntilLastElement() {

        String nextSearch = null;
        String requestedSearch = null;
        do {

            requestedSearch = _queue.poll();
            if (requestedSearch != null) {
                //System.out.println("Popped "+requestedSearch);
                nextSearch = requestedSearch;
            }


        } while (requestedSearch != null);
        return nextSearch;

    }


    private class SearchResultHandler implements ISearchResultListener {
        @Override
        public void HandleSearchResult(String searchString, SearchResults results) {
            _searchActive = false;
            tellListeners(searchString, results);
            doNextSearchIfNotSearching();
        }
    }
}
