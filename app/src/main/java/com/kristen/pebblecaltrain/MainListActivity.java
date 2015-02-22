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

        //a bunch of if statements that say, if palo alto, set location object to these
        //coordinates etc.
        targetLocation = new Location("dummyprovider");
        targetLocation.setLatitude(37.443557);
        targetLocation.setLongitude(-122.163331);

        Intent intent = new Intent(this, TransportStats.class);
        intent.putExtra("latitude", targetLocation.getLatitude());
        intent.putExtra("longitude", targetLocation.getLongitude());
        startActivity(intent);
    }
}
