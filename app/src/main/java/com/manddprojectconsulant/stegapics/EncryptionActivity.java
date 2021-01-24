package com.manddprojectconsulant.stegapics;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.manddprojectconsulant.stegapics.Text.AsyncTaskCallback.TextEncodingCallback;
import com.manddprojectconsulant.stegapics.Text.ImageSteganography;
import com.manddprojectconsulant.stegapics.Text.TextEncoding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EncryptionActivity extends Activity implements TextEncodingCallback {
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Encode Class";
    //Created variables for UI
    private TextView whether_encoded;
    private ImageView imageView;
    private EditText message;
    private EditText secret_key;
    //Objects needed for encoding
    private TextEncoding textEncoding;
    private ImageSteganography imageSteganography;
    private ProgressDialog save;
    private Uri filepath;
    //Bitmaps
    private Bitmap original_image;
    private Bitmap encoded_image;
    public static Bitmap encoded_image_save;
    AdView adsinencrypt;
    Button choose_image_button, encode_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);

        //initialized the UI components

        whether_encoded = findViewById(R.id.whether_encoded);
        imageView = findViewById(R.id.imageview);
        message = findViewById(R.id.message);
        secret_key = findViewById(R.id.secret_key);
        choose_image_button = findViewById(R.id.choose_image_button);
        encode_button = findViewById(R.id.encode_button);
        //Ads
        adsinencrypt = findViewById(R.id.adsinencrypt);

        Adshow();

        // Button save_image_button = findViewById(R.id.save_image_button);

        checkAndRequestPermissions();

        //Choose image button
        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        //Encode Button
        encode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasImage = imageView.getDrawable() == null ? false : true;
                if (hasImage) {
                    whether_encoded.setText("");
                    if (filepath != null) {
                        if (message.getText() != null) {
                            //ImageSteganography Object instantiation
                            imageSteganography = new ImageSteganography(message.getText().toString(),
                                    secret_key.getText().toString(),
                                    original_image);
                            //TextEncoding object Instantiation
                            textEncoding = new TextEncoding(EncryptionActivity.this, EncryptionActivity.this);
                            //Executing the encoding
                            textEncoding.execute(imageSteganography);
                        }
                    }
                } else {
                    Toast.makeText(EncryptionActivity.this, "Select image first for encode your message.", Toast.LENGTH_LONG).show();
                }
            }
        });
        //Save image button
    }

    private void Adshow() {
        MobileAds.initialize(this, "ca-app-pub-8674673470489334~6195848859");
        AdRequest adRequest = new AdRequest.Builder().build();
        adsinencrypt.loadAd(adRequest);
    }

    private void ImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Image set to imageView
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                original_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);

                imageView.setImageBitmap(original_image);
            } catch (IOException e) {
                Log.d(TAG, "Error : " + e);
            }
        }

    }

    // Override method of TextEncodingCallback

    @Override
    public void onStartTextEncoding() {
        //Whatever you want to do at the start of text encoding
    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        //By the end of textEncoding

        if (result != null && result.isEncoded()) {
            encoded_image = result.getEncoded_image();
            encoded_image_save = encoded_image;
            whether_encoded.setText("Encoded");
            imageView.setImageBitmap(encoded_image);


        }

        Intent intent = new Intent(EncryptionActivity.this, AskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void checkAndRequestPermissions() {
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 1);
        }
    }

    public void backpressed(View view) {

        onBackPressed();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EncryptionActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
        super.onBackPressed();


    }
}
