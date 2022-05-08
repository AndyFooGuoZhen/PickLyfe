package com.example.userprofilescreen.app;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton volleyInstance;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized  VolleySingleton getInstance(Context context){
        if(volleyInstance==null){
            volleyInstance = new VolleySingleton(context);
        }

        return volleyInstance;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

}
