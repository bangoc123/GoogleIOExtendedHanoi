package com.quang.googleio.hanoi.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quang.googleio.hanoi.R;
import com.quang.googleio.hanoi.model.Topic;
import com.quang.googleio.hanoi.ui.SpeakerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyTopicsAdapter extends RecyclerView.Adapter<MyTopicsAdapter.ViewHolder> {

    private ArrayList<Topic> listTopic;
    private OnItemClickListener mOnItemClickListener;
    private OnDeleteClickListener mOnDeleteClickListener;

    public MyTopicsAdapter(ArrayList<Topic> listTopic) {
        this.listTopic = listTopic;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_topic, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.UK);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.UK);
        try {
            holder.tvTime.setText(sdf2.format(new Date(sdf.parse(listTopic.get(position).getTimestart()).getTime() + 7 * (60 * 60 * 1000))));
        } catch (Exception e) {
            holder.tvTime.setText(listTopic.get(position).getStart());
            e.printStackTrace();
        }
        holder.tvTitle.setText(listTopic.get(position).getName());
        holder.tvTimeAlong.setText(listTopic.get(position).getDuration());
        holder.tvRoom.setText(listTopic.get(position).getLocation());
        holder.tvTag.setText(listTopic.get(position).getTopictype());
        try {
            ArrayList<String> listName = new ArrayList<>();
            ArrayList<String> listAvatar = new ArrayList<>();
            JSONArray speakers = new JSONArray(listTopic.get(position).getSpeaker());
            for (int i = 0; i < speakers.length(); i++) {
                JSONObject speaker = speakers.getJSONObject(i);
                String name = speaker.getString("name");
                String imageurl = speaker.getString("imageurl");
                listName.add(name);
                listAvatar.add(imageurl);
            }
            holder.svSpeaker.setupView(listName.toArray(new String[listName.size()]), listAvatar.toArray(new String[listAvatar.size()]));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listTopic.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvTitle, tvTimeAlong, tvRoom, tvTag;
        SpeakerView svSpeaker;
        AppCompatImageView imvClose;

        ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            svSpeaker = itemView.findViewById(R.id.svSpeaker);
            tvTimeAlong = itemView.findViewById(R.id.tvTimeAlong);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvTag = itemView.findViewById(R.id.tvTag);
            imvClose = itemView.findViewById(R.id.imvClose);
            imvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDeleteClickListener != null) {
                        mOnDeleteClickListener.onDeleteClick(v, getAdapterPosition());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(View view, int position);
    }

    public void setOnDeleteListener(OnDeleteClickListener mOnDeleteClickListener) {
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }
}
