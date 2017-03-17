package org.weibeld.flicks.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.weibeld.flicks.R;
import org.weibeld.flicks.api.ApiService;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dw on 16/02/17.
 */

public class Util {

    public static final String LOG_TAG = Util.class.getSimpleName();

    public static final int TYPE_POSTER = 0;
    public static final int TYPE_BACKDROP = 1;

    public static void toast(Activity a, String msg) {
        Toast.makeText(a, msg, Toast.LENGTH_SHORT).show();
    }

    public static Retrofit setupRetrofit() {
        // Customise Gson instance
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        // Enable logging of HTTP queries
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);

        // Append api_key parameter to every query
        Interceptor apiKeyInterceptor = chain -> {
            // Append api_key parameter to every query
            Request request = chain.request();
            HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", ApiService.API_KEY).build();
            request = request.newBuilder().url(url).build();
            return chain.proceed(request);
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor)
                .addInterceptor(logInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())  // Enable Stetho network inspection
                .build();

        // Create Retrofit instance
        return new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static void loadImage(Activity a, int type, String size, String path, ImageView imageView) {
        Resources res = a.getResources();
        Drawable placeholder = null;
        Drawable missing = null;
        switch (type) {
            case TYPE_POSTER:
                switch (size) {
                    case ApiService.POSTER_SIZE_W92:
                        placeholder = res.getDrawable(R.drawable.poster_placeholder_92x139);
                        missing = res.getDrawable(R.drawable.poster_missing_92x139);
                        break;
                    case ApiService.POSTER_SIZE_W154:
                        placeholder = res.getDrawable(R.drawable.poster_placeholder_154x231);
                        missing = res.getDrawable(R.drawable.poster_missing_154x231);
                        break;
                    case ApiService.POSTER_SIZE_W185:
                        placeholder = res.getDrawable(R.drawable.poster_placeholder_185x278);
                        missing = res.getDrawable(R.drawable.poster_missing_185x278);
                        break;
                    case ApiService.POSTER_SIZE_W342:
                        placeholder = res.getDrawable(R.drawable.poster_placeholder_342x513);
                        missing = res.getDrawable(R.drawable.poster_missing_342x513);
                        break;
                    case ApiService.POSTER_SIZE_W500:
                        placeholder = res.getDrawable(R.drawable.poster_placeholder_500x750);
                        missing = res.getDrawable(R.drawable.poster_missing_500x750);
                        break;
                    case ApiService.POSTER_SIZE_W780:
                        placeholder = res.getDrawable(R.drawable.poster_placeholder_780x1170);
                        missing = res.getDrawable(R.drawable.poster_missing_780x1170);
                        break;
                    default:
                        new Exception().printStackTrace();
                }
                break;
            case TYPE_BACKDROP:
                switch (size) {
                    case ApiService.BACKDROP_SIZE_W300:
                        placeholder = res.getDrawable(R.drawable.backdrop_placeholder_300x169);
                        missing = res.getDrawable(R.drawable.backdrop_missing_300x169);
                        break;
                    case ApiService.BACKDROP_SIZE_W780:
                        placeholder = res.getDrawable(R.drawable.backdrop_placeholder_780x439);
                        missing = res.getDrawable(R.drawable.backdrop_missing_780x439);
                        break;
                    case ApiService.BACKDROP_SIZE_W1280:
                        placeholder = res.getDrawable(R.drawable.backdrop_placeholder_1280x720);
                        missing = res.getDrawable(R.drawable.backdrop_missing_1280x720);
                        break;
                    default:
                        new Exception().printStackTrace();
                }
                break;
            default:
                new Exception().printStackTrace();
        }
        if (path != null) {
            Glide.with(a).load(ApiService.BASE_URL_IMG + size + path).placeholder(placeholder).into(imageView);
            //Log.v(LOG_TAG, ApiService.BASE_URL_IMG + size + path);
        }
        else
            Glide.with(a).load(missing).into(imageView);
    }

    public static boolean isPortrait(Activity a) {
        return (a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    public static boolean isLandscape(Activity a) {
        return (a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    public static void toggleVisibility(View v) {
        if (v.getVisibility() == View.VISIBLE) v.setVisibility(View.GONE);
        else v.setVisibility(View.VISIBLE);
    }

}
