package com.hmproductions.mypokedex;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hmproductions.mypokedex.data.PokemonContract;
import com.hmproductions.mypokedex.data.PokemonContract.PokemonEntry;
import com.hmproductions.mypokedex.ui.HistroyActivity;
import com.hmproductions.mypokedex.ui.MainActivity;

/**
 * Created by Harsh Mahajan on 28/6/2017.
 */

public class PokemonRecyclerAdapter extends RecyclerView.Adapter<PokemonRecyclerAdapter.PokemonViewHolder> {

    private  Cursor mCursor;
    private  Context mContext;

    public PokemonRecyclerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View customView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new PokemonViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder customView, int position) {

        mCursor.moveToPosition(position);

        customView.name_textView.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(PokemonEntry.COLUMN_NAME)));
        Glide
                .with(mContext)
                .load(mCursor.getString(mCursor.getColumnIndexOrThrow(PokemonEntry.COLUMN_IMAGE_URL)))
                .into(customView.image_imageView);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public  void swapCursor(Cursor cursor)
    {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    class PokemonViewHolder extends RecyclerView.ViewHolder {

        TextView name_textView;
        ImageView image_imageView;

        PokemonViewHolder(View itemView) {
            super(itemView);

            name_textView = (TextView)itemView.findViewById(R.id.list_name_textView);
            image_imageView = (ImageView)itemView.findViewById(R.id.list_image_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder
                            .setTitle("Delete Action")
                            .setMessage("Do want to delete this pokemon from history ?")
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
                                    mCursor.moveToPosition(getAdapterPosition());
                                    mContext
                                            .getContentResolver()
                                            .delete(
                                                    ContentUris.withAppendedId(
                                                            PokemonContract.CONTENT_URI,
                                                            mCursor.getInt(mCursor.getColumnIndexOrThrow(PokemonEntry.COLUMN_ID))),
                                                    null,
                                                    null);
                                    notifyItemRemoved(getAdapterPosition());
                                    if(getItemCount() ==0) {
                                        mContext.startActivity(new Intent(mContext, MainActivity.class));
                                    }
                                }
                            })
                            .show();
                }
            });

        }
    }
}
