package com.bridgecanada.prismatic.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bridgecanada.prismatic.R;
import com.bridgecanada.prismatic.data.CardData;
import com.bridgecanada.prismatic.data.Image;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import org.joda.time.DateTime;

public class CardItemViewHolder {

    TextView itemTitle;
    ImageView feedIcon;
    ImageView largeImage;
    ImageView halfImage;
    TextView itemContent;
    TextView feedTitle;
    TextView keywordAuthor;
    TextView keywordDate;

    ImageView recommendImage;
    TextView recommended;
    ImageView shareImage;
    TextView shared;
    ImageView readLaterImage;
    ImageView removeImage;

    Context _context;

    CardItemViewHolder(Context context) {
        this._context = context;

    }


    void inflateFooter(View convertView) {
        // TODO: how do we get different colours for this?  Or does it only change when
        // you click on it?



        //holder.recommendImage=(ImageView) convertView.findViewById(R.id.recommend_image);
        recommended=(TextView) convertView.findViewById(R.id.recommend_number);
        recommendImage = (ImageView) convertView.findViewById(R.id.recommend_image);
        shared=(TextView) convertView.findViewById(R.id.share_number);
        shareImage = (ImageView) convertView.findViewById(R.id.share_image);

        readLaterImage = (ImageView) convertView.findViewById(R.id.save_for_later_image);
        removeImage = (ImageView) convertView.findViewById(R.id.remove_item_image);


    }
    void setFooterData(CardData cardData, CardAdapter cardAdapter) {

        recommended.setText("" + cardData.getPrismaticActivity().getBookmark());
        recommended.setTag("Recommend "+cardData.getId());
        recommendImage.setTag("Recommend "+cardData.getId());

        recommended.setOnClickListener(cardAdapter);
        recommendImage.setOnClickListener(cardAdapter);
        shared.setText("" + cardData.getPrismaticActivity().getShare());

        shared.setTag("Share "+cardData.getId());
        shareImage.setTag("Share "+cardData.getId());

        shared.setOnClickListener(cardAdapter);
        shareImage.setOnClickListener(cardAdapter);

        readLaterImage.setTag("ReadLater "+cardData.getId());
        readLaterImage.setOnClickListener(cardAdapter);


        removeImage.setTag("Remove "+cardData.getId());
        removeImage.setOnClickListener(cardAdapter);
    }

    void inflateTitle (View convertView) {
        itemTitle=(TextView) convertView.findViewById(R.id.title);

    }
    void setTitleData(String title) {
        //Log.w(TAG, "Setting card title: " + title);
        itemTitle.setText(title);
    }
    void inflateContent (View convertView) {
        itemContent=(TextView) convertView.findViewById(R.id.content);
    }
    void setContent(String content, CardAdapter.IMAGE_TYPE imageType) {

        if (imageType == CardAdapter.IMAGE_TYPE.NONE || imageType == CardAdapter.IMAGE_TYPE.HALF) {

            String shortContent = content;
            if (content != null && content.length() > 255) {

                // todo: Make the truncation smarter, e.g. truncate at space

                shortContent = content.substring(0, 225) + "...";
            }
            itemContent.setText(shortContent);
            itemContent.setVisibility(View.VISIBLE);

        }   else {

            itemContent.setText("");
            itemContent.setVisibility(View.GONE);

        }
    }

    void inflateFeed(View convertView) {
        feedTitle = (TextView) convertView.findViewById(R.id.feedTitle);
        feedIcon = (ImageView) convertView.findViewById(R.id.feed_icon);

    }
    void setFeedData(CardData cardData, CardAdapter cardAdapter) {

        String feedName = cardData.getFeedName();
        String feedUrl = cardData.getFeedUrl();
        //String feedImage = cardData.getFeedImage();  the ICO file?
        String feedHighResImage = cardData.getFeedHighResImage();


        feedIcon.setVisibility(View.GONE);

        if (feedName == null) {

            feedTitle.setText(null);
            feedTitle.setVisibility(View.GONE);
            feedTitle.setOnClickListener(null);

            feedIcon.setOnClickListener(null);
            //holder.feedTitle.setTag(null);

        } else {

            feedTitle.setText(feedName);
            feedTitle.setOnClickListener(cardAdapter);
            feedTitle.setTag(feedUrl);

            //String theIcon = feedIcon;
            String theIcon = feedHighResImage;
            if (theIcon != null && !theIcon.equals("")) {
                // TODO: Move this logic together with the Card Adapter
                ImageLoader.getInstance().loadImage(theIcon, new ImageSize(30, 30), cardAdapter.setImageListener(feedIcon));
                feedIcon.setOnClickListener(cardAdapter);
                feedIcon.setTag(feedUrl);

            }

        }
    }
    void inflateKeywords(View convertView) {
        keywordAuthor=(TextView) convertView.findViewById(R.id.keywordAuthor);
        keywordDate=(TextView) convertView.findViewById(R.id.keywordDate);
    }

