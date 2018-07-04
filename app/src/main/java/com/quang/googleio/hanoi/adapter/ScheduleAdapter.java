package com.quang.googleio.hanoi.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private ArrayList<Topic> listTopic;
    private OnItemClickListener mOnItemClickListener;
    private OnStarClickListener mOnStarClickListener;

    public ScheduleAdapter(ArrayList<Topic> listTopic) {
        this.listTopic = listTopic;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        ColorStateList backgroundTintList = ColorStateList.valueOf(parse(listTopic.get(position).getColor()));
        holder.tvTag.setBackgroundTintList(backgroundTintList);
        if (listTopic.get(position).isBooked())
            holder.imvStar.setImageResource(R.drawable.ic_check);
        else holder.imvStar.setImageResource(R.drawable.ic_playlist_plus);
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
        ImageView imvStar;
        SpeakerView svSpeaker;

        ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            svSpeaker = itemView.findViewById(R.id.svSpeaker);
            tvTimeAlong = itemView.findViewById(R.id.tvTimeAlong);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvTag = itemView.findViewById(R.id.tvTag);
            imvStar = itemView.findViewById(R.id.imvStar);
            imvStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnStarClickListener != null) {
                        mOnStarClickListener.onStarClick(v, getAdapterPosition());
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

    public interface OnStarClickListener {
        void onStarClick(View view, int position);
    }

    public void setOnStarListener(OnStarClickListener mOnStarClickListener) {
        this.mOnStarClickListener = mOnStarClickListener;
    }

    public static int parse(String input) {
        Pattern c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
        Matcher m = c.matcher(input);

        if (m.matches()) {
            return Color.rgb(Integer.valueOf(m.group(1)),  // r
                    Integer.valueOf(m.group(2)),  // g
                    Integer.valueOf(m.group(3))); // b
        }
        return 0;
    }
}
