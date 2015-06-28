package com.example.mhyousuf.popmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mhyousuf.popmovies.constants.Constants;
import com.example.mhyousuf.popmovies.model.Result;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A detail actvity fragment containing a movie detail view.
 */
public class MovieDetailActivityFragment extends Fragment {

    String voteAvg = "0.0";
    int voteAvgLength = 0;

    String movieTitle = "";
    int movieTitleLength = 0;

    SpannableStringBuilder sb;
    String jsonMovieObj;
    Result movieDetailObj;

    //Butter Knife View Injection
    @Bind(R.id.img_movie_detail_backdrop)
    ImageView imgBackdrop;
    @Bind(R.id.img_detail_poster_movie)
    ImageView image;
    @Bind(R.id.txt_movie_detail_title)
    TextView txtTitle;
    @Bind(R.id.txt_movie_detail_date)
    TextView txtDate;
    @Bind(R.id.txt_movie_detail_vote)
    TextView txtVote;
    @Bind(R.id.txt_movie_detail_synopsis)
    TextView txtSynopsis;
    @Bind(R.id.ratingBar_vote_avg)
    RatingBar ratingVoteAvg;

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enable option menu for fragment
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //provide main layout to butter knife.
        ButterKnife.bind(this, view);

        //get extras passed from main activity
        Bundle extras = getActivity().getIntent().getExtras();

        if(extras != null) {

            //get movie detail JSON string from extras
            jsonMovieObj = extras.getString(Constants.MOVIE_OBJECT_EXTRA);
            movieDetailObj = new Gson().fromJson(jsonMovieObj, Result.class);

            //set class variables
            voteAvg = Double.toString(movieDetailObj.getVoteAverage());
            voteAvgLength = voteAvg.length();

            movieTitle = movieDetailObj.getTitle();
            movieTitleLength = movieTitle.length();

            //set layout elements
            txtDate.setText(movieDetailObj.getReleaseDate(DateFormat.MEDIUM));
            txtSynopsis.setText(movieDetailObj.getOverview());
            ratingVoteAvg.setRating((float) movieDetailObj.getVoteAverage());

            //set vote average color and make it bold
            sb = new SpannableStringBuilder(getString(R.string.movie_vote_avg,voteAvg));
            sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 235, 59)), 0, voteAvgLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, voteAvgLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            txtVote.setText(sb);


            //set movie title year, change its font size and make it italic
            sb = new SpannableStringBuilder(getString(R.string.movie_detail_title_and_year, movieTitle,movieDetailObj.getReleaseYear()));
            sb.setSpan(new StyleSpan(Typeface.ITALIC), movieTitleLength, (movieTitleLength+7), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new AbsoluteSizeSpan(14,true), movieTitleLength, (movieTitleLength+7), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            txtTitle.setText(sb);

            //add and load image to Picasso
            Picasso.with(getActivity()).load(movieDetailObj.getBackdropPath()).into(imgBackdrop);
            Picasso.with(getActivity()).load(movieDetailObj.getPosterPath()).into(image);
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
