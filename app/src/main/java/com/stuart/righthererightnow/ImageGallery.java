package com.stuart.righthererightnow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class ImageGallery extends AppCompatActivity {

    GridView gridView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout);

        /*
        //Setting up the back button
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        */



        gridView = (GridView) findViewById(R.id.grid_view);
        // Instance of ImageAdapter Class

        gridView.setAdapter(new ImageAdapter(getApplicationContext()));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), ImageEnlargedActivity.class);
                // passing array index
                i.putExtra("id", position);
                startActivity(i);
            }
        });


        if(PlaceDetail.masterPostCounter==0){

            Toast.makeText(getApplicationContext(), "No Recent Images Posted", Toast.LENGTH_SHORT).show();
        }




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
