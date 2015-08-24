package com.example.mhyousuf.popmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mhyousuf.popmovies.adapters.TrailerCursorPagerAdapter;
import com.example.mhyousuf.popmovies.data.TMDBContract;
import com.example.mhyousuf.popmovies.listeners.ShakeDetector;
import com.example.mhyousuf.popmovies.api.TMDBService;
import com.example.mhyousuf.popmovies.constants.Constants;
import com.example.mhyousuf.popmovies.model.TMDB_Movie_Reviews;
import com.example.mhyousuf.popmovies.model.TMDB_Movie_Trailers;
import com.example.mhyousuf.popmovies.model.TMDB_Review;
import com.example.mhyousuf.popmovies.model.TMDB_Trailer;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A detail actvity fragment containing a movie detail view.
 */
public class MovieDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    private static final int MOVIE_DETAIL_LOADER = 0;
    private static final int MOVIE_REVIEW_LOADER = 1;
    private static final int MOVIE_TRAILER_LOADER = 2;

    private static final String[] MOVIE_COLUMNS = {
            TMDBContract.MovieEntry.TABLE_NAME + "." + TMDBContract.MovieEntry._ID,
            TMDBContract.MovieEntry.COLUMN_BACKDROP_PATH,
            TMDBContract.MovieEntry.COLUMN_OVERVIEW,
            TMDBContract.MovieEntry.COLUMN_RELEASE_DATE,
            TMDBContract.MovieEntry.COLUMN_POSTER_PATH,
            TMDBContract.MovieEntry.COLUMN_TITLE,
            TMDBContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            TMDBContract.MovieEntry.COLUMN_IS_FAVORITE,
    };

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_BACKDROP_PATH = 1;
    public static final int COL_OVERVIEW = 2;
    public static final int COL_RELEASE_DATE = 3;
    public static final int COL_POSTER_PATH = 4;
    public static final int COL_TITLE = 5;
    public static final int COL_VOTE_AVERAGE = 6;
    public static final int COL_IS_FAVORITE = 7;


    private static final String[] REVIEW_COLUMNS = {
            TMDBContract.ReviewEntry.TABLE_NAME + "." + TMDBContract.ReviewEntry._ID,
            TMDBContract.ReviewEntry.COLUMN_AUTHOR,
            TMDBContract.ReviewEntry.COLUMN_CONTENT
    };

    public static final int COL_REVIEW_ID = 0;
    public static final int COL_AUTHOR = 1;
    public static final int COL_CONTENT = 2;

    private static final String[] TRAILER_COLUMNS = {
            TMDBContract.TrailerEntry.TABLE_NAME + "." + TMDBContract.TrailerEntry._ID,
            TMDBContract.TrailerEntry.COLUMN_VIDEO_KEY,
            TMDBContract.TrailerEntry.COLUMN_NAME
    };

    public static final int COL_TRAILER_ID = 0;
    public static final int COL_VDO_KEY = 1;
    public static final int COL_NAME = 2;


    List<TMDB_Trailer> trailerFeeds = new ArrayList<TMDB_Trailer>();

    String voteAvg = "0.0";
    int voteAvgLength = 0;

    String movieTitle = "";
    int movieTitleLength = 0;

    SpannableStringBuilder sb;
    long movieDetailId;
    boolean isTwoPaneLayout = false;

    LayoutInflater layoutInflater;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private TrailerCursorPagerAdapter mPagerAdapter;

    private SensorManager mSensorManager;

    private ShakeDetector mShakeDetector;

    private Vibrator mVibrator;

    private String mShareTrailer = null;

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

    @Bind(R.id.toggle_movie_detail_favorite)
    ToggleButton toggleFavorite;


    @Bind(R.id.vp_detail_trailers)
    ViewPager mPager;
    @Bind(R.id.cpi_detail_trailer_indicator)
    PageIndicator mIndicator;

    @Bind(R.id.lst_movie_detail_review)
    ViewGroup lstMovieReviews;

    @Bind(R.id.relative_layout_backdrop)
    RelativeLayout topDetails;

    @Bind(R.id.txt_info)
    TextView txtInfo;

    Activity mContext = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(MOVIE_REVIEW_LOADER, null, this);
        getLoaderManager().initLoader(MOVIE_TRAILER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enable option menu for fragment
        setHasOptionsMenu(true);

        setRetainInstance(true);


        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);


        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mShakeDetector = new ShakeDetector();

        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            public void onShake() {
                mVibrator.vibrate(500);

                ContentValues values = new ContentValues();
                values.put(TMDBContract.MovieEntry.COLUMN_IS_FAVORITE, !toggleFavorite.isChecked());

                getActivity().getContentResolver().update(TMDBContract.MovieEntry.CONTENT_URI, values, TMDBContract.MovieEntry._ID + " = ?", new String[]{Long.toString(movieDetailId)});

                if (toggleFavorite.isChecked()) {
                    Toast.makeText(getActivity(), getString(R.string.movie_removed_from_fav), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.movie_added_to_fav), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layoutInflater = inflater;
        View view = layoutInflater.inflate(R.layout.fragment_movie_detail, container, false);

        mContext = getActivity();

        //provide main layout to butter knife.
        ButterKnife.bind(this, view);

        //get values passed from activity
        Bundle extras = getArguments();


        if (extras != null) {

            topDetails.setVisibility(View.VISIBLE);
            txtInfo.setVisibility(View.GONE);
            //get movie detail JSON string from extras
            movieDetailId = extras.getLong(Constants.MOVIE_DETAIL_ID_EXTRA);

            toggleFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    ContentValues values = new ContentValues();
                    values.put(TMDBContract.MovieEntry.COLUMN_IS_FAVORITE, (isChecked) ? 1 : 0);

                    getActivity().getContentResolver().update(TMDBContract.MovieEntry.CONTENT_URI, values, TMDBContract.MovieEntry._ID + " = ?", new String[]{Long.toString(movieDetailId)});
                }
            });


            // Instantiate a ViewPager and a PagerAdapter.
            mPagerAdapter = new TrailerCursorPagerAdapter(getFragmentManager(), null);
            mPager.setAdapter(mPagerAdapter);

            mIndicator.setViewPager(mPager);


            getMovieTrailers(movieDetailId);
            getMovieReviews(movieDetailId);

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


    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem = menu.findItem(R.id.action_share);


        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null && mShareTrailer != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }


    /**
     * get movie trailers from service
     *
     * @param movieId
     */
    private void getMovieTrailers(long movieId) {


        TMDBService.getTMDBApiClient().getMovieTrailers(movieId, Constants.API_KEY, new Callback<TMDB_Movie_Trailers>() {
            @Override
            public void success(TMDB_Movie_Trailers tmdbMovieTrailers, Response response) {
                bindViewPager(tmdbMovieTrailers.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, "getMoviesTrailers():" + error.getMessage());
            }
        });
    }

    /**
     * get movie reviews from service
     *
     * @param movieId
     */
    private void getMovieReviews(final long movieId) {

        TMDBService.getTMDBApiClient().getMovieReviews(movieId, Constants.API_KEY, new Callback<TMDB_Movie_Reviews>() {
            @Override
            public void success(TMDB_Movie_Reviews tmdb_movie_reviews, Response response) {
                bindReviews(tmdb_movie_reviews.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                bindReviews(null);
                //Toast.makeText(getActivity(), getString(R.string.request_time_out), Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "getMoviesReviews():" + error.getMessage());
            }
        });
    }

    private void bindReviews(List<TMDB_Review> result) {
        if (result != null) {
            Vector<ContentValues> cVVector = new Vector<ContentValues>(result.size());

            for (TMDB_Review review : result) {

                ContentValues reviewValues = new ContentValues();
                reviewValues.put(TMDBContract.ReviewEntry.COLUMN_REVIEW_ID, review.getId());
                reviewValues.put(TMDBContract.ReviewEntry.COLUMN_MOVIE_KEY, movieDetailId);
                reviewValues.put(TMDBContract.ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
                reviewValues.put(TMDBContract.ReviewEntry.COLUMN_CONTENT, review.getContent());

                cVVector.add(reviewValues);
            }

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);


                int rowsInserted = mContext.getContentResolver().bulkInsert(TMDBContract.ReviewEntry.CONTENT_URI, cvArray);

                Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of reviews data");
            }
        }
    }

    private void bindViewPager(List<TMDB_Trailer> result) {
        if (result != null) {
            Vector<ContentValues> cVVector = new Vector<ContentValues>(result.size());

            for (TMDB_Trailer trailer : result) {

                ContentValues trailerValues = new ContentValues();
                trailerValues.put(TMDBContract.TrailerEntry.COLUMN_TRAILER_ID, trailer.getId());
                trailerValues.put(TMDBContract.TrailerEntry.COLUMN_MOVIE_KEY, movieDetailId);
                trailerValues.put(TMDBContract.TrailerEntry.COLUMN_VIDEO_KEY, trailer.getKey());
                trailerValues.put(TMDBContract.TrailerEntry.COLUMN_NAME, trailer.getName());

                cVVector.add(trailerValues);
            }

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);


                int rowsInserted = mContext.getContentResolver().bulkInsert(TMDBContract.TrailerEntry.CONTENT_URI, cvArray);

                Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of trailers data");
            }
            //trailerFeeds.addAll(result);

            //mPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Bundle extras = getArguments();
        if (extras == null) {
            return null;
        }

        Loader<Cursor> cLoader = null;

        //get movie detail Id from extras
        movieDetailId = extras.getLong(Constants.MOVIE_DETAIL_ID_EXTRA);

        switch (id) {
            case MOVIE_DETAIL_LOADER: {
                Uri movieItemUri = TMDBContract.MovieEntry.buildMovieUri(movieDetailId);
                cLoader = new CursorLoader(getActivity(),
                        movieItemUri,
                        MOVIE_COLUMNS,
                        null,
                        null,
                        null
                );
                break;
            }
            case MOVIE_REVIEW_LOADER: {
                Uri movieReviewsUri = TMDBContract.ReviewEntry.buildReviewUri(movieDetailId);
                cLoader = new CursorLoader(getActivity(),
                        movieReviewsUri,
                        REVIEW_COLUMNS,
                        null,
                        null,
                        null
                );
                break;
            }
            case MOVIE_TRAILER_LOADER: {
                Uri movieTrailersUri = TMDBContract.TrailerEntry.buildTrailerUri(movieDetailId);
                cLoader = new CursorLoader(getActivity(),
                        movieTrailersUri,
                        TRAILER_COLUMNS,
                        null,
                        null,
                        null
                );
                break;
            }
        }

        return cLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int loaderID = loader.getId();


        switch (loaderID) {
            case MOVIE_DETAIL_LOADER: {
                if (data != null && data.moveToFirst()) {
                    Boolean isFav = (data.getInt(COL_IS_FAVORITE) == 1);
                    toggleFavorite.setChecked(isFav);

                    //set class variables
                    voteAvg = data.getString(COL_VOTE_AVERAGE);
                    voteAvgLength = voteAvg.length();

                    movieTitle = data.getString(COL_TITLE);
                    movieTitleLength = movieTitle.length();

                    //set layout elements
                    //txtDate.setText(movieDetailId.getReleaseDate(DateFormat.MEDIUM));
                    txtDate.setText(data.getString(COL_RELEASE_DATE));
                    txtSynopsis.setText(data.getString(COL_OVERVIEW));
                    ratingVoteAvg.setRating(data.getFloat(COL_VOTE_AVERAGE));

                    //set vote average color and make it bold
                    sb = new SpannableStringBuilder(getString(R.string.movie_vote_avg, voteAvg));
                    sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 235, 59)), 0, voteAvgLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, voteAvgLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    txtVote.setText(sb);

                    sb = new SpannableStringBuilder(getString(R.string.movie_detail_title_and_year, movieTitle, Utility.extractYearFromDate(data.getString(data.getColumnIndex(TMDBContract.MovieEntry.COLUMN_RELEASE_DATE)))));
                    sb.setSpan(new StyleSpan(Typeface.ITALIC), movieTitleLength, (movieTitleLength + 7), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    sb.setSpan(new AbsoluteSizeSpan(14, true), movieTitleLength, (movieTitleLength + 7), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    txtTitle.setText(sb);

                    //add and load image to Picasso
                    Picasso.with(getActivity()).load(data.getString(COL_BACKDROP_PATH)).into(imgBackdrop);
                    Picasso.with(getActivity()).load(data.getString(COL_POSTER_PATH)).into(image);
                }
                break;
            }
            case MOVIE_REVIEW_LOADER: {

                lstMovieReviews.removeAllViews();
                while (data != null && data.moveToNext()) {

                    /*movieAuthor.setText(data.getString(COL_AUTHOR));
                    movieContent.setText(data.getString(COL_CONTENT));*/

                    View reviewItem = layoutInflater.inflate(R.layout.review_item, lstMovieReviews, false);

                    TextView author = (TextView) reviewItem.findViewById(R.id.txt_review_author);
                    TextView content = (TextView) reviewItem.findViewById(R.id.txt_review_content);

                    author.setText(data.getString(COL_AUTHOR));
                    content.setText(data.getString(COL_CONTENT));

                    lstMovieReviews.addView(reviewItem);
                }
                break;
            }
            case MOVIE_TRAILER_LOADER: {


                mPagerAdapter.swapCursor(data);
                if (data != null && data.moveToFirst()) {

                    mShareTrailer = getString(R.string.youtube_vdo_url, data.getString(COL_VDO_KEY));
                }
            }
            break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int loaderID = loader.getId();
        switch (loaderID) {
            case MOVIE_TRAILER_LOADER: {
                mPagerAdapter.swapCursor(null);
            }
            break;
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareTrailer);
        return shareIntent;
    }
}
