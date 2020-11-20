package com.serosoft.academiassu.Modules.MyRequest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.MyRequest.Models.FollowUp_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on May 07 2020.
 */

public class FollowUpAdapter extends RecyclerView.Adapter<FollowUpAdapter.MyViewHolder>
{
    Context context;
    ArrayList<FollowUp_Dto> followUpList;
    TranslationManager translationManager;

    public FollowUpAdapter(Context context, ArrayList<FollowUp_Dto> followUpList) {
        this.context = context;
        this.followUpList = followUpList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.followup_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        FollowUp_Dto list = followUpList.get(position);

        String academyLocation = ProjectUtils.getCorrectedString(list.getAcademyLocation());
        if(!academyLocation.equalsIgnoreCase("")){
            holder.tvAcademyLocation.setText(academyLocation);
        }

        String name = ProjectUtils.getCorrectedString(list.getName());
        if(!name.equalsIgnoreCase("")){
            holder.tvName.setText(name);
        }

        String remark = ProjectUtils.getCorrectedString(list.getRemarks());
        if(!remark.equalsIgnoreCase("")){
            holder.tvRemark.setText(remark);
        }

        long pDate = list.getPlannedDate();
        if(pDate != 0){
            String plannedDate = ProjectUtils.convertTimestampToDate(pDate,context);
            holder.tvPlannedDate.setText(plannedDate);
        }else{
            holder.tvPlannedDate.setText("-");
        }

        long aDate = list.getActualDate();
        if(aDate != 0){
            String actualDate = ProjectUtils.convertTimestampToDate(aDate,context);
            holder.tvActualDate.setText(actualDate);
        }else{
            holder.tvActualDate.setText("-");
        }

        long nDate = list.getNextDate();
        if(nDate != 0){
            String nextDate = ProjectUtils.convertTimestampToDate(nDate,context);
            holder.tvNextDate.setText(nextDate);
        }else{
            holder.tvNextDate.setText("-");
        }
    }

    @Override
    public int getItemCount() {

        return (null != followUpList ? followUpList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvAcademyLocation1,tvName1,tvPlannedDate1,tvActualDate1,tvNextDate1,tvRemark1;
        private TextView tvAcademyLocation,tvName,tvPlannedDate,tvActualDate,tvNextDate,tvRemark;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvAcademyLocation1 = itemView.findViewById(R.id.tvAcademyLocation1);
            tvName1 = itemView.findViewById(R.id.tvName1);
            tvPlannedDate1 = itemView.findViewById(R.id.tvPlannedDate1);
            tvActualDate1 = itemView.findViewById(R.id.tvActualDate1);
            tvNextDate1 = itemView.findViewById(R.id.tvNextDate1);
            tvRemark1 = itemView.findViewById(R.id.tvRemark1);

            tvAcademyLocation = itemView.findViewById(R.id.tvAcademyLocation);
            tvName = itemView.findViewById(R.id.tvName);
            tvPlannedDate = itemView.findViewById(R.id.tvPlannedDate);
            tvActualDate = itemView.findViewById(R.id.tvActualDate);
            tvNextDate = itemView.findViewById(R.id.tvNextDate);
            tvRemark = itemView.findViewById(R.id.tvRemark);

            tvRemark1.setText(translationManager.REMARK_KEY);
            tvName1.setText(context.getString(R.string.name));
        }
    }
}
