package org.weibeld.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    MainActivity mActivity;
    MovieList mMovieList;
    Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialise member variables
        mActivity = this;

        // Set up Retrofit
        Gson myGson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Interceptor myInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", mActivity.getString(R.string.tmdb_api_key)).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };
//        OkHttpClient client = new OkHttpClient();
//        client.interceptors().add(myInterceptor);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", mActivity.getString(R.string.tmdb_api_key)).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        }).build();


        mRetrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_base_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(myGson))
                .build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dummy:
                TmdbApiInterface apiService = mRetrofit.create(TmdbApiInterface.class);
                Call<MovieList> call = apiService.getCurrentMovies();
                call.enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, retrofit2.Response<MovieList> response) {
                        int statusCode = response.code();
                        MovieList mMovieList = response.body();
                        Log.v(LOG_TAG, mMovieList.getTitles());
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        // Log error here since request failed
                    }
                });
                return true;
        }
        return false;
    }
}
