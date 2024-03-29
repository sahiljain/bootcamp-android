package com.example.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.R;
import com.example.robolectric.DeckardActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.robolectric.util.Transcript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LongOperation extends AsyncTask<String, Void, String> {

    DeckardActivity act;
    Transcript transcript;

    public LongOperation(DeckardActivity act, Transcript transcript){
        this.act = act;
        this.transcript = transcript;
    }

    private final HttpClient httpClient = new DefaultHttpClient();
    private String Content;
    private ProgressDialog Dialog;


    protected void onPreExecute() {
        Log.d("sahil", "preexecute");
        Dialog = new ProgressDialog(act);
        Dialog.setMessage("Please wait..");
        Dialog.show();
        transcript.add("onPreExecute");

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
        String result = "";
        // Send data
        try {

            HttpGet httpGet = new HttpGet(urls[0]);
            HttpResponse response = httpClient.execute(httpGet);
            Content = inputStreamToString(response.getEntity().getContent()).toString();

        } catch (Exception ex) {
            // Error = ex.getMessage();

        }
        /*****************************************************/
        String OutputData = "";
        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(Content);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("movies");
            int lengthJsonArr = jsonMainNode.length();
            for (int i = 0; i < lengthJsonArr; i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("title");
                OutputData += name + "\n";
            }

            result = OutputData;
            Log.d("sahil", result);
            TextView tv = (TextView) act.findViewById(R.id.textView);
            tv.setText(result);
        } catch (JSONException e) {

            e.printStackTrace();
        }


        transcript.add("doInBackground");
        return result;
    }

    protected void onPostExecute(String result) {
        // NOTE: You can call UI Element here.

        // Close progress dialog
        Dialog.dismiss();


        transcript.add("onPostExecute");
    }
}