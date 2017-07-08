package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by acamara on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    public static String tabTitles[] = new String [] {"Home", "Mentions"};
    private Context context;
    Toolbar toolbar;
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
