package com.javio.apps.simpleTweets.utils;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.javio.apps.simpleTweets.R;


/**
 * Created by javiosyc on 2017/2/26.
 */

public class ToolBarUtils {

    private static final int TOOL_BAR_LOGO = R.drawable.ic_twitter_24;

    private static final String SETTINGS_FILE = "Settings";

    private ToolBarUtils() {
    }

    public static void setUpToolbar(String title, AppCompatActivity appCompatActivity) {
        Toolbar toolbar = (Toolbar) appCompatActivity.findViewById(R.id.toolbar);

        toolbar.setLogo(TOOL_BAR_LOGO);

        toolbar.setTitle(title);

        toolbar.setBackgroundColor(ContextCompat.getColor(appCompatActivity, R.color.colorActionBar));

        appCompatActivity.setSupportActionBar(toolbar);
    }

}
