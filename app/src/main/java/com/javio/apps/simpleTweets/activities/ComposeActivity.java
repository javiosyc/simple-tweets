package com.javio.apps.simpleTweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.javio.apps.simpleTweets.R;
import com.javio.apps.simpleTweets.TwitterApplication;
import com.javio.apps.simpleTweets.models.Tweet;
import com.javio.apps.simpleTweets.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    @BindView(R.id.btTweet)
    Button btTweet;
    @BindView(R.id.etTweet)
    EditText editText;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient();//singleton client


        setupViews();
    }

    private void setupViews() {

        ButterKnife.bind(this);
    }


    @OnClick(R.id.btTweet)
    public void clickTweet(View view) {
        if (TextUtils.isEmpty(editText.getText())) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        client.postStatus(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());

                Tweet tweet = Tweet.fromJSON(json);

                Intent data = new Intent();
                data.putExtra("tweet", Parcels.wrap(tweet));
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        }, editText.getText().toString());
    }

}
