package com.kristen.pebblecaltrain;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class LocationService extends Service {

    public static final String TAG = LocationService.class.getSimpleName();
    public static final String ACTION_START = "start";
    public static final int NOTIFICATION_ID = 8888;
    private LocationManager mLocationManager;
    private NotificationManager mNotificationManager;
    private Notification.Builder mBuilder;

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    private void showNotification(String destination, double latitude, double longitude) {
        mBuilder = new Notification.Builder(LocationService.this)
                .setContentTitle("Destination set: " + destination)
                .setContentText("")
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher);

        Intent resultIntent = new Intent(getBaseContext(), StatsActivity.class);
        resultIntent.putExtra("destination", destination);
        resultIntent.putExtra("latitude", latitude);
        resultIntent.putExtra("longitude", longitude);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getBaseContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        Notification notification = mBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action.equals(ACTION_START)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String destination = intent.getStringExtra("destination");
                    double latitude = intent.getDoubleExtra("latitude", 0);
                    double longitude = intent.getDoubleExtra("longitude", 0);

                    mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 5000, 3000, // provider, min time/distance
                            new LocationListener() {
                                public void onLocationChanged(Location location) {
                                    // code to run when user's location changes
                                    Log.d(TAG, "location changed");

                                }

                                public void onStatusChanged(String prov, int stat, Bundle b) {
                                }

                                public void onProviderEnabled(String provider) {
                                }

                                public void onProviderDisabled(String provider) {
                                }
                            },
                            Looper.getMainLooper()); // TODO: figure out if this is correct

                    showNotification(destination, latitude, longitude);
                }
            });
            thread.start();


        }
        // TODO: should this be START_STICKY?
        return START_NOT_STICKY;
    }

//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



}
