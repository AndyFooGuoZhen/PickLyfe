package com.example.userprofilescreen.GameScreen;

import static com.example.userprofilescreen.util.ConstUrl.*;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.userprofilescreen.EndGame.EndActivity;
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameActivity extends AppCompatActivity {
    private Button option1Btn;
    private Button option2Btn;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private ProgressBar progressBar4;
    private TextView gameTitle;
    private int perkApplied = 0;
    private TextView userTxt;
    private JSONObject userProfile;
    private String perk;
    private TextView scoreTxt;
    private TextView gameTxt;
    private String option1response;
    private String option2response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        getUserDetails();

        progressBar1 = findViewById(R.id.stats1pb);
        progressBar2 = findViewById(R.id.stats2pb);
        progressBar3 = findViewById(R.id.stats3pb);
        progressBar4 = findViewById(R.id.stats4pb);
        scoreTxt = findViewById(R.id.scoreTxt);
        gameTitle = findViewById(R.id.gameTitle);

        gameTxt = findViewById(R.id.gameTxt);
        option1Btn = (Button)findViewById(R.id.option1Btn);
        option2Btn = (Button)findViewById(R.id.option2Btn);
    }

    private void getUserDetails(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "userprofile/" + USERNAME_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String userName =response.getString("userName");
                    userProfile = response;
                    getUserGameStats();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void getUserGameStats(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "game/score/" + USERNAME_ID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    int stats1 = parseInt(response.get(0).toString());
                    int stats2 = parseInt(response.get(1).toString());
                    int stats3 = parseInt(response.get(2).toString());
                    int stats4 = parseInt(response.get(3).toString());

                    progressBar1.setProgress(stats1);
                    progressBar2.setProgress(stats2);
                    progressBar3.setProgress(stats3);
                    progressBar4.setProgress(stats4);

                    if(stats1 < 0 || stats2 < 0 || stats3 < 0 ||stats4 < 0 ){
                        Toast.makeText(GameActivity.this, "Negative stats reached", Toast.LENGTH_SHORT).show();

                        if(perkApplied==0){
                            checkPerk();
                        }

                        else{
                            Intent intent = new Intent(GameActivity.this, EndActivity.class);
                            startActivity(intent);
                        }
                    }

                    else if (stats1 >= 200 || stats2 >= 200||stats3 >= 200||stats4 >= 200) {
                        Intent intent = new Intent(GameActivity.this, EndActivity.class);
                        startActivity(intent);
                    }

                    else{
                        randomGetEvent();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameActivity.this, "Failed to fetch stats data", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void randomGetEvent(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "game/getEvent";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, userProfile, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String score = response.getString("currScore") + "pts";
                    JSONObject event = response.getJSONObject("event");
                    String gameTitleString = event.getString("name");
                    String description = event.getString("description");
                    String option1 = event.getString("option1_desc");
                    String option2 = event.getString("option2_desc");
                    option1response = event.getString("option1_response");
                    option2response = event.getString("option2_response");

                    scoreTxt.setText(score);
                    gameTitle.setText(gameTitleString);
                    gameTxt.setText((description));
                    option1Btn.setText(option1);
                    option2Btn.setText(option2);
                    option1Btn.setEnabled(true);
                    option2Btn.setEnabled(true);
                    handleOptionResponse();
                }
                catch (JSONException e) {
                    Toast.makeText(GameActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

//                Toast.makeText(GameActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void handleOptionResponse(){

        option1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(GameActivity.this, option1response, Toast.LENGTH_SHORT).show();
                gameTitle.setText("Response");
                gameTxt.setText(option1response);
                updateOptionResponse(1);
                option1Btn.setEnabled(false);
                option2Btn.setEnabled(false);
            }
        });

        option2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              Toast.makeText(GameActivity.this, option2response, Toast.LENGTH_SHORT).show();
                gameTitle.setText("Response");
                gameTxt.setText(option2response);
                updateOptionResponse(2);
                option1Btn.setEnabled(false);
                option2Btn.setEnabled(false);
            }
        });
    }

    private void updateOptionResponse(int optionSelected){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "game/updateStatus/" + USERNAME_ID + "/" + optionSelected;

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getUserGameStats();

//                try {
//                    Thread.sleep(2500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                randomGetEvent();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void checkPerk(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "game/" + USERNAME_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    perk = response.getString("perk");
                    perk = "2";

                    if(!perk.equals("null")){
                        applyPerk();
                    }

                    else{
                        Intent intent = new Intent(GameActivity.this, EndActivity.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void applyPerk(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
//        Toast.makeText(this, perk, Toast.LENGTH_SHORT).show();
        String url = BASE_URL + "perk/get/" + perk;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String ignoreDeathPerk = response.getString("ignoreDeath");
                    String revivePerk = response.getString("revive");
//                    Toast.makeText(GameActivity.this, "Revive is " + revivePerk, Toast.LENGTH_SHORT).show();

                    if (!ignoreDeathPerk.equals("1") && !revivePerk.equals("1") ){
                        Intent intent = new Intent(GameActivity.this, EndActivity.class);
                        startActivity(intent);
                    }

                    else if(ignoreDeathPerk.equals("1")){
                        perkApplied = 1;
                        gameTitle.setText("You're lucky");
                        gameTxt.setText("Death has been ignored this time");
                        option1Btn.setText("");
                        option2Btn.setText("");
                        option1Btn.setEnabled(false);
                        option2Btn.setEnabled(false);
                        new Handler().postDelayed(() -> randomGetEvent(), 2500);

                        Toast.makeText(GameActivity.this, "Death ignored", Toast.LENGTH_SHORT).show();
                    }

                    else if(revivePerk.equals("1")){
                        perkApplied =1;
                        gameTitle.setText("CONGRATULATIONS!");
                        gameTxt.setText("You Revived!");
                        option1Btn.setText("");
                        option2Btn.setText("");
                        option1Btn.setEnabled(false);
                        option2Btn.setEnabled(false);
                        updateAllStats();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void updateAllStats(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "game/updateAllStatus/" + USERNAME_ID + "/50";

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getUserGameStats();
                Toast.makeText(GameActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }


}
