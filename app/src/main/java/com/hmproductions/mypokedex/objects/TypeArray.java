package com.hmproductions.mypokedex.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by harsh on 9/5/17.
 */

public class TypeArray {

    @SerializedName("type")
    Type type;

    public TypeArray(Type type) {
        this.type = type;
    }

    public Type getType() {

        return type;
    }
}
