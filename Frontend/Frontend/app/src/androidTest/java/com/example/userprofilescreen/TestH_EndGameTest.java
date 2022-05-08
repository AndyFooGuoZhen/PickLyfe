package com.example.userprofilescreen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.widget.TextView;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.userprofilescreen.EndGame.EndActivity;
import com.example.userprofilescreen.GameScreen.GameActivity;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestH_EndGameTest {

    @Rule
    public ActivityTestRule<EndActivity> activityRule = new ActivityTestRule<>(EndActivity.class);

    @Test
    public void T1_losingTitleTest(){
        TextView gameStateTitle = activityRule.getActivity().findViewById(R.id.winloseStatus);
        String expectedString = "YOU WON";
        assertEquals(expectedString, gameStateTitle.getText());
    }

//    @Test
//    public void T2_switchingToMainMenuTest(){
//
//        ViewInteraction mainMenuBtn = onView(withId(R.id.mainmenuBtn));
//        mainMenuBtn.perform(click());
//        Intents.init();
//
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        intended(hasComponent(Main_Menu_Activity.class.getName()));
//        Intents.release();
//    }
}
