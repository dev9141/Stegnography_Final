package com.example.stagnonew;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.stagnonew.Text.AsyncTaskCallback.TextEncodingCallback;
import com.example.stagnonew.Text.ImageSteganography;
import com.example.stagnonew.Text.TextEncoding;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);

        //initialized the UI components

        whether_encoded = findViewById(R.id.whether_encoded);

        imageView = findViewById(R.id.imageview);

        message = findViewById(R.id.message);
        secret_key = findViewById(R.id.secret_key);

        Button choose_image_button = findViewById(R.id.choose_image_button);
        Button encode_button = findViewById(R.id.encode_button);
        Button save_image_button = findViewById(R.id.save_image_button);

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
            }
        });

        //Save image button
        save_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bitmap imgToSave = encoded_image;

                saveToInternalStorage(imgToSave);
            }
        });
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
    }



    private void saveToInternalStorage(Bitmap bitmapImage) {

        try {
            File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "StegoImage");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "SG-" + n + ".png";
            File file = new File(myDir, fname);

            checkAndRequestPermissions();
            FileOutputStream fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkAndRequestPermissions() {
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
}
