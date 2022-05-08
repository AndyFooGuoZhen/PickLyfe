package com.example.userprofilescreen.Chat;

import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.userprofilescreen.R;

import java.util.ArrayList;

public class chatsCardAdapter extends RecyclerView.Adapter<chatsCardAdapter.ViewHolder> {

    private Context context2;
    private ArrayList<Chat> chats;
    private String currentUser;

    public chatsCardAdapter(Context context2, ArrayList<Chat> chats, String userName){
        this.context2=context2;
        this.chats=chats;
        this.currentUser = userName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context2).inflate(R.layout.chatcard,parent,false);
        return new ViewHolder(view);
    }


    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Chat chat = chats.get(position);
    holder.setInnerDetails(chat, currentUser);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView userNameTxt, msgTxt;
        private RelativeLayout holderRelative;


        ViewHolder(View itemView){
            super(itemView);
            userNameTxt = itemView.findViewById(R.id.userName);
            msgTxt = itemView.findViewById(R.id.userMsg);
            holderRelative = itemView.findViewById(R.id.holderRelative);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        void setInnerDetails(Chat chat, String userName){

            userNameTxt.setText(chat.getUserName());
            itemView.setBackgroundColor(00);

            if(chat.getUserName().equals("User")){
                userNameTxt.setText("");
                userNameTxt.setAlpha(0);
                msgTxt.setBackgroundColor(00);
                msgTxt.setText((chat.getMessage()));
            }

            else if(!chat.getUserName().equals(userName)){
                holderRelative.setGravity(RIGHT);
                userNameTxt.setText(chat.getMessage());
                userNameTxt.setTextColor(Color.BLACK);
                userNameTxt.setBackgroundColor(Color.YELLOW);
                msgTxt.setText((chat.getUserName()));
                msgTxt.setTextColor(Color.WHITE);
                msgTxt.setBackgroundColor(Color.BLACK);
            }
            else{

                holderRelative.setGravity(LEFT);
                userNameTxt.setTextColor(Color.WHITE);
                userNameTxt.setBackgroundColor(Color.BLACK);
                msgTxt.setTextColor(Color.BLACK);
                msgTxt.setBackgroundColor(Color.parseColor("#E55E5E"));
                msgTxt.setText((chat.getMessage()));
            }

        }

    }
}
