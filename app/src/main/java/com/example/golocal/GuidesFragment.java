package com.example.golocal;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuidesFragment extends Fragment {
    private RecyclerView rvGuides;
    List<Guide> guides;
    GuidesAdapter adapter;
    Context context;
    private final String TAG = "GuidesFragment";

    public GuidesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guides, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.i(TAG, "eh");
        inflater.inflate(R.menu.menu_guides, menu);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGuides = view.findViewById(R.id.rvGuides);
        guides = new ArrayList<>();
        context = getContext();
        adapter = new GuidesAdapter(context, guides);
        rvGuides.setAdapter(adapter);
        rvGuides.setLayoutManager(new LinearLayoutManager(context));
        Log.i(TAG, "hello!!!");
        queryGuides();

    }

    // TODO: change this so that it doesn't just show all guides
    private void queryGuides() {
        ParseQuery<Guide> query = ParseQuery.getQuery(Guide.class);
        query.include(Guide.KEY_AUTHOR);
        query.findInBackground(new FindCallback<Guide>() {
            @Override
            public void done(List<Guide> guidesList, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting posts", e);
                    return;
                }
                Log.i(TAG, "guidez");
                guides.addAll(guidesList);
                Collections.reverse(guides);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
