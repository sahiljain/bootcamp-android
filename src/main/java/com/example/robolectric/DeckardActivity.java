package com.example.robolectric;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.R;


public class DeckardActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deckard);
        Button searchButton = (Button) findViewById(R.id.search_button);
        final EditText editText = (EditText) findViewById((R.id.editText));
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "search button clicked");
                String field = editText.getText().toString();
                String strURL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=4vfsqwf87nwsd2vyvzzjfjxb&q=" + field +  "&page_limit=10";
                LongOperation lo = new LongOperation(DeckardActivity.this);
                lo.execute(strURL);


            }
        });
    }



}