package com.quang.googleio.hanoi.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.quang.googleio.hanoi.R;
import com.quang.googleio.hanoi.adapter.SpeakerLibAdapter;

public class SpeakerView extends LinearLayout {

    private SpeakerLibAdapter adapter;
    private RecyclerView rvSpeaker;

    public SpeakerView(Context context) {
        super(context);
        initView(context, null);
    }

    public SpeakerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SpeakerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public SpeakerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_speaker, this);
        rvSpeaker = findViewById(R.id.rvLibSpeaker);
        rvSpeaker.setLayoutManager(new LinearLayoutManager(context));

        String[] listName;
        String[] listAvatar;
        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SpeakerView);
            CharSequence[] etName = attributes.getTextArray(R.styleable.SpeakerView_sv_list_name);
            CharSequence[] etAvatar = attributes.getTextArray(R.styleable.SpeakerView_sv_list_avatar);
            if (etName == null) etName = new CharSequence[0];
            if (etAvatar == null) etAvatar = new CharSequence[0];
            listName = new String[etName.length];
            for (int i = 0; i < etName.length; i++) {
                listName[i] = etName[i].toString();
            }
            listAvatar = new String[etAvatar.length];
            for (int i = 0; i < etAvatar.length; i++) {
                listName[i] = etAvatar[i].toString();
            }
            attributes.recycle();
        } else {
            listName = new String[0];
            listAvatar = new String[0];
        }
        adapter = new SpeakerLibAdapter(listName, listAvatar);
        rvSpeaker.setAdapter(adapter);
    }

    public void setupView(String[] listName, String[] listAvatar) {
        adapter = new SpeakerLibAdapter(listName, listAvatar);
        rvSpeaker.setAdapter(adapter);
    }
}
