package com.example.mhyousuf.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhyousuf.popmovies.constants.Constants;
import com.example.mhyousuf.popmovies.model.TMDB_Trailer;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mhyousuf on 7/5/2015.
 */
public class TrailerSlidePageFragment extends Fragment {

    private static final String LOG_TAG  = TrailerSlidePageFragment.class.getSimpleName();
    private String jsonTrailerObj;
    private TMDB_Trailer mTrailer;

    @Bind(R.id.txt_trailer_name)
    TextView trailerName;

    @Bind(R.id.img_trailer_thumb)
    ImageView trailerThumb;


    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static TrailerSlidePageFragment create(TMDB_Trailer trailer) {
        TrailerSlidePageFragment fragment = new TrailerSlidePageFragment();
        Bundle args = new Bundle();
        args.putString(Constants.TRAILER_OBJECT_EXTRA, new Gson().toJson(trailer));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonTrailerObj = getArguments().getString(Constants.TRAILER_OBJECT_EXTRA);
        mTrailer = new Gson().fromJson(jsonTrailerObj, TMDB_Trailer.class);    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_trailers_slide_page, container, false);

        //provide main layout to butter knife.
        ButterKnife.bind(this, rootView);

        // Set the title view to show the page number.
        trailerName.setText(mTrailer.getName());

        Picasso.with(getActivity()).load(getString(R.string.youtube_vdo_thumbnail_mq_url, mTrailer.getKey())).into(trailerThumb);

        Log.d(LOG_TAG, getString(R.string.youtube_vdo_thumbnail_mq_url, mTrailer.getKey()));

        trailerThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_vdo_url, mTrailer.getKey())));
                startActivity(viewVideoIntent);

            }
        });

        //getTrailerThumbnail
        return rootView;
    }

    /**
     * Returns the trailer object represented by this fragment object.
     */
    public TMDB_Trailer getTrailer() {
        return mTrailer;
    }
}
