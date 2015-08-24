package com.example.mhyousuf.popmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mhyousuf.popmovies.MainActivityFragment;
import com.example.mhyousuf.popmovies.R;
import com.example.mhyousuf.popmovies.model.TMDBMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mhyousuf on 6/16/2015.
 */
public class RecyclerViewGridAdapter extends CustomCursorAdapter<RecyclerViewGridAdapter.ViewHolder> {
    List<TMDBMovie> feeds;
    Context mContext;
    ItemClickListener itemClickListener;
    String LOG_TAG = RecyclerViewGridAdapter.class.getSimpleName();
    SpannableStringBuilder sb;


    /**
     * RecyclerView adapter constructor
      * @param context activity context
     * @param clickListener item click listener for recycler view
     */
    public RecyclerViewGridAdapter(Context context, ItemClickListener clickListener, Cursor c) {
        super(c);

        this.mContext = context;
        this.itemClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Cursor cursor = getItem(i);

        // get item object by index
        //final TMDBMovie feedItem = feeds.get(i);

        final String voteAvg = cursor.getString(MainActivityFragment.COL_VOTE_AVERAGE); //Double.toString(feedItem.getVoteAverage());
        final int voteAvgLength = voteAvg.length();

        final long movieId = cursor.getLong(MainActivityFragment.COL_MOVIE_ID);

        //use picasso cache for poster image
        Picasso.with(mContext)
                .load(cursor.getString(MainActivityFragment.COL_POSTER_PATH))
                .into(viewHolder.gridImage);

        //viewHolder.txtMovieTitle.setText(feedItem.getTitle());

        viewHolder.txtMovieTitle.setText(cursor.getString(MainActivityFragment.COL_TITLE));

        //change vote avg font to yellow and make it bold
        sb = new SpannableStringBuilder(voteAvg + "/10");
        sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 235, 59)), 0, voteAvgLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, voteAvgLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        viewHolder.txtMovieAvg.setText(sb);

        //bind click on recycler view item
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //raise custom itemClick event
                itemClickListener.itemClicked(movieId, v);
            }
        });
    }

    /*@Override
    public int getItemCount() {
        return (null != feeds ? feeds.size() : 0);
    }*/


    public static final class ViewHolder extends RecyclerView.ViewHolder {

        //Butter knife view injection
        private final View parent;
        @Bind(R.id.img_movie_poster)
        ImageView gridImage;
        @Bind(R.id.txt_movie_title)
        TextView txtMovieTitle;
        @Bind(R.id.txt_movie_avg)
        TextView txtMovieAvg;

        //ViewHolder constructor
        public ViewHolder(View view) {
            super(view);
            this.parent = view;

            //initialize butter knife
            ButterKnife.bind(this, view);
        }
    }

    //custom item click listener interface for recycler view item
    public interface ItemClickListener {
        void itemClicked(long feedItemId, View view);
    }
}


