package com.example.userprofilescreen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.userprofilescreen.util.ConstUrl.BASE_URL;
import static com.example.userprofilescreen.util.ConstUrl.USERNAME_ID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import android.util.Log;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userprofilescreen.EndGame.EndActivity;
import com.example.userprofilescreen.GameScreen.GameActivity;
import com.example.userprofilescreen.GameSetup.GameSetupActivity;
import com.example.userprofilescreen.app.VolleySingleton;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestG_GameTest {

    @Rule
    public ActivityTestRule<GameActivity> activityRule = new ActivityTestRule<>(GameActivity.class);

    //Checks if all stats are set to 50 initially
    @Test
    public void T1_checkDefaultStats(){
        ProgressBar progressBar = activityRule.getActivity().findViewById(R.id.stats1pb);
        int progress = progressBar.getProgress();
        assertThat(progress, equalTo(100));

        progressBar = activityRule.getActivity().findViewById(R.id.stats2pb);
        progress = progressBar.getProgress();
        assertThat(progress, equalTo(100));

        progressBar = activityRule.getActivity().findViewById(R.id.stats3pb);
        progress = progressBar.getProgress();
        assertThat(progress, equalTo(100));

        progressBar = activityRule.getActivity().findViewById(R.id.stats4pb);
        progress = progressBar.getProgress();
        assertThat(progress, equalTo(100));
    }

    //Checks if events are fetched to game
    @Test
    public void T2_checkEventGeneration(){
        TextView gameTxt = activityRule.getActivity().findViewById(R.id.gameTxt);
        String defaultString = "hello";
        assertNotEquals(defaultString, gameTxt.getText());
    }

    @Test
    public void T3_checkButtonContentUpdate(){
       Button option1Btn = activityRule.getActivity().findViewById(R.id.option2Btn);
        Button option2Btn = activityRule.getActivity().findViewById(R.id.option1Btn);
        assertNotEquals("", option1Btn.getText());
        assertNotEquals("", option2Btn.getText());
    }

    //Checks if stats update when user clicks a button
    @Test
    public void T4_statsChangeWhenButtonClicked(){
        ViewInteraction startBtn = onView(withId(R.id.option1Btn));
        startBtn.perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProgressBar progressBar1 = activityRule.getActivity().findViewById(R.id.stats1pb);
        ProgressBar progressBar2 = activityRule.getActivity().findViewById(R.id.stats2pb);
        ProgressBar progressBar3 = activityRule.getActivity().findViewById(R.id.stats3pb);
        ProgressBar progressBar4 = activityRule.getActivity().findViewById(R.id.stats4pb);

        Log.d("Stats1", String.valueOf(progressBar1.getProgress()));
        Log.d("Stats2", String.valueOf(progressBar2.getProgress()));
        Log.d("Stats3", String.valueOf(progressBar3.getProgress()));
        Log.d("Stats4", String.valueOf(progressBar4.getProgress()));

        if(progressBar1.getProgress()==progressBar2.getProgress()
                && progressBar2.getProgress()==progressBar3.getProgress()
                && progressBar3.getProgress()==progressBar3.getProgress())
        {
            assertFalse(true);
        }
        else{
            assertFalse(false);
        }

    }



}
