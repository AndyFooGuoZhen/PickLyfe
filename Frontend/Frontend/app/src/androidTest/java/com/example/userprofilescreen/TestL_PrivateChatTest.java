package com.example.userprofilescreen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.userprofilescreen.Chat.PrivateChatActivity;
import com.example.userprofilescreen.Chat.PrivateORGlobalChatActivity;
import com.example.userprofilescreen.Chat.SelectFriendsToChatActivity;
import com.example.userprofilescreen.GameScreen.GameActivity;
import com.example.userprofilescreen.LeaderBoard.LeaderBoardActivity;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestL_PrivateChatTest {

    @Rule
    public ActivityTestRule<SelectFriendsToChatActivity> activityRule = new ActivityTestRule<>(SelectFriendsToChatActivity.class);


    @Test
    public void T1_navigateToFriendsDM(){
        Intents.init();
        onView(withRecyclerView(R.id.friendsRecyclerView)
                .atPositionOnView(0, R.id.chatFriendBtn))
                .perform(click());
        intended(hasComponent(PrivateChatActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void T2_checkInput(){
        String testString = "Hello this is a test";
        onView(withRecyclerView(R.id.friendsRecyclerView)
                .atPositionOnView(0, R.id.chatFriendBtn))
                .perform(click());

        onView(withId(R.id.msgEditTxt)).perform(typeText(testString), closeSoftKeyboard());
        onView(withId(R.id.sendBtn)).perform(click());
        onView(withId(R.id.msgEditTxt)).check(matches(withText(testString)));
    }

    @Test
    public void T3_checkIfMessagePosted(){

        String testString = "Hello this is a test";

        onView(withRecyclerView(R.id.friendsRecyclerView)
                .atPositionOnView(0, R.id.chatFriendBtn))
                .perform(click());

        onView(withRecyclerView(R.id.chatRecyclerView)
                .atPositionOnView(0, R.id.userMsg))
                .check(matches(withText("  "+ testString)));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

}
