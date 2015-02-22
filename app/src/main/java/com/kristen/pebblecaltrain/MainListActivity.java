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

        mStations = Arrays.asList("Palo Alto", "Hillsdale", "Millbrae", "San Francisco");

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


        targetLocation = new Location("dummyprovider");

        if (mStations.get(position).equals("Palo Alto")) {
            targetLocation.setLatitude(37.443070);
            targetLocation.setLongitude(-122.164905);
        } else if (mStations.get(position).equals("Hillsdale")){
            targetLocation.setLatitude(37.537512);
            targetLocation.setLongitude(-122.297996);
        } else if (mStations.get(position).equals("Millbrae")){
            targetLocation.setLatitude(37.599997);
            targetLocation.setLongitude(-122.386529);
        } else if (mStations.get(position).equals("San Francisco")){
            targetLocation.setLatitude(37.776447);
            targetLocation.setLongitude(-122.394318);
        }

        Intent intent = new Intent(this, TransportStats.class);
        intent.putExtra("latitude", targetLocation.getLatitude());
        intent.putExtra("longitude", targetLocation.getLongitude());
        startActivity(intent);
    }
}
