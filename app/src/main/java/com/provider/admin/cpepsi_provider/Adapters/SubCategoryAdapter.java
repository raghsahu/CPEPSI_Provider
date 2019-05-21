package com.provider.admin.cpepsi_provider.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.provider.admin.cpepsi_provider.Model.SubCategoryModel;
import com.provider.admin.cpepsi_provider.R;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SubCategoryModel> subjectWithList;

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryModel> subcatlist) {
        mContext = context;
        this.subjectWithList = subcatlist;
    }


    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcat_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SubCategoryAdapter.ViewHolder holder, int position) {
        SubCategoryModel subCategoryModel = subjectWithList.get(position);
        holder.txtView.setText(subCategoryModel.getServiceSubCategory());
        holder.serviceCharge.setText(subCategoryModel.getServiceCharge());
        holder.checkBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(subjectWithList.get(position).isSelected());
        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(subjectWithList.get(position).isSelected());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                subjectWithList.get(holder.getAdapterPosition()).setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectWithList.size();
    }

    public List<SubCategoryModel> getItemDetailList() {
        return subjectWithList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtView,serviceCharge;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            txtView = itemView.findViewById(R.id.txtId);
            serviceCharge = itemView.findViewById(R.id.serviceCharge);
            checkBox = itemView.findViewById(R.id.subCheckBox);
            this.setIsRecyclable(false);
            /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (isChecked) {
                        subjectWithList.get(position).setSelected(true);
                    } else {
                        subjectWithList.get(position).setSelected(false);
                    }
                }
            });*/
        }
    }
}
