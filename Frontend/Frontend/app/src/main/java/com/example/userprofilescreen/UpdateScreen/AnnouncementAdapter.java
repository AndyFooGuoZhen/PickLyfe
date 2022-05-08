package com.example.userprofilescreen.UpdateScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userprofilescreen.R;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Announcement> announcementArrayList;

    public AnnouncementAdapter(Context context, ArrayList<Announcement> announcementArrayList) {
        this.context = context;
        this.announcementArrayList = announcementArrayList;
    }

    @NonNull
    @Override
    public AnnouncementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcementArrayList.get(position);
        holder.anncmntTitle.setText(announcement.getTitle());
        holder.anncmntDate.setText("Date: " + announcement.getDate());
        holder.anncmntDesc.setText(announcement.getDescription());
    }

    @Override
    public int getItemCount() {
        return announcementArrayList.size();
    }

    public void deleteItem(int position) {
        announcementArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<Announcement> getData() {
        return announcementArrayList;
    }

    public void restoreItem(Announcement item, int position) {
        announcementArrayList.add(position, item);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView anncmntCard;
        private TextView anncmntTitle, anncmntDate, anncmntDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anncmntCard = (CardView) itemView.findViewById(R.id.announcement_CardView);
            anncmntTitle = (TextView) itemView.findViewById(R.id.announcement_Title);
            anncmntDate = (TextView) itemView.findViewById(R.id.announcement_Date);
            anncmntDesc = (TextView) itemView.findViewById(R.id.announcement_Description);
        }
    }

    public Context getContext() {
        return context;
    }
}
