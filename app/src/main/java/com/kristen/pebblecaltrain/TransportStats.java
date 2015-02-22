package com.kristen.pebblecaltrain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.UUID;

public class TransportStats extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    private static final String TAG = TransportStats.class.getSimpleName();
    private static final UUID PEBBLE_APP_UUID = UUID.fromString("3dae475a-3873-4a8a-a3f8-c27571a422ea");
    private static final int KEY_ONE_DATA = 1;
    private static final int KEY_TWO_DATA = 2;

    private Location mLastLocation;
    private Location mTargetLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TextView mLocation;
    private float mDistance;
    private PebbleDataReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_stats);

        mLocation = (TextView) findViewById(R.id.location);
        mTargetLocation = new Location("");

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);
        mTargetLocation.setLatitude(latitude);
        mTargetLocation.setLongitude(longitude);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

        receiveCompletion();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        unregisterReceiver(mReceiver);
    }

    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Log.d(TAG, latitude + ", " + longitude);
            mLocation.setText(latitude + ", " + longitude);

        } else {
            mLocation.setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        Log.d(TAG, "check play services called");
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.d(TAG, "connection result not success");

            Toast.makeText(getApplicationContext(),
                    "This device is not supported.", Toast.LENGTH_LONG)
                    .show();
            finish();
        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        // TODO: is this call correct? also call in onConnected after startLocationUpdates? is receiveCompletion correct?
        checkDistance(mLastLocation, mTargetLocation);

        // Displaying the new location on UI
        displayLocation();
    }

    public void checkDistance(Location current_loc, Location target_loc) {
        if (current_loc.distanceTo(target_loc) < 3000) { // 850 meters is about .5 miles
            // send notification to pebble
            sendNotification();
            Log.d(TAG, "distance: " + current_loc.distanceTo(target_loc));
        } else {
            Log.d(TAG, "distance: " + current_loc.distanceTo(target_loc));
        }
    }

    public void sendNotification() {
        boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
        Log.i(getLocalClassName(), "Pebble is " + (connected ? "connected" : "not connected"));

        PebbleDictionary data = new PebbleDictionary();
        data.addString(KEY_ONE_DATA, "Wake up, dude!");
        PebbleKit.sendDataToPebble(getApplicationContext(), PEBBLE_APP_UUID, data);

        PebbleKit.registerReceivedAckHandler(getApplicationContext(), new PebbleKit.PebbleAckReceiver(PEBBLE_APP_UUID) {

            @Override
            public void receiveAck(Context context, int transactionId) {
                Log.i(getLocalClassName(), "Received ack for transaction " + transactionId);
            }

        });

        PebbleKit.registerReceivedNackHandler(getApplicationContext(), new PebbleKit.PebbleNackReceiver(PEBBLE_APP_UUID) {

            @Override
            public void receiveNack(Context context, int transactionId) {
                Log.i(getLocalClassName(), "Received nack for transaction " + transactionId);
            }

        });
    }

    public void receiveCompletion() {

        final Handler handler = new Handler();

        mReceiver = new PebbleDataReceiver(PEBBLE_APP_UUID) {

            @Override
            public void receiveData(Context context, int transactionId, PebbleDictionary data) {

                final String received = data.getString(KEY_TWO_DATA);

                Log.i(getLocalClassName(), "Received value=" + data.getString(KEY_TWO_DATA) + " for key: 2");

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (received.equals("complete")) {
                            stopLocationUpdates();
                        }
                    }

                });

                PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
            }
        };

        PebbleKit.registerReceivedDataHandler(this, mReceiver);
    }
}