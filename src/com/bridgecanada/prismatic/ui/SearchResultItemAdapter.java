package com.bridgecanada.prismatic.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bridgecanada.prismatic.R;
import com.bridgecanada.prismatic.data.ISearchResultDisplay;
import com.bridgecanada.prismatic.data.SearchResults;

import java.util.List;

/**
 * User: bridge
 * Date: 07/10/13
 */
public class SearchResultItemAdapter<T extends ISearchResultDisplay> extends BaseAdapter {

    private static LayoutInflater _inflater;
    private final Context _context;
    List<T> _searchResult;
    String searchTerm;

    public SearchResultItemAdapter(Context context, String searchTerm, List<T> searchResult) {
        this._context = context;
        this.searchTerm = searchTerm;
        _searchResult = searchResult;
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return _searchResult.size();
    }

    @Override
    public Object getItem(int i) {
        return _searchResult.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view=convertView;
        if(convertView==null) {
            view = _inflater.inflate(R.layout.search_result_list_item, null);
        }

        TextView menuItem = (TextView)view.findViewById(R.id.search_result_item_text); // title
        //ImageView thumb_image=(ImageView)view.findViewById(R.id.search_result_item_text); // icon
        T item = (T) getItem(i);

        menuItem.setText(item.getDisplayText());
        menuItem.setTag(item);
        return view;
    }
}
