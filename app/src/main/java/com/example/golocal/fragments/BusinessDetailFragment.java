package com.example.golocal.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.golocal.R;
import com.example.golocal.models.BusinessDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BusinessDetailFragment extends Fragment {

    private TextView tvBusinessTitle;
    private TextView tvBusinessAddress;
    private TextView tvBusinessDescription;
    private ImageView ivBusinessImage;
    private BusinessDataModel businessDataModel;
    private final String PLACES_URL = "https://api.foursquare.com/v3/places/";
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
        PlaceQueryCall call = new PlaceQueryCall();
        call.execute(foursquareId);

        tvBusinessTitle = view.findViewById(R.id.tvBusinessTitle);
        tvBusinessAddress = view.findViewById(R.id.tvBusinessAddress);
        tvBusinessDescription = view.findViewById(R.id.tvBusinessDescription);
        ivBusinessImage = view.findViewById(R.id.ivBusinessImage);
        tvBusinessTitle.setText(businessDataModel.getName());
        tvBusinessAddress.setText(businessDataModel.getAddress());

    }

    private class PlaceQueryCall extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Request request = new Request.Builder()
                    .url(PLACES_URL + params[0] + "?fields=description,photos")
                    .get()
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", getResources().getString(R.string.foursquare_api_key))
                    .build();


            Response response;
            String results;
            try {
                response = client.newCall(request).execute();
                results = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return results;
        }

        protected void onPostExecute(String results) {
            try {
                setFields(results);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setFields(String data) throws JSONException {
        JSONObject queryResponse = new JSONObject(data);
        String businessDescription = queryResponse.getString("description");
        tvBusinessDescription.setText(businessDescription);
        JSONArray photos = queryResponse.getJSONArray("photos");
        if (photos.getJSONObject(0) != null) {
            JSONObject businessPhoto = photos.getJSONObject(0);
            String prefix = businessPhoto.getString("prefix");
            String suffix = businessPhoto.getString("suffix");
            String dimensions = Integer.valueOf(screenWidth) + "x" + Integer.valueOf(screenWidth);
            String imageUrl = prefix + dimensions + suffix;
            Glide.with(this).load(imageUrl).override(screenWidth, screenWidth).into(ivBusinessImage);
            Log.i("DetailFragment", imageUrl);
        } else {
            ivBusinessImage.setVisibility(View.GONE);
        }
    }
}
