package com.example.golocal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
    private TextView tvUsername;
    private TextView tvBio;
    private ImageView ivProfileImage;
    private TextView tvEditBio;
    private EditText etBio;
    private Button btFinishEditBio;
    private ParseUser user;
    private final String KEY_BIO = "bio";
    private final String KEY_IMAGE = "profileImage";
    Context context;

    public ProfileFragment(ParseUser currentUser) {
        user = currentUser;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout) {
            userLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void userLogout() {
        ParseUser.logOutInBackground();
        Intent i = new Intent(getContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        tvUsername = view.findViewById(R.id.tvUsername);
        tvBio = view.findViewById(R.id.tvBio);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvEditBio = view.findViewById(R.id.tvEditBio);
        etBio = view.findViewById(R.id.etBio);
        btFinishEditBio = view.findViewById(R.id.btFinishEditBio);

        tvUsername.setText(user.getString("username"));
        tvBio.setText(user.getString("bio"));
        ParseFile image = user.getParseFile(KEY_IMAGE);
        if (image != null) {
            Glide.with(this).load(image.getUrl()).circleCrop().into(ivProfileImage);
        }

        tvEditBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBio.setVisibility(View.GONE);
                etBio.setVisibility(View.VISIBLE);
                etBio.setText(tvBio.getText().toString());
                tvEditBio.setVisibility(View.GONE);
                btFinishEditBio.setVisibility(View.VISIBLE);
                btFinishEditBio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.put(KEY_BIO, etBio.getText().toString());
                        tvBio.setText(user.getString(KEY_BIO));
                        tvBio.setVisibility(View.VISIBLE);
                        etBio.setVisibility(View.GONE);
                        tvEditBio.setVisibility(View.VISIBLE);
                        btFinishEditBio.setVisibility(View.GONE);
                    }
                });
            }
        });

    }
}
