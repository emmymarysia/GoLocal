package com.example.golocal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.golocal.activities.MainActivity;
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
    private Button btRoute;
    private BusinessAdapter adapter;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;

    public GuideDetailFragment(GuideDataModel guideDataModel, FragmentManager fragmentManager, MainActivity mainActivity) {
        this.guideDataModel = guideDataModel;
        this.fragmentManager = fragmentManager;
        this.mainActivity = mainActivity;
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
        btRoute = view.findViewById(R.id.btRoute);
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
                ProfileFragment profileFragment = new ProfileFragment(guideDataModel.getAuthor(), mainActivity);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.flContainer, profileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteFragment routeFragment = new RouteFragment((ArrayList<BusinessDataModel>) guideDataModel.getBusinessList());
                routeFragment.show(fragmentManager, "dialog");
            }
        });
    }
}
