package cn.tracker.zengyang.gpstracker;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sc;
    LocationManager lm;

    //database
    GPSHelper mDbHelper = new GPSHelper(this);
    SQLiteDatabase db;

    //UI
    TextView textlat;
    TextView textlon;
    TextView textconut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textlat = (TextView) findViewById(R.id.lonval);
        textlon = (TextView) findViewById(R.id.latval);
        textconut = (TextView) findViewById(R.id.allcount);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Context context = getApplicationContext();
            CharSequence text = "has no permission of gps.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateView(location);

        final Context context = getApplicationContext();

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 8, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateView(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
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
                updateView(location);
            }

            @Override
            public void onProviderEnabled(String provider) {
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
                updateView(location);
                Context context = getApplicationContext();
                CharSequence text = "onProviderEnabled";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }
    public void updateView(Location location) {

//        TODO
        if (location != null) {
            System.out.println("location is "+ location.getLatitude());
            StringBuffer sb = new StringBuffer();
            sb.append("实时位置信息:\n");
            sb.append("经度:");
            sb.append(location.getLongitude());
            sb.append("\n纬度:");
            sb.append(location.getLatitude());
            sb.append("\n高度:");
            sb.append(location.getAltitude());
            sb.append("\n方向:");
            sb.append(location.getBearing());
            sb.append("\n速度:");
            sb.append(location.getSpeed());
            sb.append("\n时间:");
            sb.append(location.getTime());
            String now = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss").format(location.getTime());

            sb.append("\n now:");
            sb.append(now);
            // Create a new map of values, where column names are the keys
            db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(GPSContract.FeedEntry.COLUMN_LAT, location.getLatitude());
            values.put(GPSContract.FeedEntry.COLUMN_LON, location.getLongitude());
            long rowid = db.insert(GPSContract.FeedEntry.TABLE_NAME, null, values);
            sb.append("\n共插入"+rowid+"条数据");


            textlat.setText("" + location.getLatitude());
            textlon.setText("" + location.getLongitude());
            textconut.setText("总共有"+rowid+"条记录");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db != null){
            db.close();
        }
    }
}
