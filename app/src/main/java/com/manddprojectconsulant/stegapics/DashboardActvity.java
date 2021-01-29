package com.manddprojectconsulant.stegapics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardActvity extends AppCompatActivity {

    RecyclerView rvencryptedlist;
    FloatingActionButton fabencryption, fabdecryption, fabshare, fabrate;

    //Checking with List View donot consider it

    List<Encryptedlist> encryptedlists;
    EncryptedlistAdapter encryptedlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_actvity);

        checkAndRequestPermissions();
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

    public void checkAndRequestPermissions() {
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int CameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 1);
        }
    }


    private void ListShow() {
        encryptedlists = new ArrayList<>();
        encryptedlists = getlist();

        Collections.reverse(encryptedlists);
        encryptedlistAdapter = new EncryptedlistAdapter(encryptedlists,DashboardActvity.this);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getApplicationContext());
        rvencryptedlist.setLayoutManager(mLayoutManger);
        rvencryptedlist.setItemAnimator(new DefaultItemAnimator());
        rvencryptedlist.setAdapter(encryptedlistAdapter);
    }

    public ArrayList<Encryptedlist> getlist() {
        ArrayList<Encryptedlist> encryptedlists = new ArrayList<>();
        String FolderName = getResources().getString(R.string.main_folder_name);
        File file = new File(Environment.getExternalStorageDirectory(), FolderName);
        File[] files = file.listFiles();

        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                Encryptedlist encryptedlist = new Encryptedlist();
                File f = files[i];
                Log.e("getVideoList", "File name: " + f.getName());
                //String[] FileName = f.getName().split(".");
                encryptedlist.title = f.getName().substring(0, f.getName().length()-4);
                encryptedlist.filepath = f.getPath();
                encryptedlists.add(encryptedlist);
            }
        }

        return encryptedlists;
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