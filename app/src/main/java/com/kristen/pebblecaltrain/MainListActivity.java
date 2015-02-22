package com.kristen.pebblecaltrain;

import android.app.ListActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;


public class MainListActivity extends ListActivity {

    ListView mStationsListView;
    List<String> mStations;
    ListViewAdapter mAdapter;
    Location targetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStations = Arrays.asList("San Jose", "Sunnyvale", "Palo Alto", "Redwood City",
                "Hillsdale", "San Mateo", "Millbrae", "So. San Francisco", "San Francisco");

        mAdapter = new ListViewAdapter(this, mStations);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        CheckBox checkBox = (CheckBox)v.findViewById(R.id.checkBox);
        mAdapter.deselectAll();
        mAdapter.markSelected(position, !checkBox.isChecked());

        mAdapter.notifyDataSetChanged();


        targetLocation = new Location("");
        String destination = mStations.get(position);

        if (destination.equals("San Jose")) {
            targetLocation.setLatitude(37.330402);
            targetLocation.setLongitude(-121.902124);
        } else if (destination.equals("Sunnyvale")) {
            targetLocation.setLatitude(37.378453);
            targetLocation.setLongitude(-122.030737);
        } else if (destination.equals("Palo Alto")) {
            targetLocation.setLatitude(37.443070);
            targetLocation.setLongitude(-122.164905);
        } else if (destination.equals("Redwood City")) {
            targetLocation.setLatitude(37.485480);
            targetLocation.setLongitude(-122.231941);
        } else if (destination.equals("Hillsdale")){
            targetLocation.setLatitude(37.537512);
            targetLocation.setLongitude(-122.297996);
        } else if (destination.equals("San Mateo")){
            targetLocation.setLatitude(37.568218);
            targetLocation.setLongitude(-122.324003);
        } else if (destination.equals("Millbrae")){
            targetLocation.setLatitude(37.599997);
            targetLocation.setLongitude(-122.386529);
        } else if (destination.equals("So. San Francisco")){
            targetLocation.setLatitude(37.655852);
            targetLocation.setLongitude(-122.405434);
        } else if (destination.equals("San Francisco")){
            targetLocation.setLatitude(37.776447);
            targetLocation.setLongitude(-122.394318);
        }

        Intent intent = new Intent(this, TransportStats.class);
        intent.putExtra("destination", destination);
        intent.putExtra("latitude", targetLocation.getLatitude());
        intent.putExtra("longitude", targetLocation.getLongitude());
        startActivity(intent);
    }
}
