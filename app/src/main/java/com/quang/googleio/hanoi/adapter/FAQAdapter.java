package com.quang.googleio.hanoi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.googleio.hanoi.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class FAQAdapter extends ExpandableRecyclerViewAdapter<QuestionHolder, AnswerHolder> {

    public FAQAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public QuestionHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionHolder(view);
    }

    @Override
    public AnswerHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer, parent, false);
        return new AnswerHolder(view);
    }

    @Override
    public void onBindChildViewHolder(AnswerHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {
        String answer = (String) group.getItems().get(childIndex);
        holder.setAnswer(answer);
    }

    @Override
    public void onBindGroupViewHolder(QuestionHolder holder, int flatPosition,
                                      ExpandableGroup group) {

        holder.setQuestionTitle(group);
    }
}
