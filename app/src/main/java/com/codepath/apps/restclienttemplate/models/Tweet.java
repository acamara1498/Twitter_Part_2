package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by acamara on 7/3/17.
 */

@Parcel
public class Tweet {
    //list out the attributes
    public String body;
    public long uid;// database id for the tweet
    public User user;
    public String createdAt;
    public String relativeDate;

    public Tweet(){
    }

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.relativeDate = " • "+ getRelativeTimeAgo(tweet.createdAt); //TODO - Change the relative date into the correct format
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));


        return  tweet;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        relativeDate = relativeDate.replaceAll(" ago","");
        relativeDate = relativeDate.replaceAll(" minutes","m");
        relativeDate = relativeDate.replaceAll(" minute","m");
        relativeDate = relativeDate.replaceAll(" hours","h");
        relativeDate = relativeDate.replaceAll(" hour","h");
        relativeDate = relativeDate.replaceAll(" seconds","s");
        relativeDate = relativeDate.replaceAll(" second","s");
        relativeDate = relativeDate.replaceAll(" days","d");
        relativeDate = relativeDate.replaceAll(" day","d");







        return relativeDate;
    }
}
