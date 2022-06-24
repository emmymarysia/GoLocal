package com.example.golocal;

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

public class GuideDetailFragment extends Fragment {

    private Guide guide;
    private TextView tvTitleDetail;
    private TextView tvAuthorDetail;
    private TextView tvDescriptionDetail;
    private RecyclerView rvBusinessesDetail;
    private BusinessAdapter adapter;

    public GuideDetailFragment(Guide guide) {
        this.guide = guide;
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
        adapter = new BusinessAdapter(getContext(), guide.getBusinessList());

        tvTitleDetail.setText(guide.getTitle());
        tvAuthorDetail.setText(guide.getAuthor().getUsername());
        tvDescriptionDetail.setText(guide.getDescription());
        rvBusinessesDetail.setAdapter(adapter);
        rvBusinessesDetail.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
