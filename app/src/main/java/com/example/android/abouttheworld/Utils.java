package com.example.android.abouttheworld;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


// Create the class Utils that holds the helper methods needed in fetching JSON data
public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();


    public static List<Article> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create a list of Article objects
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        // Return the articles list
        return articles;
    }


    // Create helper method that creates URL from String
    private static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    // Create helper method that makes the HTTP request
    private static String makeHttpRequest(URL url) throws IOException {
        // Create String variable that holds the json response
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        // Create variables that hold the HttpURLConnection and the InputStream
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        // Try to make the connection
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful, then read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                // Else log the error code
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            // Finally disconnect if connected and close the input stream if exists
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        // Return the JSON response
        return jsonResponse;
    }

    // Create helper method to read the input stream
    private static String readFromStream(InputStream inputStream) throws IOException {
        // Create a new StringBuilder
        StringBuilder output = new StringBuilder();

        // If the input stream isn't null
        if (inputStream != null) {

            // Create new InputStreamReader
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            // Wrap the InputStreamReader in a BufferedReader
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // Read the stream
            String line = reader.readLine();

            // Append the lines to the StringBuilder
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        // Return a string
        return output.toString();
    }

    private static List<Article> extractFeatureFromJson(String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create new List of Article objects
        List<Article> articles = new ArrayList<>();

        // Create String variables that hold the section name, the title, the author,the date, the thumbnail path
        // and the url and initialize them to empty Strings
        String sectionName = "";
        String title = "";
        String date = "";
        String author = "";
        String thumbnail = "";
        String url = "";

        // Create Uri variable that holds the web page of the article url
        Uri articleUri = null;

        // // Try parsing the JSON response
        try {
            JSONObject rootJSON = new JSONObject(newsJSON);
            JSONObject response = rootJSON.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            if (resultsArray.length() > 0) {
                for (int i = 0; i < resultsArray.length(); i++) {


                    JSONObject article = resultsArray.getJSONObject(i);
                    if (article.has("fields")) {
                        JSONObject fields = article.getJSONObject("fields");
                        if (fields.has("byline")) {
                            author = fields.getString("byline");
                        }
                        if (fields.has("thumbnail")) {
                            thumbnail = fields.getString("thumbnail");
                        }
                    }

                    if (article.has("sectionName")) {
                        sectionName = article.getString("sectionName");
                    }
                    if (article.has("webTitle")) {
                        title = article.getString("webTitle");
                    }
                    if (article.has("webPublicationDate")) {
                        date = article.getString("webPublicationDate");
                    }
                    if (article.has("webUrl")) {
                        url = article.getString("webUrl");
                        articleUri = Uri.parse(url);
                    }


                    // Use the response in creating new Article objects in articles list
                    articles.add(new Article(sectionName, title, author, date, thumbnail, articleUri));
                }
            }
        } catch (JSONException e) {
            // Catch the exception and log the error
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        // Return the list of articles
        return articles;
    }


}
