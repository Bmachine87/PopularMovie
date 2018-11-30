package com.example.android.popularmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final Context mContext;
    private final MovieAdapterOnClickHandler movieAdapterClickHandler;
    private List<Movie> movieList;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.movieAdapterClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent,false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.movieTitleTV.setText(movie.getmTitle());
        Picasso.with(mContext).load(movie.getmPoster()).placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error).into(holder.moviePosterIV);
    }

    @Override
    public int getItemCount() {
        if(movieList == null) {
            return 0;
        } else {
            return movieList.size();
        }
    }

    void clearMovieList() {
        if(movieList == null) {
            movieList = new ArrayList<>();
        } else {
            int itemCount = movieList.size();
            movieList.clear();
            notifyItemRangeRemoved(0, itemCount);
        }
    }

    void addMovieList(List<Movie> movieList) {
        int posStart = movieList.size();
        movieList.addAll(movieList);
        notifyItemRangeInserted(posStart, movieList.size());
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movieTitle)
        TextView movieTitleTV;
        @BindView(R.id.movieImage)
        ImageView moviePosterIV;

        MovieAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Movie movie = movieList.get(getAdapterPosition());
            movieAdapterClickHandler.onClick(movie);

        }
    }
}
