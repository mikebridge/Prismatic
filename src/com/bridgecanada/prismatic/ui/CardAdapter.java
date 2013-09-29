package com.bridgecanada.prismatic.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bridgecanada.prismatic.R;
import com.bridgecanada.prismatic.data.CardData;
import com.bridgecanada.prismatic.data.Image;

import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 21/04/13
 */

public class CardAdapter extends BaseAdapter implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    enum IMAGE_TYPE {LARGE, HALF, NONE}

    // TODO: can we store the context here?
    Context context;

    private ArrayList<CardData> cardArray = new ArrayList<CardData>();

    private LayoutInflater layoutInflater;

    public CardAdapter(Context context, ArrayList<CardData> initialItems) {
        layoutInflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cardArray = initialItems;
        this.context = context;
        //initImageLoader();

    }

    @Override
    public int getCount() {
        return cardArray.size();
    }

    @Override
    public Object getItem(int position) {
       return cardArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        // TODO: Clean this up
        CardItemViewHolder holder = new CardItemViewHolder(context);

        if(convertView==null){

            convertView = layoutInflater.inflate(R.layout.card_item, null);

        }

        final CardData cardData = cardArray.get(position);

        String content = cardData.getText();
        Image image = cardData.getImg();

        holder.inflateTitle(convertView);
        holder.setTitleData(cardData.getTitle());

        holder.inflateKeywords(convertView);
        holder.setKeywords(cardData, holder);

        holder.inflateFeed(convertView);
        holder.setFeedData(cardData, this);

        holder.inflateImages(convertView);
        IMAGE_TYPE imageType = holder.setImage(image, this);

        holder.inflateContent(convertView);
        holder.setContent(content, imageType);

        if(cardData.getPrismaticActivity() != null) {
            // inflate the footer and cache in holder
            holder.inflateFooter(convertView);

            // set the footer data from the card
            holder.setFooterData(cardData, this);
        }
        return convertView;

    }

    public void clearItems() {
        cardArray.clear();
    }

    SimpleImageLoadingListener setImageListener(final ImageView imageView, final RelativeLayout imageLoadingView) {

        return new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                try {
                    if (loadedImage != null) {


                        imageView.setImageBitmap(loadedImage);
                        imageView.setVisibility(View.VISIBLE);

                    } else {
                        imageView.setVisibility(View.GONE);
                    }
                    if (imageLoadingView !=null) {

                        imageLoadingView.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    // trying to fix null ptr error
                    ex.printStackTrace();
                }
            }
        };
    }


    @Override
    public void onClick(View view) {
        //ListView listView = (ListView) view.getParent();
        String tag = (String) view.getTag();
        //Object obj = listView.
        //CardData cardData = (CardData) obj;
        //itemClickedSupport.firePropertyChange("openUrl", null, cardData.getUrl());

        Toast.makeText(this.context, "clicked "+tag, Toast.LENGTH_SHORT).show();
    }

    /*
    protected void setAnimationFromIntent(ImageTagFactory imageTagFactory) {
        if (intentHasAnimation()) {
            imageTagFactory.setAnimation(getIntent().getIntExtra("animated", AnimationHelper.ANIMATION_DISABLED));
        }
    }

    private boolean intentHasAnimation() {
        return getIntent().hasExtra("animated") &&
                getIntent().getIntExtra("animated", AnimationHelper.ANIMATION_DISABLED) != AnimationHelper.ANIMATION_DISABLED;
    }
    */


}
