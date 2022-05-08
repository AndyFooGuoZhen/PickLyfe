package com.example.userprofilescreen.LeaderBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userprofilescreen.*;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context context2;
    private ArrayList<Player> players;

    public CardAdapter (Context context2, ArrayList<Player> players){
        this.context2=context2;
        this.players=players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context2).inflate(R.layout.leader_or_friends_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Player player = players.get(position);
    holder.setInnerDetails(position + 1, player);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView rankingTxt, userNameTxt, userScoreTxt;


        ViewHolder(View itemView){
            super(itemView);
            rankingTxt = itemView.findViewById(R.id.rankingTxt);
            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            userScoreTxt = itemView.findViewById(R.id.userScoreORDate);
        }

        void setInnerDetails(int ranking, Player player){
           rankingTxt.setText(""+ranking);
            userNameTxt.setText(player.getUserName());
            userScoreTxt.setText((player.getUserScore()));
        }
    }
}
