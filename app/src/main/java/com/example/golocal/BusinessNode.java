package com.example.golocal;

import com.example.golocal.models.BusinessDataModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BusinessNode {
    private BusinessDataModel business;
    private HashMap<BusinessDataModel, Double> adjacentNodes = new HashMap<>();
    private Double distanceFromSource = Double.MAX_VALUE;
    private List<BusinessNode> shortestPath = new LinkedList<>();

    public BusinessNode(BusinessDataModel business) {
        this.business = business;
    }

    public void addAdjacentNode(BusinessDataModel business, Double distance) {
        adjacentNodes.put(business, distance);
    }

    public void setDistanceFromSource(Double distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
    }

    public double getDistanceFromSource() {
        return this.distanceFromSource;
    }

    public HashMap<BusinessDataModel, Double> getAdjacentNodes() {
        return this.adjacentNodes;
    }

    public void addToShortestPath(BusinessNode node) {
        this.shortestPath.add(node);
    }

    public List<BusinessNode> getShortestPath() {
        return this.shortestPath;
    }

    public void clearShortestPath() {
        this.shortestPath.clear();
    }

    public BusinessDataModel getBusiness() {
        return this.business;
    }
}
