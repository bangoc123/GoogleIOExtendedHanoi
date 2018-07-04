package com.quang.googleio.hanoi.adapter;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.quang.googleio.hanoi.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class QuestionHolder extends GroupViewHolder {

    private TextView tvTitle;
    private ImageView arrow;

    public QuestionHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        arrow = itemView.findViewById(R.id.imvArrow);
    }

    public void setQuestionTitle(ExpandableGroup genre) {
        tvTitle.setText(genre.getTitle());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        tvTitle.setTextColor(itemView.getResources().getColor(R.color.colorAccent));
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        tvTitle.setTextColor(itemView.getResources().getColor(android.R.color.tab_indicator_text));
        arrow.setAnimation(rotate);
    }
}