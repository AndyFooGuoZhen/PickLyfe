package com.example.userprofilescreen.Friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userprofilescreen.LeaderBoard.CardAdapter;
import com.example.userprofilescreen.LeaderBoard.Player;
import com.example.userprofilescreen.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class friendsCardAdapter extends RecyclerView.Adapter<friendsCardAdapter.ViewHolder2> implements Filterable {

    private Context context;
    private ArrayList<Friend> friends;
    private ArrayList<Friend> friendsAll;

    public friendsCardAdapter (Context context, ArrayList<Friend> friends){
        this.context=context;
        this.friends=friends;
        this.friendsAll = new ArrayList<>(friends);
    }

    @NonNull
    @Override
    public friendsCardAdapter.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leader_or_friends_card,parent,false);
        return new friendsCardAdapter.ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull friendsCardAdapter.ViewHolder2 holder, int position) {
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

    class ViewHolder2 extends RecyclerView.ViewHolder{

        private TextView rankingTxt, userNameTxt, userScoreTxt;


        ViewHolder2(View itemView){
            super(itemView);
            rankingTxt = itemView.findViewById(R.id.rankingTxt);
            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            userScoreTxt = itemView.findViewById(R.id.userScoreORDate);
        }

        void setInnerDetails(int ranking, Friend friend){
            rankingTxt.setText(""+ranking);
            userNameTxt.setText(friend.getUserName());
            userScoreTxt.setText((friend.getgamesPlayed()));
        }


    }
}
