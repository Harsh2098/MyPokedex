package com.hmproductions.mypokedex.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by harsh on 9/5/17.
 */

public class Type {

    @SerializedName("name")
    String name;

    public Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
