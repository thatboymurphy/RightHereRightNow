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
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener{    // changed from FragmentActivity to AppCompatActivity to get my action bar working
                                                                                                        // Location listener is for user current location updates

    // Create new user location objects here in avoid redrawing markers in the updates
    MarkerOptions user = new MarkerOptions();
    Marker userMarker = null;

    GoogleMap mMap;


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
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.selectedPlaces.get(i).getPlacePosition(), 12));
                    }
                    else{
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SplashScreen.newUserLocation, 12));
                    }
                }

                else{
                        // the below code redraws all the saved places every time
                        mMap.addMarker(new MarkerOptions()
                                .position(MainActivity.selectedPlaces.get(i).getPlacePosition())
                                .title(MainActivity.selectedPlaces.get(i).getPlaceName())
                                .icon(BitmapDescriptorFactory.fromResource(MainActivity.selectedPlaces.get(i).getPoiFavdMarker())));
                    if(MainActivity.searchMode) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.selectedPlaces.get(i).getPlacePosition(), 12));
                    }
                    else{
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SplashScreen.newUserLocation, 12));
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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                for (Places markerData : MainActivity.selectedPlaces) {
                    if (markerData.getPlaceName().equals(marker.getTitle())) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(markerData.getPlacePosition().latitude + (double) 50 / Math.pow(2, 12), markerData.getPlacePosition().longitude), 13));

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


                       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(markerData.getPlacePosition().latitude + (double) 200 / Math.pow(2, 12), markerData.getPlacePosition().longitude), 12));

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
                Intent intent = new Intent(MapsActivity.this, PlaceDetail.class);
                intent.putExtra("Place", marker.getTitle());
                startActivity(intent);

            }
            }

            );


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
