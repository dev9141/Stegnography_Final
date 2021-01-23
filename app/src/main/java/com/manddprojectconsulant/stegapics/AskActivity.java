package com.manddprojectconsulant.stegapics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class AskActivity extends AppCompatActivity {

    ImageView ivEncryptedImage;
    Button btnShare, save_image_button;
    boolean isSave = false;
    AdView adsinask;
    InterstitialAd interstitialAdaftersave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_ask);

        initforask();


        //Ads

        AdshowinAsk();


        Bitmap imgToSave = EncryptionActivity.encoded_image_save;
        ivEncryptedImage.setImageBitmap(imgToSave);

        save_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //after save done
                saveToInternalStorage(imgToSave);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code for share
                if (isSave) {
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), imgToSave, "Image I want to share", null);
                    Uri uri = Uri.parse(path);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, "Share Image"));
                } else {
                    Toast.makeText(AskActivity.this, "First, save the image and then share.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void AdshowinAsk() {

        MobileAds.initialize(this, "ca-app-pub-8674673470489334~6195848859");
        AdRequest adRequest = new AdRequest.Builder().build();
        adsinask.loadAd(adRequest);


    }

    private void initforask() {

        save_image_button = findViewById(R.id.save_image_button);
        ivEncryptedImage = findViewById(R.id.ivEncryptedImage);
        btnShare = findViewById(R.id.btnShare);
        adsinask = findViewById(R.id.adsinask);

    }

    private void saveToInternalStorage(Bitmap bitmapImage) {
        try {
            File myDir = new File(Environment.getExternalStorageDirectory(), "StegoImage");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "SG-" + new SimpleDateFormat("yyMMddhhmm").format(Calendar.getInstance().getTime()) + ".png";
            File file = new File(myDir, fname);
            //EncryptionActivity.checkAndRequestPermissions();
            FileOutputStream fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            isSave = true;

            Adinlongshow();

            refreshGallary(myDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Adinlongshow() {

        MobileAds.initialize(this, "ca-app-pub-8674673470489334~6195848859");
        AdRequest adIRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad Activity
        interstitialAdaftersave = new InterstitialAd(this);

        // Insert the Ad Unit ID
        //add admob_interstitial_id unit id in string file
        interstitialAdaftersave.setAdUnitId("ca-app-pub-8674673470489334/3441708914");

        // Interstitial Ad load Request
        interstitialAdaftersave.loadAd(adIRequest);

        interstitialAdaftersave.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function when the Ad loads
                displayInterstitial();
            }
        });
    }

    private void displayInterstitial() {
        if (interstitialAdaftersave.isLoaded()) {
            interstitialAdaftersave.show();
        }

    }

    private void refreshGallary(File file) {
        Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        i.setData(Uri.fromFile(file));
        sendBroadcast(i);
    }
}