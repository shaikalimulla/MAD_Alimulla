package com.example.alimu.restaurantsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by alimu on 11/29/2016.
 */

public class LoadImage extends AsyncTask<String, Void, Bitmap> {

    public LoadImage(Listener listener) {

        imgListener = listener;
    }

    public interface Listener{

        void onImageLoaded(Bitmap bitmap);
        void onError();
    }

    private Listener imgListener;
    @Override
    protected Bitmap doInBackground(String... args) {

        try {

            return BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (bitmap != null) {

            imgListener.onImageLoaded(bitmap);

        } else {

            imgListener.onError();
        }
    }
}