package com.example.userprofilescreen.PerkScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userprofilescreen.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PerkRecyclerViewAdapter extends RecyclerView.Adapter<PerkRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Perk> mData;

    public PerkRecyclerViewAdapter(Context mContext, List<Perk> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.perk_cardview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.perkImageView.setImageResource(mData.get(position).getThumbnail());
        holder.perkCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Perk_Activity.setOnClickChanges(mData.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView perkImageView;
        CardView perkCardView;
//        TextView perkName, perkRarity, perkDescription;
//        Button selectButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            perkImageView = (ImageView) itemView.findViewById(R.id.perk_cvImageView);
            perkCardView = (CardView) itemView.findViewById(R.id.perk_cvCardView);
//            perkName = (TextView) itemView.findViewById(R.id.perk_NameTextView);
//            perkRarity = (TextView) itemView.findViewById(R.id.perk_RarityTextView);
//            perkDescription = (TextView) itemView.findViewById(R.id.perk_DescriptionTextView);
//            selectButton = (Button) itemView.findViewById(R.id.perk_SelectButton);
        }
    }

    public Perk getPerk(String perkName) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getPerkName().equals(perkName)) {
                return mData.get(i);
            }
        }

        return null;
    }
}
