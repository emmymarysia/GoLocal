package com.example.golocal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golocal.BusinessGraph;
import com.example.golocal.BusinessNode;
import com.example.golocal.R;
import com.example.golocal.adapters.BusinessAdapter;
import com.example.golocal.models.BusinessDataModel;
import com.example.golocal.models.GuideDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuideDetailFragment extends Fragment {

    private GuideDataModel guideDataModel;
    private TextView tvTitleDetail;
    private TextView tvAuthorDetail;
    private TextView tvDescriptionDetail;
    private RecyclerView rvBusinessesDetail;
    private BusinessAdapter adapter;
    private FragmentManager fragmentManager;

    public GuideDetailFragment(GuideDataModel guideDataModel, FragmentManager fragmentManager) {
        this.guideDataModel = guideDataModel;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitleDetail = view.findViewById(R.id.tvTitleDetail);
        tvAuthorDetail = view.findViewById(R.id.tvAuthorDetail);
        tvDescriptionDetail = view.findViewById(R.id.tvDescriptionDetail);
        rvBusinessesDetail = view.findViewById(R.id.rvBusinessesDetail);
        adapter = new BusinessAdapter(getContext(), guideDataModel.getBusinessList());

        tvTitleDetail.setText(guideDataModel.getTitle());
        tvAuthorDetail.setText(guideDataModel.getAuthor().getUsername());
        tvDescriptionDetail.setText(guideDataModel.getDescription());
        rvBusinessesDetail.setAdapter(adapter);
        rvBusinessesDetail.setLayoutManager(new LinearLayoutManager(getContext()));

        tvAuthorDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment(guideDataModel.getAuthor());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.flContainer, profileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        onRouteButton();
    }

    private void onRouteButton() {
        List<BusinessDataModel> businessList = guideDataModel.getBusinessList();
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
            HashMap<BusinessDataModel, Double> closestBusinesses = getThreeClosest(distancesList, businessListCopy);
            BusinessNode currentBusinessNode = graph.getNodes().get(business);
            if (currentBusinessNode == null) {
                currentBusinessNode = new BusinessNode(business);
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
        graph.dijkstra(graph.getNodes().get(businessList.get(0)));
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

    private HashMap<BusinessDataModel, Double> getThreeClosest(ArrayList<Double> distancesList, ArrayList<BusinessDataModel> businessList) {
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
