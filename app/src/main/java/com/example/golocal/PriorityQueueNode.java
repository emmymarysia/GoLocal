package com.example.golocal;

import com.example.golocal.models.GuideDataModel;

public class PriorityQueueNode {
    private GuideDataModel guideDataModel;
    private int priority;

    public PriorityQueueNode(GuideDataModel guideDataModel, int priority) {
        this.guideDataModel = guideDataModel;
        // assignPriority();
        this.priority = priority;
    }

    private void assignPriority() {
        // TODO: implement a priority function that assigns priority to nodes based on combination of factors (location, friends, interests, etc.)
    }

    public int getPriority() {
        return this.priority;
    }
}
