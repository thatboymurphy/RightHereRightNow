package com.stuart.righthererightnow;

import android.graphics.Bitmap;

/**
 * Created by stuart on 08/08/16.
 */
public class POISocialMediaPosts {

    private String source;
    private String imageURL;
    private String postText;
    private String userName;
    private String date ;
    private Bitmap myImg;
    private Bitmap profileImg;


    public POISocialMediaPosts()
    {

        this("null","null","null","null","null",null, null);

}

    public POISocialMediaPosts(String source, String date, String userName, String postText, String imageURL, Bitmap myImg, Bitmap profileImg)
    {
        this.date = date;
        this.userName = userName;
        this.postText = postText;
        this.imageURL = imageURL;
        this.myImg = myImg;
        this.profileImg = profileImg;
        this.source = source;

    }


    public String getSource()
    {

        return source;

    }
    public String getDate()
    {

        return date;

    }

    public String getUserName()
    {

        return userName;

    }


    public String getPostText()
    {

        return postText;

    }
    public String getImageURL()
    {

        return imageURL;

    }

    public Bitmap getImageBitmap()
    {

        return myImg;

    }

    public Bitmap getProfileImg()
    {

        return profileImg;

    }



    //SETTERS

    public void setDate(String date)
    {

        this.date=date;

    }

    public void setUserName(String userName)
    {

        this.userName = userName;

    }


    public void setPostText(String postText)
    {

       this.postText = postText;

    }
    public void setImageURL(String imageURL)
    {
        this.imageURL=imageURL;

    }

    public void setBitmap(Bitmap imageBitmap){

        imageBitmap=this.myImg;

    }




}
