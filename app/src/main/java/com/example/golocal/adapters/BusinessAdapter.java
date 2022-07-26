package com.example.golocal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golocal.R;
import com.example.golocal.models.BusinessDataModel;

import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {

    private Context context;
    private List<BusinessDataModel> businessDataModelList;

    public BusinessAdapter(Context context, List<BusinessDataModel> businessDataModels) {
        this.context = context;
        businessDataModelList = businessDataModels;
    }

    @NonNull
    @Override
    public BusinessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_business, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessAdapter.ViewHolder holder, int position) {
        BusinessDataModel businessDataModel = businessDataModelList.get(position);
        holder.bind(businessDataModel);
    }

    public void clear() {
        businessDataModelList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<BusinessDataModel> businessDataModels) {
        businessDataModelList.addAll(businessDataModels);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (businessDataModelList == null) {
            return 0;
        }
        return businessDataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitleList;
        private TextView tvDescriptionList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleList = itemView.findViewById(R.id.tvTitleList);
            tvDescriptionList = itemView.findViewById(R.id.tvDescriptionList);
        }

        public void bind(BusinessDataModel businessDataModel) {
            tvTitleList.setText(businessDataModel.getName());
            tvDescriptionList.setText(businessDataModel.getDescription());
        }
    }
}
