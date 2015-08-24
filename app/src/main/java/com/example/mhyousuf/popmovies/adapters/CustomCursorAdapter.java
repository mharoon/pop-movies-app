package com.example.mhyousuf.popmovies.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

public abstract class CustomCursorAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    Cursor mCursor;

    public CustomCursorAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (mCursor == newCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    public Cursor getItem(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
        }
        return mCursor;
    }
}