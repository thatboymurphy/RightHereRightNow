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


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // Keep all Images in array
    public Integer[] mThumbIds = {


    };

    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
    }


    @Override
    public int getCount() {
        int length;
        length = PlaceDetail.posts.size();
        return length;
    }

    @Override
    public Object getItem(int position) {
        return PlaceDetail.posts.get(position).getImageBitmap();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(PlaceDetail.posts.get(position).getImageBitmap());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));

        return imageView;
    }



}