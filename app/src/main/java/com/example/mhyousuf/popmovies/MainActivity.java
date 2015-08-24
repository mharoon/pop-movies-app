package com.example.mhyousuf.popmovies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import com.example.mhyousuf.popmovies.constants.Constants;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.ItemClickCallback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {

            mTwoPane = true;

            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, new MovieDetailActivityFragment()).commit();
            }
        } else {
            mTwoPane = false;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onItemSelected(long movieId, View view) {

        if (mTwoPane) {

            //for tablet layout only
            Bundle args = new Bundle();
            args.putLong(Constants.MOVIE_DETAIL_ID_EXTRA, movieId);

            //for tablet layout only
            MovieDetailActivityFragment movieDetailActivityFragment = new MovieDetailActivityFragment();
            movieDetailActivityFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, movieDetailActivityFragment).commit();

        } else {

            Intent intent = new Intent(this, MovieDetailActivity.class);

            //serialize the item object to JSON string and pass it to intent
            intent.putExtra(Constants.MOVIE_DETAIL_ID_EXTRA, movieId);


            //add shared transition animation to poster image only for API level greater then equal to 21.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this, view.findViewById(R.id.img_movie_poster), getString(R.string.movie_poster_transition_name));

                ActivityCompat.startActivity(this, intent, null);
            } else {

                startActivity(intent);
            }
        }

    }
}
