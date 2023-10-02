package com.manddprojectconsulant.stegapics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputEditText;
import com.manddprojectconsulant.stegapics.Text.AsyncTaskCallback.TextDecodingCallback;
import com.manddprojectconsulant.stegapics.Text.ImageSteganography;
import com.manddprojectconsulant.stegapics.Text.TextDecoding;

import java.io.IOException;

public class ExtractionActivity extends AppCompatActivity implements TextDecodingCallback {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Decode Class";
    //Initializing the UI components
    private TextView textView;
    private ImageView imageView;
    private TextView message;
    private TextInputEditText secret_key;
    private Uri filepath;
    //Bitmap
    private Bitmap original_image;
    Button choose_image_button, decode_button;
    AdView adsfordecryptionscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extraction);

        //Instantiation of UI components
        textView = findViewById(R.id.whether_decoded);
        imageView = findViewById(R.id.ivfordecrypt);
        adsfordecryptionscreen=findViewById(R.id.adsindecrypt);

        Adshow();

        message = findViewById(R.id.messagefordecrypt);
        secret_key = findViewById(R.id.D_secret_key);

        choose_image_button = findViewById(R.id.D_choose_image_button);
        decode_button = findViewById(R.id.decode_button);

        //Choose Image Button
        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        //Decode Button
        decode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filepath != null) {
                    //Making the ImageSteganography object
                    ImageSteganography imageSteganography = new ImageSteganography(secret_key.getText().toString(),
                            original_image);

                    //Making the TextDecoding object
                    TextDecoding textDecoding = new TextDecoding(ExtractionActivity.this, ExtractionActivity.this);

                    //Execute Task
                    textDecoding.execute(imageSteganography);
                }
                else {
                    textView.setText("Select Image First");
                    message.setText("Select Image First");
                }
            }
        });


    }

    private void Adshow() {

        MobileAds.initialize(this,"ca-app-pub-8674673470489334~6195848859");
        AdRequest adRequest=new AdRequest.Builder().build();
        adsfordecryptionscreen.loadAd(adRequest);



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

    @Override
    public void onStartTextEncoding() {
        //Whatever you want to do by the start of textDecoding
    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        //By the end of textDecoding

        if (result != null) {
            if (!result.isDecoded()) {
                textView.setText("No message found");
                message.setText("No Message Found in Image");
            }
            else {
                if (!result.isSecretKeyWrong()) {
                    textView.setText("Decoded");
                    message.setText("" + result.getMessage());
                } else {
                    textView.setText("Wrong secret key");
                }
            }
        } else {
            textView.setText("Select Image First");
            message.setText("Select Image First");
        }


    }

    public void backpressedforDecrypt(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ExtractionActivity.this, DashboardActvity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
        super.onBackPressed();


    }
}