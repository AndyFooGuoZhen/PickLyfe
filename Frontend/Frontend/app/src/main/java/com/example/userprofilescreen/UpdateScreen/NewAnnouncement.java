package com.example.userprofilescreen.UpdateScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.emergency.EmergencyNumber;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.userprofilescreen.R;
import com.example.userprofilescreen.app.AppController;
import com.example.userprofilescreen.util.ConstUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NewAnnouncement extends AppCompatActivity {

    String[] options = { "Announcements", "Patch" };
    private static String TAG = NewAnnouncement.class.getSimpleName();
    private EditText title, description;
    private Button applyChanges, backButton;
    private Spinner spinner;
    private boolean postTypePatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_announcement);
        postTypePatch = false;
        title = findViewById(R.id.new_ann_TitleEditText);
        description = findViewById(R.id.new_ann_DescEditText);
        applyChanges = findViewById(R.id.new_ann_ApplyChangesButton);
        backButton = findViewById(R.id.new_ann_BackButton);
        spinner = (Spinner) findViewById(R.id.new_ann_Spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, options);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    postTypePatch = false;
                } else {
                    postTypePatch = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!title.getText().toString().equals("") && !description.toString().equals("") &&
                        !(title.getText().toString() == null) && !(description.toString() == null)) {
                    makeNewPost();
                } else {
                    Toast.makeText(NewAnnouncement.this, "Unable to make post without both title and description", Toast.LENGTH_LONG).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Update_Activity.class));
            }
        });

    }

    private void makeNewPost() {
        String url, temp = null;
        final String requestBody;
        if (!postTypePatch) {
            url = ConstUrl.POST_ANNOUNCEMENT_URL;
        } else {
            url = ConstUrl.POST_PATCH_URL;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title.getText().toString());
            jsonObject.put("description", description.getText().toString());
            temp = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = temp;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Posting new announcement/post");
                Toast.makeText(NewAnnouncement.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Failed to post new announcement/post");
                Toast.makeText(NewAnnouncement.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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