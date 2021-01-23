package com.manddprojectconsulant.stegapics.Text.AsyncTaskCallback;


import com.manddprojectconsulant.stegapics.Text.ImageSteganography;

/**
 * This the callback interface for TextEncoding AsyncTask.
 */

public interface TextEncodingCallback {

    void onStartTextEncoding();

    void onCompleteTextEncoding(ImageSteganography result);

}
