package com.hmproductions.mypokedex.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Harsh Mahajan on 28/6/2017.
 *
 * Pokemon holds each pokemon's details.
 */

public class Pokemon {

    @SerializedName("name")
    String name;

    @SerializedName("weight")
    String weight;

    @SerializedName("height")
    String height;

    @SerializedName("types")
    List<TypeArray> types;

    @SerializedName("sprites")
    Sprites sprites;

    public Pokemon(String name, String weight, String height, List<TypeArray> types, Sprites sprites) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.types = types;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public List<TypeArray> getTypes() {
        return types;
    }

    public Sprites getSprites() {
        return sprites;
    }

}
