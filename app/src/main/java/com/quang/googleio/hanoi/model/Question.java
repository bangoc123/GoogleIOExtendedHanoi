package com.quang.googleio.hanoi.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.Collections;

public class Question extends ExpandableGroup {
    public Question(String question, String answer) {
        super(question, Collections.singletonList(answer));
    }
}
