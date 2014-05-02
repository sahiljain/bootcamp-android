package com.example.robolectric;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LongOperation extends AsyncTask<String, Void, String> {

    DeckardActivity act;


    public LongOperation(DeckardActivity act){
        this.act = act;

    }

    private final HttpClient httpClient = new DefaultHttpClient();
    private String Content;
    private ProgressDialog Dialog;


    protected void onPreExecute() {
        Log.d("sahil", "preexecute");
        System.err.println("search preexec");
        Dialog = new ProgressDialog(act);
        Dialog.setMessage("Please wait..");
        Dialog.show();

    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader rd = new BufferedReader(isr);

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    // Call after onPreExecute method
    protected String doInBackground(String... urls) {

        /************ Make Post Call To Web Server ***********/

        // Send data
        try {

            HttpGet httpGet = new HttpGet(urls[0]);
            HttpResponse response = httpClient.execute(httpGet);
            Content = inputStreamToString(response.getEntity().getContent()).toString();
            System.err.println("Talked to server");

        } catch (Exception ex) {
            // Error = ex.getMessage();

        }
        /*****************************************************/

        return null;
    }


    protected void onPostExecute(String result) {




        System.err.println("post exec");




        // NOTE: You can call UI Element here.

        // Close progress dialog
        Dialog.dismiss();
        String OutputData = "";
        //ArrayList<String> titles = new ArrayList<String>();
        //ArrayList<String> imageLinks = new ArrayList<String>();
        //ArrayList<String> webLinks = new ArrayList<String>();
        ArrayList<JSONObject> jsonMovies = new ArrayList<JSONObject>();
        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(Content);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("movies");
            if(jsonMainNode == null) return;
            int lengthJsonArr = jsonMainNode.length();
            for (int i = 0; i < lengthJsonArr; i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                jsonMovies.add(jsonChildNode);
                String name = jsonChildNode.optString("title");
                OutputData += name + "\n";
                //titles.add(name);
                //imageLinks.add(jsonChildNode.getJSONObject("posters").optString("thumbnail"));
                //webLinks.add(jsonChildNode.getJSONObject("links").optString("alternate"));
            }

            result = OutputData;
            Log.d("sahil", result);
            //TextView tv = (TextView) act.findViewById(R.id.textView);
            //tv.setText(result);

            ListView lv = (ListView) act.findViewById(R.id.listView);

            final Movie[] movies = new Movie[jsonMovies.size()];
            System.err.println("length returned: " + movies.length);
            for(int i = 0; i < jsonMovies.size(); i++){
                JSONObject curJSON= jsonMovies.get(i);
                String title = curJSON.optString("title");
                String imageLink = curJSON.getJSONObject("posters").getString("thumbnail");
                String rating = curJSON.getString("mpaa_rating");
                String releaseDate = curJSON.optJSONObject("release_dates").optString("theater");
                String webLink = curJSON.getJSONObject("links").optString("alternate");
                String bigImageLink = curJSON.getJSONObject("posters").getString("detailed");
                JSONArray castJSON = curJSON.getJSONArray("abridged_cast");
                String[] cast = new String[castJSON.length()];
                for(int x = 0; x < cast.length; x++){
                    cast[x] = castJSON.getJSONObject(x).optString("name");
                }
                Movie temp = new Movie(i, title, imageLink, bigImageLink, webLink, rating, releaseDate, cast);
                movies[i] = temp;
                //movies[i] = new Movie(i, titles.get(i), imageLinks.get(i), webLinks.get(i));
            }


            MovieAdapter adapter = new MovieAdapter(act, R.layout.movielayout, movies);

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Log.d("sahil", (String) view.getTag());
                    //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((String) view.getTag()));
                   // act.startActivity(browserIntent);
                    Intent newIntent = new Intent(act, SecondaryActivity.class);
                    newIntent.putExtra("title", movies[i].title);
                    newIntent.putExtra("cast", movies[i].cast);
                    newIntent.putExtra("releaseDate", movies[i].releaseDate);
                    newIntent.putExtra("rating", movies[i].rating);
                    newIntent.putExtra("webLink", movies[i].webLink);
                    newIntent.putExtra("picLink", movies[i].bigImageLink);
                    //newIntent.putExtra("largeImageLink" );
                    act.startActivity(newIntent);

                }
            });

            System.err.println(lv.getChildCount());


        } catch (Exception e) {

            e.printStackTrace();
        }


    }
}