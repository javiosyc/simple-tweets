package com.javio.apps.simpleTweets.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "d1Uin1G7G4495XgLnzpYbWzUY";       // Change this
    public static final String REST_CONSUMER_SECRET = "ICIzZgmni5kt6ihSxAPwvYn8g6YfA0atWmS51fKVPrBrgbJRV5"; // Change this

    //public static final String REST_CONSUMER_KEY = "5mMF4im8trqd8xXS0hud69ugL";       // Change this
    //public static final String REST_CONSUMER_SECRET = "P3qz3AOxhYJ7jT5dDcTaa7x4HCRoVw6cCNfnRnBerm5cj2wGGO"; // Change this

    public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)


    private static final int DEFAULT_COUNT = 25;

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }

    //Method == ENDPOINT

    /*
    Get the home timeline for the user
		Get statuses/home_timeline.json
		count= 25
		since_id=1
     */
    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        getHomeTimeline(handler, 0);
    }


    public void getHomeTimeline(AsyncHttpResponseHandler handler, long maxId) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Specify the params
        RequestParams params = new RequestParams();
        params.put("count", DEFAULT_COUNT);
        params.put("since_id", 1);

        if (maxId != 0) {
            params.put("max_id", maxId);
        }

        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

    //COMPOSE TWEET
    public void postStatus(AsyncHttpResponseHandler handler, String status) {
        String apiUrl = getApiUrl("statuses/update.json");
        // Specify the params

        RequestParams params = new RequestParams();
        params.put("status", status);

        //Execute the request
        getClient().post(apiUrl, params, handler);
    }


    //HomeTimeLine = Gets us the home function


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
