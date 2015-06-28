package com.example.mhyousuf.popmovies.adapters;

import android.content.Context;
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

import com.example.mhyousuf.popmovies.R;
import com.example.mhyousuf.popmovies.model.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mhyousuf on 6/16/2015.
 */
public class TMDBRecyclerViewAdapter extends RecyclerView.Adapter<TMDBRecyclerViewAdapter.FeedGridRowHolder> {
    List<Result> feeds;
    Context mContext;
    ItemClickListener itemClickListener;
    String LOG_TAG = TMDBRecyclerViewAdapter.class.getSimpleName();
    SpannableStringBuilder sb;


    /**
     * RecyclerView adapter constructor
      * @param context activity context
     * @param feedItemList list of result items
     * @param clickListener item click listener for recycler view
     */
    public TMDBRecyclerViewAdapter(Context context, List<Result> feedItemList, ItemClickListener clickListener) {
        this.feeds = feedItemList;
        this.mContext = context;
        this.itemClickListener = clickListener;
    }

    @Override
    public FeedGridRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, null);
        FeedGridRowHolder mh = new FeedGridRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(FeedGridRowHolder feedGridRowHolder, int i) {
        // get item object by index
        final Result feedItem = feeds.get(i);

        final String voteAvg = Double.toString(feedItem.getVoteAverage());
        final int voteAvgLength = voteAvg.length();

        //use picasso cache for poster image
        Picasso.with(mContext)
                .load(feedItem.getPosterPath())
                .into(feedGridRowHolder.gridImage);

        feedGridRowHolder.txtMovieTitle.setText(feedItem.getTitle());

        //change vote avg font to yellow and make it bold
        sb = new SpannableStringBuilder(voteAvg + "/10");
        sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 235, 59)), 0, voteAvgLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan( new StyleSpan(android.graphics.Typeface.BOLD), 0, voteAvgLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        feedGridRowHolder.txtMovieAvg.setText(sb);

        //item click listener
        feedGridRowHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //raise item click listener event
                itemClickListener.itemClicked(feedItem, v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != feeds ? feeds.size() : 0);
    }


    public static final class FeedGridRowHolder extends RecyclerView.ViewHolder {

        //Butter knife view injection
        private final View parent;
        @Bind(R.id.img_movie_poster)
        ImageView gridImage;
        @Bind(R.id.txt_movie_title)
        TextView txtMovieTitle;
        @Bind(R.id.txt_movie_avg)
        TextView txtMovieAvg;

        //ViewHolder constructor
        public FeedGridRowHolder(View view) {
            super(view);
            this.parent = view;

            //initialize butter knife
            ButterKnife.bind(this, view);
        }

        /**
         * item click listener
         * @param listener
         */
        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }

    //custom item click listener interface for recycler view item
    public interface ItemClickListener {
        void itemClicked(Result feedItem, View view);
    }
}


