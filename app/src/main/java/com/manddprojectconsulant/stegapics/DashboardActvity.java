package com.manddprojectconsulant.stegapics;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.getbase.floatingactionbutton.BuildConfig;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DashboardActvity extends AppCompatActivity {

    public static final int RequestPermissionCode = 7;
    RecyclerView rvencryptedlist;
    FloatingActionButton fabencryption, fabdecryption, fabshare, fabrate;

    //Checking with List View donot consider it

    List<Encryptedlist> encryptedlists;
    EncryptedlistAdapter encryptedlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_actvity);

        init();

        //Checking App Permission Granted or Not
        if (CheckingPermissionIsEnabledOrNot()) {
            //Entered in if Permission is granted

            new LoadImages(DashboardActvity.this).execute();
        }
        // If, If permission is not enabled then else condition will execute.
        else {
            //Calling method to enable permission.
            RequestMultiplePermission();
        }


        //Please not consider it showing the list view how it looks for design purpose
        //ListShow();
        new LoadImages(DashboardActvity.this).execute();


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

    public boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(DashboardActvity.this, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
                }, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && ReadExternalStoragePermission && WriteExternalStoragePermission ) {
                        //Toast.makeText(ActivityLogin.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        //init();
                        new LoadImages(DashboardActvity.this).execute();
                    } else {
                        finish();
                        Toast.makeText(DashboardActvity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private String ListShow() {
        try {
            encryptedlists = new ArrayList<>();
            encryptedlists = getlist();

            Collections.reverse(encryptedlists);
            /*encryptedlistAdapter = new EncryptedlistAdapter(encryptedlists, DashboardActvity.this);
            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getApplicationContext());
            rvencryptedlist.setLayoutManager(mLayoutManger);
            rvencryptedlist.setItemAnimator(new DefaultItemAnimator());
            rvencryptedlist.setNestedScrollingEnabled(false);
            rvencryptedlist.setAdapter(encryptedlistAdapter);
            encryptedlistAdapter.notifyDataSetChanged();*/

            encryptedlistAdapter = new EncryptedlistAdapter(encryptedlists, DashboardActvity.this);
            rvencryptedlist.setAdapter(encryptedlistAdapter);
            rvencryptedlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvencryptedlist.setItemAnimator(new DefaultItemAnimator());
            rvencryptedlist.setNestedScrollingEnabled(false);
            encryptedlistAdapter.notifyDataSetChanged();
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail";
        }
    }

    public ArrayList<Encryptedlist> getlist() {
        ArrayList<Encryptedlist> encryptedlists = new ArrayList<>();
        String FolderName = getResources().getString(R.string.main_folder_name);
        File file = new File(Environment.getExternalStorageDirectory(), FolderName);
        File thumbFile = new File(file, ".thumb");
        File[] files = thumbFile.listFiles();

        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                Encryptedlist encryptedlist = new Encryptedlist();
                File f = files[i];
                if (f.getName().endsWith(".jpg")) {
                    Log.e("getVideoList", "File name: " + f.getName());
                    //String[] FileName = f.getName().split(".");
                    encryptedlist.title = f.getName().substring(0, f.getName().length() - 4);
                    encryptedlist.filepath = f.getPath();
                    encryptedlists.add(encryptedlist);
                }
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

    public class LoadImages extends AsyncTask<String, String, String>{

        Context context;
        ProgressDialog progressDialog;

        public LoadImages(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading Images");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            encryptedlists = new ArrayList<>();
            encryptedlists = getlist();

            if(encryptedlists.size() > 0) {
                Collections.reverse(encryptedlists);
                return "Success";
            }
            else {
                return "NoImage";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog != null){
                progressDialog.dismiss();
            }

            if(s.equals("Success")){
                try {
                    encryptedlistAdapter = new EncryptedlistAdapter(encryptedlists, DashboardActvity.this);
                    rvencryptedlist.setAdapter(encryptedlistAdapter);
                    rvencryptedlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    encryptedlistAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(context, "getting error while loading images", Toast.LENGTH_LONG).show();
            }
        }
    }

}