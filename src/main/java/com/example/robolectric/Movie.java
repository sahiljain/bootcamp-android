package com.example.robolectric;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by dx141-xl on 2014-05-01.
 */
public class Movie {

    public int id;
    public String title;
    public String imageLink;
    public String webLink;
    public Bitmap image;

    public Movie(int id, String title, String imgLink, String weblnk) {
        this.id = id;
        this.title = title;
        this.imageLink = imgLink;
        this.webLink = weblnk;
        image = null;

        try {
            image = new DownloadImageTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... what) {

            return download_Image(imageLink);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //imageView.setImageBitmap(result);
        }

        private Bitmap download_Image(String url) {
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                Log.d("sahil", "aaand we're downloading");
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

    }
}
