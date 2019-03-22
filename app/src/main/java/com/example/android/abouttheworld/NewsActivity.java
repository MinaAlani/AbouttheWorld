package com.example.android.abouttheworld;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// Create the NewsActivity that extends from AppCompatActivity and implements LoaderManger.LoaderCallBacks
public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    // Create static final variable that holds the loader ID
    private static final int NEWS_LOADER_ID = 1;

    // Create static final variable that holds the JSON string
    private static final String NEWS_JSON = "https://content.guardianapis.com/search?show-fields=byline,thumbnail";

    // Create the static final variable that holds the api key
    private static final String API_KEY = BuildConfig.API_KEY;

    //  Create Tv variable that holds the empty tv
    TextView emptyStateText;

    // Create variable that holds the progress bar
    ProgressBar progressBar;

    // Create variable that holds the adapter
    private ArticleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Find the ProgressBar with ID progress_bar and set it to to progressBar variable
        progressBar = findViewById(R.id.progress_bar);

        // Find the tv with the ID empty_state_tv and set it to emptyStateTv variable
        emptyStateText = findViewById(R.id.empty_state_tv);

        // Find the RecyclerView with the ID recycler and set it up
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        // Set up layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {

            // Else hide the progress bar and set the text to the empty tv
            progressBar.setVisibility(View.GONE);
            emptyStateText.setText(getString(R.string.no_internet_connection));

        }

        // Construct new adapter and set it to the recyclerView
        adapter = new ArticleAdapter(this, new ArrayList<Article>());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the settings menu
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // On selecting the settings icon, start an intent that takes to the SettingsActivity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    // Set the onCreateLoader method
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        // Create shared preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the shared preference with key section
        String section = sharedPrefs.getString(getString(R.string.section_key), getString(R.string.settings_default_value));

        // Get the shared preference with key type
        String type = sharedPrefs.getString(getString(R.string.type_key), getString(R.string.settings_default_value));

        // Parse the JSON string to URI
        Uri baseUri = Uri.parse(NEWS_JSON);

        // Create Uri Builder of the provided URI
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append the api key
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        // If a section option query is selected
        if (!section.equals(getString(R.string.settings_default_value))) {
            // Append the query to the uri
            uriBuilder.appendQueryParameter("section", section);
        }

        // If a type option query is selected
        if (!type.equals(getString(R.string.settings_default_value))) {

            // Append the query to the uri
            uriBuilder.appendQueryParameter("type", type);
        }

        // Return a new NewsLoader
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    //Set the onLoadFinished method
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        // Hide the progress bar
        progressBar.setVisibility(View.GONE);
        // clear the adapter
        adapter.clear();
        // If there is a valid list of Articles then add them to the adapter's data set
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
        // Set the empty state text that appears when no data is found
        emptyStateText.setText(getString(R.string.no_news_data_found));
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

    }


}
