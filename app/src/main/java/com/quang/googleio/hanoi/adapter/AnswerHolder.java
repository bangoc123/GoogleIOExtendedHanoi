package com.quang.googleio.hanoi.adapter;

import android.view.View;
import android.widget.TextView;

import com.quang.googleio.hanoi.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class AnswerHolder extends ChildViewHolder {

    private TextView tvAnswer;

    public AnswerHolder(View itemView) {
        super(itemView);
        tvAnswer = itemView.findViewById(R.id.tvAnswer);
    }

    public void setAnswer(String name) {
        tvAnswer.setText(name);
    }
}