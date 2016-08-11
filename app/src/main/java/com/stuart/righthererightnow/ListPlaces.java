/*
* Author: Stuart Murphy
* Student ID: 10046828
* Project: Masters Thesis
* Date: 08/08/2016
*
* Description:
* The following code is work in progress for my mobile application presentation. It is due
* completion before the 29th of August 2016. This is for supervisors viewing only to see how the
* code is currently looking.
* */

package com.stuart.righthererightnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListPlaces extends AppCompatActivity {

    static ArrayAdapter arrayAdapter;
    static ArrayList<String> placeName = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_places);

        //Setting up the back button
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();


        for (int i = 0; i < MainActivity.selectedPlaces.size(); i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title",MainActivity.selectedPlaces.get(i).getPlaceName() );
            hm.put("listview_discription", MainActivity.selectedPlaces.get(i).getPlaceAddress());
            hm.put("listview_image", Integer.toString(MainActivity.selectedPlaces.get(i).getPoiIcon()));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_discription"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);



        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), PlaceDetail.class);

                // Passing the String onto next activity, Place Detail
                intent.putExtra("Location on array", position);
                startActivity(intent);

                Log.i("print list",MainActivity.selectedPlaces.toString());

            }
        });



        /*


        // Create an array adapter to display the array of names for the selected type
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placeName);

        // Clear out old list data if it is populated
        if(placeName != null || placeName.size()>=0){
            placeName.clear();
            // Update the UI
            arrayAdapter.notifyDataSetChanged();
        }

        // Iterate through POI array to pull out the name and make a new array of names.
        // This is a short term solution until I make a custom adapter for Objects
        for(Places place : MainActivity.selectedPlaces)
        {
            placeName.add(place.getPlaceName());
            // Update the UI
            arrayAdapter.notifyDataSetChanged();
        }

        // Assign from XML
        ListView listView = (ListView) findViewById(R.id.listView);

        // Combine the data and the UI representation
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), PlaceDetail.class);

                // Passing the String onto next activity, Place Detail
                intent.putExtra("Location on array", position);
                startActivity(intent);

                Log.i("print list",MainActivity.selectedPlaces.toString());

            }
        });

        */





        // Go back to the map
        Button viewOnMap = (Button) findViewById(R.id.viewOnMap);
        viewOnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent);

            }


        }  );


    }
    // Home Button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears the back stack
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
