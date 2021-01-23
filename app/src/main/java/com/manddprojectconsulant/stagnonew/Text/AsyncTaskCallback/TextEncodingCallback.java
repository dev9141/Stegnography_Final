package com.manddprojectconsulant.stagnonew.Text.AsyncTaskCallback;


import com.manddprojectconsulant.stagnonew.Text.ImageSteganography;

/**
 * This the callback interface for TextEncoding AsyncTask.
 */

public interface TextEncodingCallback {

    void onStartTextEncoding();

    void onCompleteTextEncoding(ImageSteganography result);

}
