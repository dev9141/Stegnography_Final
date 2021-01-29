package com.manddprojectconsulant.stegapics;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.manddprojectconsulant.stegapics.Text.AsyncTaskCallback.TextDecodingCallback;
import com.manddprojectconsulant.stegapics.Text.ImageSteganography;
import com.manddprojectconsulant.stegapics.Text.TextDecoding;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class AskActivity extends AppCompatActivity implements TextDecodingCallback {

    ImageView ivEncryptedImage;
    Button btnShare, save_image_button;
    boolean isSave = false;
    AdView adsinask;
    InterstitialAd interstitialAdaftersave;
    ProgressDialog progressBar;
    Bitmap imgToSave = null;
    TextView messagefoshowrdecrypt;
    TextInputLayout tlfortextshowdecrypt;
    Boolean flagScreen;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));
        setContentView(R.layout.activity_ask);

        flagScreen = getIntent().getBooleanExtra("fromList", false);

        progressBar = new ProgressDialog(this);


        initforask();


        //Ads

        AdshowinAsk();



        if (flagScreen){

            String filepath = getIntent().getStringExtra("fromfilepath");
            imgToSave = BitmapFactory.decodeFile(filepath);
            ivEncryptedImage.setImageBitmap(imgToSave);
            ivEncryptedImage.setTransitionName("image");

            LinearLayout llbutton=findViewById(R.id.llbuttonask);
            save_image_button.setVisibility(View.GONE);
            tlfortextshowdecrypt.setVisibility(View.VISIBLE);
            llbutton.setGravity(Gravity.CENTER);
            decode(filepath,imgToSave,"MNDProjects9141");

        } else {

            imgToSave = EncryptionActivity.encoded_image_save;
            ivEncryptedImage.setImageBitmap(imgToSave);

        }

        save_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new saveImageAsync(AskActivity.this, imgToSave, v).execute();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = null;
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

    private void decode(String filepath, Bitmap original_image, String secret_key) {
        if (filepath != null) {
            //Making the ImageSteganography object
            ImageSteganography imageSteganography = new ImageSteganography(secret_key,
                    original_image);

            //Making the TextDecoding object
            TextDecoding textDecoding = new TextDecoding(AskActivity.this, AskActivity.this);

            //Execute Task
            textDecoding.execute(imageSteganography);
        }
        else {
        }
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
        tlfortextshowdecrypt = findViewById(R.id.tlfordecrypt);
        messagefoshowrdecrypt = findViewById(R.id.messagefoshowrdecrypt);


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
            refreshGallary(myDir);
            Adinlongshow();

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

    public void backpressedforsave(View view) {


        onBackPressed();
    }

    @Override
    public void onBackPressed() {


            Intent i = new Intent(AskActivity.this, DashboardActvity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            overridePendingTransition(0, 0);
            finish();


        super.onBackPressed();
    }

    @Override
    public void onStartTextEncoding() {

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {
        if (result != null) {
            if (!result.isDecoded()) {

            }
            else {
                if (!result.isSecretKeyWrong()) {
                    messagefoshowrdecrypt.setText("" + result.getMessage());
                } else {
                }
            }
        } else {
        }

    }

    class saveImageAsync extends AsyncTask<String, String, String> {

        Context context;
        ProgressDialog progressBar;
        Bitmap bitmapImage;
        View v;

        public saveImageAsync(Context context, Bitmap bitmapImage, View v) {
            this.context = context;
            this.bitmapImage = bitmapImage;
            this.v = v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(context);
            progressBar.setCancelable(false);
            progressBar.setMessage("Saving encoded image...");
            progressBar.setIndeterminate(false);
            progressBar.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            saveToInternalStorage(bitmapImage);
            if (isSave) {
                return "Successful";
            } else {
                return "Fail";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (progressBar != null && progressBar.isShowing()) {
                progressBar.dismiss();
            }

            if (result.equals("Successful")) {
                Snackbar snackbar = Snackbar.make(v, "Image Save Successfully..", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(getResources().getColor(R.color.color_grey));
                snackbar.setTextColor(getResources().getColor(R.color.white));
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(v, "Image Save Fails..", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(getResources().getColor(R.color.color_grey));
                snackbar.setTextColor(getResources().getColor(R.color.white));
                snackbar.show();
            }

            super.onPostExecute(result);
        }
    }

}