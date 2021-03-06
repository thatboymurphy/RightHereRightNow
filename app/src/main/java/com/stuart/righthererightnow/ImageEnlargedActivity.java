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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageEnlargedActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_enlarged);

        //Setting up the back button
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        // get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");
       // ImageAdapter imageAdapter = new ImageAdapter(this);

        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        imageView.setImageBitmap(PlaceDetail.posts.get(position).getImageBitmap());

        ImageView profileView = (ImageView) findViewById(R.id.profilePic);
        profileView.setImageBitmap(PlaceDetail.posts.get(position).getProfileImg());

        TextView dateAndAuthor = (TextView) findViewById(R.id.dateAndAuthor);
        dateAndAuthor.setText(PlaceDetail.posts.get(position).getDate()+" \n"+PlaceDetail.posts.get(position).getUserName());
        //imageView.setImageResource(imageAdapter.mThumbIds[position]);

        TextView message = (TextView) findViewById(R.id.content);
        message.setText("via " + PlaceDetail.posts.get(position).getSource()+" \n\n"+PlaceDetail.posts.get(position).getPostText());
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