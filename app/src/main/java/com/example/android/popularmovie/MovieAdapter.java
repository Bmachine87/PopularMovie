package com.example.android.popularmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends BaseAdapter {

    private Context mContext;

    public MovieAdapter(Context context) {
        this.mContext = context;
    }

    public static class ViewHolder {
        ImageView mImageViewMovie;
    }
    @Override
    public int getCount() {
        return MainActivity.movieImages.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        //TODO: Review this logic
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_movie, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.mImageViewMovie = (ImageView) convertView.findViewById(R.id.imageview_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Picasso.with(mContext)
                .load(MainActivity.movieImages.get(i))
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error)
                .into(viewHolder.mImageViewMovie);

        return convertView;
    }
}
