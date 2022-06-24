package com.example.golocal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    ParseUser currentUser;
    MapFragment mapFragment;
    GuidesFragment guidesFragment;
    ProfileFragment profileFragment;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = ParseUser.getCurrentUser();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        mapFragment = new MapFragment(this);
        guidesFragment = new GuidesFragment(this);
        profileFragment = new ProfileFragment(ParseUser.getCurrentUser());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_guides:
                        fragment = guidesFragment;
                        break;
                    case R.id.action_profile:
                        fragment = profileFragment;
                        break;
                    case R.id.action_map:
                    default:
                        fragment = mapFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_map);
    }
}