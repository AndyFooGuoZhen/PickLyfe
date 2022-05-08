package com.example.userprofilescreen.PerkScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.AppController;
import com.example.userprofilescreen.util.ConstUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class Perk_Activity extends AppCompatActivity {

    private static String TAG = Perk_Activity.class.getSimpleName();
    private List<Perk> perkList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PerkRecyclerViewAdapter perkRecyclerViewAdapter;
    private int[] logoList = new int[12];
    private static TextView perkName, perkRarity, perkDescription;
    private Button selectButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perk);

        recyclerView = findViewById(R.id.perk_RecyclerView);

        // Temporary solution for logo to showcase
        logoList[0] = R.drawable.pear;
        logoList[1] = R.drawable.pome;
        logoList[2] = R.drawable.starfruit;
        logoList[3] = R.drawable.lychee;
        logoList[4] = R.drawable.peach;
        logoList[5] = R.drawable.dragonfruit;
        logoList[6] = R.drawable.grape;
        logoList[7] = R.drawable.pineapple;
        logoList[8] = R.drawable.banana;
        logoList[9] = R.drawable.cherry;
        logoList[10] = R.drawable.apple;
        logoList[11] = R.drawable.rambutan;

        getPerkListReq();
        initRecyclerView();

        perkName = (TextView) findViewById(R.id.perk_NameTextView);
        perkRarity = (TextView) findViewById(R.id.perk_RarityTextView);
        perkDescription = (TextView) findViewById(R.id.perk_DescriptionTextView);
        selectButton = (Button) findViewById(R.id.perk_SelectButton);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPerkReq();
            }
        });
    }

    private void getPerkListReq() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ConstUrl.GET_PERKLIST_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        int id = obj.getInt("id");
                        int rarity = obj.getInt("rarity");
                        String perkName = obj.getString("perkName");
                        String description = obj.getString("description");

                        perkList.add(new Perk(id, rarity, logoList[i] , perkName, description));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                initRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest, TAG);
    }

    private void initRecyclerView() {
        perkRecyclerViewAdapter = new PerkRecyclerViewAdapter(this, perkList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(perkRecyclerViewAdapter);
    }

    private void setPerkReq() {
        String perk = perkName.getText().toString().substring(6);
        Perk equipPerk = perkRecyclerViewAdapter.getPerk(perk);
        String url = ConstUrl.BASE_URL + "userprofile/" + ConstUrl.USERNAME_ID + "/perkID/" + equipPerk.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Equipped Perk");
                Toast.makeText(Perk_Activity.this, response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "ERROR: " + error.getMessage());
                Toast.makeText(Perk_Activity.this, "Failed to Handle Request", Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    public static void setOnClickChanges(Perk p) {
        perkName.setText("Name: " + p.getPerkName());
        perkRarity.setText("Rarity: " + p.getRarity());
        perkDescription.setText(p.getDescription());
    }

//    TODO: Add and show inventory of user
//    public static void setInventory(Perk p) {
//
//    }
}