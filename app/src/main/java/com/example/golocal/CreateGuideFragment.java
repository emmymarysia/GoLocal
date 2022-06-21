package com.example.golocal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CreateGuideFragment extends Fragment {
    private EditText etTitle;
    private EditText etDescription;
    private RecyclerView rvBusinesses;
    private ConstraintLayout editBusinessLayout;
    private Button btAddBusiness;
    Guide guide;
    List<Business> businessList;

    public CreateGuideFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_guide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        guide = new Guide();
        businessList = new ArrayList<>();
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        rvBusinesses = view.findViewById(R.id.rvBusinesses);
        editBusinessLayout = view.findViewById(R.id.editBusinessLayout);
        btAddBusiness = view.findViewById(R.id.btAddBusiness);

        // TODO: add adapter for rvBusinesses

        btAddBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBusinessLayout.setVisibility(View.VISIBLE);
                EditText etName = v.findViewById(R.id.etName);
                EditText etDescription = v.findViewById(R.id.etBusinessDescription);
                Button btAdd = v.findViewById(R.id.btAdd);
                btAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etName.getText().toString();
                        String description = etName.getText().toString();
                        Business business = new Business();
                        business.setName(name);
                        business.setDescription(description);
                        businessList.add(business);
                    }
                });
            }
        });

    }
}
