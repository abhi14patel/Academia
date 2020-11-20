package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.FeePayers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.databinding.FeePayerItemsBinding;

import java.util.ArrayList;

/**
 * Created by Abhishek 08/10/20
 * View Binding
 */

public class FeePayerAdapter extends RecyclerView.Adapter<FeePayerAdapter.MyViewHolder> {

    Context context;
    ArrayList<FeePayer_Dto> feePayerList;
    private OnItemClickListener mItemClickListener;
    String emailId = "-";

    public FeePayerAdapter(Context context, ArrayList<FeePayer_Dto> feePayerList){

        this.context = context;
        this.feePayerList = feePayerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        FeePayerItemsBinding feePayerItemsBinding = FeePayerItemsBinding.inflate(layoutInflater,parent,false);

        return new MyViewHolder(feePayerItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(feePayerList.get(position));
    }

    @Override
    public int getItemCount() {
        return feePayerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        FeePayerItemsBinding binding;

        public MyViewHolder(@NonNull FeePayerItemsBinding feePayerItemsBinding){
            super(feePayerItemsBinding.getRoot());

            this.binding = feePayerItemsBinding;

            View view = binding.getRoot();
            view.setOnClickListener(this);
        }

        public void bind(FeePayer_Dto item) {

            String payerName = ProjectUtils.getCorrectedString(item.getPayerName());
            binding.tvName.setText(payerName);

            String mobileNumber = ProjectUtils.getCorrectedString(item.getMobileNumber());
            binding.tvMobileNumber.setText(mobileNumber);

            String type = ProjectUtils.getCorrectedString(item.getPayerType());
            binding.tvFeePayerType.setText(ProjectUtils.capitalize(type));

            if(type.equalsIgnoreCase("Person")){
                emailId = ProjectUtils.getCorrectedString(item.getEmailHome());
            }else{
                emailId = ProjectUtils.getCorrectedString(item.getEmailWork());
            }
            binding.tvEmail.setText(emailId);

        }

        @Override
        public void onClick(View v) {

            if(mItemClickListener != null)
            {
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener)
    {
        this.mItemClickListener = mItemClickListener;
    }
}
