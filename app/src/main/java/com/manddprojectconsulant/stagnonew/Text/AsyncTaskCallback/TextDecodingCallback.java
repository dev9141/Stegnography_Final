package com.manddprojectconsulant.stagnonew.Text.AsyncTaskCallback;


import com.manddprojectconsulant.stagnonew.Text.ImageSteganography;

/**
 * This the callback interface for TextDecoding AsyncTask.
 */

public interface TextDecodingCallback {

    void onStartTextEncoding();

    void onCompleteTextEncoding(ImageSteganography result);

}
