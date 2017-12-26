package com.example.android.bookapppreference;

import android.graphics.Bitmap;

/**
 * Created by Noemi on 12/25/2017.
 */

public class Book {

    private String mAuthor;

    private String mTitle;

    private String mDescription;

    private Bitmap mImage;

    private double mPrice;

    private String mUrl;

    private String mPublished;

    public Book ( String author, String title, String description, Bitmap image, double price, String url, String published){
        mAuthor = author;
        mImage = image;
        mPrice = price;
        mDescription = description;
        mTitle = title;
        mUrl = url;
        mPublished = published;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getTitle (){
        return mTitle;
    }

    public String getDescription(){
        return mDescription;
    }

    public Bitmap getImage(){
        return mImage;
    }

    public double getPrice(){
        return mPrice;
    }

    public String getUrl(){
        return mUrl;
    }

    public String getPublished(){ return mPublished;}



}
