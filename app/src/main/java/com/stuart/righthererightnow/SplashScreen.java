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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import java.util.ArrayList;

public class SplashScreen extends Activity implements LocationListener {
    // Hard Coded Lat Lngs
    //Double lat = 52.655746;
    //Double lng = -8.619048;

    //Double lat =  52.674166;
    //Double lng =  -8.573931;

    DownloadTask task,task2,task3,task4;
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

    // For Google Places API section
    int jsonCounter = 1;
    String pageToken = "";

    // String varibles to place into URL
    String drink = "bar|night_club";
    String food = "restaurant";
    String coffee = "cafe";
    String fun = "amusement_park|art_gallery|casino|zoo|stadium|movie_theater|shopping_mall|museum";

    String browserKey = "AIzaSyCmcj932D55W-U7bk2rPDmy0khT_u9JfJA";

    //Setting up the alert box boolean to only show on start up
    static boolean infoShown = false;

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


        // Fill the master array of places on start up
        if(masterList.size()==0 || masterList.size()== -1)
        {
            task = new DownloadTask();
            task2 = new DownloadTask();
            task3 = new DownloadTask();
            task4 = new DownloadTask();
            findJSON();
        }

        Log.i("tester","good news");



    }

    public void findJSON() {
        String call1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lat.toString() + "," + lng.toString() + "&radius=3000&type=" + drink
                +"&key="+browserKey;
        String call2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lat.toString() + "," + lng.toString() + "&radius=3000&type=" + food +
                "&key="+browserKey;
        String call3 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lat.toString() + "," + lng.toString() + "&radius=3000&type=" + coffee +
                "&key="+browserKey;
        String call4 ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lat.toString() + "," + lng.toString() + "&radius=3000&type=" + fun +
                "&key="+browserKey;


        if(counter<4) {
            try {

            /*
            Google Places API offers up to 60 results for each query. They are split up into 20 per
            page and each new page is accessed using the Page Token. If there are more than 20,
            the page token is provided and can be used to access a new URL of 20 places.

            In this instances, I only want 20 places so I have commented out the process of getting
            more

             */

                //  if(pageToken == "")
                //  {
                // First time around there is no page token, run this

                ;

                if(counter==0){
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, call1);

                }
                else if(counter==1){
                    task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, call2);

                }
                else if(counter==2){
                    task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, call3);

                }

                else if(counter==3){
                    task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, call4);

                }


                //  }

                //else
                //  {
                // If there is an additional results page, the page token wil lbe passed up and run this URL
                //  task.execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat.toString() + "," + lat.toString() + "&radius=2000&type=" + typeURL +"&key=AIzaSyCmcj932D55W-U7bk2rPDmy0khT_u9JfJA&pagetoken=" + pageToken);
                // }


            } catch (Exception e) {

                e.printStackTrace();
                Log.i("URL Search", "Unsuccessful");

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

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    response += current;
                    data = reader.read();

                }
                counter++;
                findJSON();

                // Pass down the source code of the URL in form of a string
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
            String name = "";

            try {

                // The entire response of the URL page is made into an JSON object
                JSONObject obj1 = new JSONObject(response);

                // Accessible values can now be pulled from this object.
                try
                {
                    pageToken = obj1.getString("next_page_token");
                }

                // If there are less than 20 pages, there is no value for pageToken so its value is
                // assigned "" to avoid errors.
                catch(Exception e){
                    pageToken = "";
                }

                // Within in the entire response are arrays that hold nest information
                JSONArray jsonarr = obj1.getJSONArray("results");

                // Once an array of JSONObjects are created, we can iterate though the array
                // searching for objects we require within
                for (int i = 0; i < jsonarr.length(); i++)
                {
                    String resource = jsonarr.getJSONObject(i).toString();
                    JSONObject obj2 = new JSONObject(resource);

                    // Getting the name of a place
                    name = obj2.getString("name");

                    // Getting the address of a place, try/catch as some palces do not have address
                    String address = "";
                    try{
                        address = obj2.getString("vicinity");}
                    catch(Exception e){
                        address = "No Given Address";
                    }

                    // Getting the location of a place
                    String geoNest = obj2.getJSONObject("geometry").toString();

                    JSONObject obj3 = new JSONObject(geoNest);
                    String locationLng = obj3.getJSONObject("location").getString("lng");
                    String locationLat = obj3.getJSONObject("location").getString("lat");


                    // Getting the types of a place
                    String[] allTypes = null;
                    try {

                        JSONArray types = (JSONArray) ((JSONObject) jsonarr.get(i)).get("types");
                        String result = types.toString();
                        result = result.substring(1, result.length() - 1);
                        allTypes = result.split(",");
                        allTypes = sortTypes(allTypes);



                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // Getting the reference for a palces photo
                    String placePhoto = "";
                    try
                    {
                        JSONArray jsonarr2 = obj2.getJSONArray("photos");

                        for (int j = 0; j < jsonarr2.length(); j++)
                        {
                            String resource2 = jsonarr2.getJSONObject(j).toString();
                            JSONObject obj4 = new JSONObject(resource2);
                            placePhoto = obj4.getString("photo_reference");
                        }

                    } catch(Exception e)
                    {
                        // Keep it empty so a later condition tells the UI there is no image available
                        placePhoto = "";
                    }

                    // Using the reference to get the actual image
                    ImageDownloader task = new ImageDownloader();
                    Bitmap myImg = null;
                    try
                    {
                        if(placePhoto != "")
                        {
                            // It then executes the task of image downloader
                            myImg = task.execute("https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&maxheight=250&phot" +
                                    "oreference=" + placePhoto + "&key=AIzaSyCmcj932D55W-U7bk2rPDmy0khT_u9JfJA\n").get();
                        }

                        else
                        {
                            myImg = task.execute("http://www.askdeb.com/wp-content/uploads/2013/07/No-Photo-Available.png").get();
                        }


                    }
                    catch (Exception e) {

                        e.printStackTrace();
                    }

                    int poiIcon;

                    if (allTypes[0].equals("Bar")|| allTypes[0].equals("Night Club"))
                    {
                        poiIcon = R.drawable.beerblack;
                    }
                    else if (allTypes[0].equals("Restaurant"))
                    {
                        poiIcon = R.drawable.foodblack;
                    }
                    else if (allTypes[0].equals("Cafe"))
                    {
                        poiIcon = R.drawable.coffeeblack;
                    }
                    else
                    {
                        poiIcon = R.drawable.otherblack;
                    }

                    double roundUp = CalculateLatLngDistance.distance(newUserLocation.latitude,newUserLocation.longitude,Double.parseDouble(locationLat) ,Double.parseDouble(locationLng) ,"K");

                    // Notihng is on list...add away
                    if(masterList.size() <= 0){
                        Places newPOI = new Places(name, allTypes, address, new LatLng(Double.parseDouble(locationLat), Double.parseDouble(locationLng)), myImg, poiIcon, roundUp);
                        masterList.add(newPOI);

                    }

                    // To avoid adding duplicates i.e. some pubs are also restaurants
                    else{
                        boolean isOnList = false;
                        for (Places place : masterList) {

                            if (place.getPlaceName().equals(name)) {
                                isOnList = true;
                                break;
                            }

                        }
                        // Only add it if it is not already on the list
                        if(!isOnList){
                            Places newPOI = new Places(name, allTypes, address, new LatLng(Double.parseDouble(locationLat), Double.parseDouble(locationLng)), myImg, poiIcon, roundUp);
                            masterList.add(newPOI);

                        }
                    }

                    //   Log.i("Counter", jsonCounter + "\n");
                    // Log.i("LatLng", locationLat + " " + locationLng + "\n");
                    //  Log.i("Location Name",name + "\n");
                    // Log.i("Types", allTypes[0] + "," + allTypes[1]+ "," + allTypes[2]+ "\n");
                    // Log.i("Location Address", address + "\n");
                    // Log.i("Picture", placePhoto + "\n");

                    jsonCounter++;

                }


                // If there is another results page, pageToken will have a value and return to the JSON() to read the next URL
                if(pageToken != "" )
                {
                    // findJSON(); // commented out because I decided I only want one page that has 20 results
                    //  Log.i("Could load more pages", pageToken + "\n");
                    jsonCounter = 1;

                    if(counter==4) {
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "POIs in your area uploaded", Toast.LENGTH_LONG).show();

                        // close this activity
                        finish();
                    }
                }

                // If it is empty that means that there are no more results pages and the process is complete
                else
                {
                    Log.i("JSON Search Status", " Request Complete");
                    jsonCounter = 1;

                    if(counter==4) {
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "POIs in your area uploaded", Toast.LENGTH_LONG).show();

                        // close this activity
                        finish();
                    }

                }


            } catch (JSONException e) {

                Log.i("JSON Search Status", "Something went wrong in JSON process.Check array access");

            }



        }
    }

    public String[] sortTypes(String[] allTypes){

        // Improving the format
        for(int j = 0; j < allTypes.length;j++)
        {
            switch (allTypes[j]) {
                case "\"bar\"":

                    allTypes[j] = "Bar";
                    break;
                case "\"night_club\"":
                    allTypes[j] = "Night Club";
                    break;
                case "\"amusement_park\"":

                    allTypes[j] = "Amusements";
                    break;
                case "\"art_gallery\"":

                    allTypes[j] = "Art Gallery";

                    break;
                case "\"cafe\"":

                    allTypes[j] = "Cafe";

                    break;
                case "\"casino\"":

                    allTypes[j] = "Casino";

                    break;

                case "\"zoo\"":

                    allTypes[j] = "Zoo";

                    break;

                case "\"stadium\"":
                    allTypes[j] = "Stadium";

                    break;
                case "\"shopping_mall\"":

                    allTypes[j] = "Shopping Centre";

                    break;

                case "\"movie_theater\"":

                    allTypes[j] = "Cinema";

                    break;
                case "\"restaurant\"":

                    allTypes[j] = "Restaurant";

                    break;
                case "\"museum\"":

                    allTypes[j] = "Museum";

                    break;

                case "\"food\"":

                    allTypes[j] = "Food";

                    break;

                case "\"meal_takeaway\"":

                    allTypes[j] = "Take Away";

                    break;

                case "\"bakery\"":

                    allTypes[j] = "Bakery";

                    break;

                case "\"lodging\"":

                    allTypes[j] = "Accommodation";

                    break;
                default:
                    allTypes[j] = "";
                    break;
            }

        }
        return allTypes;
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

                // Convert content of URL to bitmap
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                //return to the original metohd that called it
                return myBitmap;


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
            locationManager.requestLocationUpdates(provider, 200, 1, this);


            if (location != null) {

                Log.i("Location info", "Location achieved!");


                onLocationChanged(location);

            } else {

                Log.i("Location info", "No location :(");

            }

        }
    }



    // The important method for updating the users location

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