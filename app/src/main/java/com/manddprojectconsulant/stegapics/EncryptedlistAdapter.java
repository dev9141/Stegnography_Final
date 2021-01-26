package com.manddprojectconsulant.stegapics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EncryptedlistAdapter extends RecyclerView.Adapter<EncryptedlistAdapter.ViewHolder> {

        List<Encryptedlist> encryptedlists;

    public EncryptedlistAdapter(List<Encryptedlist> encryptedlists) {

        this.encryptedlists=encryptedlists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.encryptedlistdisplay, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;



    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Encryptedlist encryptedlist=encryptedlists.get(position);

        holder.textencrypt.setText(encryptedlist.getTitle());
        holder.ivencryptedtextimage.setImageResource(encryptedlist.getImageid());


    }



    @Override
    public int getItemCount() {

        return encryptedlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardforclickingondetailpage;
        TextView textencrypt;
        ImageView ivencryptedtextimage;
        Button share_button,delete_button;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            cardforclickingondetailpage=itemView.findViewById(R.id.cardforclickingondetailpage);
            textencrypt=itemView.findViewById(R.id.textencrypt);
            ivencryptedtextimage=itemView.findViewById(R.id.ivencryptedtextimage);
            share_button=itemView.findViewById(R.id.share_button);
            delete_button=itemView.findViewById(R.id.delete_button);
        }
    }
}
