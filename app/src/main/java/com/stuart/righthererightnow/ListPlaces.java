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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ListPlaces extends AppCompatActivity {

    static ArrayAdapter arrayAdapter;
    static ArrayList<String> placeName = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_places);


        //Setting up the back button
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();


        for (int i = 0; i < MainActivity.selectedPlaces.size(); i++) {

            // Do the calucaltion, from user to POI

            // Format the reuslt
            DecimalFormat df = new DecimalFormat("#0.0");

            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title",MainActivity.selectedPlaces.get(i).getPlaceName());
            hm.put("listview_address",MainActivity.selectedPlaces.get(i).getPlaceAddress());
            hm.put("listview_discription", (df.format(MainActivity.selectedPlaces.get(i).getDistanceFromUser())+" km away"));
            hm.put("listview_image", Integer.toString(MainActivity.selectedPlaces.get(i).getPoiIcon()));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title","listview_address", "listview_discription"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_address, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);



        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (Places listData : MainActivity.selectedPlaces) {
                    if (listData.getPlaceName().equals(MainActivity.selectedPlaces.get(position).getPlaceName())) {

                        for (Places place : SplashScreen.masterList) {

                            if (place.getPlaceName().equals(listData.getPlaceName())) {

                                place.setMasterPostCounter(0);
                                place.setCounterPast6Hours(0);
                                place.setCounter6to12(0);
                                place.setCounter12to18(0);
                                place.setCounter18to24(0);

                            }


                        }

                        listData.setMasterPostCounter(0);
                        listData.setCounterPast6Hours(0);
                        listData.setCounter6to12(0);
                        listData.setCounter12to18(0);
                        listData.setCounter18to24(0);

                        listData.posts.clear(); // clear out old data as it will be reloaded with request

                        String userRecentMedia = "";
                        InstagramTask instaTask;
                        String instaResponse = "";
                        String emptyInsta = "{\"meta\": {\"code\": 200}, \"data\": []}";

                        userRecentMedia = "https://api.instagram.com/v1/media/search?lat=" +
                                listData.getPlacePosition().latitude + "&lng=" + listData.getPlacePosition().longitude + "&distance=140&access_token=" + SplashScreen.accessToken;

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
                                                listData.setCounterPast6Hours(listData.getCounterPast6Hours() + 1);

                                            } else if (diffHours >= 6 && diffHours < 12) {
                                                listData.setCounter6to12(listData.getCounter6to12() + 1);
                                            } else if (diffHours >= 12 && diffHours < 18) {
                                                listData.setCounter12to18(listData.getCounter12to18() + 1);
                                            } else {
                                                listData.setCounter18to24(listData.getCounter18to24() + 1);
                                            }

                                            listData.setMasterPostCounter(listData.getMasterPostCounter() + 1);


                                        } else {
                                            listData.setMasterPostCounter(listData.getMasterPostCounter() + 1);
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
                                    listData.addPosts(post);

                                }

                            } else {

                                Log.i("Posts", "Zero posts");
                            }

                        } catch (JSONException e) {

                            Log.i("JSON Search Status", "Something went wrong in JSON process.Check array access");

                        }

                    }
                }









                Intent intent = new Intent(getApplicationContext(), PlaceDetail.class);
                // Passing the String onto next activity, Place Detail
                intent.putExtra("Location on array", position);
                startActivity(intent);


            }
        });


        // Go back to the map
        Button viewOnMap = (Button) findViewById(R.id.viewOnMap);
        viewOnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)

     {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent);

            }


        }  );


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
