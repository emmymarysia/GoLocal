package com.example.golocal;

import android.util.Log;

import com.example.golocal.models.BusinessDataModel;

import java.util.HashMap;

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
}
