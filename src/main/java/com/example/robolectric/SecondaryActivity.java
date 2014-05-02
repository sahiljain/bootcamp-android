package com.example.robolectric;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.R;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class SecondaryActivity extends Activity {

    public String bigImageLink;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        String title = getIntent().getExtras().getString("title");
        TextView titleView = (TextView) findViewById(R.id.txtTitle);
        titleView.setText(title);

        TextView castView = (TextView) findViewById(R.id.txtCast);
        String castString = "";
        for(String s : getIntent().getExtras().getStringArray("cast")){
            castString += s + "\n";
        }
        castView.setText(castString);

        TextView ratingView = (TextView) findViewById(R.id.txtRating);
        ratingView.setText(getIntent().getExtras().getString("rating"));

        TextView releaseDateView = (TextView) findViewById(R.id.txtReleaseDate);
        releaseDateView.setText(getIntent().getExtras().getString("releaseDate"));

        img = (ImageView) findViewById(R.id.imgMain);
        bigImageLink = getIntent().getExtras().getString("picLink");
        new DownloadImageTask().execute();
        //img.setImageBitmap(new DownloadImageTask().execute().get());



    }

    public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... what) {

            return download_Image(bigImageLink);
            //return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            img.setImageBitmap(result);
        }

        private Bitmap download_Image(String url) {
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                Log.d("sahil", "downloading big");
                //img.setImageBitmap(mIcon11);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

    }



}
