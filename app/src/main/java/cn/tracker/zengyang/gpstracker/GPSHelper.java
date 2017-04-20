package cn.tracker.zengyang.gpstracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.tracker.zengyang.gpstracker.GPSContract;


/**
 * Created by zengyang on 2017/4/19.
 */


public class GPSHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GPSdataBase";

    private static final String TEXT_TYPE = " TEXT";
    private static final String TIME_TYPE = " TimeStamp";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + GPSContract.FeedEntry.TABLE_NAME + " (" +
                    GPSContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    GPSContract.FeedEntry.COLUMN_LAT + TEXT_TYPE + COMMA_SEP +
                    GPSContract.FeedEntry.COLUMN_LON+ TEXT_TYPE + COMMA_SEP +
                    GPSContract.FeedEntry.COLUMN_TIME + TIME_TYPE + " NOT NULL DEFAULT (datetime('now','localtime'))" +" )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + GPSContract.FeedEntry.TABLE_NAME;

    public GPSHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
