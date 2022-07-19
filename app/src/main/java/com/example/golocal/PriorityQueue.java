package com.example.golocal;

import com.example.golocal.models.GuideDataModel;

import java.util.ArrayList;
import java.util.List;

public class PriorityQueue {
    private ArrayList<PriorityQueueNode> maxHeap = new ArrayList<>();

    public void insert(PriorityQueueNode node) {
        maxHeap.add(node);
        int insertedNodeIndex = maxHeap.size() - 1;
        int insertedNodePriority = node.getPriority();
        while (insertedNodeIndex != 0 && insertedNodePriority > getParent(insertedNodeIndex).getPriority()) {
            int parentIndex = getParentIndex(insertedNodeIndex);
            swap(getParentIndex(insertedNodeIndex), insertedNodeIndex);
            insertedNodeIndex = parentIndex;
        }
    }

    public PriorityQueueNode peek() {
        if (maxHeap.size() == 0) {
            return null;
        }
        return maxHeap.get(0);
    }

    public PriorityQueueNode remove() {
        if (maxHeap.size() == 0) {
            return null;
        }
        PriorityQueueNode head = maxHeap.get(0);
        swap(0, maxHeap.size()-1);
        maxHeap.remove(maxHeap.size()-1);
        fixHeap();
        return head;
    }

    private void fixHeap() {
        int nodeIndex = 0;
        if (maxHeap.size() == 0) {
            return;
        }
        int nodePriority = maxHeap.get(0).getPriority();
        while (getLeftChild(nodeIndex) != null || getRightChild(nodeIndex) != null) {
            PriorityQueueNode leftChild = getLeftChild(nodeIndex);
            PriorityQueueNode rightChild = getRightChild(nodeIndex);
            int leftChildIndex = getLeftChildIndex(nodeIndex);
            int rightChildIndex = getRightChildIndex(nodeIndex);
            if (leftChild != null && rightChild != null) {
                int leftChildPriority = leftChild.getPriority();
                int rightChildPriority = rightChild.getPriority();
                if (nodePriority >= leftChildPriority && nodePriority >= rightChildPriority) {
                    return;
                } else if (leftChildPriority > rightChildPriority) {
                    swap(nodeIndex, leftChildIndex);
                    nodeIndex = leftChildIndex;
                } else {
                    swap(nodeIndex, rightChildIndex);
                    nodeIndex = rightChildIndex;
                }
            } else if (leftChild == null) {
                int rightChildPriority = rightChild.getPriority();
                if (nodePriority >= rightChildPriority) {
                    return;
                } else {
                    swap(nodeIndex, rightChildIndex);
                    nodeIndex = rightChildIndex;
                }
            } else if (rightChild == null) {
                int leftChildPriority = leftChild.getPriority();
                if (nodePriority >= leftChildPriority) {
                    return;
                } else {
                    swap(nodeIndex, leftChildIndex);
                    nodeIndex = leftChildIndex;
                }
            }
        }
    }

    private void swap(int parentIndex, int childIndex) {
        PriorityQueueNode temp = maxHeap.get(parentIndex);
        maxHeap.set(parentIndex, maxHeap.get(childIndex));
        maxHeap.set(childIndex, temp);
    }

    private PriorityQueueNode getParent(int nodeIndex) {
        if (nodeIndex > 0) {
            return maxHeap.get((nodeIndex-1)/2);
        }
        return null;
    }

    private int getParentIndex(int nodeIndex) {
        return (nodeIndex-1)/2;
    }

    private PriorityQueueNode getLeftChild(int nodeIndex) {
        int leftChildIndex = 2 * nodeIndex + 1;
        if (leftChildIndex < maxHeap.size()) {
            return maxHeap.get(leftChildIndex);
        }
        return null;
    }

    private PriorityQueueNode getRightChild(int nodeIndex) {
        int rightChildIndex = 2 * nodeIndex + 2;
        if (rightChildIndex < maxHeap.size()) {
            return maxHeap.get(rightChildIndex);
        }
        return null;
    }

    private int getLeftChildIndex(int nodeIndex) {
        return (2 * nodeIndex) + 1;
    }

    private int getRightChildIndex(int nodeIndex) {
        return (2 * nodeIndex) + 2;
    }

    public String toString() {
        String toString = "";
        for (int i = 0; i < maxHeap.size(); i++) {
            toString += String.valueOf(maxHeap.get(i).getPriority());
            toString += " ";
        }
        return toString;
    }

    public int size() {
        return this.maxHeap.size();
    }

    public void clear() {
        maxHeap.clear();
    }

    public ArrayList<GuideDataModel> getAllGuidesInOrder() {
        ArrayList<GuideDataModel> guides = new ArrayList<>();
        ArrayList<PriorityQueueNode> nodes = new ArrayList<>();
        while (maxHeap.size() > 0) {
            PriorityQueueNode nextNode = remove();
            guides.add(nextNode.getGuideDataModel());
            nodes.add(nextNode);
        }
        insertAllNodes(nodes);
        return guides;
    }

    public void insertAllGuides(List<GuideDataModel> guidesList) {
        for (GuideDataModel guide: guidesList) {
            PriorityQueueNode currentGuide = new PriorityQueueNode(guide);
            insert(currentGuide);
        }
    }

    public void insertAllNodes(List<PriorityQueueNode> nodesList) {
        for (PriorityQueueNode node: nodesList) {
            insert(node);
        }
    }
}
