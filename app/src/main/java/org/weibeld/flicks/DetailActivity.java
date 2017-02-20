package org.weibeld.flicks;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.weibeld.flicks.api.ApiService;
import org.weibeld.flicks.databinding.ActivityDetailBinding;
import org.weibeld.flicks.util.Util;

import static org.weibeld.flicks.api.ApiResponseMovieList.Movie;

/**
 * Created by dw on 20/02/17.
 */

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    ActivityDetailBinding b;
    DetailActivity mActivity;
    Movie mMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        b.toolbar.setTitle(R.string.title_detail_activity);
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovie = (Movie) getIntent().getSerializableExtra(getString(R.string.intent_extra_movie));
        mActivity = this;

        if (Util.isPortrait(this))
            Util.loadImage(this, Util.TYPE_BACKDROP, ApiService.BACKDROP_SIZE_W780, mMovie.backdropPath, b.ivImage);
        else
            Util.loadImage(this, Util.TYPE_POSTER, ApiService.POSTER_SIZE_W342, mMovie.posterPath, b.ivImage);
        b.tvTitle.setText(mMovie.title);
        float rating = (float) mMovie.voteAverage/2;
        b.ratingBar.setRating((rating));
        Log.v(LOG_TAG, "Rating: " + b.ratingBar.getRating());
        b.tvRatingInfo.setText(mMovie.voteAverage + "/10 (" + mMovie.voteCount + " votes)");
        b.tvReleaseDate.setText("Released " + mMovie.releaseDate);
        b.tvOverview.setText(mMovie.overview);

    }
}
