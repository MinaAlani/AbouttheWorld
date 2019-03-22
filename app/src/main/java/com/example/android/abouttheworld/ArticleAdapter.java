package com.example.android.abouttheworld;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;


//  Create ArticleAdapter that binds each article data with the list item element
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    // Create global variables for the context and the List of articles
    private Context context;
    private List<Article> articles;

    // Create the constructor
    public ArticleAdapter(@NonNull Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    // Create public method to add all adapter data
    public void addAll(@NonNull final List<Article> list) {
        articles.addAll(list);
        notifyDataSetChanged();
    }

    // Create public method to clear adapter data
    public void clear() {
        final int size = articles.size();
        articles.clear();
        notifyItemRangeRemoved(0, size);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // Bind the views using butterknife
        @BindView(R.id.section)
        TextView sectionName;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.article_container)
        ConstraintLayout articleContainer;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bind(int position) {

            // Get the current Article
            final Article currentArticle = articles.get(position);

            //If the section name is an empty String, hide the section name tv
            if (currentArticle.getSection().equals("")) {
                sectionName.setVisibility(View.GONE);
            } else {
                // Else set the section name to it
                sectionName.setText(currentArticle.getSection());
            }

            // Set the article title to the title tv
            title.setText(currentArticle.getTitle());

            // Set the author name to it to the author tv
            author.setText(currentArticle.getAuthor());

            // Set the date to the date tv
            date.setText(getDate(currentArticle.getDate()));

            // Check if a thumbnail is available or not
            if (currentArticle.getThumbnail().equals("") || currentArticle.getThumbnail() == null) {
                // Hide the image ImageView if the thumbnail isn't provided
                image.setVisibility(View.GONE);
            } else {
                // Else if the thumbnail path exists, load the image using Picasso into the image ImageView
                Picasso.get().load(currentArticle.getThumbnail()).into(image);

            }

            // Set a click listener on the article container layout that opens an intent to view the web page
            // using the url provided for the article
            articleContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, currentArticle.getUrl());
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            });

        }

        // Create private method to set the date format
        private String getDate(String date) {
            try {
                // The date from the Api is in this format 2018-03-23T05:34:37Z, So make a Simple date format
                // That uses the same format.
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                // Parse the passed in date by using the formatter.
                Date value = formatter.parse(date);

                // Set the desired format
                SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a" + "\nMMM dd, yyyy");

                // Get the time zone of the device
                dateFormatter.setTimeZone(TimeZone.getDefault());
                // Format the date using the formatter.
                date = dateFormatter.format(value);
            } catch (Exception e) {
                date = " 00:00\n00-00-0000";
                e.printStackTrace();
            }
            // Return the date
            return date;
        }
    }
}
