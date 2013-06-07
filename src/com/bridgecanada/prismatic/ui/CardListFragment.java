package com.bridgecanada.prismatic.ui;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bridgecanada.prismatic.R;
import com.bridgecanada.prismatic.data.CardData;
import com.bridgecanada.prismatic.data.Image;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/04/13
 * Time: 11:48 AM
 */
public class CardListFragment extends ListFragment implements OnDismissCallback, AbsListView.OnScrollListener {

    private final String TAG = getClass().getSimpleName();
    private int _lastNotificationIndex = 0; // the last index when we last notified of scroll-to-end

    private ArrayList<CardData> listItems = new ArrayList<CardData>();

    CardAdapter adapter;

	public CardListFragment() {
		Log.v(TAG, "constructor");
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.w(TAG, "activityCreated");
        super.onActivityCreated(savedInstanceState);
        setUpListView();

    }

    /**
     * find the list view and bind an adapter to this object's list
     * items.
     */
    private void setUpListView() {
        //listItems = GetTestData();
        //adapter = new ArrayAdapter<String>(getActivity(),
        //        android.R.layout.simple_list_item_1, listItems)

        // set up the empty listview and card adapter on the list items
        ListView listView = getListView();
        if (listView !=null) { // this isn't getting set up correctly in the tests
            listView.setOnScrollListener(this);
        }
        adapter = new CardAdapter(getActivity(), listItems);
        //SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(adapter, this));
        //swingBottomInAnimationAdapter.setListView(listView);

        setListAdapter(adapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
          // do nothing
    }

    @Override
    // TODO: don't fire when loading...
    public void onScroll(AbsListView lw, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {

        switch(lw.getId()) {
            case android.R.id.list:

               // Log.i(TAG, "firstVisibleItem: "+firstVisibleItem+" visibleItemCount:"+visibleItemCount+" totalItem:"+totalItemCount) ;
                int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount) {
                   //Log.i(TAG, "NOTIFYING!!");
                   notifyLastItemVisible(firstVisibleItem, totalItemCount);

                }

        }
    }

    /**
     * Notify observers when last item is visible---this only happens if the
     * last item is greater than the last notification time.
     * side-effect: last-notified is updated.
     *
     * Currently this ignores the update when the index is zero, since _lastNotificationIndex is
     * set to zero.
     */
    private void notifyLastItemVisible(int firstVisibleItem, int totalItemCount) {
        // TODO: If the previous list is still loading, we should probably ignore this event?
        //
        if (totalItemCount > _lastNotificationIndex) {
            _lastNotificationIndex = totalItemCount;
            //for(PropertyChangeListener listener: scrollToEndSupport.getPropertyChangeListeners()) {
              //  Log.i(TAG, "informing "+listener.toString());

            //}
            scrollToEndSupport.firePropertyChange("totalItemCount", firstVisibleItem, totalItemCount);
            //Toast.makeText(getActivity(), "THE LAST ITEM ("+totalItemCount+") IS VISIBLE!", Toast.LENGTH_SHORT).show();
        }


    }

    public void addItem(CardData newItem) {
        //Log.v(TAG, "addItem");
        listItems.add(newItem);
        adapter.notifyDataSetChanged();
        //Toast.makeText(getActivity(), "Added Item # " + listItems.size() +"!", Toast.LENGTH_SHORT).show();
    }


    public void clearItems() {
        _lastNotificationIndex=0;
        adapter.clearItems();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {

        //listItems.
        Object obj = listView.getItemAtPosition(position);
        CardData cardData = (CardData) obj;
        itemClickedSupport.firePropertyChange("openUrl", null, cardData);
        //Toast.makeText(getActivity(), "Clicked on id "+id +"!", Toast.LENGTH_SHORT).show();
        //Log.v(TAG, "onItemCLick");
    }


    @Override
    public void onDismiss(ListView listView, int[] reverseSortedPositions) {

        //Log.v(TAG, "onDismiss called!!");
        for (int position : reverseSortedPositions) {
            //Log.w(TAG, "Removing item at "+position);
            //adapter.remove(position);
            //adapter.removeItem(position);
            listItems.remove(position);
            adapter.notifyDataSetChanged();
		}
    }

    private final PropertyChangeSupport scrollToEndSupport = new PropertyChangeSupport(this);

    private final PropertyChangeSupport itemClickedSupport = new PropertyChangeSupport(this);

    public void addOnScrollToEndListener(PropertyChangeListener listener) {
        scrollToEndSupport.addPropertyChangeListener(listener);
    }

    public void removeOnScrollToEndListener(PropertyChangeListener listener) {
        scrollToEndSupport.removePropertyChangeListener(listener);
    }


    public void addOnItemClickedListener(PropertyChangeListener listener) {
        itemClickedSupport.addPropertyChangeListener(listener);
    }

    public void removeOnItemClickedListener(PropertyChangeListener listener) {
        itemClickedSupport.removePropertyChangeListener(listener);
    }

}
