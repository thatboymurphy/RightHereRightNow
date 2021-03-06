/*
* Author: Stuart Murphy
* Student ID: 10046828
* Project: Masters Thesis
* Date: 29/08/2016
*
* Most Rec2nt 29th aug
*
* Description:
* This mobile application is for a MSc in Interactive Media in the University Of Limerick, The app
* is capable of displaying near by places of interest and also present any recent social media activity
* from that location. The idea is to present users with the most recent ongoings at places they are
* near by right now. This work will stil lbe in development for the coming months.
* */

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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlaceDetail extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    int chosenPOIArrayPos = -1;
    String chosenPOIName;

    // Place variables
    static String name, address, type, type2,type3, distance, openStatus, rating;
    static Bitmap image;
    static boolean fav;

    static int masterPostCounter, counterPast6Hours, counter6to12, counter12to18, counter18to24;

    static ArrayList<POISocialMediaPosts> posts;


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

        DecimalFormat df = new DecimalFormat("#0.0");

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

             masterPostCounter = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getMasterPostCounter();
             counterPast6Hours = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getCounterPast6Hours();
             counter6to12 = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getCounter6to12();
             counter12to18 = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getCounter12to18();
             counter18to24 = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getCounter18to24();
             posts = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getPosts();


            distance = df.format(MainActivity.selectedPlaces.get(chosenPOIArrayPos).getDistanceFromUser())+" km away";
            rating = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getRating();
            openStatus = MainActivity.selectedPlaces.get(chosenPOIArrayPos).getOpenStatus();


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

                        masterPostCounter = selectedPlace.getMasterPostCounter();
                        counterPast6Hours = selectedPlace.getCounterPast6Hours();
                        counter6to12 = selectedPlace.getCounter6to12();
                        counter12to18 = selectedPlace.getCounter12to18();
                        counter18to24 = selectedPlace.getCounter18to24();
                        posts = selectedPlace.getPosts();

                        distance = df.format(selectedPlace.getDistanceFromUser())+" km away";
                        rating = selectedPlace.getRating();
                        openStatus = selectedPlace.getOpenStatus();

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
