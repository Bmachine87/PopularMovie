package com.example.android.popularmovie;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
    // JSON Keys
    private static final String TITLE = "title";
    private static final String POSTER = "poster_path";
    private static final String DESCRIPTION = "overview";
    private static final String RATING = "vote_average";
    private static final String RELEASE = "release_date";

    private final String language;
    private final AsyncTaskCompleteListener<List<Movie>> listener;

    FetchMovieTask(String language, AsyncTaskCompleteListener<List<Movie>> listener) {
        this.language = language;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStart();
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        try {
            Uri uri = Uri.parse("https://api.themoviedb.org/3/movie/").buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter("api_key", String.valueOf(R.string.API_KEY))
                    .appendQueryParameter("language", language)
                    .appendQueryParameter("page", params[1])
                    .build();
            URL url = new URL(uri.toString());

            String jsonMoviesResponse = getResponseFromHttpUrl(url);

            return getMoviesDataFromJson(jsonMoviesResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> moviesList) {
        super.onPostExecute(moviesList);
        listener.onTaskComplete(moviesList);
    }

    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            return builder.toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    private List<Movie> getMoviesDataFromJson(String moviesJsonStr) throws JSONException {
        List<Movie> moviesList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(moviesJsonStr);
        JSONArray movies = jsonObject.getJSONArray("results");
        for (int i = 0; i < movies.length(); i++) {
            JSONObject movieDetail = movies.getJSONObject(i);

            /* movieName is original name of movie
             * posterPath is image url of the poster of movie
             * movieDescription is the plot of movie
             * userRating is user rating of movie
             * releaseDate is release date of movie
            */

            String movieName = movieDetail.getString(TITLE);
            String posterPath = "http://image.tmdb.org/t/p/w500/" + movieDetail.getString(POSTER);
            String movieDescription = movieDetail.getString(DESCRIPTION);
            String userRating = movieDetail.getString(RATING);
            String releaseDate = movieDetail.getString(RELEASE);

            moviesList.add(new Movie(movieName, posterPath, movieDescription, userRating, releaseDate));
        }
        return moviesList;
    }
}