    public void setKeywords(CardData cardData, CardItemViewHolder holder) {

        if(cardData.getAuthor() != null &&
                cardData.getAuthor().getName() !=null &&
                !cardData.getAuthor().getName().equals("")) {

            String byAuthor = String.format(_context.getString(R.string.by_author), cardData.getAuthor().getName());
            holder.keywordAuthor.setText(byAuthor);
            holder.keywordAuthor.setVisibility(View.VISIBLE);

        } else {

            clearTextView(holder.keywordAuthor);

        }

        if(cardData.getDate() > 0) {

            long dateLong = cardData.getDate();
            //DateTime date = new DateTime(dateLong);

            String timeStr = (new TimeAgoFormatter(DateTime.now())).format(new DateTime(dateLong));

            holder.keywordDate.setText(timeStr);
            holder.keywordDate.setVisibility(View.VISIBLE);

        } else {

            clearTextView(holder.keywordDate);

        }

//        if(cardData.getTopics() !=null) {
//            for(String topic: cardData.getTopics()) {
//                holder.keywordTopic1.setText(topic);
//                holder.keywordTopic1.setVisibility(View.VISIBLE);
//            }
//        }
    }


    private void clearTextView(TextView textView) {
        textView.setText(null);
        textView.setVisibility(View.GONE);
    }

    public void inflateImages(View convertView) {

        largeImage=(ImageView) convertView.findViewById(R.id.large_image);
        //ImageView largeImage = (ImageView) convertView.findViewById(R.id.large_image);
        halfImage=(ImageView) convertView.findViewById(R.id.half_image);

    }

    public CardAdapter.IMAGE_TYPE setImage(Image image, CardAdapter cardAdapter) {

        largeImage.setVisibility(View.GONE);
        //largeImage.setVisibility(View.GONE);
        halfImage.setVisibility(View.GONE);

        if (image != null) {
            //Log.w(TAG, "## IMAGE AT Position "+position +" FROM " + image.getUrl());
            //holder.image.set

            DisplayMetrics metrics = cardAdapter.context.getResources().getDisplayMetrics();
            int displayWidth = metrics.widthPixels;
            //int displayHeight = metrics.heightPixels;
            //Log.e(TAG, "DEVICE WIDTH IS "+displayWidth);
            //Log.e(TAG, "IAGE WIDTH IS "+image.getWidth());

            boolean useLarge= image.getWidth() > displayWidth / 2;

            if (useLarge) {
                //holder.largeImage.setVisibility(View.VISIBLE);
                //largeImage.setVisibility(View.VISIBLE);
                //ImageLoader.getInstance().displayImage(image.getUrl(), holder.largeImage);
                //_imageLoader.displayImage(image.getUrl(), largeImage);
                ImageLoader.getInstance().loadImage(image.getUrl(), cardAdapter.setImageListener(largeImage));

                return CardAdapter.IMAGE_TYPE.LARGE;
            } else {
                //holder.halfImage.setVisibility(View.VISIBLE);
                //ImageLoader.getInstance().displayImage(image.getUrl(), holder.halfImage);
                ImageLoader.getInstance().loadImage(image.getUrl(), cardAdapter.setImageListener(halfImage));
                return CardAdapter.IMAGE_TYPE.HALF;
            }
        }
        else {

            //holder.largeImage.setVisibility(View.GONE);
            //largeImage.setVisibility(View.GONE);
            //holder.halfImage.setVisibility(View.GONE);
            return CardAdapter.IMAGE_TYPE.NONE;
        }
    }
    //holder.keywordTopic1=(TextView) convertView.findViewById(R.id.keywordTopic1);
    //holder.keywordTopic2=(TextView) convertView.findViewById(R.id.keywordTopic2);
    //holder.keywordTopic3=(TextView) convertView.findViewById(R.id.keywordTopic3);
    //holder.keywordTopic4=(TextView) convertView.findViewById(R.id.keywordTopic4);
    //holder.keywordTopic5=(TextView) convertView.findViewById(R.id.keywordTopic5);

//        TextView keywordTopic1;
//        TextView keywordTopic2;
//        TextView keywordTopic3;
//        TextView keywordTopic4;
//        TextView keywordTopic5;

}