package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.fragments.TweetsListFragments;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragments.TweetSelectedListener {

    public static final int REQUEST_CODE = 20;
    public static final int RESULT_CODE = 10;

    ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // get the view pager
        vpPager = (ViewPager) findViewById(R.id.viewpager);

        // set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));

        // setup the tab layour to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);


        findViewById(R.id.ibCompose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        });
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

    @Override
    public void onTweetSelected(Tweet tweet) {
        Toast.makeText(this, tweet.body, Toast.LENGTH_LONG).show();
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
