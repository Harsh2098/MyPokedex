package com.hmproductions.mypokedex.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hmproductions.mypokedex.data.PokemonContract.PokemonEntry;

/**
 * Created by Harsh Mahajan on 28/6/2017.
 */

public class PokemonDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "pokemon";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + PokemonEntry.TABLE_NAME + " (" +
                    PokemonEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PokemonEntry.COLUMN_NAME + " TEXT NOT NULL," +
                    PokemonEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL );";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + PokemonEntry.TABLE_NAME;


    public PokemonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
