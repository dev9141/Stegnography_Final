package com.manddprojectconsulant.stegapics;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class EncryptedlistAdapter extends RecyclerView.Adapter<EncryptedlistAdapter.ViewHolder> {

    public static Bitmap myBitmap;
    List<Encryptedlist> encryptedlists;
    Context context;

    public EncryptedlistAdapter(List<Encryptedlist> encryptedlists, Context context) {

        this.encryptedlists = encryptedlists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.encryptedlistdisplay, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Encryptedlist encryptedlist = encryptedlists.get(position);

        holder.textencrypt.setText(encryptedlist.getTitle());
        myBitmap = BitmapFactory.decodeFile(encryptedlist.filepath);
        holder.ivencryptedtextimage.setImageBitmap(myBitmap);


        holder.cardforclickingondetailpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i=new Intent(context,AskActivity.class);
                    i.putExtra("fromList",true);
                    i.putExtra("fromfilepath",encryptedlist.filepath);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(encryptedlist.filepath);
                file.delete();
                encryptedlists.remove(position);
                notifyDataSetChanged();

            }
        });

        holder.share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "image text");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(encryptedlist.filepath));
                shareIntent.setType("image/*");
                context.startActivity(Intent.createChooser(shareIntent, "Share image via:"));

            }
        });

    }


    @Override
    public int getItemCount() {

        return encryptedlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardforclickingondetailpage;
        TextView textencrypt;
        ImageView ivencryptedtextimage;
        Button share_button, delete_button;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            cardforclickingondetailpage = itemView.findViewById(R.id.cardforclickingondetailpage);
            textencrypt = itemView.findViewById(R.id.textencrypt);
            ivencryptedtextimage = itemView.findViewById(R.id.ivencryptedtextimage);
            share_button = itemView.findViewById(R.id.share_button);
            delete_button = itemView.findViewById(R.id.delete_button);
        }
    }
}
