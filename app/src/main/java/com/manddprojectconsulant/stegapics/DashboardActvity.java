package com.manddprojectconsulant.stegapics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kila.apprater_dialog.lars.AppRater;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DashboardActvity extends AppCompatActivity {

    RecyclerView rvencryptedlist;
    FloatingActionButton fabencryption, fabdecryption, fabshare, fabrate;

    //Checking with List View donot consider it

    List<Encryptedlist> encryptedlists = new ArrayList<>();
    EncryptedlistAdapter encryptedlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_actvity);


        init();

        //Please not consider it showing the list view how it looks for design purpose
        ListShow();


        fabencryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(DashboardActvity.this, EncryptionActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();


            }
        });
        fabdecryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(DashboardActvity.this, ExtractionActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();


            }
        });

        fabshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sharethelink();
            }
        });

        fabrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rating();
            }
        });
    }

    private void ListShow() {

        encryptedlistAdapter = new EncryptedlistAdapter(encryptedlists);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getApplicationContext());
        rvencryptedlist.setLayoutManager(mLayoutManger);
        rvencryptedlist.setItemAnimator(new DefaultItemAnimator());
        rvencryptedlist.setAdapter(encryptedlistAdapter);

        prepareitem();

    }

    private void prepareitem() {

        Encryptedlist item = new Encryptedlist(R.drawable.picone,"Wipro");
        encryptedlists.add(item);
        item = new Encryptedlist(R.drawable.pictwo,"TCS");
        encryptedlists.add(item);
        item = new Encryptedlist(R.drawable.picthree,"Samsung");
        encryptedlists.add(item);

    }


    private void init() {

        //Floating button

        fabencryption = findViewById(R.id.fabencryption);
        fabdecryption = findViewById(R.id.fabdecryption);
        fabshare = findViewById(R.id.fabshare);
        fabrate = findViewById(R.id.fabrate);
        rvencryptedlist = findViewById(R.id.rvencryptedlist);

        //Setting Imageview

//        ivsettings=findViewById(R.id.ivsetting);


    }

    public void Rating() {

        //Rating bar code

        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (Exception e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }


        /*//Please check this out upwards code
        Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show();
*/

    }

    public void Sharethelink() {


        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Screen Cam");
            String shareMessage = "\nPlease recommend this app to your circle.\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            //shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }


    }

}