package com.example.golocal;

import com.example.golocal.models.BusinessDataModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BusinessNode {
    private BusinessDataModel business;
    private HashMap<BusinessDataModel, Double> adjacentNodes = new HashMap<>();
    private Double shortestPathDistance = Double.MAX_VALUE;
    private List<BusinessNode> shortestPath = new LinkedList<>();

    public void addAdjacentNode(BusinessDataModel business, Double distance) {
        adjacentNodes.put(business, distance);
    }
}
