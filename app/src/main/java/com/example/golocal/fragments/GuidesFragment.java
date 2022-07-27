package com.example.golocal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golocal.PriorityQueue;
import com.example.golocal.R;
import com.example.golocal.activities.MainActivity;
import com.example.golocal.adapters.GuidesAdapter;
import com.example.golocal.models.GuideDataModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class GuidesFragment extends Fragment {

    private final String TAG = "GuidesFragment";

    private RecyclerView rvGuides;
    private PriorityQueue guidesPriorityQueue = new PriorityQueue();
    public GuidesAdapter adapter;
    private Context context;
    private MainActivity mainActivity;

    public GuidesFragment(MainActivity main) {
        mainActivity = main;
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
        inflater.inflate(R.menu.menu_guides, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.compose) {
            Fragment fragment = new CreateGuideFragment(mainActivity);
            mainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvGuides = view.findViewById(R.id.rvGuides);
        adapter = new GuidesAdapter(context, guidesPriorityQueue, mainActivity);
        context = getContext();
        rvGuides.setAdapter(adapter);
        rvGuides.setLayoutManager(new LinearLayoutManager(context));
        queryGuides();
    }

    public void queryGuides() {
        ParseQuery<GuideDataModel> query = ParseQuery.getQuery(GuideDataModel.class);
        query.include(GuideDataModel.KEY_AUTHOR);
        query.findInBackground(new FindCallback<GuideDataModel>() {
            @Override
            public void done(List<GuideDataModel> guidesList, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting posts", e);
                    return;
                }
                adapter.clear();
                try {
                    guidesPriorityQueue.insertAllGuides(guidesList);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

                ArrayList<GuideDataModel> guidesInPriorityOrder = new ArrayList<>();
                guidesInPriorityOrder.addAll(guidesPriorityQueue.getAllGuidesInOrder());

                adapter.addAll(guidesInPriorityOrder);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
