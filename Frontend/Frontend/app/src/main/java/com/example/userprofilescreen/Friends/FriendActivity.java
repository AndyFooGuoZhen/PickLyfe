package com.example.userprofilescreen.Friends;

import static com.example.userprofilescreen.util.ConstUrl.BASE_URL;
import static com.example.userprofilescreen.util.ConstUrl.USERNAME_ID;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;
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
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.VolleySingleton;
import com.example.userprofilescreen.util.ConstUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerViewFriends;
    private friendsCardAdapter fCardAdapter;

    private RecyclerView recyclerViewAddFriends;
    private addFriendsAdapter addFCardAdapter;

    private ArrayList<Friend> friendList;
    private ArrayList<String> friendNames;
    private ArrayList<String> allUsersList;

    private JSONObject userProfile;
    private TextView gamesPlayed;
    private TextView item1;
    private TextView item2;
    private TextView select;
    ColorStateList def;
    private SearchView searchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_activity);
        setupActivity(findViewById(R.id.friendPage));

        gamesPlayed = findViewById(R.id.gamesPlayedtxt);
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        select = findViewById(R.id.select);
        def = item2.getTextColors();
        searchBar = findViewById(R.id.searchBar);

        getUserDetails();
        InitializeFriendsCardView();
    }

    @Override
    public void onClick(View view) {
        searchBar.setQuery("", false);

        if (view.getId() == R.id.item1){
            select.animate().x(0).setDuration(100);
            gamesPlayed.setAlpha(1);
            item1.setTextColor(Color.WHITE);
            item2.setTextColor(def);
            InitializeFriendsCardView();
            recyclerViewAddFriends.setAlpha(0);
            recyclerViewFriends.setAlpha(1);


        } else if (view.getId() == R.id.item2){
            gamesPlayed.setAlpha(0);
            item1.setTextColor(def);
            item2.setTextColor(Color.WHITE);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
            InitializeAddFriendsCardView();
            recyclerViewFriends.setAlpha(0);
            recyclerViewAddFriends.setAlpha(1);
        }
    }
    private void getUserDetails(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "userprofile/" + ConstUrl.USERNAME_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String userName =response.getString("userName");
                    userProfile = response;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FriendActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

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
                    Toast.makeText(FriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                finally {
                    fCardAdapter = new friendsCardAdapter(FriendActivity.this, friendList);
                    recyclerViewFriends.setAdapter(fCardAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FriendActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
        updateSearchbar(1);
    }

    private void InitializeAddFriendsCardView(){
        recyclerViewAddFriends=findViewById(R.id.friendsRecyclerView);
        recyclerViewAddFriends.setLayoutManager(new LinearLayoutManager(this));
        allUsersList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "user/all";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for(int i=0; i<response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        String userName = obj.getString("userName");

                        if(userName.equals(userProfile.getString("userName"))) {
                            continue;
                        }

                        allUsersList.add(userName);
                    }
                }
                catch(JSONException e){
                    Toast.makeText(FriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                finally {
                    addFCardAdapter = new addFriendsAdapter(FriendActivity.this, friendNames, allUsersList, userProfile);
                    recyclerViewAddFriends.setAdapter(addFCardAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FriendActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
        updateSearchbar(2);
    }

    //Conitnuously updates searchbars
    private void updateSearchbar(int recyclerNo){
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchBar.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //add condition for recycler no

                if(recyclerNo==1){
                    fCardAdapter.getFilter().filter(s);
                }
                else if(recyclerNo==2){
                    addFCardAdapter.getFilter().filter(s);
                }

                return false;
            }
        });
    }

    //Hides keyboard when user touches other areas other than searchbar
    public void setupActivity(View view){
        if(!(view instanceof SearchView)){
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(FriendActivity.this);
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
