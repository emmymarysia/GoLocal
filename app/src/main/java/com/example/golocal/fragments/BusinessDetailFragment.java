package com.example.golocal.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.golocal.AsyncTasks.PlacesAsyncCall;
import com.example.golocal.R;
import com.example.golocal.models.BusinessDataModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Function;
import okhttp3.OkHttpClient;

public class BusinessDetailFragment extends Fragment {

    private final String PLACES_URL = "https://api.foursquare.com/v3/places/";

    private TextView tvBusinessTitle;
    private TextView tvBusinessAddress;
    private TextView tvBusinessDescription;
    private ImageView ivBusinessImage;
    private BusinessDataModel businessDataModel;
    private OkHttpClient client = new OkHttpClient();
    private int screenWidth;

    public BusinessDetailFragment(BusinessDataModel clickedBusiness) {
        this.businessDataModel = clickedBusiness;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_business_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        String foursquareId = businessDataModel.getFoursquareId();
        tvBusinessTitle = view.findViewById(R.id.tvBusinessTitle);
        tvBusinessAddress = view.findViewById(R.id.tvBusinessAddress);
        tvBusinessDescription = view.findViewById(R.id.tvBusinessDescription);
        ivBusinessImage = view.findViewById(R.id.ivBusinessImage);
        Function<String, Void> postExecuteMethod = this::setFields;
        PlacesAsyncCall call = new PlacesAsyncCall(postExecuteMethod);
        call.setViewFields(tvBusinessDescription, ivBusinessImage, screenWidth, getContext());
        call.execute(foursquareId, getString(R.string.foursquare_api_key), PlacesAsyncCall.FROM_DETAIL_FRAGMENT);

        tvBusinessTitle.setText(businessDataModel.getName());
        tvBusinessAddress.setText(businessDataModel.getAddress());

    }

    private Void setFields(String data) {
        this.tvBusinessDescription = tvBusinessDescription;
        this.ivBusinessImage = ivBusinessImage;
        this.screenWidth = screenWidth;
        JSONObject queryResponse = null;
        try {
            queryResponse = new JSONObject(data);
            String businessDescription = queryResponse.optString("description");
            if (businessDescription != null) {
                tvBusinessDescription.setText(businessDescription);
            } else {
                tvBusinessDescription.setVisibility(View.GONE);
            }
            JSONArray photos = queryResponse.getJSONArray("photos");
            if (photos.getJSONObject(0) != null) {
                JSONObject businessPhoto = photos.getJSONObject(0);
                String prefix = businessPhoto.getString("prefix");
                String suffix = businessPhoto.getString("suffix");
                String dimensions = Integer.valueOf(screenWidth) + "x" + Integer.valueOf(screenWidth);
                String imageUrl = prefix + dimensions + suffix;
                Glide.with(getContext()).load(imageUrl).override(screenWidth, screenWidth).into(ivBusinessImage);
            } else {
                ivBusinessImage.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
