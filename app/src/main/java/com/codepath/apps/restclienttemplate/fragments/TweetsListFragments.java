package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by acamara on 7/3/17.
 */

public abstract class TweetsListFragments extends Fragment implements TweetAdapter.TweetAdapterListener {

    public interface TweetSelectedListener {
        // handle tweet selection
        public void onTweetSelected(Tweet tweet);
        public void postedTweet(Tweet tweet);
    }

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    public SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);

        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvTweets.addItemDecoration(itemDecoration);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets, this);
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTweets.setAdapter(tweetAdapter);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return v;
    }

    public void addItems (JSONArray response){
        Tweet tweet = null;
        try {
            for (int i = 0; i<response.length(); i++)
            {
                tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        ((TweetSelectedListener) getActivity()).onTweetSelected(tweet);
    }

    public void postedTweet(Tweet tweet)
    {
        tweets.add(0, tweet);
        tweetAdapter.notifyItemInserted(0);
        rvTweets.getLayoutManager().scrollToPosition(0);
    }

    public abstract void fetchTimelineAsync(int page);


    public void fetchTweets(JSONArray response) {
        //Log.d("TwitterClient", response.toString());
        // iterate through the JSON Array
        // for each entry, deserialize the JSON Object
        for (int i = 0; i < response.length(); i++) {
            // convert each object to a tweet model
            // add that Tweet model our data source
            // notify the adapter that we've added an item
            Tweet tweet = null;
            try {
                tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size() - 1);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
