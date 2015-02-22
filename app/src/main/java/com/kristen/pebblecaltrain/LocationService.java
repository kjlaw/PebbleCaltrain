package com.kristen.pebblecaltrain;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.util.logging.Handler;

public class LocationService extends Service {
    private Looper mLocationLooper;
    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            //ACTUALLY DOES JOB
        }
    }

    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        //obtain message from the mServiceHandler, set arg1, and send the message
        //SENDS MESSAGE TO START JOB, WHILE KEEPING TRACK OF WHAT WAS COMPLETED

    }

    public LocationService() {


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy () {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
