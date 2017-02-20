package org.weibeld.flicks.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.weibeld.flicks.R;
import org.weibeld.flicks.api.ApiService;

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
            Log.v(LOG_TAG, ApiService.BASE_URL_IMG + size + path);
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

}
