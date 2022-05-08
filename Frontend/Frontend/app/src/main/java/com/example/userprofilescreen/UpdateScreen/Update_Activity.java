package com.example.userprofilescreen.UpdateScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.userprofilescreen.Main_Menu_Activity;
import com.example.userprofilescreen.More.More_Activity;
import com.example.userprofilescreen.R;
import com.example.userprofilescreen.app.AppController;
import com.example.userprofilescreen.util.ConstUrl;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Update_Activity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = Update_Activity.class.getSimpleName();
    private RecyclerView recyclerViewAnnouncement, recyclerViewPatch;
    private AnnouncementAdapter announcementAdapter;
    private PatchAdapter patchAdapter;
    private ArrayList<Announcement> announcementArrayList;
    private ArrayList<Patch> patchArrayList;
    private TextView tabOption1, tabOption2, tabSelect;
    private FloatingActionButton fab;
    private ColorStateList secondary;
    private RelativeLayout relativeLayout;
    private SwipeToDeleteCallback swipeToDeleteCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        relativeLayout = findViewById(R.id.update_RelativeLayout);
        tabOption1 = findViewById(R.id.updates_TabOption1);
        tabOption2 = findViewById(R.id.updates_TabOption2);
        fab = findViewById(R.id.update_FloatingActionButton);
        tabOption1.setOnClickListener(this);
        tabOption2.setOnClickListener(this);
        tabSelect = findViewById(R.id.updates_select);
        secondary = tabOption2.getTextColors();
        initBottomNavigationView();
        initAnnouncementCardView();
        if (ConstUrl.isAdmin) {
            enableSwipeToDeleteAndUndo();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConstUrl.isAdmin) {
                    startActivity(new Intent(getApplicationContext(), NewAnnouncement.class));
                } else {
                    Toast.makeText(Update_Activity.this, "You do not have the privellege to perform this action", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.updates_TabOption1:
                tabSelect.animate().x(0).setDuration(100);
                tabOption1.setTextColor(Color.WHITE);
                tabOption2.setTextColor(secondary);
                initAnnouncementCardView();
                if (ConstUrl.isAdmin) {
                    enableSwipeToDeleteAndUndo();
                }
                recyclerViewPatch.setAlpha(0);
                recyclerViewAnnouncement.setAlpha(1);
                break;
            case R.id.updates_TabOption2:
                tabOption1.setTextColor(secondary);
                tabOption2.setTextColor(Color.WHITE);
                int size = tabOption2.getWidth();
                tabSelect.animate().x(size).setDuration(100);
                initPatchCardView();
                recyclerViewAnnouncement.setAlpha(0);
                recyclerViewPatch.setAlpha(1);
                break;
            default:
                break;
        }
    }

    private void initBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_updates);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.menu_updates:
                        startActivity(new Intent(getApplicationContext(), Update_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), Main_Menu_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_more:
                        startActivity(new Intent(getApplicationContext(), More_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void initAnnouncementCardView() {
        recyclerViewAnnouncement = findViewById(R.id.update_RecyclerView);
        recyclerViewAnnouncement.setLayoutManager(new LinearLayoutManager(this));

        announcementArrayList = new ArrayList<Announcement>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ConstUrl.GET_ANNOUNCEMENT_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String title = obj.getString("title");
                        String description = obj.getString("description");
                        String date = obj.getString("date");
                        date += " | " + obj.getString("time");
                        long id = obj.getLong("id");

                        announcementArrayList.add(new Announcement(title, date, description, id));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        announcementAdapter = new AnnouncementAdapter(Update_Activity.this, announcementArrayList);
                        recyclerViewAnnouncement.setAdapter(announcementAdapter);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Announcement Error: " +error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest, TAG);
    }


    private void initPatchCardView() {
        recyclerViewPatch = findViewById(R.id.update_RecyclerView);
        recyclerViewPatch.setLayoutManager(new LinearLayoutManager(this));
        patchArrayList = new ArrayList<Patch>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ConstUrl.GET_PATCH_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String title = obj.getString("title");
                        String description = obj.getString("description");
                        String date = obj.getString("date");
                        long id = obj.getLong("id");

                        patchArrayList.add(new Patch(title, date, description, id));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        patchAdapter = new PatchAdapter(Update_Activity.this, patchArrayList);
                        recyclerViewPatch.setAdapter(patchAdapter);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Patch Error: " +error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest, TAG);
    }


    private void enableSwipeToDeleteAndUndo() {

            swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                    final int position = viewHolder.getAdapterPosition();
                    final Announcement item = announcementAdapter.getData().get(position);
                    String url = ConstUrl.BASE_URL + "ann/delete/" + item.getId();
                    announcementAdapter.deleteItem(position);
                    StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Successfully deleted Announcement");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Failed to delete Announcement");
                        }
                    });

                    AppController.getInstance().addToRequestQueue(stringRequest, TAG);

                    Snackbar snackbar = Snackbar
                            .make(relativeLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            announcementAdapter.restoreItem(item, position);
                            recyclerViewAnnouncement.scrollToPosition(position);
                            restoreAnnouncement(item);
                        }
                    });

                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();

                }
            };

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
            itemTouchhelper.attachToRecyclerView(recyclerViewAnnouncement);
    }

    private void restoreAnnouncement(Announcement item) {
        String temp = null;
        final String requestBody;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", item.getTitle());
            jsonObject.put("description", item.getDescription());
            temp = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestBody = temp;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstUrl.POST_ANNOUNCEMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Restored Announcement");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Failed to restore Announcement");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json");
                return header;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private void restorePatch(Patch item) {
        String temp = null;
        final String requestBody;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", item.getTitle());
            jsonObject.put("description", item.getDescription());
            temp = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestBody = temp;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstUrl.POST_PATCH_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Restored Patch");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Failed to restore Patch");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json");
                return header;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }
}