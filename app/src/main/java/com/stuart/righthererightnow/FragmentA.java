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



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

public class FragmentA extends Fragment
{

    // Layout attributes
    ImageView placeImage;
    TextView placeName;
    TextView placeAddress;
    TextView placeType;
    boolean isItFavd;

    TextView distanceFrom;
    TextView rating;
    TextView openStatus;


    public FragmentA()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        final Button favButton = (Button) view.findViewById(R.id.favBtn);
        favButton.setText(R.string.add_fav);

        // Initialize the UI ayytibutes
        placeName = (TextView) view.findViewById(R.id.placeName);
        placeType = (TextView) view.findViewById(R.id.placeType);
        placeImage = (ImageView) view.findViewById(R.id.placeImage);
        placeAddress = (TextView) view.findViewById(R.id.placeAddress);
        distanceFrom = (TextView) view.findViewById(R.id.placeDistance);
        rating = (TextView) view.findViewById(R.id.rating);
        openStatus = (TextView) view.findViewById(R.id.openStatus);

        String stringTypes = PlaceDetail.type+", "+PlaceDetail.type2+", "+PlaceDetail.type3;

        // Fill their details from specif place detail from previous screen
        placeName.setText(PlaceDetail.name);
        placeAddress.setText(PlaceDetail.address);
        placeType.setText(stringTypes);
        placeImage.setImageBitmap(PlaceDetail.image);
        distanceFrom.setText(PlaceDetail.distance);
        rating.setText(PlaceDetail.rating + "/5");
        openStatus.setText(PlaceDetail.openStatus);

        isItFavd = PlaceDetail.fav;


        // Determining what the button should say
        if(isItFavd)
        {
            favButton.setText(R.string.remove_fav);
        }

        else
        {
            favButton.setText(R.string.add_fav);
        }

        favButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!isItFavd){
                    // If it is already a favourite, change the text after button tapped
                    favButton.setText(R.string.remove_fav);
                    // Indicate the successful change to the user
                    Toast.makeText(getActivity().getApplicationContext(), "Added To Favourites!", Toast.LENGTH_SHORT).show();

                    // Change the booleans
                    PlaceDetail.fav = true;
                    isItFavd = true;

                    // go through the master list array
                    for(Places masterListItem: SplashScreen.masterList)
                    {
                        // If the POI equals the name of the current open POI, add it to
                        // the favourites array
                        if(PlaceDetail.name.equals(masterListItem.getPlaceName()))
                        {

                            masterListItem.setPlaceFav(true);
                        }
                    }

                    // Set the boolean statuses to all show true in any other arrays it is in
                    for(Places selectedItems: MainActivity.selectedPlaces)
                    {
                        if(PlaceDetail.name.equals(selectedItems.getPlaceName()))
                        {

                            selectedItems.setPlaceFav(true);
                            MainActivity.myFavourites.add(selectedItems); // this used to be adding the master list one instead
                        }
                    }

                    for(Places favItems: MainActivity.myFavourites)
                    {
                        if(PlaceDetail.name.equals(favItems.getPlaceName()))
                        {
                            favItems.setPlaceFav(true);
                        }
                    }


                }

                else
                {
                    favButton.setText(R.string.add_fav);
                    Toast.makeText(getActivity().getApplicationContext(), "Removed From Favourites!", Toast.LENGTH_SHORT).show();

                    PlaceDetail.fav = false;
                    isItFavd = false;

                    for(Places masterListItem: SplashScreen.masterList)
                    {
                        if(PlaceDetail.name.equals(masterListItem.getPlaceName()))
                        {
                            MainActivity.myFavourites.remove(masterListItem);
                            masterListItem.setPlaceFav(false);
                        }
                    }

                    for(Places selectedItems: MainActivity.selectedPlaces)
                    {
                        if(PlaceDetail.name.equals(selectedItems.getPlaceName()))
                        {
                            selectedItems.setPlaceFav(false);
                        }
                    }

                    for(Places favItems: MainActivity.myFavourites)
                    {
                        if(PlaceDetail.name.equals(favItems.getPlaceName()))
                        {
                            favItems.setPlaceFav(false);
                        }
                    }

                }
                }
        });

        return view;
    }
}
