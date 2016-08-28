package com.stuart.righthererightnow;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SocialMediaComments extends AppCompatActivity {



    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "mrsmurphysbabyboy\n30-Aug-16  18:45", "awhiskeyorthree\n30-Aug-16  18:45", "jonnyg9t9\n30-Aug-16  18:45", "shanerussell86\n30-Aug-16  18:45",
            "danejamess\n30-Aug-16  18:45", "allenwixted\n30-Aug-16  18:45", "bobsuruncle3\n30-Aug-16  18:45", "barryhackett1\n30-Aug-16  18:45",
    };


    int[] listviewImage = new int[]{
            R.drawable.stuart, R.drawable.kelly, R.drawable.john, R.drawable.shane,
            R.drawable.dane, R.drawable.allen, R.drawable.ger, R.drawable.barry,
    };

    String[] listviewShortDescription = new String[]{
            "This area is where all non picture dependent content will go. For example Facebook statuses/check ins and Twitter tweets that are all geo located.",
            "This area is where all non picture dependent content will go. For example Facebook statuses/check ins and Twitter tweets that are all geo located.",
            "This area is where all non picture dependent content will go. For example Facebook statuses/check ins and Twitter tweets that are all geo located.",
            "This area is where all non picture dependent content will go. For example Facebook statuses/check ins and Twitter tweets that are all geo located.",
            "This area is where all non picture dependent content will go. For example Facebook statuses/check ins and Twitter tweets that are all geo located.",
            "This area is where all non picture dependent content will go. For example Facebook statuses/check ins and Twitter tweets that are all geo located.",
            "This area is where all non picture dependent content will go. For example Facebook statuses/check ins and Twitter tweets that are all geo located.",
            "This area is where all non picture dependent content will go. For example Facebook statuses/check ins and Twitter tweets that are all geo located.",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media_comments);

        /*
        //Setting up the back button
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        */

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_discription", listviewShortDescription[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_discription"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.social_media_comment, from, to);
        ListView androidListView = (ListView) findViewById(R.id.all_comments);
        androidListView.setAdapter(simpleAdapter);
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