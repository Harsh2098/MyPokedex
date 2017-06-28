package com.hmproductions.mypokedex.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.hmproductions.mypokedex.data.PokemonContract;
import com.hmproductions.mypokedex.data.PokemonContract.PokemonEntry;
import com.hmproductions.mypokedex.utils.PokemonLoader;
import com.hmproductions.mypokedex.R;
import com.hmproductions.mypokedex.data.Pokemon;
import com.hmproductions.mypokedex.utils.QueryUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String POKEAPI_BASE_URL = "http://pokeapi.co/api/v2/pokemon/";
    private static final int LOADER_ID = 101;
    EditText name_editText;
    TextView name_textView, weight_textView, height_textView, type_textView;
    ImageView pokemon_imageView;
    Button goButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding Views
        name_editText = (EditText)findViewById(R.id.name_editText);
        name_textView = (TextView)findViewById(R.id.name_textView);
        weight_textView = (TextView)findViewById(R.id.weight_textView);
        height_textView = (TextView)findViewById(R.id.height_textView);
        type_textView = (TextView)findViewById(R.id.type_textView);
        pokemon_imageView = (ImageView)findViewById(R.id.pokemon_imageView);

        goButton = (Button)findViewById(R.id.goButton);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        GoButtonClickListener();
    }

    private void GoButtonClickListener() {

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(LOADER_ID, null, MainActivity.this);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateUI(String jsonResponse)
    {
        Pokemon pokemon =  QueryUtils.extractPokemonDetails(jsonResponse);
        if(pokemon.getName() == null) {
            name_textView.setText("This pokemon does not exist !");
            pokemon_imageView.setImageResource(R.mipmap.error_pokemon);
            weight_textView.setText("WEIGHT : unknown");
            type_textView.setText  ("TYPE   : unknown");
            height_textView.setText("HEIGHT : unknown");
        }
        else {
            name_textView.setText(pokemon.getName());
            weight_textView.setText("WEIGHT :" + pokemon.getWeight());
            type_textView.setText  ("TYPE   : " + pokemon.getType());
            height_textView.setText("HEIGHT : " + pokemon.getHeight());
            Glide.with(this).load(pokemon.getImageURL()).into(pokemon_imageView);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PokemonEntry.COLUMN_NAME, pokemon.getName());
            contentValues.put(PokemonEntry.COLUMN_IMAGE_URL, pokemon.getImageURL());

            boolean insertion = insertIntoDatabase(pokemon.getName());
            if(insertion)
                getContentResolver().insert(PokemonContract.CONTENT_URI, contentValues);
        }
    }

    private boolean insertIntoDatabase(String name)
    {
        Cursor cursor = getContentResolver().query(PokemonContract.CONTENT_URI,null,null,null,null,null);

        if(cursor != null)
            while(cursor.moveToNext())
                if(cursor.getString(cursor.getColumnIndexOrThrow(PokemonEntry.COLUMN_NAME)).equals(name)) {
                    cursor.close();
                    return false;
                }
        return true;

    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        weight_textView.setText("WEIGHT : loading...");
        type_textView.setText  ("TYPE   : loading...");
        height_textView.setText("HEIGHT : loading...");
        name_textView.setText("loading...");
        return new PokemonLoader(this, POKEAPI_BASE_URL + name_editText.getText().toString().toLowerCase());
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        updateUI(data);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.history_item :
                if(getContentResolver().query(PokemonContract.CONTENT_URI,null,null,null,null,null).getCount() != 0)
                    startActivity(new Intent(MainActivity.this, HistroyActivity.class));
                else
                    Toast.makeText(MainActivity.this,"History is Empty",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
