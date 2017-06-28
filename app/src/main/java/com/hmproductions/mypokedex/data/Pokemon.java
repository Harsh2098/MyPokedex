package com.hmproductions.mypokedex.data;

/**
 * Created by Harsh Mahajan on 28/6/2017.
 */

public class Pokemon {
    
    String mName, mWeight, mHeight, mType, mImageURL;

    public Pokemon(String name, String weight, String height, String type, String imageURL) {
        mName = name;
        mWeight = weight;
        mHeight = height;
        mType = type;
        mImageURL = imageURL;
    }

    public String getName() {
        return mName;
    }

    public String getWeight() {
        return mWeight;
    }

    public String getHeight() {
        return mHeight;
    }

    public String getType() {
        return mType;
    }

    public String getImageURL() {
        return mImageURL;
    }

}
