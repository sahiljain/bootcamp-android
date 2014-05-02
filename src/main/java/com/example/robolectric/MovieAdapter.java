package com.example.robolectric;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.R;

import java.io.InputStream;

/**
 * Created by dx141-xl on 2014-05-01.
 */
public class MovieAdapter extends ArrayAdapter<Movie>{
    Context mContext;
    int layoutResourceId;
    Movie data[] = null;
    Bitmap images[];

    public MovieAdapter(Context mContext, int layoutResourceId, Movie[] data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout.
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
         */
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        Movie objectItem = data[position];

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(objectItem.title);
        //textViewItem.setTag(objectItem);

        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);

        if(objectItem.image!=null){
            Log.i("sahil", "image not null");
           img.setImageBitmap(objectItem.image);
        }else{
            Log.i("sahil", "image null");
            if(img.getTag()==null)
            objectItem.requestDownloadAndPlace(img);
        }

        convertView.setTag(objectItem.webLink);

        return convertView;

    }





}
