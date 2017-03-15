package com.projects.alexanderauer.popularmovies.customAdapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.projects.alexanderauer.popularmovies.MovieDetailActivity;
import com.projects.alexanderauer.popularmovies.R;
import com.projects.alexanderauer.popularmovies.entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Alex on 09.03.2017.
 *
 * Adapter for the movie poster collection
 *
 */

public class MovieGridAdapter extends ArrayAdapter<Movie> {
    private final static String IMAGE_DB_BASE_URL = "http://image.tmdb.org/t/p/",
                                IMAGE_SIZE = "w500/";

    public MovieGridAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get current item to display
        final Movie movie = getItem(position);

        // recycle view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        // get image view and set content via the Picasso library
        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.movie_poster);
        Picasso.with(this.getContext())
                .load(IMAGE_DB_BASE_URL + IMAGE_SIZE + movie.getPosterPath())
                .into(moviePoster);

        // set a click listener to jump into the detail of a movie
        moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create intent, put the movie data object into its extras and start the activity
                Intent startMovieDetailActivity = new Intent(getContext(), MovieDetailActivity.class);

                startMovieDetailActivity.putExtra(getContext().getString(R.string.extra_movie),movie);

                getContext().startActivity(startMovieDetailActivity);
            }
        });

        return convertView;
    }
}
