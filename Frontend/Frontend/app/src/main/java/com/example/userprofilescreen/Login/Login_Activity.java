package com.example.userprofilescreen.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.userprofilescreen.Main_Menu_Activity;
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.AppController;
import com.example.userprofilescreen.util.ConstUrl;

import org.json.JSONException;
import org.json.JSONObject;

public class Login_Activity extends AppCompatActivity {

    private String TAG = Login_Activity.class.getSimpleName();
    private Button loginButton;
    private EditText loginUsernameEmail, loginPassword;
    private TextView signUpTextView, forgetTextView;
    private int resetPasswordCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_LoginButton);
        loginUsernameEmail = findViewById(R.id.login_UsernameEditText);
        loginPassword = findViewById(R.id.login_PasswordEditText);
        forgetTextView = findViewById(R.id.login_ForgetPasswordTextView);
        signUpTextView = findViewById(R.id.login_SignUpTextView);

        SpannableString signUpMessage = new SpannableString("Don't have an account? Sign-up");
        ClickableSpan spanSignUp = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Login_Activity.this, Register_Activity.class));
            }
        };

        signUpMessage.setSpan(spanSignUp, 23, 30, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        signUpTextView.setText(signUpMessage);
        signUpTextView.setMovementMethod(LinkMovementMethod.getInstance());

        forgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Activity.this, Forgot_Password_Activity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoginReq();
            }
        });
    }

    private void getLoginReq() {
        // TODO: Take Username and Password to send request
        String url = ConstUrl.GET_LOGIN_URL + loginUsernameEmail.getText().toString() + "/" + loginPassword.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.startsWith("Login success! Welcome ")) {
                    ConstUrl.USERNAME = response.substring(23, response.length());
                    getUsernameID(ConstUrl.USERNAME);
                } else {
                    loginUsernameEmail.setText("");
                    loginPassword.setText("");
                }

                Toast.makeText(Login_Activity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Set Text to "" and give error message
                loginUsernameEmail.setText("");
                loginPassword.setText("");
                Toast.makeText(Login_Activity.this, "Failed to handle login request", Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private void getUsernameID(String name) {
        String url = ConstUrl.GET_USERNAME_ID_URL + "name/" + name;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                long id = 0;
                int status = 0;
                try {
                    id = response.getLong("id");
                    status = response.getInt("passwordStatus");
                    ConstUrl.isAdmin = response.getString("role").equals("Admin");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ConstUrl.USERNAME_ID = id;

                if (status == 0) {
                    startActivity(new Intent(Login_Activity.this, Main_Menu_Activity.class));
                } else {
                    startActivity(new Intent(Login_Activity.this, Reset_Password_Activity.class));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

}