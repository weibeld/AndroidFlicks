package org.weibeld.flicks.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by dw on 16/02/17.
 */

public class Util {

    public static void toast(Activity a, String msg) {
        Toast.makeText(a, msg, Toast.LENGTH_SHORT).show();
    }

}