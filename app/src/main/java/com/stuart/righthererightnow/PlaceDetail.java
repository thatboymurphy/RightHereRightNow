package com.stuart.righthererightnow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;

public class PlaceDetail extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    int chosenPOIArrayPos = -1;
    String chosenPOIName;

    // Place variables
    static String name, address, type, type2,type3;
    static Bitmap image;
    static boolean fav;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        // Setting up the tabbed layout
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //Setting up the back button
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        // I will have two intents sending values (from the map and list. This means that each
        // sends a String or an int. I use the try catch to determine which it receives.
        Intent intent  = getIntent();
        try
        {
            // If coming from list, it send its postion in the array being used. From that I can
            // alll the other variables required to populate the Plce Detail page.
            chosenPOIArrayPos = intent.getIntExtra("Location on array",-1);

            name = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getPlaceName();
            type = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getPlaceType1();
            type2 = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getPlaceType2();
            type3 = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getPlaceType3();
            address = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getPlaceAddress();
            image = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getPlaceImage();
            fav = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getisFavd();

        } catch(Exception e)
        {
            // If the try fails, this means the intent was sent from the map. The map activity passes
            // on a String value for the name of the place. From this I can determine the rest of the
            // variables for that place

            chosenPOIName = intent.getStringExtra("Place");

            if(MainActivity.selectedPlaces.size() > 0)
            {
                for (Places selectedPlace : MainActivity.selectedPlaces)
                {
                    if (chosenPOIName.equals(selectedPlace.getPlaceName()))
                    {
                        name = selectedPlace.getPlaceName();
                        type = selectedPlace.getPlaceType1();
                        type2 = selectedPlace.getPlaceType2();
                        type3 = selectedPlace.getPlaceType3();
                        address = selectedPlace.getPlaceAddress();
                        image = selectedPlace.getPlaceImage();
                        fav = selectedPlace.getisFavd();
                    }
                }
            }
            e.printStackTrace();
        }

        setTitle(name); // Gives the name of the place to the action bar

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.home)
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears the back stack
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
