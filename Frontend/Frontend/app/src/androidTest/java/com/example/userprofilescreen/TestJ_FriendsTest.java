package com.example.userprofilescreen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.userprofilescreen.Chat.PrivateChatActivity;
import com.example.userprofilescreen.Friends.FriendActivity;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJ_FriendsTest {

    @Rule
    public ActivityTestRule<FriendActivity> activityRule = new ActivityTestRule<>(FriendActivity.class);

    @Test
    public void T1_checkIfFriendsInServer(){

        String friendNameTest = "name2";

        onView(withId(R.id.item2)).perform(click());
        onView(withRecyclerView(R.id.friendsRecyclerView)
                .atPositionOnView(0, R.id.userNameTxt))
                .check(matches(withText(friendNameTest)));

    }

    @Test
    public void T2_checkIfFriendAdded(){

        String friendNameTest = "name2";
        onView(withId(R.id.item2)).perform(click());
        onView(withRecyclerView(R.id.friendsRecyclerView)
                .atPositionOnView(0, R.id.addFriendBtn))
                .perform(click());

        onView(withId(R.id.item1)).perform(click());
        onView(withRecyclerView(R.id.friendsRecyclerView)
                .atPositionOnView(0, R.id.userNameTxt))
                .check(matches(withText(friendNameTest)));

    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }


}
