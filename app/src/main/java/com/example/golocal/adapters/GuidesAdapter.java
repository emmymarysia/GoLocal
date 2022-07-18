package com.example.golocal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golocal.fragments.GuideDetailFragment;
import com.example.golocal.activities.MainActivity;
import com.example.golocal.R;
import com.example.golocal.models.GuideDataModel;

import java.util.List;

public class GuidesAdapter extends RecyclerView.Adapter<GuidesAdapter.ViewHolder> {

    private Context context;
    private List<GuideDataModel> guideDataModels;
    private MainActivity mainActivity;

    public GuidesAdapter(Context context, List<GuideDataModel> guideDataModels, MainActivity main) {
        this.context = context;
        this.guideDataModels = guideDataModels;
        mainActivity = main;
    }

    @NonNull
    @Override
    public GuidesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuidesAdapter.ViewHolder holder, int position) {
        GuideDataModel guideDataModel = guideDataModels.get(position);
        holder.bind(guideDataModel);
    }

    @Override
    public int getItemCount() {
        return guideDataModels.size();
    }

    public void clear() {
        guideDataModels.clear();
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
                GuideDataModel guideDataModel = guideDataModels.get(position);
                Fragment fragment = new GuideDetailFragment(guideDataModel, mainActivity.fragmentManager);
                FragmentTransaction fragmentTransaction = mainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }
}
