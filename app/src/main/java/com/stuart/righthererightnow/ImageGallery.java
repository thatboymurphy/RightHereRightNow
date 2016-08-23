package com.stuart.righthererightnow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class ImageGallery extends Activity{

    GridView gridView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout);



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



}
