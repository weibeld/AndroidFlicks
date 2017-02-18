package org.weibeld.flicks;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.weibeld.flicks.api.ApiResponseMovieList;
import org.weibeld.flicks.api.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.weibeld.flicks.R.id.ivBackdrop;
import static org.weibeld.flicks.R.id.ivPoster;
import static org.weibeld.flicks.api.ApiResponseMovieList.Movie;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    MainActivity mActivity;
    Retrofit mRetrofit;
    ListView mListView;
    ArrayAdapter<Movie> mAdapter;
    List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialise member variables
        mActivity = this;
        mRetrofit = setupRetrofit();
        mListView = (ListView) findViewById(R.id.lvMovies);
        mMovies = new ArrayList<>();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mAdapter = new MovieAdapterLandscape(this, (ArrayList<Movie>) mMovies);
        }
        else {
            mAdapter = new MovieAdapterPortrait(this, (ArrayList<Movie>) mMovies);
        }
        mListView.setAdapter(mAdapter);
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
            case R.id.action_refresh:
                mAdapter.clear();
                getNowPlaying();
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
                Log.v(LOG_TAG, "page: " + body.page);
                Log.v(LOG_TAG, "total pages: " + body.totalPages);
                Log.v(LOG_TAG, "total results: " + body.totalResults);
                Log.v(LOG_TAG, "dates from " + body.dates.minimum + " to " + body.dates.maximum);
                List<Movie> movies = body.results;
                for (Movie movie : movies) {
                    Log.v(LOG_TAG, movie.title);
                }
                mAdapter.addAll(movies);
            }

            @Override
            public void onFailure(Call<ApiResponseMovieList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Retrofit setupRetrofit() {
        // Customise Gson instance
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        // Customise OkHttpClient (add interceptor to append api_key parameter to every query)
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // Append api_key parameter to every query
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", ApiService.API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        }).build();

        // Create Retrofit instance
        return new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    public class MovieAdapterPortrait extends ArrayAdapter<Movie> {

        private final String LOG_TAG = MovieAdapterPortrait.class.getSimpleName();

        public MovieAdapterPortrait(Context context, ArrayList<Movie> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Movie movie = getItem(position);
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie_portrait, parent, false);
                viewHolder = createNewViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvTitle.setText(movie.title);
            viewHolder.tvOverview.setText(movie.overview);
            if (movie.posterPath != null) {
                Glide.with(mActivity)
                        .load(ApiService.BASE_URL_IMG + ApiService.POSTER_SIZE_W154 + movie.posterPath)
                        .placeholder(getDrawable(R.drawable.poster_placeholder_154x231))
                        .into(viewHolder.ivPoster);
            }
            else
                Glide.with(mActivity)
                        .load(getDrawable(R.drawable.poster_none_154x231))
                        .into(viewHolder.ivPoster);

            return convertView;
        }

        private ViewHolder createNewViewHolder(View convertView) {
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.ivPoster = (ImageView) convertView.findViewById(ivPoster);
            return viewHolder;
        }

        private class ViewHolder {
            TextView tvTitle;
            TextView tvOverview;
            ImageView ivPoster;
        }
    }


    public class MovieAdapterLandscape extends ArrayAdapter<Movie> {

        private final String LOG_TAG = MovieAdapterPortrait.class.getSimpleName();

        public MovieAdapterLandscape(Context context, ArrayList<Movie> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Movie movie = getItem(position);
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie_landscape, parent, false);
                viewHolder = createNewViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvTitle.setText(movie.title);
            viewHolder.tvOverview.setText(movie.overview);
            if (movie.posterPath != null) {
                Glide.with(mActivity)
                        .load(ApiService.BASE_URL_IMG + ApiService.BACKDROP_SIZE_W300 + movie.backdropPath)
                        .placeholder(getDrawable(R.drawable.backdrop_placeholder_300x169))
                        .into(viewHolder.ivBackdrop);
            }
            else
                Glide.with(mActivity)
                        .load(getDrawable(R.drawable.backdrop_none_300x169))
                        .into(viewHolder.ivBackdrop);

            return convertView;
        }

        private ViewHolder createNewViewHolder(View convertView) {
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.ivBackdrop = (ImageView) convertView.findViewById(ivBackdrop);
            return viewHolder;
        }

        private class ViewHolder {
            TextView tvTitle;
            TextView tvOverview;
            ImageView ivBackdrop;
        }
    }
}
