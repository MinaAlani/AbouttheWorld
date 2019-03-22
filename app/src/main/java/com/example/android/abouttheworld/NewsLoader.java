package com.example.android.abouttheworld;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

// Create a NewsLoader that extends AsyncTaskLoader
public class NewsLoader extends AsyncTaskLoader<List<Article>> {

    // Create String variable that holds the data url
    private String mUrl;

    /**
     * Create the constructor
     *
     * @param context is the activity context
     * @param url     is the data url
     */

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }


    @Override
    // Override the onStartLoading method to force the load
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    // Override the loadInBackground method
    public List<Article> loadInBackground() {

        // If no url provided return null
        if (mUrl == null) {
            return null;
        }

        // Create a list of Article objects from fetching data using the url provided
        List<Article> articles = Utils.fetchNewsData(mUrl);

        // Return the list
        return articles;
    }
}
