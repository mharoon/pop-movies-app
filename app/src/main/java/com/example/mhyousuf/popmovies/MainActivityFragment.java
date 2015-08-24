package com.example.mhyousuf.popmovies;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mhyousuf.popmovies.adapters.RecyclerViewGridAdapter;
import com.example.mhyousuf.popmovies.api.TMDBService;
import com.example.mhyousuf.popmovies.constants.Constants;
import com.example.mhyousuf.popmovies.data.TMDBContract;
import com.example.mhyousuf.popmovies.model.TMDBMovie;
import com.example.mhyousuf.popmovies.model.TMDB_Movie_Feeds;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RecyclerViewGridAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    //loader identity
    private static final int MOVIE_LOADER = 0;

    //loader projection
    private static final String[] MOVIE_COLUMNS = {
            TMDBContract.MovieEntry.TABLE_NAME + "." + TMDBContract.MovieEntry._ID,
            TMDBContract.MovieEntry.COLUMN_POSTER_PATH,
            TMDBContract.MovieEntry.COLUMN_TITLE,
            TMDBContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            TMDBContract.MovieEntry.COLUMN_POPULARITY
    };

    //column indices
    public static final int COL_MOVIE_ID = 0;
    public static final int COL_POSTER_PATH = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_VOTE_AVERAGE = 3;
    public static final int COL_POPULARITY = 4;

    enum SortType {
        MOST_POPULAR,
        HIGHEST_RATED
    }


    RecyclerViewGridAdapter recyclerAdapter;
    GridLayoutManager gridLayoutManager;

    boolean requestInProgress = false; //boolean to discard new request till last finished
    boolean restartLoader = false; //mark true when there is any change in sort order

    int previousVisibleItems, visibleItemCount, totalItemCount; //infinite scroll

    int pageIndex = 1;
    String sortedBy = Constants.SORT_BY_POPULARITY;
    Boolean isFavSelected = false;

    int selectedSortOption = SortType.MOST_POPULAR.ordinal();

    //Butte Knife view injection
    @Bind(R.id.recycler_view_grid)
    RecyclerView gridView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (isConnected()) {
            //get user sort preference
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int sortOption = Integer.parseInt(prefs.getString(Constants.PREF_SORT_LIST_KEY, "0"));

            //get data according to user sort preferences
            setCriteriaAndGetFeeds(selectedSortOption,sortOption);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enable fragment menu item
        setHasOptionsMenu(true);
        //retain fragment instance on re-creation
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        //set different number of column for different orientation
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2); //set 2 column layout
        }else{
            gridLayoutManager = new GridLayoutManager(getActivity(), 4); //set 4 column layout
        }

        gridView.setLayoutManager(gridLayoutManager); //set layout manager
        gridView.addOnScrollListener(mScrollListener); //add onScroll listener

        recyclerAdapter = new RecyclerViewGridAdapter(getActivity(), this, null); //call adapter constructor
        gridView.setAdapter(recyclerAdapter);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_switch);
        View view = MenuItemCompat.getActionView(menuItem);

        Switch favSwich = (Switch) view.findViewById(R.id.switch_fav);

        favSwich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(getActivity(), "Checked " + isChecked, Toast.LENGTH_SHORT).show();
                toggleFavoriteItems(isChecked);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            case R.id.action_sort: {

                //dialog to choose sort options
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setSingleChoiceItems(R.array.sort_options, selectedSortOption, null)
                        .setTitle(R.string.sort_by_dialog_title)
                        .setPositiveButton(R.string.btn_dialog_OK, dialogOkListener)
                        .setNegativeButton(R.string.btn_dialog_Cancel, dialogCancelListener)
                        .show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);


            visibleItemCount = recyclerView.getChildCount(); // total items visible
            totalItemCount = gridLayoutManager.getItemCount(); //total current items
            previousVisibleItems = gridLayoutManager.findFirstVisibleItemPosition(); //scrolled item count

            //fetch new data when only 10 items left at bottom
            if (totalItemCount > 0 && totalItemCount - (visibleItemCount + previousVisibleItems) < Constants.ITEM_THRESHOLD) {
                //getMovieFeeds(pageIndex, sortedBy);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

    };

    @Override
    public void itemClicked(long feedItemId, View view) {

        //notify the parent activity about item click
        ((ItemClickCallback) getActivity()).onItemSelected(feedItemId, view);


    }


    private DialogInterface.OnClickListener dialogOkListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            int point = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

            //get data from service if there is any change in sort option
            setCriteriaAndGetFeeds(selectedSortOption, point);

            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener dialogCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    /*
    * Store user sort selection and get data based on it.
    * */
    private void setCriteriaAndGetFeeds(int oldSortValue, int newSortValue) {



        //fetch new data when there is any change in sort criteria
        if (oldSortValue != newSortValue || pageIndex == 1) {


            //reset the value to default
            //pageIndex = 1;
            gridView.scrollToPosition(0);
            sortedBy = Constants.SORT_BY_POPULARITY;

            selectedSortOption = newSortValue;

            //save user sort preference to shared preferences
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.PREF_SORT_LIST_KEY, String.valueOf(selectedSortOption)); // value to store
            editor.apply();

            getLoaderManager().restartLoader(MOVIE_LOADER, null, this);

            if(pageIndex == 1) {
                getMovieFeeds(pageIndex, sortedBy);
            }
        }
    }

    /**
     * get the data from service
     *
     * @param pageIndex
     * @param sortBy
     */
    private void getMovieFeeds(int pageIndex, String sortBy) {

        //entertain the request after previous request completion
        if (!requestInProgress) {
            requestInProgress = true;

            //show progress bar
            progressBar.setVisibility(View.VISIBLE);

            TMDBService.getTMDBApiClient().getMovieFeeds(sortBy, pageIndex, Constants.API_KEY, new Callback<TMDB_Movie_Feeds>() {
                @Override
                public void success(TMDB_Movie_Feeds tmdbMovieFeeds, Response response) {
                    //bind adapter with new data
                    bindGrid(tmdbMovieFeeds.getResults());
                }

                @Override
                public void failure(RetrofitError error) {
                    bindGrid(null);
                    Toast.makeText(getActivity(), getString(R.string.request_time_out), Toast.LENGTH_LONG).show();
                    Log.d("mhyousuf.moviestreamer", "MainActivityFragment:" + error.getMessage());
                }
            });
        }

    }

    private void bindGrid(List<TMDBMovie> results) {
        if (results != null) {
            Vector<ContentValues> cVVector = new Vector<ContentValues>(results.size());

            //push new feeds data
            //movieFeeds.addAll(results);


            //increment page to next page
            pageIndex++;

            //insert into databases

            for (TMDBMovie movie : results) {

                ContentValues movieValues = new ContentValues();
                movieValues.put(TMDBContract.MovieEntry._ID, movie.getId());
                movieValues.put(TMDBContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
                movieValues.put(TMDBContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                movieValues.put(TMDBContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                movieValues.put(TMDBContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                movieValues.put(TMDBContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
                movieValues.put(TMDBContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                movieValues.put(TMDBContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
                movieValues.put(TMDBContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());

                cVVector.add(movieValues);
            }

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                int rowsInserted = getActivity().getContentResolver().bulkInsert(TMDBContract.MovieEntry.CONTENT_URI, cvArray);

                Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of movies data");
            }

        }

        //mark request completed
        requestInProgress = false;

        //hide progress bar
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selection = null;
        String[] selectionArgs = null;

        Uri movieUri = TMDBContract.MovieEntry.CONTENT_URI;

        if(isFavSelected){
            selection = TMDBContract.MovieEntry.COLUMN_IS_FAVORITE + " = ?";
            selectionArgs = new String[]{Integer.toString(1)};
        }

        return new CursorLoader(
                getActivity(),
                movieUri,
                MOVIE_COLUMNS,
                selection,
                selectionArgs,
                getMoviesLoaderSortOrder(selectedSortOption)
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        recyclerAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        recyclerAdapter.swapCursor(null);

    }

    /*
    * Get loader's sort order based on user selection
    * */
    private String getMoviesLoaderSortOrder(int selectedSortCriteria) {

        SortType st = SortType.values()[selectedSortCriteria];
        String loaderSortOrder = TMDBContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC";

        //set sort parameter based on user selection
        switch (st) {
            case HIGHEST_RATED: {
                loaderSortOrder = TMDBContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC";
                break;
            }
            case MOST_POPULAR: {
                loaderSortOrder = TMDBContract.MovieEntry.COLUMN_POPULARITY + " DESC";
                break;
            }
        }

        return loaderSortOrder;
    }

    /*
    * Check internet connection
    * */
    public boolean isConnected() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void toggleFavoriteItems(boolean isChecked){
        //persist preference if needed

        isFavSelected = isChecked;
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    /*
    * Interface to notify parent activity when item has been selected
    * */
    public interface ItemClickCallback {
        /*
        * Movie detail fragment's callback when item has been selected
        * */
        public void onItemSelected(long movieId, View view);
    }


}
