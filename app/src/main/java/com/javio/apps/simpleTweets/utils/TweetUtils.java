package com.javio.apps.simpleTweets.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by javiosyc on 2017/3/3.
 */

public class TweetUtils {
    private TweetUtils() {
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();

            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS ).toString();

            relativeDate = relativeDate.replaceAll(" minute(s?)","m").replaceAll(" hour(s?)","h").replaceAll(" day(s?)","d");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
