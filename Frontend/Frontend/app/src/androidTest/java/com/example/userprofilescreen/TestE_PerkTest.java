package com.example.userprofilescreen;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.core.AllOf.allOf;


import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;


import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.rule.ActivityTestRule;

import com.example.userprofilescreen.Login.Forgot_Password_Activity;
import com.example.userprofilescreen.Login.Login_Activity;
import com.example.userprofilescreen.Login.Register_Activity;
import com.example.userprofilescreen.PerkScreen.Perk;
import com.example.userprofilescreen.PerkScreen.Perk_Activity;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestE_PerkTest {
    private static final int DELAY_MS = 500;

    @Rule
    public ActivityTestRule<Perk_Activity> activityRule = new ActivityTestRule<>(Perk_Activity.class);

    @Test
    public void perkDetails() {
        String perkName = "Name: Pear";
        String rarity = "Rarity: 1";
        String description = "Multiply event score by 10%";
        ViewInteraction recyclerPerk = onView(withId(R.id.perk_RecyclerView));
        recyclerPerk.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.perk_NameTextView)).check(matches(withText(endsWith(perkName))));
        onView(withId(R.id.perk_RarityTextView)).check(matches(withText(endsWith(rarity))));
        onView(withId(R.id.perk_DescriptionTextView)).check(matches(withText(endsWith(description))));
    }
}
