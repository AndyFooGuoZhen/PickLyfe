package com.example.userprofilescreen.Friends;

import static com.example.userprofilescreen.util.ConstUrl.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.VolleySingleton;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class addFriendsAdapter extends RecyclerView.Adapter<addFriendsAdapter.ViewHolder3> implements Filterable {

    private Context context;
    private ArrayList<String> friends;
    private ArrayList<String> users;
    private ArrayList<String> usersAll;
    private JSONObject userProfile;

    public addFriendsAdapter (Context context,ArrayList<String>friends, ArrayList<String> allUsers, JSONObject userProfile){
        this.context=context;
        this.friends=friends;
        this.users = allUsers;
        this.usersAll = new ArrayList<>(users);
        this.userProfile = userProfile;
    }


    @NonNull
    @Override
    public addFriendsAdapter.ViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_friends_card,parent,false);
        return new addFriendsAdapter.ViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull addFriendsAdapter.ViewHolder3 holder, int position) {

        String user = users.get(position);
        holder.userName = user;
        holder.setInnerDetails(position + 1, user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<String> filteredUser = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredUser.addAll(usersAll);
            }else{
                for(String user : usersAll){
                    if(user.toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredUser.add(user);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredUser;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            users.clear();
            users.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();

        }
    };

    class ViewHolder3 extends RecyclerView.ViewHolder{

        private String userName;
        private TextView rankingTxt, userNameTxt,addBtn;


        ViewHolder3(View itemView){
            super(itemView);
            rankingTxt = itemView.findViewById(R.id.rankingTxt);
            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            addBtn = itemView.findViewById(R.id.addFriendBtn);

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(context, userName, Toast.LENGTH_SHORT).show();
                    addBtn.setEnabled(false);
                    addFriend(userName);
                }
            });
        }

        void setInnerDetails(int ranking, String user){
            rankingTxt.setText(""+ranking);
            userNameTxt.setText(user);

            if(friends.contains(user)){
                addBtn.setEnabled(false);
            }

            else{
                addBtn.setEnabled(true);
            }
        }

        void addFriend(String userName){
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
            String url = BASE_URL + "user/add/" + userName;

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "failed to Add friend", Toast.LENGTH_SHORT).show();
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

    }
}


