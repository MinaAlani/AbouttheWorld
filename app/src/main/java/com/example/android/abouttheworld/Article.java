package com.example.android.abouttheworld;

import android.net.Uri;

// Create class Article that holds info about each news article
public class Article {
    // Create String variable that holds the section name
    private String mSection;

    // Create String variable that holds the news title
    private String mTitle;

    // Create String variable that holds the author name
    private String mAuthor;

    // Create String variable that holds the article's date
    private String mDate;

    // Create String variable that holds the thumbnail url path
    private String mThumbnail;

    // Create Uri variable that holds the Uri of the article
    private Uri mUrl;

    /**
     * Create Article constructor
     *
     * @param section   is the section name
     * @param title     is the news title
     * @param author    is the name of the author
     * @param date      is the date of the article
     * @param thumbnail is the thumbnail url path
     * @param url       is the url for the news article
     */

    public Article(String section, String title, String author, String date, String thumbnail, Uri url) {
        mSection = section;
        mTitle = title;
        mAuthor = author;
        mDate = date;
        mThumbnail = thumbnail;
        mUrl = url;
    }

    // Create getter methods
    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public Uri getUrl() {
        return mUrl;
    }
}
