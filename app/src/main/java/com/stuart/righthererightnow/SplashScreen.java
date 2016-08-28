/*
* Author: Stuart Murphy
* Student ID: 10046828
* Project: Masters Thesis
* Date: 08/08/2016
*
* Most Recnt 17th aug
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
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SplashScreen extends Activity implements LocationListener {
    int counter = 0;

    // An array for types of Places
    static ArrayList<Places> masterList = new ArrayList<Places>();

    // User location variables
    static LocationManager locationManager;
    Location location;
    static String provider; //Stores the name of the location provider
    static LatLng newUserLocation = new LatLng(0, 0);

    // Declared globally for user location access
    static Double lat;
    static Double lng;

    // String varibles to place into URL
    String drink = "bar|night_club";
    String food = "restaurant";
    String coffee = "cafe";
    String fun = "amusement_park|art_gallery|casino|zoo|stadium|movie_theater|shopping_mall|museum";

    String browserKey = "AIzaSyCmcj932D55W-U7bk2rPDmy0khT_u9JfJA";

    //Setting up the alert box boolean to only show on start up
    static boolean infoShown = false;

    String call1, call2, call3, call4;

    DownloadTask task1, task2, task3,task4;


    static String accessToken = "543153997.3bb3331.66f453d8e7d24f638824352145af3263";
    static String currentDateFormatted = "";
   static  DateFormat formatter = null;

    static DateFormat uiFormatter = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Makes splash screen the entire screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        // Set up the users whereabouts
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Gets the best provider available at the time, boolean decides whether to return only enabled providers
        provider = locationManager.getBestProvider(new Criteria(), false);

        getLocation();

        //Date formatting
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        uiFormatter = new SimpleDateFormat("dd-MMM-yy  HH:mm");

        //get current date time with Date()
        Date now = new Date();
        currentDateFormatted = formatter.format(now);


         call1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lat.toString() + "," + lng.toString() + "&radius=3000&type=" + drink
                +"&key="+browserKey;
         call2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lat.toString() + "," + lng.toString() + "&radius=3000&type=" + food +
                "&key="+browserKey;
         call3 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lat.toString() + "," + lng.toString() + "&radius=3000&type=" + coffee +
                "&key="+browserKey;
         call4 ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lat.toString() + "," + lng.toString() + "&radius=3000&type=" + fun +
                "&key="+browserKey;


        // Fill the master array of places on start up
        if(masterList.size()==0 || masterList.size()== -1)
        {

            if(counter<4) {
                try {
                    task1 = new DownloadTask();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                        task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, call1);
                    }
                    else {
                        task1.execute(call1);
                    }

                    task2 = new DownloadTask();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                        task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, call2);
                    }
                    else {
                        task2.execute(call2);


                    }

                    task3 = new DownloadTask();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                        task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, call3);
                    }
                    else {
                        task3.execute(call3);
                    }


                    task4 = new DownloadTask();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                        task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, call4);
                    }
                    else {
                        task4.execute(call4);
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                    Log.i("URL Search", "Unsuccessful");

                }
            }
        }

    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls)
        {
            // This method reads through the source code of the given URL


            String response = "";
            URL url;
            HttpURLConnection urlConnection = null;
            InputStream in = null;

            try {

                        url = new URL(urls[0]);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        in = urlConnection.getInputStream();
                        InputStreamReader reader = new InputStreamReader(in);
                        int data = reader.read();
                        while (data != -1) {
                            char current = (char) data;
                            response += current;
                            data = reader.read();
                        }
                in.close();
                urlConnection.disconnect();


                    return response;

                } catch (Exception e) {
                    Log.i("Google Places Search", "Error reading source code of URL");
                }


                return null;

        }

        @Override
        protected void onPostExecute(String response) {
            // This where where we use JSON technique to pull objects and what we need from the string passed down
            super.onPostExecute(response);
            Log.i("Status", "Beginning Parse");
                try {

                    // The entire response of the URL page is made into an JSON object
                    JSONObject obj1 = new JSONObject(response);

                    // Within in the entire response are arrays that hold nest information
                    JSONArray jsonarr = obj1.getJSONArray("results");

                    // Once an array of JSONObjects are created, we can iterate though the array
                    // searching for objects we require within
                    for (int i = 0; i < jsonarr.length(); i++) {
                        String resource = jsonarr.getJSONObject(i).toString();
                        JSONObject obj2 = new JSONObject(resource);

                        String name = "";
                        try {
                            // Getting the name of a place
                            name = obj2.getString("name");
                        } catch (Exception e) {
                            name = "No Found Name";
                        }

                        // Getting the address of a place, try/catch as some palces do not have address
                        String address = "";
                        try {
                            address = obj2.getString("vicinity");
                           //address.replace(",","\n");
                        } catch (Exception e) {
                            address = "No Found Address";
                        }

                        String locationLng = "";
                        String locationLat = "";
                        // Getting the location of a place
                        try {
                            String geoNest = obj2.getJSONObject("geometry").toString();
                            JSONObject obj3 = new JSONObject(geoNest);
                            locationLng = obj3.getJSONObject("location").getString("lng");
                            locationLat = obj3.getJSONObject("location").getString("lat");
                        } catch (Exception e) {
                            locationLng = "No Found Longitude";
                            locationLat = "No Found Latitude";
                        }

                        // Getting the types of a place
                        String[] allTypes = null;
                        try {
                            JSONArray types = (JSONArray) ((JSONObject) jsonarr.get(i)).get("types");
                            String result = types.toString();
                            result = result.substring(1, result.length() - 1).replace("_"," ").replace("\"","");
                            allTypes = result.split(",");

                            //allTypes = sortTypes(allTypes);
                        } catch (JSONException e) {
                            allTypes = null;
                        }

                        // Assign POI an icon
                        int poiIcon, poiFavdMarker, poiMarker;
                        if (allTypes[0].equalsIgnoreCase("Bar") || allTypes[0].equalsIgnoreCase("Night Club")) {
                            poiIcon = R.drawable.beerblack;
                            poiFavdMarker = R.drawable.beerfav;
                            poiMarker = R.drawable.beernormal;

                        } else if (allTypes[0].equalsIgnoreCase("Restaurant")||allTypes[0].equalsIgnoreCase("Food")||allTypes[0].equalsIgnoreCase("Meal Takeaway")||allTypes[0].equalsIgnoreCase("Meal Delivery")) {
                            poiIcon = R.drawable.foodblack;
                            poiFavdMarker = R.drawable.foodfav;
                            poiMarker = R.drawable.foodnormal;

                        } else if (allTypes[0].equalsIgnoreCase("Cafe")) {
                            poiIcon = R.drawable.coffeeblack;
                            poiFavdMarker = R.drawable.coffeefav;
                            poiMarker = R.drawable.coffeenorm;

                        } else {
                            poiIcon = R.drawable.otherblack;
                            poiFavdMarker = R.drawable.otherfav;
                            poiMarker = R.drawable.othernorm;

                        }

                        // placesg the reference for a palces photo
                        String photoRef = "";
                        try {
                            JSONArray jsonarr2 = obj2.getJSONArray("photos");
                            for (int j = 0; j < jsonarr2.length(); j++) {
                                String resource2 = jsonarr2.getJSONObject(j).toString();
                                JSONObject obj4 = new JSONObject(resource2);
                                photoRef = obj4.getString("photo_reference");
                            }
                        } catch (Exception e) {
                            // Keep it empty so a later condition tells the UI there is no image available
                            photoRef = "";
                        }

                        // Using the reference to get the actual image
                        ImageDownloader task = new ImageDownloader();
                        Bitmap myImg = null;
                        try {
                            if (photoRef != "") {
                                // It then executes the task of image downloader
                                myImg = task.execute("https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&maxheight=250&phot" +
                                        "oreference=" + photoRef + "&key=AIzaSyCmcj932D55W-U7bk2rPDmy0khT_u9JfJA\n").get();
                            } else {
                                myImg = task.execute("http://www.askdeb.com/wp-content/uploads/2013/07/No-Photo-Available.png").get();
                            }


                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                        String rating = "";
                        try {
                            // Getting the name of a place
                            rating = obj2.getString("rating");
                        } catch (Exception e) {
                            rating = "No Rating Set";
                        }

                        String openStatus = "";
                        // Getting the location of a place
                        try {
                            String geoNest = obj2.getJSONObject("opening_hours").toString();
                            JSONObject obj3 = new JSONObject(geoNest);
                            openStatus = obj3.getString("open_now");

                            if(openStatus.equals("true")){
                                openStatus = "OPEN";
                            }
                            else{
                                openStatus = "CLOSED";
                            }

                        } catch (Exception e) {
                            openStatus = "It is a mystery!";
                        }

                        // Calculate the POIs distance from user
                        double roundUp = CalculateLatLngDistance.distance(newUserLocation.latitude, newUserLocation.longitude, Double.parseDouble(locationLat), Double.parseDouble(locationLng), "K");

                        int masterPostCounter = 0;
                        int counterPast6Hours = 0;
                        int counter6to12 = 0;
                        int counter12to18 = 0;
                        int counter18to24 = 0;

                        ArrayList<POISocialMediaPosts> userPosts = new ArrayList<POISocialMediaPosts>();


                        // If notihng is on list...add away
                        if (masterList.size() <= 0) {

                            Places newPOI = new Places(name,rating,openStatus, allTypes, address, new LatLng(Double.parseDouble(locationLat), Double.parseDouble(locationLng)), photoRef, myImg, poiIcon,poiMarker,poiFavdMarker, roundUp,
                                    masterPostCounter, counterPast6Hours, counter6to12, counter12to18, counter18to24, userPosts);
                            masterList.add(newPOI);


                        }
                        // To avoid adding duplicates i.e. some pubs are also restaurants
                        else {
                            boolean isOnList = false;
                            for (Places place : masterList) {
                                if (place.getPlaceName().equals(name)) {
                                    isOnList = true;
                                    break; //break here if it finds one copy
                                }
                            }
                            // Only add it if it is not already on the list
                            if (!isOnList) {
                                POISocialMediaPosts post = new POISocialMediaPosts();
                                Places newPOI = new Places(name, rating,openStatus, allTypes, address, new LatLng(Double.parseDouble(locationLat), Double.parseDouble(locationLng)), photoRef, myImg, poiIcon,poiMarker,poiFavdMarker, roundUp,
                                        masterPostCounter, counterPast6Hours, counter6to12, counter12to18, counter18to24, userPosts);
                                masterList.add(newPOI);
                            }
                        }
                    }

                    counter++;
                    Log.i("JSON Search Status", " Request Complete");

                } catch (JSONException e) {
                    Log.i("JSON Search Status", "Something went wrong in JSON process.Check array access");
                }



            if(counter==4) {

                String userRecentMedia = "";
                InstagramTask instaTask;
                String instaResponse ="";
                String emptyInsta = "{\"meta\": {\"code\": 200}, \"data\": []}";

                for(Places place: masterList) {

                      userRecentMedia = "https://api.instagram.com/v1/media/search?lat=" +
                            place.getPlacePosition().latitude + "&lng=" + place.getPlacePosition().longitude + "&distance=140&access_token=" + accessToken;

                    try
                    {
                        instaTask = new InstagramTask();
                        instaResponse = instaTask.execute(userRecentMedia).get();

                    } catch (Exception e)
                    {
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

                        if(!instaResponse.equals(emptyInsta)) {
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

                                String imageDateFormatted = formatter.format(dateTrans);


                                Date d1 = null;
                                Date d2 = null;

                                String difference = "";

                                try{

                                    d1 = formatter.parse(imageDateFormatted);
                                    d2 = formatter.parse(currentDateFormatted);

                                    //in milliseconds
                                    long diff = d2.getTime() - d1.getTime();

                                    long diffSeconds = diff / 1000 % 60;
                                    long diffMinutes = diff / (60 * 1000) % 60;
                                    long diffHours = diff / (60 * 60 * 1000) % 24;
                                    long diffDays = diff / (24 * 60 * 60 * 1000);

                                    difference = (String.valueOf(diffDays)+ " days, "+String.valueOf(diffHours)+ " hours, "+String.valueOf(diffMinutes)+ " minutes, "+String.valueOf(diffSeconds)+ " seconds AGO");

                                    if(diffDays<1){

                                        if(diffHours<6){
                                            place.setCounterPast6Hours(place.getCounterPast6Hours()+1);

                                        }
                                        else if(diffHours>=6&&diffHours<12){
                                            place.setCounter6to12(place.getCounter6to12()+1);
                                        }
                                        else if(diffHours>=12&&diffHours<18){
                                            place.setCounter12to18(place.getCounter12to18()+1);
                                        }
                                        else{
                                            place.setCounter18to24(place.getCounter18to24()+1);
                                        }

                                        place.setMasterPostCounter(place.getMasterPostCounter()+1);


                                    }
                                    else{
                                        place.setMasterPostCounter(place.getMasterPostCounter()+1);
                                    }

                                }
                                catch (Exception e) {
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

                                String uiDate = uiFormatter.format(dateTrans);
                                String source = "Instagram";

                                //Log.i("Date", x + "\n");
                                Log.i("POI", place.getPlaceName() + "\n");
                                Log.i("Date", uiDate + "\n");
                                Log.i("UserName", userName + "\n");
                                Log.i("Post", postText + "\n");
                                Log.i("URL", imageURL + "\n");
                                Log.i("Diff", difference + "\n");




                                POISocialMediaPosts post = new POISocialMediaPosts(source,uiDate, userName, postText, imageURL, bitImg, profileBitImg);
                                place.addPosts(post);


                            }

                        }

                        else{

                            Log.i("Posts","Zero posts");
                        }

                    }
                    catch (JSONException e) {

                        Log.i("JSON Search Status", "Something went wrong in JSON process.Check array access");

                    }



                }



                    for(Places place: masterList) {
                       String test1 = place.getPlaceName();
                        int test2 =  place.getPosts().size();
                        Log.i("Tester  ", test1 + "    " + test2);
                    }


                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "POIs in your area uploaded", Toast.LENGTH_LONG).show();

                // close this activity
                finish();
            }








        }
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
        protected Bitmap doInBackground(String... urls){


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



    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                //provider = locationManager.NETWORK_PROVIDER;
                //location = locationManager.getLastKnownLocation(provider);
                Log.i("info","need to ask permission since it is note accepted in the first time");
                // this part triggers to ask permission when access is not granted in the first time user runs the app (this triggers in the second run)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                // after first installation this part triggers so getLocation() should be called from onRequestPermissionsResult method
                Log.i("info", "if statement working, permission yet not granted");
            }

            return;
        } else {

            // This will be null on an emulator, so it will make the rest of the process crash when searching URL
            // Gets a quick fix on where the user is likely to be, may take a few seconds to get
            // the reading, be awre of that when waiting at start
            location = locationManager.getLastKnownLocation(provider);

            // Be sure to get the most recent whereabouts of user
            // the provider we are using. minimum time between location updates, number of meters, context(the app)
            locationManager.requestLocationUpdates(provider, 500, 1, this);


            if (location != null) {

                Log.i("Location info", "Location achieved!");


                onLocationChanged(location);

            } else {

                Log.i("Location info", "No location :(");

            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        // Cautious of LatLng on emulator as it sets to null causing error.
        // Used terminal to send values instead and update the LatLng.
        lat = location.getLatitude();
        lng = location.getLongitude();

        Log.i("Latitude", lat.toString());
        Log.i("Longitude", lng.toString());

        Log.i("Tracker", "Just about to update lat lng");
        newUserLocation = new LatLng(lat, lng);

        // This may need to go back in // m.remove();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                // grantResults[0] = -1
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("INFO", "ACCESS GRANTED");
                    //getLocation(); this will crash due to a bug in android system 6.0.x but once this bug is solved you can call your method without restarting your app
                } else {
                    Log.i("INFO", "ACCESS DENIED");
                }
                return;
            }
        }

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