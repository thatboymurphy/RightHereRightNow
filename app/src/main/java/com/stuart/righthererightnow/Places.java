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

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class Places {

    private String placeName;
    private String rating;
    private String openStatus;
    private String[] placeTypes;


    private String placeType1;
    private String placeType2;
    private String placeType3;

    private String placeAddress;
    private LatLng position;
    private String photoRef;
    private Bitmap placeImage;

    private double distanceFromUser;

    private int poiIcon;
    private int poiFavdMarker;
    private int poiMarker;

    boolean isItFavd = false;


    // Instagram Stuff
    private int masterPostCounter;
    private int counterPast6Hours;
    private int counter6to12;
    private int counter12to18;
    private int counter18to24;

    ArrayList<POISocialMediaPosts> posts;


    public Places()
    {

        this("null","null","null",null,"null",null,"null", null,0,0,0,0.0,0,0,0,0,0,null);

    }

    public Places(String placeName,String rating, String openStatus, String[] placeTypes, String placeAddress, LatLng position,String photoRef, Bitmap placeImage, int poiIcon, int poiMarker, int poiFavdMarker, double distanceFromUser,
                  int masterPostCounter,int counterPast6Hours, int counter6to12, int counter12to18, int counter18to24, ArrayList posts )
    {
        this.placeName = placeName;
        this.rating=rating;
        this.openStatus = openStatus;
        this.placeTypes = placeTypes;
        this.placeAddress = placeAddress;
        this.position = position;
        this.photoRef = photoRef;
        this.placeImage  = placeImage;
        this.poiIcon = poiIcon;
        this.poiFavdMarker = poiFavdMarker;
        this.poiMarker = poiMarker;
        this.distanceFromUser = distanceFromUser;

        placeType1 = placeTypes[0];
        placeType1 = org.apache.commons.lang3.text.WordUtils.capitalizeFully(placeType1);

        placeType2 = placeTypes[1];
        placeType2 = org.apache.commons.lang3.text.WordUtils.capitalizeFully(placeType2);

        placeType3 = placeTypes[2];
        placeType3 = org.apache.commons.lang3.text.WordUtils.capitalizeFully(placeType3);


        this.masterPostCounter = masterPostCounter;
        this.counterPast6Hours = counterPast6Hours;
        this.counter6to12 = counter6to12;
        this.counter12to18 = counter12to18;
        this.counter18to24 = counter18to24;
        this.posts = posts;

    }


    public String[] getPlaceTypes()
    {

        return placeTypes;

    }

    public String getPlaceName()
    {

        return placeName;

    }

    public String getRating()
    {

        return rating;

    }

    public String getOpenStatus()
    {

        return openStatus;

    }




    public String getPlaceType1()
    {

        return placeType1;

    }
    public String getPlaceType2()
    {

        return placeType2;

    }

    public String getPlaceType3()
    {

        return placeType3;

    }


    public String getPlaceAddress()
           {

            return placeAddress;

    }


    public LatLng getPlacePosition()
    {

        return position;

    }

    public String getPhotoRef()
    {

        return photoRef;

    }

    public Bitmap getPlaceImage()
    {

        return placeImage;

    }

    public int getPoiIcon()
    {

        return poiIcon;

    }
    public int getPoiFavdMarker()
    {

        return poiFavdMarker;

    }

    public int getPoiMarker()
    {

        return poiMarker;

    }

    public boolean getisFavd(){

        return isItFavd;
    }

    public double getDistanceFromUser(){

        return distanceFromUser;
    }

    public int getMasterPostCounter(){

        return masterPostCounter;
    }

    public int getCounterPast6Hours(){

        return counterPast6Hours;
    }

    public int getCounter6to12(){

        return counter6to12;
    }

    public int getCounter12to18(){

        return counter12to18;
    }

    public int getCounter18to24(){

        return counter18to24;
    }

    public ArrayList<POISocialMediaPosts> getPosts(){

        return posts;
    }






    // SETTERS

    public void addPosts(POISocialMediaPosts post){

        posts.add(post);

    }



    public void setPlaceFav(boolean favourites){

        this.isItFavd = favourites;

    }

    public void setPlaceImage(Bitmap placeImage){

        this.placeImage = placeImage;

    }

    public void setMasterPostCounter(int masterPostCounter){

        this.masterPostCounter = masterPostCounter;
    }

    public void setCounterPast6Hours(int counterPast6Hours){

        this.counterPast6Hours = counterPast6Hours;
    }

    public void setCounter6to12(int counter6to12){

        this.counter6to12 = counter6to12;
    }

    public void setCounter12to18(int counter12to18){

        this.counter12to18 = counter12to18;
    }

    public void setCounter18to24(int counter18to24){

        this.counter18to24 = counter18to24;
    }





}
