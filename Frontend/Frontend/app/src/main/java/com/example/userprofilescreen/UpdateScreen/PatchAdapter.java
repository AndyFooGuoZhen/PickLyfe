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

public class PatchAdapter  extends RecyclerView.Adapter<PatchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Patch> patchArrayList;

    public PatchAdapter(Context context, ArrayList<Patch> patchArrayList) {
        this.context = context;
        this.patchArrayList = patchArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patch_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Patch patch = patchArrayList.get(position);
        holder.patchTitle.setText(patch.getTitle());
        holder.patchDate.setText(patch.getDate());
        holder.patchDescription.setText(patch.getDescription());
    }

    @Override
    public int getItemCount() {
        return patchArrayList.size();
    }

    public void deleteItem(int position) {
        patchArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Patch item, int position) {
        patchArrayList.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<Patch> getData() {
        return patchArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView patchTitle, patchDate, patchDescription;
        private CardView patchCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patchTitle = (TextView) itemView.findViewById(R.id.patch_Title);
            patchDate = (TextView) itemView.findViewById(R.id.patch_Date);
            patchDescription = (TextView) itemView.findViewById(R.id.patch_Description);
            patchCardView = (CardView) itemView.findViewById(R.id.patch_CardView);
        }
    }

    public Context getContext() {
        return context;
    }
}
