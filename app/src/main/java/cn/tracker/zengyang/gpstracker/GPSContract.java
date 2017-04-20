package cn.tracker.zengyang.gpstracker;

import android.provider.BaseColumns;

/**
 * Created by zengyang on 2017/4/19.
 */

public final class GPSContract {
    private GPSContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "gpsdata";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LON = "lon";
        public static final String COLUMN_TIME = "createtime";



    }
}
