package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragments;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragments.TweetSelectedListener {

    public static final int REQUEST_CODE = 20;

    ViewPager vpPager;
    private Toolbar toolbar;
    TwitterClient client;
    ImageButton ibProfile;
    Context context;



    private int[] tabIconsSelected = {
            R.drawable.ic_vector_home,
            R.drawable.ic_vector_search,
            R.drawable.ic_vector_notifications,
            R.drawable.ic_vector_messages
    };

    private int[] tabIconsNotSelected = {
            R.drawable.ic_vector_home_stroke,
            R.drawable.ic_vector_search_stroke,
            R.drawable.ic_vector_notifications_stroke,
            R.drawable.ic_vector_messages_stroke
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        context = this;
        final TextView tvActivityTitle = (TextView) findViewById(R.id.tvActivityTitle);
        ibProfile = (ImageButton) findViewById(R.id.ibProfile);

        client = TwitterApp.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String profilePicURL = response.getString("profile_image_url");
                    Glide.with(context)
                            .load(profilePicURL)
                            .bitmapTransform(new RoundedCornersTransformation(context, 100, 0))
                            .into(ibProfile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        // get the view pager
        vpPager = (ViewPager) findViewById(R.id.viewpager);

        // set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));

        // setup the tab layour to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        tabLayout.getTabAt(0).setIcon(tabIconsSelected[0]);
        tabLayout.getTabAt(1).setIcon(tabIconsSelected[1]);
        tabLayout.getTabAt(2).setIcon(tabIconsSelected[2]);
        tabLayout.getTabAt(3).setIcon(tabIconsSelected[3]);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tvActivityTitle.setText(TweetsPagerAdapter.tabTitles[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


//        findViewById(R.id.ibCompose).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
//                startActivityForResult(intent, REQUEST_CODE);
//                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
//            }
//        });


        //TODO below
//        ibProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onProfileView();
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }


    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);

        startActivity(i);
    }

    public void onImageSelected(Tweet tweet) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", tweet.user.screenName);
        startActivity(i);
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        Toast.makeText(this, tweet.body, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void postedTweet(Tweet tweet) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            // Extract name value from result extras
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));

            ((TweetsPagerAdapter) vpPager.getAdapter()).mPageReferenceMap.get(0).postedTweet(tweet);

            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Tweet sent!", Toast.LENGTH_SHORT).show();
        }
    }
}
