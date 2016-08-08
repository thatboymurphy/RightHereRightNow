package com.stuart.righthererightnow;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by stuart on 19/07/16.
 */
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

    boolean isItFavd = false;


    public Places()
    {

        this("null",null,"null","null",null, null);

    }

    public Places(String placeName, String[] placeTypes, String placeAddress, String placeContactNumber, LatLng position, Bitmap placeImage)
    {
        this.placeName = placeName;
        this.placeTypes = placeTypes;
        this.placeAddress = placeAddress;
        this.placeContactNumber = placeContactNumber;
        this.position = position;
        this.placeImage  = placeImage;

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
