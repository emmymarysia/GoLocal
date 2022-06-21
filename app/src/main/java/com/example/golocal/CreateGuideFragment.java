package com.example.golocal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CreateGuideFragment extends Fragment {
    private EditText etTitle;
    private EditText etDescription;
    private RecyclerView rvBusinesses;
    private Button btAddBusiness;
    private Button btFinishCompose;
    private EditText etName;
    private EditText etBusinessDescription;
    private Button btAdd;
    Guide guide;
    List<Business> businessList;
    MainActivity mainActivity;
    BusinessAdapter adapter;
    private final String TAG = "CreateGuideFragment";

    public CreateGuideFragment(MainActivity main) {
        mainActivity = main;
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
        btAddBusiness = view.findViewById(R.id.btAddBusiness);
        btFinishCompose = view.findViewById(R.id.btFinishCompose);
        etName = view.findViewById(R.id.etName);
        etBusinessDescription = view.findViewById(R.id.etBusinessDescription);
        btAdd = view.findViewById(R.id.btAdd);

        adapter = new BusinessAdapter(getContext(), businessList);
        rvBusinesses.setAdapter(adapter);
        rvBusinesses.setLayoutManager(new LinearLayoutManager(getContext()));

        btAddBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setVisibility(View.VISIBLE);
                etBusinessDescription.setVisibility(View.VISIBLE);
                btAdd.setVisibility(View.VISIBLE);
                btAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etName.getText().toString();
                        String description = etBusinessDescription.getText().toString();
                        Business business = new Business();
                        business.setName(name);
                        business.setDescription(description);
                        businessList.add(business);
                        business.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error while saving", e);
                                }
                            }
                        });
                        etName.setText("");
                        etBusinessDescription.setText("");
                        etName.setVisibility(View.GONE);
                        etBusinessDescription.setVisibility(View.GONE);
                        btAdd.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        btFinishCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guide.setTitle(etTitle.getText().toString());
                guide.setDescription(etDescription.getText().toString());
                guide.setAuthor(ParseUser.getCurrentUser());
                guide.setBusinessList(businessList);
                guide.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving", e);
                        }
                        etTitle.setText("");
                        etDescription.setText("");
                        mainActivity.guidesFragment.adapter.clear();
                        //mainActivity.guidesFragment.queryGuides();
                    }
                });

                Fragment fragment = mainActivity.guidesFragment;
                mainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();

            }
        });
    }
}
