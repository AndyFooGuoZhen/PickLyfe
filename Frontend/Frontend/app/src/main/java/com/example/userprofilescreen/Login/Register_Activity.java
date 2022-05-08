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
import com.android.volley.toolbox.StringRequest;
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.AppController;
import com.example.userprofilescreen.util.ConstUrl;

public class Register_Activity extends AppCompatActivity {

    private String TAG = Register_Activity.class.getSimpleName();
    private Button registerButton;
    private EditText userName, userEmail, userPassword, userPassword2;
    private TextView haveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.register_RegisterButton);
        userName = findViewById(R.id.register_UsernameEditText);
        userEmail = findViewById(R.id.register_EmailEditText);
        userPassword = findViewById(R.id.register_PasswordEditText);
        userPassword2= findViewById(R.id.register_ConfirmPasswordEditText);
        haveAccount = findViewById(R.id.register_LoginTextView);

        SpannableString loginMessage = new SpannableString("Already have an account? Log-in");
        ClickableSpan spanSignUp = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Register_Activity.this, Login_Activity.class));
            }
        };
        loginMessage.setSpan(spanSignUp, 25, 31, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        haveAccount.setText(loginMessage);
        haveAccount.setMovementMethod(LinkMovementMethod.getInstance());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkUsername() && checkPassword() && checkEmail()) {
                    setupAccountReq();
                } else {
                    resetFields();
                }
            }
        });
    }



    private boolean checkUsername() {

        if (!userName.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    private boolean checkEmail(){
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (userEmail.getText().toString().matches(emailRegex)) {
            return true;
        }
        return false;
    }

    private boolean checkPassword() {

        if (userPassword.getText().toString().equals(userPassword2.getText().toString()) &&
                !userPassword.getText().toString().equals("") &&
                !userPassword2.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    private void setupAccountReq() {
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("userName", userName.getText().toString());
//            jsonObject.put("email", userEmail.getText().toString());
//            jsonObject.put("password", userPassword.getText().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstUrl.POST_USERACCOUNT_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(Register_Activity.this, response.toString(), Toast.LENGTH_SHORT).show();
//                if (response.startsWith("Successful")) {
//                    startActivity(new Intent(Register_Activity.this, Login_Activity.class));
//                }
//                else {
//                    resetFields();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }) {
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                return jsonObject.toString().getBytes();
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }
//        };
//
//        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
        String url = ConstUrl.POST_USERACCOUNT_URL + "/" + userEmail.getText().toString() + "/" +
                        userName.getText().toString() + "/" + userPassword.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Register_Activity.this, response, Toast.LENGTH_SHORT).show();
                if (response.startsWith("Successful")) {
                    startActivity(new Intent(Register_Activity.this, Login_Activity.class));
                }
                else {
                    resetFields();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register_Activity.this, "Error Registering Now", Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private void resetFields() {
        userName.setText("");
        userEmail.setText("");
        userPassword.setText("");
        userPassword2.setText("");
        Toast.makeText(Register_Activity.this, "Please fill in all valid data in the fields", Toast.LENGTH_SHORT).show();
    }
}