package com.example.golocal.fragments;

import static android.app.Activity.RESULT_OK;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private static final int PICK_PHOTO_CODE = 1046;
    private final String photoFileName = "photo.jpg";
    private int shortAnimationDuration;

    private final String KEY_BIO = "bio";
    private static final String KEY_IMAGE = "profileImage";
    private static final String KEY_FRIENDS = "friends";
    private final String TAG = "ProfileFragment";

    private TextView tvUsername;
    private TextView tvBio;
    private ImageView ivProfileImage;
    private Button btEditBio;
    private EditText etBio;
    private Button btFinishEditBio;
    private ImageButton ibEditProfileImage;
    private ImageButton ibAddFriend;
    private ImageButton ibRemoveFriend;
    private ParseUser user;
    private Context context;
    private File photoFile;
    private HashSet<String> friendIds = new HashSet<>();

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
        btEditBio = view.findViewById(R.id.btEditBio);
        etBio = view.findViewById(R.id.etBio);
        btFinishEditBio = view.findViewById(R.id.btFinishEditBio);
        ibEditProfileImage = view.findViewById(R.id.ibEditProfileImage);
        ibAddFriend = view.findViewById(R.id.ibAddFriend);
        ibRemoveFriend = view.findViewById(R.id.ibRemoveFriend);

        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        List<ParseUser> friends = ParseUser.getCurrentUser().getList(KEY_FRIENDS);
        if (friends != null) {
            for (int i = 0; i < friends.size(); i++) {
                String id = friends.get(i).getObjectId();
                friendIds.add(id);
            }
        }

        if (!user.hasSameId(ParseUser.getCurrentUser())) {
            // if the user is different from the current user, remove profile customization buttons
            ibEditProfileImage.setVisibility(View.GONE);
            btEditBio.setVisibility(View.GONE);
            setAddFriendListener();
            setRemoveFriendListener();
            if (!friendIds.contains(user.getObjectId())) {
                ibAddFriend.setVisibility(View.VISIBLE);
            } else {
                ibRemoveFriend.setVisibility(View.VISIBLE);
            }
        }

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
                CharSequence[] items = {"Select from gallery", "Take a new photo", "Cancel"};

                new MaterialAlertDialogBuilder(context)
                        .setTitle("Edit Profile Image").setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        onPickPhoto(getView());
                                        break;
                                    case 1:
                                        launchCamera();
                                        break;
                                    case 2:
                                        dialog.dismiss();
                                }
                            }
                        }).show();
            }
        });

        btEditBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crossfade(tvBio, etBio);
                etBio.setText(tvBio.getText().toString());
                crossfade(btEditBio, btFinishEditBio);
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
                        crossfade(etBio, tvBio);
                        crossfade(btFinishEditBio, btEditBio);
                    }
                });
            }
        });
    }

    private void crossfade(View startingView, View endingView) {
        endingView.setAlpha(0f);
        endingView.setVisibility(View.VISIBLE);

        endingView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        startingView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startingView.setVisibility(View.GONE);
                    }
                });
    }

    private void setAddFriendListener() {
        ibAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                List<ParseUser> friends = currentUser.getList(KEY_FRIENDS);
                if (friends == null) {
                    friends = new ArrayList<>();
                }
                friends.add(user);
                friendIds.add(user.getObjectId());
                currentUser.put(KEY_FRIENDS, friends);
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "error while saving friends", e);
                        }
                    }
                });
                ibAddFriend.setVisibility(View.GONE);
                ibRemoveFriend.setVisibility(View.VISIBLE);
                ibAddFriend.setVisibility(View.GONE);
            }
        });
    }

    private void setRemoveFriendListener() {
        ibRemoveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                List<ParseUser> friends = currentUser.getList(KEY_FRIENDS);
                if (friends == null) {
                    friends = new ArrayList<>();
                }
                for (int i = 0; i < friends.size(); i++) {
                    if (friends.get(i).hasSameId(user)) {
                        friends.remove(friends.get(i));
                    }
                }
                friendIds.remove(user.getObjectId());
                currentUser.put(KEY_FRIENDS, friends);
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "error while saving friends", e);
                        }
                    }
                });
                ibAddFriend.setVisibility(View.VISIBLE);
                ibRemoveFriend.setVisibility(View.GONE);
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
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(takenImage, 100, 100, true);
                saveAndShowImage(resizedBitmap);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_PHOTO_CODE && (data != null)) {
            Uri photoUri = data.getData();
            Bitmap selectedImage = loadFromUri(photoUri);
            saveAndShowImage(selectedImage);
        }
    }

    private void saveAndShowImage(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapBytes = stream.toByteArray();
        user.put(KEY_IMAGE, new ParseFile(bitmapBytes));
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
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }
}
