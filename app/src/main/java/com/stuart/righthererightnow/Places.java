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

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;


public class Places {

    private String placeName;
    private String[] placeTypes;

    private String placeMasterType="";
    private String placeType1;
    private String placeType2;
    private String placeType3;


    private String placeAddress;
    private String placeContactNumber;
    private LatLng position;
    private Bitmap placeImage;

    private int poiIcon;

    boolean isItFavd = false;




    public Places()
    {

        this("null",null,"null","null",null, null,0);

    }

    public Places(String placeName, String[] placeTypes, String placeAddress, String placeContactNumber, LatLng position, Bitmap placeImage, int poiIcon)
    {
        this.placeName = placeName;
        this.placeTypes = placeTypes;
        this.placeAddress = placeAddress;
        this.placeContactNumber = placeContactNumber;
        this.position = position;
        this.placeImage  = placeImage;
        this.poiIcon = poiIcon;

        placeType1 = placeTypes[0];
        placeType2 = placeTypes[1];
        placeType3 = placeTypes[2];




    }


    public String[] getPlaceTypes()
    {

        return placeTypes;

    }

    public String getPlaceName()
    {

        return placeName;

    }

    public String getPlaceMasterType()
    {

        return placeMasterType;

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

    public String getPlaceContactNumber()
    {

        return placeContactNumber;

    }

    public LatLng getPlacePosition()
    {

        return position;

    }

    public Bitmap getPlaceImage()
    {

        return placeImage;

    }

    public int getPoiIcon()
    {

        return poiIcon;

    }

    public boolean getisFavd(){

        return isItFavd;
    }






    // SETTERS



    public void setPlaceMasterType(String placeMasterType)
    {
        this.placeMasterType = placeMasterType;
    }

    public void setPlaceTypes(String[] placeTypes)
    {

        this.placeTypes = placeTypes;

    }

    public void setPlaceAddress(String placeAddress)
    {

        this.placeAddress = placeAddress;

    }

    public void setPlaceContactNumber(String placeContactNumber)
    {

        this.placeContactNumber = placeContactNumber;

    }

    public void setPosition(LatLng position)
    {

        this.position = position;

    }

    public void setPlaceFav(boolean favourites){

        this.isItFavd = favourites;

    }





}
