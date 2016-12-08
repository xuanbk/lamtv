package lamtv.project.com.myapplication.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.Locale;

public final class LocationBroker {

    private final static String LOG_TAG = "LocationBroker";
    private static LocationBroker instance;
    private static LocationManager lm;
    private static DeepDiveIntoLocationFinder locationFinder;

    public final static Location getLocation() {
        if (lm == null || instance == null) {
            return null;
        }

        return locationFinder.getLastBestLocation(DeepDiveIntoLocationFinder.MAX_DISTANCE, System.currentTimeMillis() - DeepDiveIntoLocationFinder.MAX_TIME);
    }

    public final static void init(Context context) {
        if (!enable) {
            return;
        }
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (instance == null) {
            instance = new LocationBroker();
        }

        locationFinder = instance.new DeepDiveIntoLocationFinder(context);
    }

    // 機能制限できるようにする
    private static boolean enable;

    /**
     * 位置情報機能の有効無効を設定する。
     *
     * @param flg 有効にする場合は true を、無効にする場合は false を渡す。
     */
    public static void enable(boolean flg) {
        enable = flg;
    }

    private static String appendLocation(String url) {
        Location location = getLocation();
        if (location == null) {
            return url;
        }

        // 末尾が ?a& などで終わる URL に対応
        String connectionWord = "";
        if (url.contains("?")) {
            if (!url.endsWith("&")) {
                connectionWord = "&";
            }
        } else {
            connectionWord = "?";
        }

        return String.format(Locale.getDefault(), "%s%slat=%f&lon=%f", url, connectionWord,
                location.getLatitude(),
                location.getLongitude());
    }


	/*
	 * A Deep Dive Into Location
	 *
	 * refs: http://android-developers.blogspot.jp/2011/06/deep-dive-into-location.html
	 */

    class DeepDiveIntoLocationFinder {
        private static final int DEFAULT_RADIUS = 150;
        static final int MAX_DISTANCE = DEFAULT_RADIUS / 2;
        static final long MAX_TIME = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

        private static final String SINGLE_LOCATION_UPDATE_ACTION = "jp.co.sevenbank.money.SINGLE_LOCATION_UPDATE_ACTION";

        private PendingIntent singleUpdatePI;
        private LocationListener locationListener;
        private Context context;
        private Criteria criteria;

        DeepDiveIntoLocationFinder(Context context) {
            this.context = context;

            criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_LOW);

            Intent updateIntent = new Intent(SINGLE_LOCATION_UPDATE_ACTION);
            singleUpdatePI = PendingIntent.getBroadcast(context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Location getLastBestLocation(int minDistance, long minTime) {
            Location bestResult = null;
            float bestAccuracy = Float.MAX_VALUE;
            long bestTime = Long.MIN_VALUE;

            for (String provider : lm.getAllProviders()) {
                Location location = lm.getLastKnownLocation(provider);
                if (location != null) {
                    float accuracy = location.getAccuracy();
                    long time = location.getTime();

                    if ((time > minTime) && (accuracy < bestAccuracy)) {
                        bestResult = location;
                        bestAccuracy = accuracy;
                        bestTime = time;
                    } else if ((time < minTime) && (bestAccuracy == Float.MAX_VALUE) && (time > bestTime)) {
                        bestResult = location;
                        bestTime = time;
                    }
                }
            }

            if ((locationListener != null) && ((bestTime < minTime) || (bestAccuracy > minDistance))) {
                IntentFilter intentFilter = new IntentFilter(SINGLE_LOCATION_UPDATE_ACTION);
                context.registerReceiver(singleUpdateReceiver, intentFilter);
            }
            return bestResult;
        }

        private BroadcastReceiver singleUpdateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(singleUpdateReceiver);

                if (locationListener != null) {
                    Location location = (Location)intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
                    if (location != null) {
                        locationListener.onLocationChanged(location);
                    }
                }

                lm.removeUpdates(singleUpdatePI);
            }
        };

        void setChangedLocationListener(LocationListener l) {
            locationListener = l;
        }
    }

    public static void requestLocationUpdates(Criteria criteria, PendingIntent pi) {
        if (lm != null) {
            lm.requestLocationUpdates(DeepDiveIntoLocationFinder.MAX_TIME, DeepDiveIntoLocationFinder.MAX_DISTANCE, criteria, pi);
        }
    }

}