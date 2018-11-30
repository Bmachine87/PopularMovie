package com.example.android.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Movie> movieList;
    public static MovieAdapter movieAdapter;
    public RecyclerView recyclerView;
    public RecyclerViewScrollListener mScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numOfColumns());
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addItemDecoration(new GridItemDecor(this));

        movieAdapter = new MovieAdapter(this,
                (MovieAdapter.MovieAdapterOnClickHandler) this);
        recyclerView.setAdapter(movieAdapter);

        mScrollListener = new RecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                fetchNewMovies(page);
            }
        };
        recyclerView.addOnScrollListener(mScrollListener);

//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                populateUI();
//            }
//        });
    }

    private void populateUI() {
        mScrollListener.resetState();
        movieAdapter.clearMovieList();
        fetchNewMovies(1);
    }

    private void fetchNewMovies(int page) {
        int sorting = Settings.getSort(this);
        String sortMethod = getResources().getStringArray(R.array.pref_sort_by_values)[sorting];

        FetchMovieTask moviesTask = new FetchMovieTask(getString(R.string.language),
                (AsyncTaskCompleteListener<List<Movie>>) this);
        moviesTask.execute(sortMethod, String.valueOf(page));
    }


    public void onClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_DETAILS_INTENT_KEY, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);

        MenuItem item = menu.findItem(R.id.recent);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pref_sort_by_keys, R.layout.settings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(Settings.getSort(MainActivity.this));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Settings.setSort(MainActivity.this, i);
                populateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return true;
    }

    private int numOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 500;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        return false;
    }

//    @Override
//    public void onTaskStart() {
//        if (!isOnline()) {
//            internetStatusTv.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onTaskComplete(List<Movie> result) {
//        mSwipeRefreshLayout.setRefreshing(false);
//        if (result != null) {
//            internetStatusTv.setVisibility(View.GONE);
//            mMoviesAdapter.addMoviesList(result);
//        }
//    }



}
