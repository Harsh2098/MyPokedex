package com.hmproductions.mypokedex.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Harsh Mahajan on 28/6/2017.
 */

public class PokemonContract {

    public static final String CONTENT_AUTHORITY = "com.hmproductions.mypokedex";

    private static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CAPTION = "pokemon";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_CAPTION);

    public class PokemonEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "pokemon";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE_URL = "imageURL";
    }
}
