package com.example.userprofilescreen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.userprofilescreen.RecyclerUtil.atPosition;
import static org.hamcrest.CoreMatchers.endsWith;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.userprofilescreen.Chat.GlobalChatActivity;
import com.example.userprofilescreen.EndGame.EndActivity;

import org.hamcrest.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestI_ChatTest {

    @Rule
    public ActivityTestRule<GlobalChatActivity> activityRule = new ActivityTestRule<>(GlobalChatActivity.class);

    @Test
    public void T1_checkInput(){
        String testString = "Hello this is a test";
        onView(withId(R.id.msgEditTxt)).perform(typeText(testString), closeSoftKeyboard());
        onView(withId(R.id.sendBtn)).perform(click());
        onView(withId(R.id.msgEditTxt)).check(matches(withText(testString)));
    }

    @Test
    public void T2_checkIfMessagePosted(){
        String testString = "Hello this is a test";
        ViewInteraction recyclerChat = onView(withId(R.id.chatRecyclerView));

        onView(withRecyclerView(R.id.chatRecyclerView)
                .atPositionOnView(0, R.id.userMsg))
                .check(matches(withText(" "+ testString)));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }


}
