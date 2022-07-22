package com.example.golocal;

import android.util.Log;

import com.example.golocal.models.BusinessDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BusinessGraph {
    private HashMap<BusinessDataModel, BusinessNode> nodes = new HashMap<>();

    public void addNode(BusinessDataModel business, BusinessNode node) {
        nodes.put(business, node);
    }

    public void getBusinesses() {
        for (BusinessDataModel businessDataModel : nodes.keySet()) {
            Log.e("business", businessDataModel.getName());
        }
    }

    public HashMap<BusinessDataModel, BusinessNode> getNodes() {
        return this.nodes;
    }

    public void dijkstra(BusinessNode source) {
        source.setDistanceFromSource(0.0);
        ArrayList<BusinessNode> nodesToVisit = new ArrayList<>();
        for (BusinessNode node : getNodes().values()) {
            nodesToVisit.add(node);
        }

        while (nodesToVisit.size() > 0) {
            BusinessNode nextNode = getNextNode(nodesToVisit);
            HashMap<BusinessDataModel, Double> adjacentNodes = nextNode.getAdjacentNodes();
            for (BusinessDataModel neighbor : adjacentNodes.keySet()) {
                BusinessNode neighborNode = nodes.get(neighbor);
                if (nodesToVisit.contains(neighborNode)) {
                    Double altDistance = nextNode.getDistanceFromSource() + adjacentNodes.get(neighbor);
                    if (altDistance < neighborNode.getDistanceFromSource() && nextNode.getDistanceFromSource() != Double.MAX_VALUE) {
                        neighborNode.setDistanceFromSource(altDistance);
                        LinkedList<BusinessNode> previousPath = (LinkedList<BusinessNode>) nextNode.getShortestPath();
                        neighborNode.clearShortestPath();
                        for (BusinessNode node : previousPath) {
                            neighborNode.addToShortestPath(node);
                        }
                        neighborNode.addToShortestPath(nextNode);
                    }
                }
            }
        }
    }

    private BusinessNode getNextNode(ArrayList<BusinessNode> nodesToVisit) {
        double minDistance = Double.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < nodesToVisit.size(); i++) {
            double distance = nodesToVisit.get(i).getDistanceFromSource();
            if (distance < minDistance) {
                minDistance = distance;
                minIndex = i;
            }
        }
        BusinessNode nextNodeToVisit = nodesToVisit.get(minIndex);
        nodesToVisit.remove(minIndex);
        return nextNodeToVisit;
    }
}
