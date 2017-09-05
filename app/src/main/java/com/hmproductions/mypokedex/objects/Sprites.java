package com.hmproductions.mypokedex.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by harsh on 9/4/17.
 */

public class Sprites {

    @SerializedName("front_default")
    String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
