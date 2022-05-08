package com.example.userprofilescreen.EndGame;

import static com.example.userprofilescreen.util.ConstUrl.*;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userprofilescreen.GameScreen.GameActivity;
import com.example.userprofilescreen.GameSetup.GameSetupActivity;
import com.example.userprofilescreen.Main_Menu_Activity;
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EndActivity extends AppCompatActivity {

    private TextView winOrLoseTxt;
    private TextView scoreTxt;
    private TextView hScoreTxt;
    private TextView nEventTxt;
    private Button mainMenuBtn;
    private Button playBtn;
    private JSONObject userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endgame_activity);

        winOrLoseTxt = findViewById(R.id.winloseStatus);
        scoreTxt = findViewById(R.id.scoreTxt);
        hScoreTxt = findViewById(R.id.hScoreTxt);
        nEventTxt = findViewById(R.id.nEventTxt);
        mainMenuBtn = findViewById(R.id.mainmenuBtn);
        playBtn = findViewById(R.id.playBtn);

        updateGameMessage();
        fetchUserDetails();

    }

    private void updateGameMessage(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "game/score/" + USERNAME_ID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for(int i=0; i<4; i++){
                        if(parseInt(response.get(i).toString()) < 0){
                            winOrLoseTxt.setText("YOU LOST");
                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EndActivity.this, "Failed to fetch stats data", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void fetchUserDetails(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "userprofile/" + USERNAME_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String userName =response.getString("userName");
                    userProfile = response;
                    fetchGameDetails();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EndActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    //shows score and numeventsurvived
    private void fetchGameDetails(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "game/" + USERNAME_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    scoreTxt.setText(response.getString("currScore"));
                    nEventTxt.setText(response.getString("numEventsSurvived"));
                    terminateGame();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EndActivity.this, "Failed to fetch game detais", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    //updates highscore and highestnumevents survived
    private void terminateGame(){
        //terminates and destroy games
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "game/end";

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(EndActivity.this, response, Toast.LENGTH_SHORT).show();
                fetchHighScore();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EndActivity.this, "Failed to terminate game", Toast.LENGTH_LONG).show();
            }
        }){
            public byte[] getBody() {
                return userProfile.toString().getBytes();
            }
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(stringRequest);
    }

    private void fetchHighScore(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "userprofile/" + USERNAME_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    hScoreTxt.setText(response.getString("highScore"));

                    mainMenuBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(EndActivity.this, Main_Menu_Activity.class);
                            startActivity(intent);
                        }
                    });

                    playBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(EndActivity.this, GameSetupActivity.class);
                            startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EndActivity.this, "Failed to fetch high score", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

}

