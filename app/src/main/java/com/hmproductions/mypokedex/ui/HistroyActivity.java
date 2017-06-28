package com.hmproductions.mypokedex.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hmproductions.mypokedex.PokemonRecyclerAdapter;
import com.hmproductions.mypokedex.R;
import com.hmproductions.mypokedex.data.PokemonContract;

public class HistroyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 3;
    RecyclerView history_recyclerView;
    PokemonRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histroy);
        setTitle("History");

        if(getSupportActionBar() != null)
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        // Binding recycler view
        history_recyclerView = (RecyclerView)findViewById(R.id.pokemon_history_recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HistroyActivity.this);
        mAdapter = new PokemonRecyclerAdapter(HistroyActivity.this);

        //history_recyclerView.setHasFixedSize(false);
        history_recyclerView.setAdapter(mAdapter);
        history_recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        history_recyclerView.addItemDecoration(itemDecoration);

        getSupportLoaderManager().restartLoader(LOADER_ID, null,this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.histroy_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.delete_item :
                AlertDialog.Builder builder = new AlertDialog.Builder(HistroyActivity.this);
                builder
                        .setTitle("Irreversible Action")
                        .setMessage("Do want to permanently delete history ?")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(true)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getContentResolver().delete(PokemonContract.CONTENT_URI, null,null);
                                mAdapter.swapCursor(null);
                                startActivity(new Intent(HistroyActivity.this, MainActivity.class));
                                finish();
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                PokemonContract.PokemonEntry.COLUMN_ID,
                PokemonContract.PokemonEntry.COLUMN_NAME,
                PokemonContract.PokemonEntry.COLUMN_IMAGE_URL
        };
        return new CursorLoader(this,PokemonContract.CONTENT_URI, projection, null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
