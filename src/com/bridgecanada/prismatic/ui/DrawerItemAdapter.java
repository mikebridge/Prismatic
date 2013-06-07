package com.bridgecanada.prismatic.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bridgecanada.prismatic.MainActivity;
import com.bridgecanada.prismatic.R;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 27/05/13
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class DrawerItemAdapter extends BaseAdapter {

    private static LayoutInflater _inflater;
    private final Context _context;
    List<DrawerItem> _menuItems;

    public DrawerItemAdapter(Context context, List<DrawerItem> menuItems) {
        this._context = context;
       _menuItems = menuItems;
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _menuItems.size();
    }

    @Override
    public Object getItem(int i) {
        return _menuItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view=convertView;
        if(convertView==null) {
            view = _inflater.inflate(R.layout.drawer_list_item, null);
        }

        //view.setOnClickListener(new DrawerItemClickListener() );
        TextView menuItem = (TextView)view.findViewById(R.id.drawer_text_item); // title
        ImageView thumb_image=(ImageView)view.findViewById(R.id.drawer_item_icon); // icon
        DrawerItem item = (DrawerItem) getItem(i);

        //HashMap &lt;String, String&gt; song = new HashMap&lt;String, String&gt;();
        //song = data.get(position);
        menuItem.setText(item.getTitle());
        menuItem.setTag(item);
        // Setting all values in listview
        //title.setText(song.get(CustomizedListView.KEY_TITLE));
        //artist.setText(song.get(CustomizedListView.KEY_ARTIST));
        //duration.setText(song.get(CustomizedListView.KEY_DURATION));
        //imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return view;
    }

//    private class DrawerItemClickListener implements ListView.OnClickListener {
//
//
//        @Override
//
//        public void onItemClick(AdapterView parent, View view, int position, long id) {
//            Bundle args = new Bundle();
//            DrawerItem item = (DrawerItem) getItem(position);
//            args.putString("msg", "Clicked on id "+id);
//            Toast toast = Toast.makeText(_context, "clicked on "+item.getTitle(), Toast.LENGTH_SHORT);
//            toast.show();
//            //selectItem(position);
//        }
//
//        @Override
//        public void onClick(View view) {
//            //To change body of implemented methods use File | Settings | File Templates.
//        }
//    }
}
