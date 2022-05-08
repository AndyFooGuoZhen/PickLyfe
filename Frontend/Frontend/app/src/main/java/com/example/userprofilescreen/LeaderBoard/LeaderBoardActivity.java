package com.example.userprofilescreen.LeaderBoard;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.VolleySingleton;
import com.example.userprofilescreen.util.ConstUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaderBoardActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private CardAdapter adapter;
    private ArrayList<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);
        InitializeCardView();
    }

    private void InitializeCardView(){
        recyclerView=findViewById(R.id.leaderBoardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        playerList = new ArrayList<Player>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = ConstUrl.BASE_URL + "userprofile/leaderboard/score";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for(int i=0; i<response.length(); i++){
                        JSONObject obj = response.getJSONObject(i);
                        String userName = obj.getString("userName");
                        String userScore = obj.getString("highScore");

                        playerList.add(new Player(userName, userScore));
                    }
                }
                catch(JSONException e){
                    Toast.makeText(LeaderBoardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                finally {
                    adapter = new CardAdapter(LeaderBoardActivity.this, playerList);
                    recyclerView.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LeaderBoardActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }


}
