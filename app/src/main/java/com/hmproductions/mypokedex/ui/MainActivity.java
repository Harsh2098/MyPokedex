package com.hmproductions.mypokedex.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hmproductions.mypokedex.PokeApiClient;
import com.hmproductions.mypokedex.R;
import com.hmproductions.mypokedex.objects.TypeArray;
import com.hmproductions.mypokedex.objects.Pokemon;
import com.hmproductions.mypokedex.data.PokemonContract;
import com.hmproductions.mypokedex.data.PokemonContract.PokemonEntry;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
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
        name_editText = (EditText) findViewById(R.id.name_editText);
        name_textView = (TextView) findViewById(R.id.name_textView);
        weight_textView = (TextView) findViewById(R.id.weight_textView);
        height_textView = (TextView) findViewById(R.id.height_textView);
        type_textView = (TextView) findViewById(R.id.type_textView);
        pokemon_imageView = (ImageView) findViewById(R.id.pokemon_imageView);

        goButton = (Button) findViewById(R.id.goButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();

        GoButtonClickListener(okHttpClient);

    }

    private void GoButtonClickListener(OkHttpClient okHttpClient) {

        goButton.setOnClickListener(view -> {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(POKEAPI_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PokeApiClient pokeApiClient = retrofit.create(PokeApiClient.class);
            Call<Pokemon> call = pokeApiClient.getPokemonDetails(name_editText.getText().toString().toLowerCase());

            progressBar.setVisibility(View.VISIBLE);
            call.enqueue(new Callback<Pokemon>() {
                @Override
                public void onResponse(@NonNull Call<Pokemon> call, @NonNull Response<Pokemon> response) {
                    Pokemon pokemon = response.body();
                    updateUI(pokemon);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<Pokemon> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

        });
    }

    private void updateUI(Pokemon pokemon) {
        if (pokemon== null || pokemon.getName() == null || pokemon.getSprites() == null) {
            name_textView.setText("This pokemon does not exist !");
            pokemon_imageView.setImageResource(R.mipmap.error_pokemon);
            weight_textView.setText("WEIGHT : unknown");
            type_textView.setText("TYPE   : unknown");
            height_textView.setText("HEIGHT : unknown");
        } else {
            name_textView.setText(pokemon.getName());
            weight_textView.setText("WEIGHT :" + pokemon.getWeight());
            type_textView.setText("TYPE   : " + extractTypes(pokemon));
            height_textView.setText("HEIGHT : " + pokemon.getHeight());
            Glide.with(this).load(pokemon.getSprites().getImageURL()).into(pokemon_imageView);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PokemonEntry.COLUMN_NAME, pokemon.getName());
            contentValues.put(PokemonEntry.COLUMN_IMAGE_URL, pokemon.getSprites().getImageURL());

            boolean insertion = insertIntoDatabase(pokemon.getName());
            if (insertion)
                getContentResolver().insert(PokemonContract.CONTENT_URI, contentValues);
        }
    }

    private String extractTypes(Pokemon pokemon) {

        StringBuilder stringBuilder = new StringBuilder();

        List<TypeArray> list = pokemon.getTypes();

        for(TypeArray currentItem : list) {
            stringBuilder.append(currentItem.getType().getName() ).append(",");
        }

        return stringBuilder.toString();//.substring(0, stringBuilder.length()-1);
    }

    private boolean insertIntoDatabase(String name) {
        Cursor cursor = getContentResolver().query(PokemonContract.CONTENT_URI, null, null, null, null, null);

        if (cursor != null)
            while (cursor.moveToNext())
                if (cursor.getString(cursor.getColumnIndexOrThrow(PokemonEntry.COLUMN_NAME)).equals(name)) {
                    cursor.close();
                    return false;
                }
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.history_item:
                if (getContentResolver().query(PokemonContract.CONTENT_URI, null, null, null, null, null).getCount() != 0)
                    startActivity(new Intent(MainActivity.this, HistroyActivity.class));
                else
                    Toast.makeText(MainActivity.this, "History is Empty", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
