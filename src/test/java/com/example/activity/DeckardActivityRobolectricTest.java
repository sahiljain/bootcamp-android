package com.example.activity;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.R;
import com.example.robolectric.DeckardActivity;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.Transcript;

import org.robolectric.shadows.ShadowToast;
import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

import static org.fest.assertions.api.Assertions.doesNotHave;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
public class DeckardActivityRobolectricTest {

    private DeckardActivity activity;
    public Transcript transcript;

    @Before
    public void setup(){
        activity = Robolectric.buildActivity(DeckardActivity.class).create().get();
        transcript = new Transcript();
        Robolectric.getBackgroundScheduler().pause();
        Robolectric.getUiThreadScheduler().pause();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
    }

    @Test
    public void testActivityCreated() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void testGUIElementsCreated() throws Exception {
        Button btn = (Button) activity.findViewById(R.id.search_button);
        assertNotNull(btn);

        TextView tv = (TextView) activity.findViewById(R.id.textView);
        assertNotNull(tv);

        EditText et = (EditText) activity.findViewById(R.id.editText);
        assertNotNull(et);
    }

    @Test
    public void testMovie() throws Exception {

        TextView tv = (TextView) activity.findViewById(R.id.textView);

        String field = "romeo";
        String strURL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=4vfsqwf87nwsd2vyvzzjfjxb&q=" + field +  "&page_limit=10";
        LongOperation lo = new LongOperation(activity, transcript);
        lo.execute(strURL);

        transcript.assertEventsSoFar("onPreExecute");

        Robolectric.runBackgroundTasks();
        transcript.assertEventsSoFar("doInBackground");
        System.err.print(lo.get(1000, TimeUnit.MILLISECONDS));
        assertThat(lo.get().length() > 0);

        Robolectric.runUiThreadTasks();
        transcript.assertEventsSoFar("onPostExecute");


        assertThat(tv.getText().length() > 0);
    }

    @Test
    public void testInvalidMovie() throws  Exception {
        TextView tv = (TextView) activity.findViewById(R.id.textView);

        String field = "rosfsdfsdfmeo";
        String strURL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=4vfsqwf87nwsd2vyvzzjfjxb&q=" + field +  "&page_limit=10";
        LongOperation lo = new LongOperation(activity, transcript);
        lo.execute(strURL);

        transcript.assertEventsSoFar("onPreExecute");

        Robolectric.runBackgroundTasks();
        transcript.assertEventsSoFar("doInBackground");
        System.err.print(lo.get(1000, TimeUnit.MILLISECONDS));
        assertThat(lo.get().length() > 0);

        Robolectric.runUiThreadTasks();
        transcript.assertEventsSoFar("onPostExecute");


        assertThat(tv.getText().length() == 0);

    }
}
