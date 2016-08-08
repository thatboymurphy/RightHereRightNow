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

import java.util.ArrayList;

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
