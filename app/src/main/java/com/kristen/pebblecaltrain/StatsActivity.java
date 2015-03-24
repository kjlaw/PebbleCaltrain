package com.kristen.pebblecaltrain;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import java.util.UUID;

public class StatsActivity extends Activity
//        implements ConnectionCallbacks,
//        OnConnectionFailedListener, LocationListener
    {

    private static final String TAG = StatsActivity.class.getSimpleName();
    private static final UUID PEBBLE_APP_UUID = UUID.fromString("3dae475a-3873-4a8a-a3f8-c27571a422ea");
    private static final int KEY_ONE_DATA = 1;
    private static final int KEY_TWO_DATA = 2;

    private Intent mLocIntent;

    private Location mLastLocation;
    private Location mTargetLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TextView mDestination;
    private TextView mLocation;
    private TextView mDistance;
    private TextView mSpeed;
    private PebbleDataReceiver mReceiver;
    private String mDestinationName;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        mDestination = (TextView) findViewById(R.id.destination);
        mLocation = (TextView) findViewById(R.id.location);
        mDistance = (TextView) findViewById(R.id.distance);
        mSpeed = (TextView) findViewById(R.id.speed);
        mButton = (Button) findViewById(R.id.cancelButton);
        mTargetLocation = new Location("");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "button clicked");
                stopService(mLocIntent);
            }
        });

        Intent intent = getIntent();
        mDestinationName = intent.getStringExtra("destination");
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);
        mTargetLocation.setLatitude(latitude);
        mTargetLocation.setLongitude(longitude);

//        // First we need to check availability of play services
//        if (checkPlayServices()) {
//
//            // Building the GoogleApi client
//            buildGoogleApiClient();

            // send request to LocationService using an intent
            mLocIntent = new Intent(this, LocationService.class);
            mLocIntent.putExtra("destination", mDestinationName);
            mLocIntent.putExtra("latitude", mTargetLocation.getLatitude());
            mLocIntent.putExtra("longitude", mTargetLocation.getLongitude());
            mLocIntent.setAction(LocationService.ACTION_START);
            startService(mLocIntent);

//            createLocationRequest();
//
//        }
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        checkPlayServices();
//
//        // Resuming the periodic location updates
//        if (mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
//        }
//
//        receiveCompletion();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        stopLocationUpdates();
//        unregisterReceiver(mReceiver);
//    }
//
//    /**
//     * Method to display information about the location on UI
//     * */
//    private void displayInformation() {
//
//        mLastLocation = LocationServices.FusedLocationApi
//                .getLastLocation(mGoogleApiClient);
//
//        if (mLastLocation != null) {
//            double latitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();
//
//            Log.d(TAG, latitude + ", " + longitude);
//            mDestination.setText(mDestinationName);
//            mLocation.setText(latitude + ", " + longitude);
//            mDistance.setText(mLastLocation.distanceTo(mTargetLocation) + " meters");
//            mSpeed.setText(mLastLocation.getSpeed() + " m/s");
//
//        } else {
//            mLocation.setText("(Couldn't get the location. Make sure location is enabled on the device)");
//        }
//    }
//
//    /**
//     * Creating google api client object
//     * */
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//    }
//
//    /**
//     * Creating location request object
//     * */
//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    /**
//     * Method to verify google play services on the device
//     * */
//    private boolean checkPlayServices() {
//        Log.d(TAG, "check play services called");
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            Log.d(TAG, "connection result not success");
//
//            Toast.makeText(getApplicationContext(),
//                    "This device is not supported.", Toast.LENGTH_LONG)
//                    .show();
//            finish();
//        }
//        return true;
//    }
//
//    /**
//     * Starting the location updates
//     * */
//    protected void startLocationUpdates() {
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
//    }
//
//    /**
//     * Stopping location updates
//     */
//    protected void stopLocationUpdates() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(
//                mGoogleApiClient, this);
//    }
//
//    /**
//     * Google api callback methods
//     */
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
//                + result.getErrorCode());
//    }
//
//    @Override
//    public void onConnected(Bundle arg0) {
//
//        // Once connected with google api, get the location
//        displayInformation();
//
//        startLocationUpdates();
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        // Assign the new location
//        mLastLocation = location;
//
////        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();
//
//        checkDistance(mLastLocation, mTargetLocation);
//
//        // Displaying the new location on UI
//        displayInformation();
//    }
//
//    public void checkDistance(Location current_loc, Location target_loc) {
//        if (current_loc.distanceTo(target_loc) < 3000) { // 850 meters is about .5 miles
//            // send notification to pebble
//            sendNotification();
//            Log.d(TAG, "distance: " + current_loc.distanceTo(target_loc));
//        } else {
//            Log.d(TAG, "distance: " + current_loc.distanceTo(target_loc));
//        }
//    }
//
//    public void sendNotification() {
//        boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
//        Log.i(getLocalClassName(), "Pebble is " + (connected ? "connected" : "not connected"));
//
//        PebbleDictionary data = new PebbleDictionary();
//        data.addString(KEY_ONE_DATA, "Wake up,\ndude!");
//        PebbleKit.sendDataToPebble(getApplicationContext(), PEBBLE_APP_UUID, data);
//
//        PebbleKit.registerReceivedAckHandler(getApplicationContext(), new PebbleKit.PebbleAckReceiver(PEBBLE_APP_UUID) {
//
//            @Override
//            public void receiveAck(Context context, int transactionId) {
//                Log.i(getLocalClassName(), "Received ack for transaction " + transactionId);
//            }
//
//        });
//
//        PebbleKit.registerReceivedNackHandler(getApplicationContext(), new PebbleKit.PebbleNackReceiver(PEBBLE_APP_UUID) {
//
//            @Override
//            public void receiveNack(Context context, int transactionId) {
//                Log.i(getLocalClassName(), "Received nack for transaction " + transactionId);
//            }
//
//        });
//    }
//
//    public void receiveCompletion() {
//
//        final Handler handler = new Handler();
//
//        mReceiver = new PebbleDataReceiver(PEBBLE_APP_UUID) {
//
//            @Override
//            public void receiveData(Context context, int transactionId, PebbleDictionary data) {
//                PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
//                Log.d(TAG, "sent ack to pebble");
//
//                final String received = data.getString(KEY_TWO_DATA);
//
//                Log.i(getLocalClassName(), "Received value=" + data.getString(KEY_TWO_DATA) + " for key: 2");
//
//                handler.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        if (received.equals("complete")) {
//                            stopLocationUpdates();
//                            finish();
//                        }
//                    }
//
//                });
//            }
//        };
//
//        PebbleKit.registerReceivedDataHandler(this, mReceiver);
//    }
}