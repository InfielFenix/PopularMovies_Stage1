package com.projects.alexanderauer.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.projects.alexanderauer.popularmovies.customAdapters.MovieGridAdapter;
import com.projects.alexanderauer.popularmovies.entities.Movie;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Alex on 09.03.2017.
 *
 * The main fragment that contains the grid view with the movie collection
 *
 */

public class MainActivityFragment extends Fragment {
    final static String THE_MOVIE_DB_BASIC_URL = "https://api.themoviedb.org/3/movie/",
                        THE_MOVIE_DB_PARAM_API_KEY = "api_key",
                        // TODO: set your own API key
                        THE_MOVIE_DB_API_KEY = "<api_key>",
                        THE_MOVIE_DB_URL_EXT_MOST_POPULAR = "popular",
                        THE_MOVIE_DB_URL_EXT_TOP_RATED = "top_rated";

    private String currentSortType = THE_MOVIE_DB_URL_EXT_MOST_POPULAR;

    private GridView gridView;

    private ArrayList<Movie> movieBuffer;

    public MainActivityFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load data of the previous session, if available
        if (savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.extra_movies)))
            loadMovies(currentSortType);
        else {
            movieBuffer = savedInstanceState.getParcelableArrayList(getString(R.string.extra_movies));
            currentSortType = savedInstanceState.getString(getString(R.string.extra_sort_type));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate the fragment_main layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // get and lay down the grid view
        gridView = (GridView) rootView.findViewById(R.id.movie_grid);

        // set the movies
        setMovies(movieBuffer);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // store data
        outState.putParcelableArrayList(getString(R.string.extra_movies), movieBuffer);
        outState.putString(getString(R.string.extra_sort_type), currentSortType);

        super.onSaveInstanceState(outState);
    }

    public void loadMovies(String sortType) {
        // check if the new sort type is not the same as the current one
        if (sortType != this.currentSortType || movieBuffer == null) {
            // set the new sort type as the current one
            this.currentSortType = sortType;

            // check the internet connection, otherwise the app would crash by doing the
            // web request
            if(isConnected2Internet())
                // execute asynchronous task to load the movies
                new GetMovieData().execute(sortType);
            else
                Toast.makeText(getActivity().getApplicationContext(), R.string.no_internet_conn,Toast.LENGTH_LONG).show();
        }
    }

    public void setMovies(ArrayList<Movie> movies) {
        // buffer the movies in a local array list
        movieBuffer = movies;

        // load the data into the grid view
        if (movieBuffer != null && movieBuffer.size() > 0)
            gridView.setAdapter(new MovieGridAdapter(getActivity(), movies));
    }

    // method to check internet connection
    private boolean isConnected2Internet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // asynchronous task which sends a web request and retrieves the movie data from themoviedb
    public class GetMovieData extends AsyncTask<String, String, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... sortOrders) {
            if (sortOrders.length == 0)
                return null;

            String sortOrder = sortOrders[0];

            // build the correct URI
            Uri builtUri = Uri.parse(THE_MOVIE_DB_BASIC_URL + sortOrder).buildUpon()
                    .appendQueryParameter(THE_MOVIE_DB_PARAM_API_KEY, THE_MOVIE_DB_API_KEY).build();

            HttpURLConnection urlConnection = null;
            try {
                // get URL
                URL url = new URL(builtUri.toString());
                // open connection
                urlConnection = (HttpURLConnection) url.openConnection();

                // create an input stream reader
                InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());
                // read the retrieved data with the Gson library which converts and puts it directly
                // into a MovieCollection object
                MovieCollection movieCollection = new Gson().fromJson(reader,MovieCollection.class);

                return movieCollection.movies;
            } catch (MalformedURLException e) {
                Log.e("MalformedURLException", e.getMessage());
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }

        // just for the Gson library to easy convert the retrieved data into a set of movie objects
        private class MovieCollection {
            @SerializedName("results")
            ArrayList<Movie> movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

            // set the movies as soon as loading is finished
            setMovies(movies);
        }
    }
}
