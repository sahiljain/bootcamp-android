package com.example.activity;

import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.R;
import com.example.robolectric.DeckardActivity;
import com.example.robolectric.Movie;
import com.example.robolectric.SecondaryActivity;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowHandler;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.util.Transcript;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
public class DeckardActivityRobolectricTest {

    private DeckardActivity activity;
    public Transcript transcript;

    @Before
    public void setup(){
        activity = Robolectric.buildActivity(DeckardActivity.class).attach().create().start().resume().visible().get();
        transcript = new Transcript();
        Robolectric.getBackgroundScheduler().pause();
        Robolectric.getUiThreadScheduler().pause();
        Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
    }

    @Test
    public void testHomeActivityCreated() throws Exception {

        assertNotNull(activity);
    }

    @Test
    public void testGUIMainActivity() throws Exception {
        Button btn = (Button) activity.findViewById(R.id.search_button);
        assertNotNull(btn);

        EditText et = (EditText) activity.findViewById(R.id.editText);
        assertNotNull(et);

        ListView lv = (ListView) activity.findViewById(R.id.listView);
        assertNotNull(lv);
    }

    @Test
    public void testAPIAccessValid() throws Exception {

        EditText et = (EditText) activity.findViewById(R.id.editText);
        et.setText("Rocky");

        Button btn = (Button) activity.findViewById(R.id.search_button);
        Robolectric.clickOn(btn);

        Robolectric.runBackgroundTasks();
        Robolectric.runUiThreadTasks();

        ListView lv = (ListView) activity.findViewById(R.id.listView);
        Robolectric.runBackgroundTasks(); Robolectric.runUiThreadTasks();
        //assertThat(lv.getAdapter().getCount() > 0);
        //TextView title = (TextView) lv.getChildAt(0).findViewById(R.id.txtTitle);
        //System.err.println(title.getText());

    }

    @Test
    public void testAPIAccessInvalid() throws Exception {

        EditText et = (EditText) activity.findViewById(R.id.editText);
        et.setText("lkjlkjlk");

        Button btn = (Button) activity.findViewById(R.id.search_button);
        Robolectric.clickOn(btn);

        Robolectric.runBackgroundTasks();
        Robolectric.runUiThreadTasks();

        ListView lv = (ListView) activity.findViewById(R.id.listView);
        assertThat(lv.getAdapter().getCount() == 0);

    }

    @Test
    public void testNewActivityStarted() throws Exception {

        EditText et = (EditText) activity.findViewById(R.id.editText);
        et.setText("rocky");

        Button btn = (Button) activity.findViewById(R.id.search_button);
        Robolectric.clickOn(btn);

        Robolectric.runBackgroundTasks();
        Robolectric.runUiThreadTasks();

        ListView lv = (ListView) activity.findViewById(R.id.listView);
        ShadowHandler.idleMainLooper();
        ShadowListView  slv = Robolectric.shadowOf(lv);
        slv.performItemClick(0);
        ShadowActivity sa = Robolectric.shadowOf(activity);
        Intent si = sa.getNextStartedActivity();

        System.err.println(si.getComponent().getClassName());
        assertEquals(SecondaryActivity.class.getName(), si.getComponent().getClassName());
    }

    @Test
    public void testGUINewActivity() throws Exception {

        Intent newIntent = new Intent(activity, SecondaryActivity.class);
        newIntent.putExtra("title", "Rocky");
        newIntent.putExtra("cast", new String[]{"Sylvester Stallone", "Jennifer Aniston"});
        newIntent.putExtra("releaseDate", "2014/04/28");
        newIntent.putExtra("rating", "PG");
        newIntent.putExtra("webLink", "http://www.google.ca");
        newIntent.putExtra("picLink", "http://images.rottentomatoescdn.com/images/redesign/poster_default.gif");
        SecondaryActivity secondaryActivity = Robolectric.buildActivity(SecondaryActivity.class).withIntent(newIntent).attach().create().start().resume().visible().get();

        ImageView img = (ImageView) secondaryActivity.findViewById(R.id.imgMain);
        assertNotNull(img);

        TextView title = (TextView) secondaryActivity.findViewById(R.id.txtTitle);
        assertNotNull(title);
        assertEquals("Rocky", title.getText());

        TextView cast = (TextView) secondaryActivity.findViewById(R.id.txtCast);
        assertNotNull(cast);
        assertEquals("Sylvester Stallone\nJennifer Aniston\n", cast.getText());

        TextView rating = (TextView) secondaryActivity.findViewById(R.id.txtRating);
        assertNotNull(rating);
        assertEquals("PG", rating.getText());

        TextView releaseDate = (TextView) secondaryActivity.findViewById(R.id.txtReleaseDate);
        assertNotNull(releaseDate);
        assertEquals("2014/04/28", releaseDate.getText());

    }

}
