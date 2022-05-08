package com.example.userprofilescreen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.userprofilescreen.GameScreen.GameActivity;
import com.example.userprofilescreen.GameSetup.GameSetupActivity;

import java.util.Arrays;


@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestF_GameSetupTest {

    public static long userNameID = 1;

    @Rule
    public ActivityTestRule<GameSetupActivity> activityRule = new ActivityTestRule<>(GameSetupActivity.class);

    //Checks if difficulty set to Medium
    @Test
    public void gameSetupDifficulty(){
        ViewInteraction numPicker = onView(withClassName(Matchers.equalTo(NumberPicker.class.getName())));
        ViewInteraction numPickerInput = onView(withParent(withClassName(Matchers.equalTo(NumberPicker.class.getName()))));
        numPicker.perform(setNumber(1));
        numPickerInput.check(matches(withText("MEDIUM")));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Clicks on start game and verify if the screen has changed to the actual game screen
    @Test
    public void startGame(){
        Intents.init();
        ViewInteraction startBtn = onView(withId(R.id.startBtn));
        startBtn.perform(click());
        intended(hasComponent(GameActivity.class.getName()));
        Intents.release();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static ViewAction setNumber(final int num) {
        return new ViewAction() {

            NumberPicker np;

            @Override
            public void perform(UiController uiController, View view) {
                NumberPicker np = (NumberPicker) view;
                np.setValue(num);
            }

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(NumberPicker.class);
            }


        };
    }

}
