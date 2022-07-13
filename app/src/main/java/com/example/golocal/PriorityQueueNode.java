package com.example.golocal;

import com.example.golocal.models.GuideDataModel;

public class PriorityQueueNode {
    private GuideDataModel guideDataModel;
    private int priority;
    private PriorityQueueNode leftChild = null;
    private PriorityQueueNode rightChild = null;

    public PriorityQueueNode(GuideDataModel guideDataModel) {
        this.guideDataModel = guideDataModel;
        assignPriority();
    }

    private void assignPriority() {
        // TODO: implement a priority function that assigns priority to nodes based on combination of factors (location, friends, interests, etc.)
    }
}
