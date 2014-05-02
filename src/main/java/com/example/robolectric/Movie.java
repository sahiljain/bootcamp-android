package com.example.robolectric;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

/**
 * Created by dx141-xl on 2014-05-01.
 */
public class Movie {

    public int id;
    public String title;
    public String imageLink;
    public String webLink;
    public String rating;
    public String releaseDate;
    public String[] cast;
    public String bigImageLink;
    public Bitmap image = null;

    public Movie(int id, String title, String imgLink, String bigImageLink, String weblnk, String rating, String releaseDate, String[] cast) {
        this.id = id;
        this.title = title;
        this.imageLink = imgLink;
        this.webLink = weblnk;
        this.bigImageLink = bigImageLink;
        this.rating = rating;
        this.cast = cast;
        this.releaseDate = releaseDate;

        //download an image here
    }

    public void requestDownloadAndPlace(ImageView img) {
        img.setTag(Boolean.TRUE);
        new DownloadImageTask(img, imageLink).execute();
        Log.i("sahil", imageLink);

    }

    public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
        ImageView img;
        String link;

        public DownloadImageTask(ImageView img, String link){
            super();
            this.link = link;
            this.img = img;
        }

        @Override
        protected Bitmap doInBackground(Void... what) {

            return download_Image(link);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            img.setImageBitmap(result);

            img.setTag(Boolean.TRUE);
            image = result;
            img.setTag(null);
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
