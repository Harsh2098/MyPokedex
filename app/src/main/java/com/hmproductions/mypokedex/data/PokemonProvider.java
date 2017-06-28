package com.hmproductions.mypokedex.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.hmproductions.mypokedex.data.PokemonContract.PokemonEntry;

/**
 * Created by Harsh Mahajan on 28/6/2017.
 */

public class PokemonProvider extends ContentProvider {


    static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    PokemonDbHelper mDatabaseHelper;
    SQLiteDatabase mDatabase;

    private static final int URI_WITHOUT_PATH = 100;
    private static final int URI_WITH_PATH = 101;

    static {
        mUriMatcher.addURI(PokemonContract.CONTENT_AUTHORITY, PokemonContract.PATH_CAPTION, URI_WITHOUT_PATH);
        mUriMatcher.addURI(PokemonContract.CONTENT_AUTHORITY, PokemonContract.PATH_CAPTION + "/#", URI_WITH_PATH);
    }

    @Override
    public boolean onCreate() {

        mDatabaseHelper = new PokemonDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        mDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor;

        switch (mUriMatcher.match(uri))
        {
            case URI_WITHOUT_PATH :
                cursor = mDatabase.query(PokemonEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case URI_WITH_PATH :
                selection = PokemonContract.PokemonEntry.COLUMN_ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = mDatabase.query(PokemonEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default : throw new IllegalArgumentException("Cannot serve URI request at this moment.");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        mDatabase = mDatabaseHelper.getWritableDatabase();

        long colID;
        switch (mUriMatcher.match(uri)) {
            case URI_WITHOUT_PATH:
                colID = mDatabase.insert(PokemonEntry.TABLE_NAME, null, values);
                if (colID == -1)
                    Toast.makeText(getContext(), "Failed to insert", Toast.LENGTH_SHORT).show();
                else
                    getContext().getContentResolver().notifyChange(uri, null);
                break;

            default:
                throw new IllegalArgumentException("Cannot serve URI request at this moment.");
        }
        return ContentUris.withAppendedId(uri, colID);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        mDatabase = mDatabaseHelper.getWritableDatabase();
        int noOfRowsDeleted = 0;

        switch (mUriMatcher.match(uri))
        {
            case URI_WITHOUT_PATH :
                noOfRowsDeleted = mDatabase.delete(PokemonEntry.TABLE_NAME, null,null);
                getContext().getContentResolver().notifyChange(uri, null);
                break;

            case URI_WITH_PATH :
                selection = PokemonEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri))};
                noOfRowsDeleted = mDatabase.delete(PokemonEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;

            default: throw new IllegalArgumentException("Delete failed");
        }

        return noOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new IllegalArgumentException("Update is not implemented here");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
