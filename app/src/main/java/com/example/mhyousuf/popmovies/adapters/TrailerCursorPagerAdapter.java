package com.example.mhyousuf.popmovies.adapters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mhyousuf.popmovies.MovieDetailActivityFragment;
import com.example.mhyousuf.popmovies.TrailerSlidePageFragment;
import com.example.mhyousuf.popmovies.model.TMDB_Trailer;

/**
 * Created by mhyousuf on 8/23/2015.
 */
public class TrailerCursorPagerAdapter<F extends Fragment> extends FragmentStatePagerAdapter {
    private Cursor cursor;

    public TrailerCursorPagerAdapter(FragmentManager fm, Cursor cursor) {
        super(fm);
        this.cursor = cursor;
    }

    @Override
    public Fragment getItem(int position) {
        if (cursor == null) // shouldn't happen
            return null;

        cursor.moveToPosition(position);
        try {

            TMDB_Trailer trailer = new TMDB_Trailer();
            trailer.setId(cursor.getString(MovieDetailActivityFragment.COL_TRAILER_ID));
            trailer.setKey(cursor.getString(MovieDetailActivityFragment.COL_VDO_KEY));
            trailer.setName(cursor.getString(MovieDetailActivityFragment.COL_NAME));

            return TrailerSlidePageFragment.create(trailer);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int getCount() {
        if (cursor == null)
            return 0;
        else
            return cursor.getCount();
    }

    public void swapCursor(Cursor c) {
        if (cursor == c)
            return;

        this.cursor = c;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}
