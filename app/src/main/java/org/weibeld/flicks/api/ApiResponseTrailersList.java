package org.weibeld.flicks.api;

import java.util.ArrayList;

/**
 * Created by dw on 20/02/17.
 */

public class ApiResponseTrailersList {
    public int id;
    public ArrayList<YoutubeTrailer> youtube;

    public static class YoutubeTrailer {
        public String name;
        public String size;
        public String source;
        public String type;
    }
}
