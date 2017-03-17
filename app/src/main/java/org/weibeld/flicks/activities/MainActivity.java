package org.weibeld.flicks.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.weibeld.flicks.R;
import org.weibeld.flicks.api.ApiResponseMovieList;
import org.weibeld.flicks.api.ApiService;
import org.weibeld.flicks.databinding.ActivityMainBinding;
import org.weibeld.flicks.databinding.ItemMovieBinding;
import org.weibeld.flicks.dialogs.TermsDialogFragment;
import org.weibeld.flicks.events.TermsAcceptanceEvent;
import org.weibeld.flicks.managers.SharedPrefManager;
import org.weibeld.flicks.util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static org.weibeld.flicks.api.ApiResponseMovieList.Movie;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    MainActivity mActivity;
    Retrofit mRetrofit;
    ListView mListView;
    ArrayAdapter<Movie> mAdapter;
    List<Movie> mMovies;
    SwipeRefreshLayout mSwipeRefresh;
    ActivityMainBinding b;
    SharedPrefManager mPrefMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(b.toolbar);
        mPrefMgr = SharedPrefManager.getInstance(this);

        if (!haveTermsBeenPreviouslyAccepted()) {
            EventBus.getDefault().register(this);
            TermsDialogFragment dialog = new TermsDialogFragment();
            dialog.show(getSupportFragmentManager(), getString(R.string.tag_terms_dialog));
        }
        else
            initActivity();
    }

    // Called when the user dismisses the terms of use dialog
    @Subscribe
    public void onTermsAcceptanceEvent(TermsAcceptanceEvent event) {
        EventBus.getDefault().unregister(this);
        // If the user clicked the positive button
        if (event.isAccepted()) {
            mPrefMgr.getPrefs().edit().putBoolean(getString(R.string.pref_terms_accepted), true).apply();
            initActivity();
        }
        // If the user clicked the negative button
        else
            finish();
    }

    public void initActivity() {
        // Initialise member variables
        mActivity = this;
        mRetrofit = Util.setupRetrofit();
        mListView = b.lvMovies;
        mMovies = new ArrayList<>();
        mAdapter = new MovieAdapter(this, (ArrayList<Movie>) mMovies);
        mListView.setAdapter(mAdapter);

        // Set up "pull to refresh"
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        // Setup refresh listener which triggers new data loading
        mSwipeRefresh.setOnRefreshListener(() -> {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            mAdapter.clear();
            getNowPlaying();
        });
        // Configure the refreshing colors
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        mListView.setOnItemClickListener((parent, view, position, id) -> {
            // Start DetailActiviry and put
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(getString(R.string.intent_extra_movie), mMovies.get(position));
            startActivity(intent);
        });

        getNowPlaying();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_terms:
                startActivity(new Intent(this, TermsActivity.class));
                return true;
        }
        return false;
    }

    private void getNowPlaying() {
        ApiService api = mRetrofit.create(ApiService.class);
        Call<ApiResponseMovieList> call = api.apiGetNowPlaying();
        call.enqueue(new Callback<ApiResponseMovieList>() {
            @Override
            public void onResponse(Call<ApiResponseMovieList> call, retrofit2.Response<ApiResponseMovieList> response) {
                int statusCode = response.code();
                ApiResponseMovieList body = response.body();
                List<Movie> movies = body.results;
                mAdapter.addAll(movies);
                mSwipeRefresh.setRefreshing(false);  // Remove the spinning referesh item
            }

            @Override
            public void onFailure(Call<ApiResponseMovieList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private boolean haveTermsBeenPreviouslyAccepted() {
        return mPrefMgr.getPrefs().getBoolean(getString(R.string.pref_terms_accepted), false);
    }


    public class MovieAdapter extends ArrayAdapter<Movie> {

        private final String LOG_TAG = MovieAdapter.class.getSimpleName();

        public MovieAdapter(Context context, ArrayList<Movie> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Movie movie = getItem(position);
            ItemMovieBinding binding;

            if (convertView == null) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_movie, parent, false);
                convertView = binding.getRoot();
                convertView.setTag(binding);
            }
            else
                binding = (ItemMovieBinding) convertView.getTag();

            binding.tvTitle.setText(movie.title);
            binding.tvOverview.setText(movie.overview);
            if (Util.isPortrait(mActivity))
                Util.loadImage(mActivity, Util.TYPE_POSTER, ApiService.POSTER_SIZE_W185, movie.posterPath, binding.ivImage);
            else
                Util.loadImage(mActivity, Util.TYPE_BACKDROP, ApiService.BACKDROP_SIZE_W780, movie.backdropPath, binding.ivImage);

            return convertView;
        }
    }

}
