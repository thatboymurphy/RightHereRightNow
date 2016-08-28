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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity { //changed from extends AppCompatActivity in order to get full screen working

    static ArrayList<Places> selectedPlaces = new ArrayList<Places>();
    static ArrayList<Places> myFavourites = new ArrayList<Places>();
    static boolean searchMode; //This will help determine where to animate the map camera too after selections


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // BUTTONS
        Button drinkButton = (Button) findViewById(R.id.drinkButton);
        Button foodButton = (Button) findViewById(R.id.foodButton);
        Button coffeeButton = (Button) findViewById(R.id.coffeeButton);
        Button funButton = (Button) findViewById(R.id.funButton);
        Button trendingButton = (Button) findViewById(R.id.trendingButton);
        Button favButton = (Button) findViewById(R.id.favButton);
        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button settingsButton = (Button) findViewById(R.id.settingsButton);


        // The below methods listen out for which buttons are being selected
        drinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                selectedPlaces.clear();

                // Read through the master list acquire from Google Places API
                for(Places place : SplashScreen.masterList)
                {
                    // Get the types of places this POI is known for
                    String[] types = place.getPlaceTypes();

                    // Read through these types
                    for(int i = 0; i < types.length; i++)
                    {
                        //if the the type Bar or Night Club is used assing it as Drink
                  if(types[i].equalsIgnoreCase("Bar")||types[i].equalsIgnoreCase("Night Club"))
                        {

                            selectedPlaces.add(place);

                            //We only want it added once, so if this statement was true, we got it
                            break;

                        }

                        else{
                            break;
                        }

                    }
                }
                searchMode = false;
                // Call on the new activity
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent);

            }

        }  );

        foodButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                selectedPlaces.clear();

                for(Places place : SplashScreen.masterList)
                {
                    String[] types = place.getPlaceTypes();

                    for(int i = 0; i < place.getPlaceTypes().length; i++)
                    {

                        if (types[i].equalsIgnoreCase("Restaurant"))
                        {

                            selectedPlaces.add(place);
                            break;
                        }

                    }
                }



                searchMode = false;
                // Call on the new activity
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent);
            }


        }  );

        coffeeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                selectedPlaces.clear();

                for(Places place : SplashScreen.masterList)
                {
                    String[] types = place.getPlaceTypes();

                    for(int i = 0; i < place.getPlaceTypes().length; i++)
                    {

                        if (types[i].equalsIgnoreCase("Cafe"))
                        {

                            selectedPlaces.add(place);
                            break;
                        }

                    }
                }
                searchMode = false;
                // Call on the new activity
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent);

            }

        }  );

        funButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                selectedPlaces.clear();

                for(Places place : SplashScreen.masterList)
                {
                    String[] types = place.getPlaceTypes();

                    for(int i = 0; i < place.getPlaceTypes().length; i++)
                    {

                        if (types[i].equalsIgnoreCase("Amusement Park")
                            || types[i].equalsIgnoreCase("Art Gallery")
                                || types[i].equalsIgnoreCase("Casino")
                                || types[i].equalsIgnoreCase("Zoo")
                                || types[i].equalsIgnoreCase("Stadium")
                                || types[i].equalsIgnoreCase("Shopping Mall")
                                || types[i].equalsIgnoreCase("Movie Theatre")
                                || types[i].equalsIgnoreCase("Museum")
                                )
                        {

                            selectedPlaces.add(place);
                            break;
                        }
                        else{
                            Log.i("tester",Integer.toString(selectedPlaces.size()));

                        }

                    }
                }
                searchMode = false;
                // Call on the new activity
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent);

            }


        }  );

        trendingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectedPlaces.clear();

                for(Places place: SplashScreen.masterList){

                    if(place.getCounterPast6Hours()>0||place.getCounter6to12()>0||place.getCounter12to18()>0||place.getCounter18to24()>0){

                        selectedPlaces.add(place);
                    }



                }


                searchMode = false;
                // Call on the new activity
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent);
            }
        }  );

        favButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchMode = false;
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent);

                // Clear the array from previous selection types
                selectedPlaces.clear();

                // Get specific types and add them to list
                for(Places place : myFavourites){

                    selectedPlaces.add(place);

                }

                if(selectedPlaces.size() == 0 || selectedPlaces.size()==-1){
                    Toast.makeText(getApplicationContext(), "You have no favourites saved :(", Toast.LENGTH_LONG).show();
                }
            }


        }  );

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchMode = true;

                selectedPlaces.clear();
                // Call on the new activity
                Intent intent = new Intent(v.getContext(), SearchFeature.class);
                startActivity(intent);


            }


        }  );

        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }


        }  );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_gen, menu);
        return true;

    }




}
