package com.example.userprofilescreen.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userprofilescreen.EndGame.EndActivity;
import com.example.userprofilescreen.Friends.Friend;
import com.example.userprofilescreen.GameSetup.GameSetupActivity;
import com.example.userprofilescreen.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class friendsChatCardAdapter extends RecyclerView.Adapter<friendsChatCardAdapter.ViewHolder4> implements Filterable {
    private Context context;
    private ArrayList<Friend> friends;
    private ArrayList<Friend> friendsAll;


    public friendsChatCardAdapter(Context context, ArrayList<Friend> friends){
        this.context=context;
        this.friends=friends;
        this.friendsAll = new ArrayList<>(friends);
    }

    @NonNull
    @Override
    public ViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friends_chat_card,parent,false);
        return new ViewHolder4(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder4 holder, int position) {
        Friend friend = friends.get(position);
        holder.setInnerDetails(position + 1, friend);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<Friend> filteredFriend = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredFriend.addAll(friendsAll);
            }else{
                for(Friend friend : friendsAll){
                    if(friend.getUserName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredFriend.add(friend);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredFriend;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            friends.clear();
            friends.addAll((Collection<? extends Friend>) filterResults.values);
            notifyDataSetChanged();

        }
    };

    class ViewHolder4 extends RecyclerView.ViewHolder{

        private TextView rankingTxt, userNameTxt;
        private Button chatBtn;

        ViewHolder4(View itemView){
            super(itemView);
            rankingTxt = itemView.findViewById(R.id.rankingTxt);
            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            chatBtn = itemView.findViewById(R.id.chatFriendBtn);

            chatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConstPrivateChatDetails.friends_Name = userNameTxt.getText().toString();
                    Intent intent = new Intent(context, PrivateChatActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        void setInnerDetails(int ranking, Friend friend){
            rankingTxt.setText(""+ranking);
            userNameTxt.setText(friend.getUserName());
        }


    }
}
