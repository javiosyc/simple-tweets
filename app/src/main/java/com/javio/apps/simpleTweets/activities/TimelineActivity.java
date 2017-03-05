package com.javio.apps.simpleTweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.javio.apps.simpleTweets.R;
import com.javio.apps.simpleTweets.TwitterApplication;
import com.javio.apps.simpleTweets.adapters.TweetsArrayAdapter;
import com.javio.apps.simpleTweets.listeners.EndlessScrollListener;
import com.javio.apps.simpleTweets.models.Tweet;
import com.javio.apps.simpleTweets.models.Tweet_Table;
import com.javio.apps.simpleTweets.models.User;
import com.javio.apps.simpleTweets.network.TwitterClient;
import com.javio.apps.simpleTweets.utils.ToolBarUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class TimelineActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 200;
    private TwitterClient client;

    private LinkedList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;

    @BindView(R.id.lvTweets)
    ListView lvTweets;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeContainer;


    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient();//singleton client

        setupViews();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_post) {
            startComposeActivity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startComposeActivity() {
        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);

        startActivityForResult(intent, REQUEST_CODE);
    }

    private void setupViews() {

        ToolBarUtils.setUpToolbar("", this);

        ButterKnife.bind(this);

        tweets = new LinkedList<>();

        aTweets = new TweetsArrayAdapter(this, tweets);

        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.d("DEBUG", "page: " + page + " , totalItemCount" + totalItemsCount);

                Long maxId = tweets.get(tweets.size() - 1).getUid() - 1;

                loadNextData(maxId);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startComposeActivity();
            }
        });

        loadTweetsFromDB();

        if (tweets.size() == 0)
            populateTimeline();
        //swipeRefreshLayoutInit();
    }

    private void loadTweetsFromDB() {
        List<Tweet> tweets = SQLite.select().from(Tweet.class).orderBy(Tweet_Table.uid,false).queryList();
        aTweets.addAll(tweets);
        aTweets.notifyDataSetChanged();
    }

    private void swipeRefreshLayoutInit() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("DEBUG", "");
                populateTimeline();
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));

            tweets.addFirst(tweet);
            aTweets.notifyDataSetChanged();
        }
    }

    private void loadNextData(long maxId) {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());

                List<Tweet> tweets = Tweet.fromJSONArray(json);
                saveTweets(tweets);
                aTweets.addAll(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        }, maxId);
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());

                List<Tweet> tweets = Tweet.fromJSONArray(json);

                saveTweets(tweets);

                aTweets.addAll(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void saveTweets(List<Tweet> tweets) {
        for (Tweet tweet : tweets) {
            User user = tweet.getUser();
            user.save();
            tweet.save();
        }
    }
}
