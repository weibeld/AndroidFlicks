package org.weibeld.flicks;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.weibeld.flicks.api.ApiResponseTrailersList;
import org.weibeld.flicks.api.ApiService;
import org.weibeld.flicks.databinding.ActivityDetailBinding;
import org.weibeld.flicks.databinding.ItemTrailerBinding;
import org.weibeld.flicks.util.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static org.weibeld.flicks.api.ApiResponseMovieList.Movie;
import static org.weibeld.flicks.api.ApiResponseTrailersList.YoutubeTrailer;

/**
 * Created by dw on 20/02/17.
 */

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    ActivityDetailBinding b;
    DetailActivity mActivity;
    Movie mMovie;
    Retrofit mRetrofit;
    ArrayList<YoutubeTrailer> mTrailers;
    ListView mTrailersList;
    TrailerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        b.toolbar.setTitle(R.string.title_detail_activity);
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovie = (Movie) getIntent().getSerializableExtra(getString(R.string.intent_extra_movie));
        mActivity = this;
        mRetrofit = Util.setupRetrofit();

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

        getTrailers();
    }

    private void getTrailers() {
        ApiService api = mRetrofit.create(ApiService.class);
        Call<ApiResponseTrailersList> call = api.apiGetTrailers(mMovie.id);
        call.enqueue(new Callback<ApiResponseTrailersList>() {
            @Override
            public void onResponse(Call<ApiResponseTrailersList> call, retrofit2.Response<ApiResponseTrailersList> response) {
                mTrailers = response.body().youtube;

                for (YoutubeTrailer trailer : mTrailers) {
                    View trailerItem = getTrailerView(trailer);
                    b.trailersContainer.addView(trailerItem);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseTrailersList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private View getTrailerView(YoutubeTrailer trailer) {
        ItemTrailerBinding binding;
        binding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.item_trailer, b.trailersContainer, false);
        binding.tvLink.setText(YOUTUBE_BASE_URL + trailer.source);
        binding.tvTitle.setText(trailer.name);
        return binding.getRoot();
    }

    // TODO: cannot have a ListView inside a ScrollView, display trailers in some other way
    public class TrailerAdapter extends ArrayAdapter<YoutubeTrailer> {
        private final String LOG_TAG = TrailerAdapter.class.getSimpleName();

        public TrailerAdapter(Context context, ArrayList<YoutubeTrailer> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            YoutubeTrailer trailer = getItem(position);
            ItemTrailerBinding binding;

            if (convertView == null) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_trailer, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(binding);
            }
            else
                binding = (ItemTrailerBinding) convertView.getTag();

            binding.tvTitle.setText(trailer.name);
            binding.tvLink.setText(YOUTUBE_BASE_URL + trailer.source);

            return convertView;
        }
    }
}
