package com.example.golocal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.golocal.R;

public class BusinessDetailFragment extends Fragment {

    private TextView tvBusinessTitle;
    private TextView tvBusinessAddress;
    private String businessTitle;
    private String businessAddress;

    public BusinessDetailFragment(String title, String address) {
        businessTitle = title;
        businessAddress = address;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_business_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBusinessTitle = view.findViewById(R.id.tvBusinessTitle);
        tvBusinessAddress = view.findViewById(R.id.tvBusinessAddress);
        tvBusinessTitle.setText(businessTitle);
        tvBusinessAddress.setText(businessAddress);
    }
}
