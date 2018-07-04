package com.quang.googleio.hanoi.adapter;

import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quang.googleio.hanoi.R;

public class SpeakerLibAdapter extends RecyclerView.Adapter<SpeakerLibAdapter.ViewHolder> {

    private String[] listName;
    private String[] listAvatar;

    public SpeakerLibAdapter(String[] listName, String[] listAvatar) {
        this.listName = listName;
        this.listAvatar = listAvatar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lib_speaker, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(listName[position]);
        Glide.with(holder.itemView).load(listAvatar[position]).into(holder.imvAvatar);
    }

    @Override
    public int getItemCount() {
        return listName.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView imvAvatar;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            imvAvatar = itemView.findViewById(R.id.imvAvatar);
        }

    }
}
