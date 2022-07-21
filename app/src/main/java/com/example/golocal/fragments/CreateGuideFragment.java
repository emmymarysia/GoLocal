package com.example.golocal.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golocal.AsyncTasks.PlacesAsyncCall;
import com.example.golocal.AsyncTasks.SearchAsyncCall;
import com.example.golocal.R;
import com.example.golocal.activities.MainActivity;
import com.example.golocal.adapters.BusinessAdapter;
import com.example.golocal.models.BusinessDataModel;
import com.example.golocal.models.GuideDataModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CreateGuideFragment extends Fragment {

    private final String TAG = "CreateGuideFragment";
    private final String PLACES_SEARCH_URL = "https://api.foursquare.com/v3/places/search";

    private EditText etTitle;
    private EditText etDescription;
    private RecyclerView rvBusinesses;
    private Button btAddBusiness;
    private Button btFinishCompose;
    private EditText etName;
    private EditText etBusinessDescription;
    private Button btAdd;
    private GuideDataModel guideDataModel;
    private List<BusinessDataModel> businessDataModelList;
    private MainActivity mainActivity;
    private BusinessAdapter adapter;
    private SearchAsyncCall call;

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

        Function<String, Void> postExecuteMethod = this::parseBusinessFromJson;
        call = new SearchAsyncCall(postExecuteMethod);
        guideDataModel = new GuideDataModel();
        businessDataModelList = new ArrayList<>();
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        rvBusinesses = view.findViewById(R.id.rvBusinesses);
        btAddBusiness = view.findViewById(R.id.btAddBusiness);
        btFinishCompose = view.findViewById(R.id.btFinishCompose);
        etName = view.findViewById(R.id.etName);
        etBusinessDescription = view.findViewById(R.id.etBusinessDescription);
        btAdd = view.findViewById(R.id.btAdd);

        adapter = new BusinessAdapter(getContext(), businessDataModelList);
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
                        String userLocation = mainActivity.getLocation();
                        call.execute(name, userLocation, getString(R.string.foursquare_api_key));
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
                guideDataModel.setTitle(etTitle.getText().toString());
                guideDataModel.setDescription(etDescription.getText().toString());
                guideDataModel.setAuthor(ParseUser.getCurrentUser());
                guideDataModel.setBusinessList(businessDataModelList);
                guideDataModel.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving", e);
                        }
                        etTitle.setText("");
                        etDescription.setText("");
                        mainActivity.guidesFragment.adapter.clear();
                    }
                });

                Fragment fragment = mainActivity.guidesFragment;
                mainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();

            }
        });
    }

    public Void parseBusinessFromJson(String response) {
        JSONObject queryResponse = null;
        try {
            String name = etName.getText().toString();
            String description = etBusinessDescription.getText().toString();
            etName.setText("");
            etBusinessDescription.setText("");
            BusinessDataModel businessDataModel = new BusinessDataModel();
            businessDataModel.setName(name);
            businessDataModel.setDescription(description);
            queryResponse = new JSONObject(response);
            JSONArray results = queryResponse.getJSONArray("results");
            JSONObject business = results.getJSONObject(0);
            String latitude = business.getJSONObject("geocodes").getJSONObject("main").getString("latitude");
            Log.e("latitude", latitude);
            String longitude = business.getJSONObject("geocodes").getJSONObject("main").getString("longitude");
            String address = business.getJSONObject("location").getString("address");
            businessDataModel.setLatitude(latitude);
            businessDataModel.setLongitude(longitude);
            businessDataModel.setAddress(address);
            businessDataModelList.add(businessDataModel);
            businessDataModel.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error while saving", e);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
