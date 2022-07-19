package com.example.golocal.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golocal.PriorityQueue;
import com.example.golocal.PriorityQueueNode;
import com.example.golocal.fragments.GuideDetailFragment;
import com.example.golocal.activities.MainActivity;
import com.example.golocal.R;
import com.example.golocal.models.GuideDataModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class GuidesAdapter extends RecyclerView.Adapter<GuidesAdapter.ViewHolder> {

    private Context context;
    private PriorityQueue guidesPriorityQueue;
    private MainActivity mainActivity;
    private final static String KEY_LIKED_GUIDES = "likedGuides";
    private final static String TAG = "guidesAdapter";
    private ArrayList<GuideDataModel> addedGuides = new ArrayList<>();

    public GuidesAdapter(Context context, PriorityQueue guidesPriorityQueue, MainActivity main) {
        this.context = context;
        this.guidesPriorityQueue = guidesPriorityQueue;
        mainActivity = main;
    }

    public void addAll(ArrayList<GuideDataModel> guidesInOrder) {
        this.addedGuides.addAll(guidesInOrder);
        this.notifyDataSetChanged();
    }



    @NonNull
    @Override
    public GuidesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuidesAdapter.ViewHolder holder, int position) {
        GuideDataModel guideDataModel = addedGuides.get(position);
        holder.bind(guideDataModel);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialAlertDialogBuilder(v.getContext())
                        .setTitle(guideDataModel.getTitle())
                        .setMessage(guideDataModel.getDescription())
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Favorite Guide", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                List<GuideDataModel> likedGuides = currentUser.getList(KEY_LIKED_GUIDES);
                                if (likedGuides == null) {
                                    likedGuides = new ArrayList<>();
                                }
                                boolean guideIsAlreadyLiked = false;
                                for (GuideDataModel guide: likedGuides) {
                                    if (guide.hasSameId(guideDataModel)) {
                                        guideIsAlreadyLiked = true;
                                    }
                                }
                                if (!guideIsAlreadyLiked) {
                                    likedGuides.add(guideDataModel);
                                }
                                currentUser.put(KEY_LIKED_GUIDES, likedGuides);
                                currentUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            Log.e(TAG, "Error saving user", e);
                                        }
                                    }
                                });
                            }
                        })
                        .show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return addedGuides.size();
    }

    public void clear() {
        guidesPriorityQueue.clear();
        addedGuides.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            itemView.setOnClickListener(this);
        }

        public void bind(GuideDataModel guideDataModel) {
            tvTitle.setText(guideDataModel.getTitle());
            tvAuthor.setText(guideDataModel.getAuthor().getUsername());
            tvDescription.setText(guideDataModel.getDescription());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                GuideDataModel guideDataModel = addedGuides.get(position);
                Fragment fragment = new GuideDetailFragment(guideDataModel, mainActivity.fragmentManager);
                FragmentTransaction fragmentTransaction = mainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }
}
