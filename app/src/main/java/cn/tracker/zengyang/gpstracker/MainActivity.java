package cn.tracker.zengyang.gpstracker;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import static android.R.attr.action;

public class MainActivity extends AppCompatActivity implements NoticeDialogFragment.NoticeDialogListener {

    SharedPreferences sc;
    LocationManager lm;

    //database
    GPSHelper mDbHelper = new GPSHelper(this);
    SQLiteDatabase db;

    //UI
    TextView textlat;
    TextView textlon;
    TextView textconut;

    //action
    final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
    DialogFragment newFragment = new NoticeDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textlat = (TextView) findViewById(R.id.lonval);
        textlon = (TextView) findViewById(R.id.latval);
        textconut = (TextView) findViewById(R.id.allcount);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //notice user to setting the gps

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.add(newFragment, null);
//            ft.commitAllowingStateLoss();

//            Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
//            intent.putExtra("enabled", true);
//            sendBroadcast(intent);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "打开GPS。。。。", Toast.LENGTH_LONG);

            toast.show();
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateView(location);



        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 8, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "地理位置改变，"+location, Toast.LENGTH_LONG);

                toast.show();
                updateView(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Context context = getApplicationContext();
                if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Toast toast = Toast.makeText(getApplicationContext(),
                        "状态更改，"+provider+"状态为"+status, Toast.LENGTH_LONG);

                toast.show();
                Location location = lm.getLastKnownLocation(provider);
                updateView(location);

            }

            @Override
            public void onProviderEnabled(String provider) {
                Context context = getApplicationContext();
                if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = lm.getLastKnownLocation(provider);

//                CharSequence text = "onProviderEnabled";
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
                updateView(location);


            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "不支持"+provider+"定位,请打开设置", Toast.LENGTH_LONG);

                toast.show();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(newFragment, null);
                ft.commitAllowingStateLoss();

                //newFragment.show(getSupportFragmentManager(), "missiles");


            }
        });

    }
    public void updateView(Location location) {

//        TODO
        if (location != null) {

            // Create a new map of values, where column names are the keys
            db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(GPSContract.FeedEntry.COLUMN_LAT, location.getLatitude());
            values.put(GPSContract.FeedEntry.COLUMN_LON, location.getLongitude());
            long rowid = db.insert(GPSContract.FeedEntry.TABLE_NAME, null, values);

            textlat.setText("" + location.getLatitude());
            textlon.setText("" + location.getLongitude());
            textconut.setText("总共有"+rowid+"条记录");
            Context context = getApplicationContext();
            CharSequence text = "正在更新视图。。。。";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db != null){
            db.close();
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.startActivity(new Intent(action));
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
