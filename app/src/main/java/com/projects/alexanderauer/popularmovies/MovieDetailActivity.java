package com.projects.alexanderauer.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.alexanderauer.popularmovies.entities.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by Alex on 12.03.2017.
 *
 * Activity for displaying the details of a movie
 *
 */

public class MovieDetailActivity extends AppCompatActivity {
    private final static String IMAGE_DB_BASE_URL = "http://image.tmdb.org/t/p/",
                                IMAGE_SIZE = "w342/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // activate the back arrow in the upper left corner of the app
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(getString(R.string.extra_movie))){
            // get movie object from extras
            Movie movie = getIntent().getParcelableExtra(getString(R.string.extra_movie));

            // set title
            TextView tvMovieTitle = (TextView) findViewById(R.id.movie_title);
            tvMovieTitle.setText(movie.getTitle());

            // set the movie poster with the Picasso library
            ImageView ivMoviePoster = (ImageView) findViewById(R.id.movie_poster);
            Picasso.with(this)
                    .load(IMAGE_DB_BASE_URL + IMAGE_SIZE + movie.getPosterPath())
                    .into(ivMoviePoster);

            // set the release year
            TextView tvReleaseDate = (TextView) findViewById(R.id.release_date);
            if(movie.getReleaseDate().contains("-"))
                tvReleaseDate.setText(movie.getReleaseDate().split("-")[0]);
            else
                tvReleaseDate.setText(movie.getReleaseDate());

            // set user rating value with the extension of the maximum
            TextView tvUserRating = (TextView) findViewById(R.id.user_rating);
            tvUserRating.setText(movie.getUserRating() + "/10");

            // set the plot synopsis
            TextView tvOverview = (TextView) findViewById(R.id.overview);
            tvOverview.setText(movie.getOverview());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // trigger the onBackPressed event when back arrow gets pressed
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
