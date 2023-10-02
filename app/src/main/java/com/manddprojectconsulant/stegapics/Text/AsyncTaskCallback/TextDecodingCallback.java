package com.manddprojectconsulant.stegapics.Text.AsyncTaskCallback;


import com.manddprojectconsulant.stegapics.Text.ImageSteganography;

/**
 * This the callback interface for TextDecoding AsyncTask.
 */

public interface TextDecodingCallback {

    void onStartTextEncoding();

    void onCompleteTextEncoding(ImageSteganography result);

}
