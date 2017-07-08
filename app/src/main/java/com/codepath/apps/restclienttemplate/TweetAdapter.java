package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by acamara on 7/3/17.
 */


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    public List<Tweet> tweets;
    Context context;
    private TweetAdapterListener listener;

    //declare an interface required by the viewHolder
    public interface TweetAdapterListener{
        public void onItemSelected(View view, int position, boolean isPic);
    }

    //pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener){
        this.tweets = tweets;
        this.listener = listener;
    }
    //for each row, inflate the layout and cache references into viewholder

    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);


        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }


    //bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // get data according to position
        Tweet tweet = tweets.get(position);


        //populate the views according to this data
        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvHandle.setText("@" + tweet.user.screenName);
        holder.tvTimeStamp.setText(tweet.relativeDate);



        holder.ivProfileImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // adapter to tell fragment to tell activity
                listener.
                        onItemSelected(v, position, true);

            }
        });

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 150,0))
                .into(holder.ivProfileImage);

    }




    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //create viewholderclass

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvHandle;
        public TextView tvTimeStamp;


        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            
            //handle the click event
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        //get the position of row element
                        int position = getAdapterPosition();

                        // fire the listener callback
                        listener.onItemSelected(view, position, false);
                    }
                }
            });
        }


    }
}