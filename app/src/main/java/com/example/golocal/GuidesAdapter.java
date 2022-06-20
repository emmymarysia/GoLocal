package com.example.golocal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GuidesAdapter extends RecyclerView.Adapter<GuidesAdapter.ViewHolder> {

    private Context context;
    private List<Guide> guides;

    public GuidesAdapter(Context context, List<Guide> guides) {
        this.context = context;
        this.guides = guides;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Guide guide) {
        }
    }
}
