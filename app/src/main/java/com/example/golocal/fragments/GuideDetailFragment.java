package com.example.golocal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golocal.R;
import com.example.golocal.adapters.BusinessAdapter;
import com.example.golocal.models.GuideDataModel;

public class GuideDetailFragment extends Fragment {

    private GuideDataModel guideDataModel;
    private TextView tvTitleDetail;
    private TextView tvAuthorDetail;
    private TextView tvDescriptionDetail;
    private RecyclerView rvBusinessesDetail;
    private BusinessAdapter adapter;

    public GuideDetailFragment(GuideDataModel guideDataModel) {
        this.guideDataModel = guideDataModel;
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
    }
}
