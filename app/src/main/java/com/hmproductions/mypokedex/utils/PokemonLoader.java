package com.hmproductions.mypokedex.utils;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Harsh Mahajan on 24/2/2017.
 */

public class PokemonLoader extends AsyncTaskLoader<String>
{
    private static final String LOG_TAG = "Exception thrown !";
    private String mUrlString;

    public PokemonLoader(Context context, String url)
    {
        super(context);
        mUrlString = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground()
    {
        String jsonString = "";
        URL url = null;

        try {
            url = createURL(mUrlString);
        } catch (IOException e) {
            e.printStackTrace();

        }

        if(url != null)
            try {
                jsonString = makeHttpConnection(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return jsonString;
    }

    private URL createURL(String stringURL) throws IOException
    {
        URL url = null;

        try
        {
            url = new URL(stringURL);
        } catch(MalformedURLException e)
        {
            Log.d(LOG_TAG,"Malformed URL");
        }

        return url;

    }

    private String makeHttpConnection(URL url) throws IOException
    {
        String jsonResponse = "";

        if(url==null)
            return jsonResponse;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream =null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            jsonResponse = readFromInputStream(inputStream);
        } catch (IOException e){
            Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());

        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromInputStream(InputStream inputStream) throws IOException
    {
        StringBuilder builder = new StringBuilder();

        if(inputStream !=null) {
            InputStreamReader reader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = bufferedReader.readLine();

            while (line != null)
            {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }

        return builder.toString();
    }
}
