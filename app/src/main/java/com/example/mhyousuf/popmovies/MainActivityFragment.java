package com.example.mhyousuf.popmovies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mhyousuf.popmovies.adapters.TMDBRecyclerViewAdapter;
import com.example.mhyousuf.popmovies.api.TMDBService;
import com.example.mhyousuf.popmovies.constants.Constants;
import com.example.mhyousuf.popmovies.model.Result;
import com.example.mhyousuf.popmovies.model.TMDBFeedsData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements TMDBRecyclerViewAdapter.ItemClickListener{

    static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    TMDBRecyclerViewAdapter gridAdapter;
    GridLayoutManager gridLayoutManager;
    boolean requestInProgress = false;

    int previousVisibleItems, visibleItemCount, totalItemCount;

    int pageIndex = 1;
    String sortedBy = Constants.SortByPopularity;
    List<Result> dataFeeds = new ArrayList<Result>();

    enum SortType {
        MOST_POPULAR,
        HIGHEST_RATED
    }

    int selectedSortOption = SortType.MOST_POPULAR.ordinal();

    //Butte Knife view injection
    @InjectView(R.id.recycler_view_grid)
    RecyclerView gridView;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get & set user preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        selectedSortOption = Integer.parseInt(prefs.getString("sort_list", "0"));

        //enable fragment menu item
        setHasOptionsMenu(true);
        //retain fragment instance on re-creation
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);

        //set different number of column for different orientation
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2); //set 2 column layout
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 4); //set 4 column layout
        }

        gridView.setLayoutManager(gridLayoutManager); //set layout manager
        gridView.addOnScrollListener(mScrollListener); //add onScroll listener

        gridAdapter = new TMDBRecyclerViewAdapter(getActivity(), dataFeeds, this); //call adapter constructor
        gridView.setAdapter(gridAdapter);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

        if(isConnected()) {
            //get and set user sort preference
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int sortOption = Integer.parseInt(prefs.getString("sort_list", "0"));

            //get data according to user sort preferences
            setCriteriaAndGetFeeds(selectedSortOption, sortOption);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main, menu);
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
                        .setTitle("Sort By")
                        .setPositiveButton(R.string.btn_dialog_OK, dialogOkListener)
                        .setNegativeButton(R.string.btn_dialog_Cancel, dialogCancelListener)
                        .show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * get the data from service
     * @param pageIndex
     * @param sortBy
     */
    private void getMovieFeeds(int pageIndex, String sortBy) {

        //entertain the request afte previous request completion
        if (!requestInProgress) {
            requestInProgress = true;

            //show progress bar
            progressBar.setVisibility(View.VISIBLE);

            TMDBService.getTMDBApiClient().getFeeds(sortBy, pageIndex, Constants.API_KEY, new Callback<TMDBFeedsData>() {
                @Override
                public void success(TMDBFeedsData tmdbFeedsData, Response response) {
                    //bind adapter with new data
                    bindGrid(tmdbFeedsData.getResults());
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), getString(R.string.request_time_out), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    Log.d("mhyousuf.moviestreamer", "MainActivityFragment:" + error.getMessage());
                }
            });
        }

    }


    private void bindGrid(List<Result> results) {
        if (results != null) {

            //push new feeds data
            dataFeeds.addAll(results);

            //notify adapter about data change
            gridAdapter.notifyDataSetChanged();

            //hide progress bar
            progressBar.setVisibility(View.GONE);

            //increment page to next page
            pageIndex++;
        }

        //mark request completed successfully
        requestInProgress = false;
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
                getMovieFeeds(pageIndex, sortedBy);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

    };

    @Override
    public void itemClicked(Result feedItem, View view) {

        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);

        //serialize the item object to JSON string and pass it to intent
        intent.putExtra(Constants.MOVIE_OBJECT_EXTRA, new Gson().toJson(feedItem));

        //add shared transition animation to poster image only for API level greater then equal to 21.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.img_movie_poster), "moviePoster");
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        } else {
            startActivity(intent);
        }
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

    private void setCriteriaAndGetFeeds(int oldSortValue, int newSortValue){
        //fetch new data when there is any change in sort criteria
        if (oldSortValue != newSortValue || pageIndex == 1) {

            //reset the value to default
            pageIndex = 1;
            gridView.scrollToPosition(0);
            dataFeeds.clear();

            selectedSortOption = newSortValue;
            SortType st = SortType.values()[selectedSortOption];

            //set sort parameter based on user selection
            switch (st){
                case HIGHEST_RATED:{
                    sortedBy = Constants.SortByHighestRated;
                    break;
                }
                case MOST_POPULAR:{
                    sortedBy = Constants.SortByPopularity;
                    break;
                }
            }

            //save user sort preference to shared preferences
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("sort_list", String.valueOf(selectedSortOption)); // value to store
            editor.apply();

            getMovieFeeds(pageIndex, sortedBy); //get feeds starting from first page
        }
    }

    public boolean isConnected() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
