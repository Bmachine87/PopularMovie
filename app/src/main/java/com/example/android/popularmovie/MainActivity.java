package com.example.android.popularmovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Movie> movieList;
    public static ArrayList<String> movieImages;
    public static MovieAdapter movieAdapter;
    public static String sortBy;
    public static String lastSortBy;
    public static GridView movieGridView;
    public static Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_movie_details, new MovieFragment())
                    .commit();
        }
    }

    // Creates OptionMenu in mainactivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.recent) {
            startActivity(new Intent(this, SortMovieActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MovieFragment extends Fragment {
        public MovieFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootV = inflater.inflate(R.layout.movie_fragment, container, false);
            setHasOptionsMenu(true);

            movieGridView = (GridView) rootV.findViewById(R.id.gridview_movie);
            //TODO: Verify you do not need to setNumColumns or set orientation
            movieGridView.setAdapter(movieAdapter);
            movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                    intent.putExtra("movie_id", movieList.get(position).getmID());
                    intent.putExtra("movie_position", position);
                    startActivity(intent);
                }
            });

            toast = Toast.makeText(rootV.getContext(),"",Toast.LENGTH_SHORT);
            return rootV;
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            outState.putParcelableArrayList("movies", MainActivity.movieList);
            outState.putStringArrayList("images", MainActivity.movieImages);
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            if(savedInstanceState != null && savedInstanceState.containsKey("movies")) {
                movieList = savedInstanceState.getParcelableArrayList("movies");
                movieImages = savedInstanceState.getStringArrayList("images");
            } else {
                movieList = new ArrayList<Movie>();
                movieImages = new ArrayList<String>();
                movieAdapter = new MovieAdapter(getActivity());
                updateMovieList();
            }
            super.onCreate(savedInstanceState);
        }

        //TODO: Update with SharedPreferences
        private void updateMovieList() {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sortBy = sharedPrefs.getString(getString(R.string.sort_by), getString(R.string.sort_by_default));
            new FetchMovieTask().execute(sortBy, null);

        }

        //TODO: Update MovieFragment onResume (requires SharedPreferences
        @Override
        public void onResume() {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sortBy = sharedPrefs.getString(getString(R.string.sort_by), getString(R.string.sort_by_default));

            if(lastSortBy != null && !sortBy.equals(lastSortBy)) {
                movieList = new ArrayList<Movie>();
                movieImages = new ArrayList<String>();
                updateMovieList();
            }
            lastSortBy = sortBy;
            super.onResume();
        }
    }

}
