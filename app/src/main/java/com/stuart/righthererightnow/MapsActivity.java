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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener{    // changed from FragmentActivity to AppCompatActivity to get my action bar working
                                                                                                        // Location listener is for user current location updates

    // Create new user location objects here in avoid redrawing markers in the updates
    MarkerOptions user = new MarkerOptions();
    Marker userMarker = null;

    GoogleMap mMap;
    ProgressBar wheel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // If the app is started for the first time.
        if(!SplashScreen.infoShown) {
            showMarkerLegend();

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Setting up the back button
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        Button goToList = (Button) findViewById(R.id.goToList);
        goToList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Clear out old list data if it is populated
                if(ListPlaces.placeName != null && ListPlaces.placeName.size()>0){
                    ListPlaces.placeName.clear();
                    ListPlaces.arrayAdapter.notifyDataSetChanged();
                }

                Intent intent = new Intent(v.getContext(), ListPlaces.class);
                startActivity(intent);

            }


        }  );

        Button infoBtn = (Button) findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Clear out old list data if it is populated
               showMarkerLegend();

            }


        }  );

    }

    public void showMarkerLegend(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Blue markers are bars and night clubs\n\n" +
                "Green markers are restaurants and take outs\n\n" +
                "Orange markers are coffee shops\n\n" +
                "Red markers are museums, art galleries, stadiums, zoos and more\n\n" +
                "Starred markers are your saved Favourites")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Know your marker meanings!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SplashScreen.infoShown = true;
                    }
                })
        .show();
       // AlertDialog alert = builder.create();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    // Go back to home menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.home){

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clears the back stack
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    // Prepares the map with new markers each time

    @Override
    public void onMapReady(GoogleMap googleMap) {
         mMap = googleMap;

        /* This if statement checks to see if the array has contents. If so the camera of the map is
        set to Limerick City. The for loop then iterates through the list of venues that were selected
        in the previous screen.
         */

        //clear the map for new markers
        mMap.clear();

       Collections.sort(MainActivity.selectedPlaces, new Comparator<Places>() {
         @Override
           public int compare(Places place1, Places place2) {
            return Double.compare(place1.getDistanceFromUser(),place2.getDistanceFromUser());
           }
       });

        if(MainActivity.selectedPlaces.size() != -1 && MainActivity.selectedPlaces.size() != 0)
        {

            SplashScreen.locationManager.requestLocationUpdates(SplashScreen.provider, 200, 1, this);
            Log.i("Listener"," Listening for user movement");

            // start for loop at 1 to avoid default 0,0 latlng
            for (int i = 0; i < MainActivity.selectedPlaces.size(); i++)
            {
                if(!MainActivity.selectedPlaces.get(i).getisFavd()){

                    // the below code redraws all the saved places every time
                    mMap.addMarker(new MarkerOptions()
                            .position(MainActivity.selectedPlaces.get(i).getPlacePosition())
                            .title(MainActivity.selectedPlaces.get(i).getPlaceName())
                            .icon(BitmapDescriptorFactory.fromResource(MainActivity.selectedPlaces.get(i).getPoiMarker())));
                    if(MainActivity.searchMode) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.selectedPlaces.get(i).getPlacePosition(), 13));
                    }
                    else{
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SplashScreen.newUserLocation, 13));
                    }
                }

                else{
                        // the below code redraws all the saved places every time
                        mMap.addMarker(new MarkerOptions()
                                .position(MainActivity.selectedPlaces.get(i).getPlacePosition())
                                .title(MainActivity.selectedPlaces.get(i).getPlaceName())
                                .icon(BitmapDescriptorFactory.fromResource(MainActivity.selectedPlaces.get(i).getPoiFavdMarker())));
                    if(MainActivity.searchMode) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.selectedPlaces.get(i).getPlacePosition(), 13));
                    }
                    else{
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SplashScreen.newUserLocation, 13));
                    }
                }

            }


            if (mMap != null) {
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newUserLocation, 12)); // the position and zoom value of map

                if (userMarker != null) {
                    userMarker.setPosition(SplashScreen.newUserLocation);
                } else {
                    userMarker = mMap.addMarker(user
                            .position(SplashScreen.newUserLocation)
                            .title("Your location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.usermarker)));


                    //MainActivity.locationManager.removeUpdates(this);
                    //Log.i("Listener", "Stopped listening for user movement"); // WHICH MEANS ITS TURNED OFF UNTIL Map is restarted
                }

            }

        }
        else{

                Toast.makeText(getApplicationContext(), "Nothing from the top POIs in this area", Toast.LENGTH_SHORT).show();

        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                for (Places markerData : MainActivity.selectedPlaces) {
                    if (markerData.getPlaceName().equals(marker.getTitle())) {
                        if(mMap.getCameraPosition().zoom>14){
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(markerData.getPlacePosition().latitude + (double) 100 / Math.pow(2, mMap.getCameraPosition().zoom), markerData.getPlacePosition().longitude), mMap.getCameraPosition().zoom));
                        }
                        else {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(markerData.getPlacePosition().latitude + (double) 100 / Math.pow(2, 14), markerData.getPlacePosition().longitude), 14));
                        }
                    }}
                marker.showInfoWindow();
                return true;
            }
        });



            // Setting up my custom inflaters
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()

            {
                @Override
                public View getInfoWindow (Marker marker){

                return null;
            }


                @Override
                public View getInfoContents (Marker marker){

                for (Places markerData : MainActivity.selectedPlaces) {
                    if (markerData.getPlaceName().equals(marker.getTitle())) {


                        // create info contents as View
                        View contentView = getLayoutInflater().inflate(R.layout.info_window_contents, null);
                        // View.inflate(getApplicationContext(), R.layout.info_window_contents, null);

                        // Set image
                        ImageView contentImageView = (ImageView) contentView.findViewById(R.id.info_window_image);
                        contentImageView.setImageBitmap(markerData.getPlaceImage());

                        // Set title
                        TextView contentTitleTextView = (TextView) contentView.findViewById(R.id.info_window_title);
                        contentTitleTextView.setText(markerData.getPlaceName());

                        // Set snippet as address retrieved
                        TextView contentSnippetTextView = (TextView) contentView.findViewById(R.id.info_window_snippet);
                        contentSnippetTextView.setText(markerData.getPlaceAddress());

                        wheel = (ProgressBar) contentView.findViewById(R.id.wheel);
                        wheel.setVisibility(View.GONE);



                        // return newly created View
                        return contentView;
                    }

                }
                return null;
            }});

            // When Info Window is pushed, it will take user to new activity for the POI
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()

            {

                @Override
                public void onInfoWindowClick (Marker marker){


                    if (!marker.getTitle().equals("Your location")) {
                        for (Places markerData : MainActivity.selectedPlaces)
                        {
                            if (markerData.getPlaceName().equals(marker.getTitle()))
                            {

                                wheel.setVisibility(View.VISIBLE);

                                for (Places place : SplashScreen.masterList)
                                {

                                    if (place.getPlaceName().equals(markerData.getPlaceName())) {

                                        place.setMasterPostCounter(0);
                                        place.setCounterPast6Hours(0);
                                        place.setCounter6to12(0);
                                        place.setCounter12to18(0);
                                        place.setCounter18to24(0);

                                    }

                                }

                                markerData.setMasterPostCounter(0);
                                markerData.setCounterPast6Hours(0);
                                markerData.setCounter6to12(0);
                                markerData.setCounter12to18(0);
                                markerData.setCounter18to24(0);

                                markerData.posts.clear(); // clear out old data as it will be reloaded with requestbe reloaded with request

                                String userRecentMedia = "";
                                InstagramTask instaTask;
                                String instaResponse = "";
                                String emptyInsta = "{\"meta\": {\"code\": 200}, \"data\": []}";

                                userRecentMedia = "https://api.instagram.com/v1/media/search?lat=" +
                                        markerData.getPlacePosition().latitude + "&lng=" + markerData.getPlacePosition().longitude + "&distance=140&access_token=" + SplashScreen.accessToken;

                                try {
                                    instaTask = new InstagramTask();
                                    instaResponse = instaTask.execute(userRecentMedia).get();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.i("URL Search", "Unsuccessful");
                                }

                                String imageURL = "";
                                String postText = "";
                                String userName = "";
                                String profilePicUrl = "";
                                String date = "";
                                Bitmap bitImg = null;
                                Bitmap profileBitImg = null;


                                try {

                                    if (!instaResponse.equals(emptyInsta)) {
                                        JSONObject obj1 = new JSONObject(instaResponse);

                                        JSONArray jsonArr = obj1.getJSONArray("data");


                                        for (int i = 0; i < jsonArr.length(); i++) {
                                            String resource = jsonArr.getJSONObject(i).toString();
                                            JSONObject obj2 = new JSONObject(resource);

                                            String geoNest = obj2.getJSONObject("images").toString(); //RESOURCE OBJECT
                                            JSONObject obj3 = new JSONObject(geoNest);
                                            imageURL = obj3.getJSONObject("standard_resolution").getString("url"); //REACHED THE FIELDS

                                            ImageDownloader task = new ImageDownloader();
                                            try {
                                                // It then executes the task of image downloader
                                                bitImg = task.execute(imageURL).get();

                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }


                                                date = obj2.getString("created_time");



                                                long x = Long.parseLong(date) * 1000;
                                                Date dateTrans = new Date(x);

                                                String imageDateFormatted = SplashScreen.formatter.format(dateTrans);

                                                Date d1 = null;
                                                Date d2 = null;

                                                try {

                                                    d1 = SplashScreen.formatter.parse(imageDateFormatted);
                                                    d2 = SplashScreen.formatter.parse(SplashScreen.currentDateFormatted);

                                                    //in milliseconds
                                                    long diff = d2.getTime() - d1.getTime();

                                                    long diffHours = diff / (60 * 60 * 1000) % 24;
                                                    long diffDays = diff / (24 * 60 * 60 * 1000);


                                                    if (diffDays < 1) {

                                                        if (diffHours < 6) {
                                                            markerData.setCounterPast6Hours(markerData.getCounterPast6Hours() + 1);

                                                        } else if (diffHours >= 6 && diffHours < 12) {
                                                            markerData.setCounter6to12(markerData.getCounter6to12() + 1);
                                                        } else if (diffHours >= 12 && diffHours < 18) {
                                                            markerData.setCounter12to18(markerData.getCounter12to18() + 1);
                                                        } else {
                                                            markerData.setCounter18to24(markerData.getCounter18to24() + 1);
                                                        }

                                                        markerData.setMasterPostCounter(markerData.getMasterPostCounter() + 1);


                                                    } else {
                                                        markerData.setMasterPostCounter(markerData.getMasterPostCounter() + 1);
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            try {
                                                geoNest = obj2.getJSONObject("caption").toString(); //RESOURCE OBJECT
                                                obj3 = new JSONObject(geoNest);
                                                postText = obj3.getString("text");
                                            }
                                            catch(Exception e){
                                                postText = "";
                                            }


                                            geoNest = obj2.getJSONObject("user").toString(); //RESOURCE OBJECT
                                            obj3 = new JSONObject(geoNest);
                                            userName = obj3.getString("username");
                                            profilePicUrl = obj3.getString("profile_picture");
                                            ImageDownloader task2 = new ImageDownloader();
                                            try {
                                                // It then executes the task of image downloader
                                                profileBitImg = task2.execute(profilePicUrl).get();

                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }

                                            String uiDate = SplashScreen.uiFormatter.format(dateTrans);
                                            String source = "Instagram";


                                            POISocialMediaPosts post = new POISocialMediaPosts(source, uiDate, userName, postText, imageURL, bitImg, profileBitImg);
                                            markerData.addPosts(post);

                                        }

                                    } else {

                                        Log.i("Posts", "Zero posts");
                                    }

                                } catch (JSONException e) {

                                    Log.i("JSON Search Status", "Something went wrong in JSON process.Check array access");

                                }

                            }
                        }


                        Intent intent = new Intent(MapsActivity.this, PlaceDetail.class);
                        intent.putExtra("Place", marker.getTitle());
                        startActivity(intent);
                    }

            }
            }

            );


    }

    public class InstagramTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls)
        {

            String response = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1)
                {
                    char current = (char) data;
                    response += current;
                    data = reader.read();

                }

                return response;

            } catch (Exception e) {
                Log.i("Instagram API Search", "Error reading source code of URL");
                response = "nothing";
            }
            return response;
        }

    }



    // This happens on the background thread and is called on when performing JSO read through
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        // the String... is like an array 'varargs'
        @Override
        protected Bitmap doInBackground(String... urls) {


            URL url;
            HttpURLConnection connection = null; // think of it like a browser to fetch the contents

            try {
                //converts th URL to a valid URL
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.connect();
                InputStream inputStream = connection.getInputStream();


                //return to the original metohd that called it
                return BitmapFactory.decodeStream(inputStream);


            } catch (Exception e) {

                e.printStackTrace();


            }
            return null;

        }


    }


    @Override
    protected void onPause() {
        super.onPause();

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                    //provider = locationManager.NETWORK_PROVIDER;
                    //location = locationManager.getLastKnownLocation(provider);
                    Log.i("info","need to ask permission since it is note accepted in the first time");
                    // need to ask permission since it is note accepted in the first time
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                }
                else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    // after first installation this triggers so location should be called from onRequestPermissionsResult method
                    Log.i("info", "if statement working, permission yet not granted");
                }

                return;
            } else {
                SplashScreen.locationManager.removeUpdates(this);
                Log.i("Listener","Puased. Stopped listening for user movement");
            }
        } catch (Exception e) {
            Log.i("Exception", "Exception on pause");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                    //provider = locationManager.NETWORK_PROVIDER;
                    //location = locationManager.getLastKnownLocation(provider);
                    Log.i("info","need to ask permission since it is note accepted in the first time");
                    // need to ask permission since it is note accepted in the first time
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                }
                else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    // after first installation this triggers so location should be called from onRequestPermissionsResult method
                    Log.i("info", "if statement working, permission yet not granted");
                }

                return;
            }
            else {

                SplashScreen.locationManager.requestLocationUpdates(SplashScreen.provider, 400, 1, this); // 20000 is the interval between user location update
                Log.i("Listener"," Listening for user movement");

            }
        } catch (Exception e) {
            Log.i("Exception", "Exception on resume");
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        // Cautious of LatLng on emulator as it sets to null cay=using error. Used terminal to send values instead.
        SplashScreen.lat = location.getLatitude();
        SplashScreen.lng = location.getLongitude();

        Log.i("Latitude", SplashScreen.lat.toString());
        Log.i("Longitude", SplashScreen.lng.toString());

        Log.i("Tracker", "Just about to update lat lng");
        SplashScreen.newUserLocation = new LatLng(SplashScreen.lat, SplashScreen.lng);

        // This may need to go back in // m.remove();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
