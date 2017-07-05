package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by acamara on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String [] {"Home", "Search", "Mentions", "Messages"};
    private Context context;
    public ConcurrentHashMap<Integer, TweetsListFragments> mPageReferenceMap = new ConcurrentHashMap();

    public TweetsPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    //return total # of fragments
    @Override
    public int getCount() {
        return tabTitles.length;
    }

    //return the fragment to use depending on the position
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {
            HomeTimelineFragment r = new HomeTimelineFragment();
            mPageReferenceMap.put(position, r);
            return r;

        } else if (position == 1) {
            MentionsTimelineFragment r = new MentionsTimelineFragment();
            mPageReferenceMap.put(position, r);
            return r;
        } else if (position == 2) {
            MentionsTimelineFragment r = new MentionsTimelineFragment();
            mPageReferenceMap.put(position, r);
            return r;
        } else if (position == 3) {
            MentionsTimelineFragment r = new MentionsTimelineFragment();
            mPageReferenceMap.put(position, r);
            return r;
        } else {
            return null;
        }

    }

    //return title
    @Override
    public CharSequence getPageTitle(int position) {
        // generate title based on item position
        return null;
    }
}
