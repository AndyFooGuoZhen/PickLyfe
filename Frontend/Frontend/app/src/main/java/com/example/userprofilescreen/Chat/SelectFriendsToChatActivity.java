package com.example.userprofilescreen.Chat;

import static com.example.userprofilescreen.util.ConstUrl.BASE_URL;
import static com.example.userprofilescreen.util.ConstUrl.USERNAME_ID;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.userprofilescreen.Friends.Friend;
import com.example.userprofilescreen.R;
import com.example.userprofilescreen.app.VolleySingleton;
import com.example.userprofilescreen.util.ConstUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectFriendsToChatActivity extends AppCompatActivity {

    private JSONObject userProfile;
    private RecyclerView recyclerViewFriends;
    private ArrayList<Friend> friendList;
    private ArrayList<String> friendNames;
    private friendsChatCardAdapter fCardAdapter;
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_friends_tochat_activity);
        searchBar = findViewById(R.id.searchBar);
        getUserDetails();
        InitializeFriendsCardView();
    }

    //get user profile
    private void getUserDetails(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "userprofile/" + ConstUrl.USERNAME_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ConstPrivateChatDetails.user_Name  =response.getString("userName");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectFriendsToChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    // get and display friends  (needs to be tweaked)
    private void InitializeFriendsCardView(){
        recyclerViewFriends=findViewById(R.id.friendsRecyclerView);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        friendList = new ArrayList<>();
        friendNames = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "user/friendList/" + USERNAME_ID;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for(int i=0; i<response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        String userName = obj.getString("userName");
                        String userGamesPlayed = obj.getString("numGamesPlayed");
                        friendNames.add(userName);
                        friendList.add(new Friend(userName, userGamesPlayed));
                    }
                }
                catch(JSONException e){
                    Toast.makeText(SelectFriendsToChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                finally {
                    fCardAdapter = new friendsChatCardAdapter(SelectFriendsToChatActivity.this, friendList);
                    recyclerViewFriends.setAdapter(fCardAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectFriendsToChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
        updateSearchbar();

    }

    //Continuously updates searchbars
    private void updateSearchbar(){
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchBar.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //add condition for recycler no
                fCardAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    //Hides keyboard when user touches other areas other than searchbar
    public void setupActivity(View view){
        if(!(view instanceof SearchView)){
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SelectFriendsToChatActivity.this);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupActivity(innerView);
            }
        }
    }

    //Method to hide keyboard when there isnt any input
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }


}
