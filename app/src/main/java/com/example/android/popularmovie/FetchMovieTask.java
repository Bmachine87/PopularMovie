package com.example.android.popularmovie;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchMovieTask extends AsyncTask<String, Void, String> {
    Toast jsonErrorToast = MainActivity.toast;
    String API_KEY = String.valueOf(R.string.API_KEY);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        if(params.length== 0) {
            return null;
        }

        String sortBy = params[0];

        // Build URI
        Uri builtUri = Uri.parse(MovieAPI.API_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter("api key", API_KEY)
                .build();

        String response;
        // return JSON response if available; return toast error if not available
        try {
            response = getMovieJSON(builtUri);
            return response;
        } catch (Exception e) {
            jsonErrorToast.setText("This is not the movie you are looking for");
            jsonErrorToast.setDuration(Toast.LENGTH_SHORT);
            jsonErrorToast.show();
            return null;
        }
    }

    private String getMovieJSON(Uri builtUri) {
        // init inputStream
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        StringBuffer stringBuffer;
        HttpURLConnection httpURLConnection;
        BufferedReader reader;
        String movieJSON;

        try {
            /*
            Create new URL from string value of URI, open a connection, set the request method,
            and open the connection
            */
            URL url = new URL(builtUri.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            /*
            Get the inputStream from the URL and set up a reader for the inputStream.
            Set up a buffer and append the info returned from the reader to the readerLine
             */
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            stringBuffer = new StringBuffer();
            if(inputStream == null) {
                return null;
            }

            reader = new BufferedReader(inputStreamReader);
            String readerLine = reader.readLine();
            //Use a while because we want to read everything until it's empty
            while ((readerLine != null)) {
                stringBuffer.append(readerLine + "\n");
            }

            if(stringBuffer.length() == 0) {
                return null;
            }

            movieJSON = stringBuffer.toString();
        } catch (IOException e) {
            Log.v("FetchMovieTask", "Exception caught, check the stringBuffer");
            e.printStackTrace();
            return null;
        }

        // get your movieJSON here!
        return movieJSON;
    }

    @Override
    protected void onPostExecute(String movieResult) {
        if (movieResult != null) {
            movieJSON(movieResult);
        } else {
            jsonErrorToast.setText("Cannot return results, try again later");
            jsonErrorToast.setDuration(Toast.LENGTH_SHORT);
            jsonErrorToast.show();
        }
    }

    public static void movieJSON(String movieStringJSON) {
        //TODO: be sure to clear out any previous value from the mainActivty

        try {
            if(movieStringJSON != null) {
                JSONObject movieObject = new JSONObject(movieStringJSON);
                JSONArray movieArray = movieObject.getJSONArray("results");

                for(int i = 0; i < movieArray.length(); i++) {
                    Movie movieInfo = new Movie();
                    movieInfo.setmID(movieObject.getInt("id"));
                    movieInfo.setmTitle(movieObject.getString("title"));
                    //TODO: Update setmPoster logic to handle null response with generic image
                    movieInfo.setmPoster(movieObject.getString("poster_path"));
                    movieInfo.setmRating(movieObject.getString("vote_average"));
                    //TODO: Update setmDescription logic to handle null response with custom msg
                    movieInfo.setmDescription(movieObject.getString("overview"));
                    //TODO: Update setmReleaseDate logic to parse and reformat the date (try/catch)
                    movieInfo.setmReleaseDate(movieObject.getString("release_date"));
                }

            }
        } catch (JSONException e){
            Log.v("FetchMovieTask", "Error with JSON parsing");
            e.printStackTrace();
        }

        //TODO: add to Mainactivity's movieList
        //TODO: update MainActivity's movieAdapter that data set has changed
    }
}
