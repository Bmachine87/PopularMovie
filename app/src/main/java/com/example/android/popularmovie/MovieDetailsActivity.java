package com.example.android.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {


    public static Movie movie;
    public static Intent intent;
    public static TextView title, release, rating, synopsis;
    public static ImageView poster;

    public static final String MOVIE_DETAILS_INTENT_KEY = "com.example.android.popularmovie";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_movie_details,
                    new MovieDetailsFrag()).commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public static class MovieDetailsFrag extends Fragment {

        public MovieDetailsFrag() {}

        public View onCreate(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
            View rootV = getLayoutInflater().inflate(R.layout.movie_details_fragment, container, false);
            initMovieDetails(rootV);
            setValues(rootV);
            return rootV;
        }

        public void initMovieDetails(View rootV) {
            movie = new Movie();

            MovieDetailsActivity.intent = getActivity().getIntent();

            int movie_position = intent.getIntExtra("movie_position", 0);
            movie = MainActivity.movieList.get(movie_position);

            title = (TextView) rootV.findViewById(R.id.title);
            release = (TextView) rootV.findViewById(R.id.release_date);
            rating = (TextView) rootV.findViewById(R.id.rating);
            synopsis = (TextView) rootV.findViewById(R.id.synopsis);
            poster = (ImageView) rootV.findViewById(R.id.poster);
        }

        public void setValues(View rootV) {
            title.setText(movie.getmTitle());
            //title.setVisibility(View.VISIBILE);
            release.setText(movie.getmReleaseDate());
            rating.setText(movie.getmRating());
            synopsis.setText(movie.getmDescription());

            String movie_poster_url;

            if(movie.getmPoster() == MovieAPI.IMAGE_NOT_FOUND) {
                movie_poster_url = MovieAPI.IMAGE_NOT_FOUND;
            } else {
                movie_poster_url = MovieAPI.IMAGE_URL + MovieAPI.IMAGE_SIZE + movie.getmPoster();
            }

            Picasso.with(rootV.getContext()).load(movie_poster_url).into(poster);
            poster.setVisibility(View.VISIBLE);
        }
    }
}
