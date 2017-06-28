package com.hmproductions.mypokedex.utils;

import com.hmproductions.mypokedex.data.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Harsh Mahajan on 28/6/2017.
 */

public class QueryUtils {

    public static Pokemon extractPokemonDetails(String jsonResponse) {

        String name = null;
        String weight = null;
        String height = null;
        StringBuilder typeBuilder = null;
        String imageURL = null;
        try {
            JSONObject root = new JSONObject(jsonResponse);

            name = root.getString("name").toUpperCase();
            weight = String.valueOf(root.getInt("weight")) + " kg";
            height = String.valueOf(root.getInt("height")) + " feet";

            typeBuilder = new StringBuilder("");
            JSONArray types = root.getJSONArray("types");
            for (int i = 0; i < types.length(); ++i) {
                JSONObject currentObject = types.getJSONObject(i);
                JSONObject currentType = currentObject.getJSONObject("type");

                typeBuilder.append(currentType.getString("name").toUpperCase()).append(", ");
            }
            typeBuilder.deleteCharAt(typeBuilder.length()-2);

            JSONObject sprites = root.getJSONObject("sprites");
            imageURL = sprites.getString("front_default");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(name ==null)
            return new Pokemon(null,null,null,null,null);

        return new Pokemon(name, weight, height, typeBuilder.toString(), imageURL);
    }

}
