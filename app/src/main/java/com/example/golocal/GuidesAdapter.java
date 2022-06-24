package com.example.golocal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.List;

public class GuidesAdapter extends RecyclerView.Adapter<GuidesAdapter.ViewHolder> {

    private Context context;
    private List<Guide> guides;
    private MainActivity mainActivity;

    public GuidesAdapter(Context context, List<Guide> guides, MainActivity main) {
        this.context = context;
        this.guides = guides;
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
        Guide guide = guides.get(position);
        holder.bind(guide);
    }

    @Override
    public int getItemCount() {
        return guides.size();
    }

    public void clear() {
        guides.clear();
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

        public void bind(Guide guide) {
            tvTitle.setText(guide.getTitle());
            tvAuthor.setText(guide.getAuthor().getUsername());
            tvDescription.setText(guide.getDescription());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Guide guide = guides.get(position);
                Fragment fragment = new GuideDetailFragment(guide);
                FragmentTransaction fragmentTransaction = mainActivity.fragmentManager.beginTransaction().replace(R.id.flContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }
}
