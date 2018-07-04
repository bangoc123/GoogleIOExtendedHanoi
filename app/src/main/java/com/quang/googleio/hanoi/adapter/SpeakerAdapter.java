package com.quang.googleio.hanoi.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quang.googleio.hanoi.R;
import com.quang.googleio.hanoi.model.Topic;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.ViewHolder> {

    private ArrayList<Speaker> listSpeaker;

    public SpeakerAdapter(String speakers) {
        listSpeaker = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(speakers);
            for (int i = 0; i < array.length(); i++) {
                JSONObject speaker = array.getJSONObject(i);
                String name = speaker.getString("name");
                String company = speaker.getString("company");
                String info = speaker.getString("info");
                String imageurl = speaker.getString("imageurl");
                listSpeaker.add(new Speaker(imageurl, name, company, info));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_speaker, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.tvName.setText(listSpeaker.get(position).getName());
        holder.tvCompany.setText(listSpeaker.get(position).getCompany());
        holder.tvInfo.setText(listSpeaker.get(position).getInfo());
        Glide.with(holder.itemView).load(listSpeaker.get(position).getAvatar()).into(holder.imvAvatar);
    }

    @Override
    public int getItemCount() {
        return listSpeaker.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvCompany, tvInfo;
        CircleImageView imvAvatar;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvInfo = itemView.findViewById(R.id.tvInfo);
            imvAvatar = itemView.findViewById(R.id.imvAvatar);
        }
    }

    private class Speaker {
        private String avatar;
        private String name;
        private String company;
        private String info;

        public Speaker() {
        }

        Speaker(String avatar, String name, String company, String info) {
            this.avatar = avatar;
            this.name = name;
            this.company = company;
            this.info = info;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
