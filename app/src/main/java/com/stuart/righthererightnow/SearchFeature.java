package com.stuart.righthererightnow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class SearchFeature extends AppCompatActivity {

    String browserKey = "AIzaSyCmcj932D55W-U7bk2rPDmy0khT_u9JfJA";
    String call1;
    DownloadTask task1;
    boolean onSystem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_feature);

        //Setting up the back button
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }


    }



    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {



                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber()+place.getId());

                String id = "";
                try{
                    id = place.getId();
                }
                catch(Exception e){
                    id = "";
                }


                ((TextView) findViewById(R.id.searched_address)).setText("\nGive us a sec. Finding POI");


                call1 ="https://maps.googleapis.com/maps/api/place/details/json?placeid="+place.getId()+"&key="+browserKey;

                try {
                    task1 = new DownloadTask();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                        task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, call1);
                   }
                    else {
                       task1.execute(call1);
                   }


                  } catch (Exception e) {

                   e.printStackTrace();
                   Log.i("URL Search", "Unsuccessful");
                  }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
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


                // Response is my string

                // The entire response of the URL page is made into an JSON object
                JSONObject obj1 = new JSONObject(response);
                JSONObject res = obj1.getJSONObject("result");


                    //String resource = obj1.getJSONObject("results").toString();
                   // Log.i("Check Point", resource);

                    //JSONObject obj2 = new JSONObject(resource);

                    String name = "";
                    try {
                        // Getting the name of a place
                        name = res.getString("name");


                    } catch (Exception e) {
                        name = "No Found Name";
                    }

                // Getting the address of a place, try/catch as some palces do not have address
                String address = "";
                try {
                    address = res.getString("vicinity");
                } catch (Exception e) {
                    address = "No Found Address";
                }

                String locationLng = "";
                String locationLat = "";
                // Getting the location of a place
                try {
                    String geoNest = res.getJSONObject("geometry").toString();
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
                    String types = res.getJSONArray("types").toString();

                    JSONArray tester = new JSONArray(types);
                    String result = types;
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

                // placesg the reference for a places photo
                String photoRef = "";
                try {
                    JSONArray jsonarr2 = res.getJSONArray("photos");
                    for (int j = 0; j < jsonarr2.length(); j++) {
                        String resource2 = jsonarr2.getJSONObject(j).toString();
                        JSONObject obj4 = new JSONObject(resource2);
                        photoRef = obj4.getString("photo_reference");
                        break;
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



                // Calculate the POIs distance from user
                double roundUp = CalculateLatLngDistance.distance(SplashScreen.newUserLocation.latitude, SplashScreen.newUserLocation.longitude, Double.parseDouble(locationLat), Double.parseDouble(locationLng), "K");

                int masterPostCounter = 0;
                int counterPast6Hours = 0;
                int counter6to12 = 0;
                int counter12to18 = 0;
                int counter18to24 = 0;

                ArrayList<POISocialMediaPosts> userPosts = new ArrayList<POISocialMediaPosts>();


                for(Places favd : MainActivity.myFavourites)
                {
                    if(name.equals(favd.getPlaceName())){
                        MainActivity.selectedPlaces.add(favd);
                        onSystem = true;

                        Intent i = new Intent(SearchFeature.this, MapsActivity.class);
                        startActivity(i);
                        // close this activity
                        finish();

                    }

                }
                if(!onSystem) {
                    for (Places master : SplashScreen.masterList) {
                        if (name.equals(master.getPlaceName())) {
                            MainActivity.selectedPlaces.add(master);
                            onSystem = true;

                            Intent i = new Intent(SearchFeature.this, MapsActivity.class);
                            startActivity(i);
                            // close this activity
                            finish();

                        }

                    }
                }

                if(!onSystem) {
                    try {


                        Places newPOI = new Places(name, allTypes, address, new LatLng(Double.parseDouble(locationLat), Double.parseDouble(locationLng)), photoRef, myImg, poiIcon, poiMarker, poiFavdMarker, roundUp,
                                masterPostCounter, counterPast6Hours, counter6to12, counter12to18, counter18to24, userPosts);
                        MainActivity.selectedPlaces.add(newPOI);

                        Intent i = new Intent(SearchFeature.this, MapsActivity.class);
                        startActivity(i);

                        // close this activity
                        finish();
                    } catch (Exception e) {
                        ((TextView) findViewById(R.id.searched_address)).setText("\nChoice not specific enough. Try Again.");


                    }
                }




            } catch (JSONException e) {
                Log.i("JSON Search Status", "Something went wrong in JSON process.Check array access");
            }





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
}
