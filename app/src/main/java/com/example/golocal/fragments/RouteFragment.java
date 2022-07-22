package com.example.golocal.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.golocal.BusinessGraph;
import com.example.golocal.BusinessNode;
import com.example.golocal.R;
import com.example.golocal.models.BusinessDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteFragment extends DialogFragment {

    private ArrayList<BusinessDataModel> businessList;
    private Button btFindRoute;
    private AutoCompleteTextView startingBusiness;
    private AutoCompleteTextView endingBusiness;
    private ListView listViewPath;
    private BusinessDataModel start;
    private BusinessDataModel end;


    public RouteFragment(ArrayList<BusinessDataModel> businessList) {
        this.businessList = businessList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.btFindRoute = view.findViewById(R.id.btFindRoute);
        this.startingBusiness = view.findViewById(R.id.startingBusinessDropdown);
        this.endingBusiness = view.findViewById(R.id.endingBusinessDropdown);
        this.listViewPath = view.findViewById(R.id.listViewPath);
        ArrayList<String> businessNames = new ArrayList<>();
        for (BusinessDataModel business : businessList) {
            businessNames.add(business.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, businessNames);
        startingBusiness.setAdapter(adapter);
        endingBusiness.setAdapter(adapter);
        startingBusiness.setThreshold(1);
        endingBusiness.setThreshold(1);
        startingBusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("hello", "we are here");
                start = businessList.get(position);
            }
        });

        endingBusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                end = businessList.get(position);
            }
        });

        btFindRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start == null) {
                    Log.e("hello?", "wtf");
                }
                onRouteButton(start, end);
            }
        });
    }

    private void onRouteButton(BusinessDataModel startingBusiness, BusinessDataModel endingBusiness) {
        BusinessGraph graph = new BusinessGraph();
        for (BusinessDataModel business : businessList) {
            String latitude = business.getLatitude();
            String longitude = business.getLongitude();
            ArrayList<Double> distancesList = new ArrayList<>();
            for (BusinessDataModel visitBusiness : businessList) {
                if (visitBusiness.hasSameId(business)) {
                    continue;
                } else {
                    String visitLatitude = visitBusiness.getLatitude();
                    String visitLongitude = visitBusiness.getLongitude();
                    double distance = distanceBetweenPoints(latitude, longitude, visitLatitude, visitLongitude);
                    distancesList.add(distance);
                }
            }
            ArrayList<BusinessDataModel> businessListCopy = new ArrayList<>();
            businessListCopy.addAll(businessList);
            businessListCopy.remove(business);
            HashMap<BusinessDataModel, Double> closestBusinesses = getThreeClosestBusinesses(distancesList, businessListCopy);
            BusinessNode currentBusinessNode = graph.getNodes().get(business);
            if (currentBusinessNode == null) {
                currentBusinessNode = new BusinessNode(business);
                if (currentBusinessNode.getBusiness().getName().equals(startingBusiness.getName())) {
                    Log.e("we did it!", "yay!");
                }
            }
            for (BusinessDataModel key : closestBusinesses.keySet()) {
                Double distance = closestBusinesses.get(key);
                currentBusinessNode.addAdjacentNode(key, distance);
                BusinessNode neighborBusiness = graph.getNodes().get(key);
                if (neighborBusiness == null) {
                    neighborBusiness = new BusinessNode(key);
                }
                // this makes all the pointers "doubly linked" so that we guarantee every node is reachable
                neighborBusiness.addAdjacentNode(business, distance);
                graph.addNode(key, neighborBusiness);
            }
            graph.addNode(business, currentBusinessNode);
        }
        graph.getBusinesses();
        BusinessNode startingBusinessNode = graph.getNodes().get(startingBusiness);
        graph.dijkstra(startingBusinessNode);
        displayResults(graph, endingBusiness);
    }

    private void displayResults(BusinessGraph graph, BusinessDataModel endingBusiness) {
        BusinessNode endingBusinessNode = graph.getNodes().get(endingBusiness);
        List<BusinessNode> shortestPath = endingBusinessNode.getShortestPath();
        ArrayList<String> routeBusinessNames = new ArrayList<>();
        for (BusinessNode node : shortestPath) {
            BusinessDataModel businessDataModel = node.getBusiness();
            routeBusinessNames.add(businessDataModel.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, routeBusinessNames);
        listViewPath.setAdapter(adapter);
    }

    private double distanceBetweenPoints(String latitude1, String longitude1, String latitude2, String longitude2) {
        double lat1 = Math.toRadians(Double.parseDouble(latitude1));
        double long1 = Math.toRadians(Double.parseDouble(longitude1));
        double lat2 = Math.toRadians(Double.parseDouble(latitude2));
        double long2 = Math.toRadians(Double.parseDouble(longitude2));

        double longitudeDistance = long2 - long1;
        double latitudeDistance = lat2 - lat1;
        double step1 = Math.pow(Math.sin(latitudeDistance / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(longitudeDistance / 2),2);
        double computation = 2 * Math.asin(Math.sqrt(step1));

        double earthRadius = 3956;
        return computation * earthRadius;
    }

    private HashMap<BusinessDataModel, Double> getThreeClosestBusinesses(ArrayList<Double> distancesList, ArrayList<BusinessDataModel> businessList) {
        HashMap<BusinessDataModel, Double> closest = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            double minDistance = Double.MAX_VALUE;
            int minIndex = 0;
            for (int j = 0; j < distancesList.size(); j++) {
                double distance = distancesList.get(j);
                if (distance < minDistance) {
                    minDistance = distance;
                    minIndex = j;
                }
            }
            Double distance = distancesList.get(minIndex);
            BusinessDataModel closestBusiness = businessList.get(minIndex);
            closest.put(closestBusiness, distance);
            distancesList.remove(minIndex);
            businessList.remove(minIndex);
        }
        return closest;
    }
}
