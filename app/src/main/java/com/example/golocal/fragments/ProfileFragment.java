package com.example.golocal.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.golocal.R;
import com.example.golocal.activities.LoginActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ProfileFragment extends Fragment {
    private TextView tvUsername;
    private TextView tvBio;
    private ImageView ivProfileImage;
    private TextView tvEditBio;
    private EditText etBio;
    private Button btFinishEditBio;
    private ImageButton ibEditProfileImage;
    private ParseUser user;
    private final String KEY_BIO = "bio";
    private final String KEY_IMAGE = "profileImage";
    private Context context;
    private final String TAG = "ProfileFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private File photoFile;
    public String photoFileName = "photo.jpg";

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
        ibEditProfileImage = view.findViewById(R.id.ibEditProfileImage);

        tvUsername.setText(user.getString("username"));
        tvBio.setText(user.getString("bio"));
        ParseFile image = user.getParseFile(KEY_IMAGE);
        if (image != null) {
            Glide.with(this).load(image.getUrl()).circleCrop().into(ivProfileImage);
        }

        ibEditProfileImage.setImageDrawable(getContext().getDrawable(R.drawable.add_button_outline_24));
        ibEditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

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
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "error updating bio", e);
                                }
                            }
                        });
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

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(takenImage, 100, 100, true);
                ivProfileImage.setImageBitmap(resizedBitmap);
                user.put(KEY_IMAGE, new ParseFile(photoFile));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving profile image", e);
                            Toast.makeText(context, "Error while saving profile image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Glide.with(this)
                        .load(user.getParseFile(KEY_IMAGE).getUrl())
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .circleCrop()
                        .into(ivProfileImage);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
