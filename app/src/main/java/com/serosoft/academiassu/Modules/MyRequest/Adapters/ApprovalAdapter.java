package com.serosoft.academiassu.Modules.MyRequest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Helpers.StatusClass;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.MyRequest.Models.Approval_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on July 22 2020.
 */

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.MyViewHolder>
{
    Context context;
    ArrayList<Approval_Dto> approvalList;
    TranslationManager translationManager;
    int color;

    public ApprovalAdapter(Context context, ArrayList<Approval_Dto> approvalList) {
        this.context = context;
        this.approvalList = approvalList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approval_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Approval_Dto list = approvalList.get(position);

        String status = ProjectUtils.getCorrectedString(list.getActionType());
        if(!status.equalsIgnoreCase("")){

            if(status.equalsIgnoreCase(StatusClass.ASSIGNED)){
                color = context.getResources().getColor(R.color.colorNeutral);
            }else if(status.equalsIgnoreCase(StatusClass.APPROVED)){
                color = context.getResources().getColor(R.color.colorPositive);
            }else if(status.equalsIgnoreCase(StatusClass.WITHDRAWN)){
                color = context.getResources().getColor(R.color.colorNegative);
            }else if(status.equalsIgnoreCase(StatusClass.CLOSED)){
                color = context.getResources().getColor(R.color.colorPositive);
            }else if(status.equalsIgnoreCase(StatusClass.CANCELLED)){
                color = context.getResources().getColor(R.color.colorNegative);
            }else if(status.equalsIgnoreCase(StatusClass.COMPLETED)){
                color = context.getResources().getColor(R.color.colorPositive);
            }else if(status.equalsIgnoreCase(StatusClass.ESCALATED)){
                color = context.getResources().getColor(R.color.colorNegative);
            }else if(status.equalsIgnoreCase(StatusClass.GIVEN)){
                color = context.getResources().getColor(R.color.colorNeutral);
            }else if(status.equalsIgnoreCase(StatusClass.REJECTED)){
                color = context.getResources().getColor(R.color.colorNegative);
            }else if(status.equalsIgnoreCase(StatusClass.SUBMITTED)){
                color = context.getResources().getColor(R.color.colorNeutral);
            }else{
                color = context.getResources().getColor(R.color.colorNeutral);
            }
            holder.tvActionType.setText(status);
            holder.tvActionType.setTextColor(color);
        }

        String remark = ProjectUtils.getCorrectedString(list.getRemarks());
        if(!remark.equalsIgnoreCase("")){
            holder.tvRemark.setText(remark);
        }


        long aDate = list.getActionDate();
        if(aDate != 0){
            String actualDate = ProjectUtils.convertTimestampToDate(aDate,context);
            holder.tvDOA.setText(actualDate);
        }else{
            holder.tvDOA.setText("-");
        }

        String assignedUser = ProjectUtils.getCorrectedString(list.getAssignedUser());
        if(!assignedUser.equalsIgnoreCase("")){
            holder.tvAssignedUser.setText(assignedUser);
        }else{
            holder.tvAssignedUser.setText("-");
        }

    }

    @Override
    public int getItemCount() {

        return (null != approvalList ? approvalList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvRemark1,tvAssignedUser1;
        private TextView tvDOA,tvAssignedUser,tvActionType,tvRemark;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvRemark1 = itemView.findViewById(R.id.tvRemark1);
            tvAssignedUser1 = itemView.findViewById(R.id.tvAssignedUser1);

            tvDOA = itemView.findViewById(R.id.tvDOA);
            tvAssignedUser = itemView.findViewById(R.id.tvAssignedUser);
            tvActionType = itemView.findViewById(R.id.tvActionType);
            tvRemark = itemView.findViewById(R.id.tvRemark);

            tvRemark1.setText(translationManager.REMARK_KEY);
            tvAssignedUser1.setText(translationManager.ASSIGNED_USER_KEY+":");
        }
    }
}
