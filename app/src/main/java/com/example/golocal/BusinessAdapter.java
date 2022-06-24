package com.example.golocal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {

    private Context context;
    private List<Business> businessList;

    public BusinessAdapter(Context context, List<Business> businesses) {
        this.context = context;
        businessList = businesses;
    }

    @NonNull
    @Override
    public BusinessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_business, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessAdapter.ViewHolder holder, int position) {
        Business business = businessList.get(position);
        holder.bind(business);
    }

    public void clear() {
        businessList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Business> businesses) {
        businessList.addAll(businesses);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitleList;
        private TextView tvDescriptionList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleList = itemView.findViewById(R.id.tvTitleList);
            tvDescriptionList = itemView.findViewById(R.id.tvDescriptionList);
        }

        public void bind(Business business) {
            tvTitleList.setText(business.getName());
            tvDescriptionList.setText(business.getDescription());
        }
    }
}
