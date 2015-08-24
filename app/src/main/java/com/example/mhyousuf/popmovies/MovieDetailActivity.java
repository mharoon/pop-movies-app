package com.example.mhyousuf.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE = "DetailActivity:image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if(savedInstanceState == null){

            Bundle extras = getIntent().getExtras();

            //for tablet layout only
            MovieDetailActivityFragment movieDetailActivityFragment = new MovieDetailActivityFragment();
            movieDetailActivityFragment.setArguments(extras);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.movie_detail_container, movieDetailActivityFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

}
