package com.serosoft.academiassu.Modules.MyRequest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Helpers.StatusClass;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.MyRequest.HostelLeaveRequestDetailsActivity;
import com.serosoft.academiassu.Modules.MyRequest.LeaveRequestDetailsActivity;
import com.serosoft.academiassu.Modules.MyRequest.Models.MyRequest_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on May 07 2020.
 */

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.MyViewHolder>
{
    Context context;
    ArrayList<MyRequest_Dto> myRequestList;
    TranslationManager translationManager;
    int color;

    public MyRequestAdapter(Context context, ArrayList<MyRequest_Dto> myRequestList) {
        this.context = context;
        this.myRequestList = myRequestList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myrequest_main_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MyRequest_Dto list = myRequestList.get(position);

        String requestId = ProjectUtils.getCorrectedString(list.getRequestId());
        if(!requestId.equalsIgnoreCase("")){
            holder.tvRequestId.setText(requestId);
        }

        String requestBy = ProjectUtils.getCorrectedString(list.getRequestBy());
        if(!requestBy.equalsIgnoreCase("")){
            holder.tvRequestBy.setText(requestBy);
        }

        String requestType = ProjectUtils.getCorrectedString(list.getRequestType());
        if(!requestType.equalsIgnoreCase("")){
            holder.tvRequestType.setText(requestType);
        }

        String assignedTo = ProjectUtils.getCorrectedString(list.getRequestAssignedTo());
        if(!assignedTo.equalsIgnoreCase("")){
            holder.tvAssignedTo.setText(assignedTo);
        }

        String date = ProjectUtils.getCorrectedString(list.getRequestDate());
        if(!date.equalsIgnoreCase("")){
            holder.tvDate.setText(date);
        }

        String status = ProjectUtils.getCorrectedString(list.getServiceRequestStatus());
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
            holder.tvStatus.setText(status);
            holder.tvStatus.setTextColor(color);
        }

        holder.cardPersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String requestCode = ProjectUtils.getCorrectedString(list.getRequestId());
                String requestId = ProjectUtils.getCorrectedString(""+list.getId());
                String status = ProjectUtils.getCorrectedString(list.getServiceRequestStatus());

                if(requestType.equalsIgnoreCase(Consts.LEAVE_REQUEST)){

                    Intent intent = new Intent(context, LeaveRequestDetailsActivity.class);
                    intent.putExtra(Consts.REQUEST_ID,requestId);
                    intent.putExtra(Consts.REQUEST_CODE,requestCode);
                    intent.putExtra(Consts.STATUS,status);
                    context.startActivity(intent);

                }else if(requestType.equalsIgnoreCase(Consts.HOSTEL_LEAVE_REQUEST)){

                    Intent intent = new Intent(context, HostelLeaveRequestDetailsActivity.class);
                    intent.putExtra(Consts.REQUEST_ID,requestId);
                    intent.putExtra(Consts.REQUEST_CODE,requestCode);
                    intent.putExtra(Consts.STATUS,status);
                    context.startActivity(intent);

                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return (null != myRequestList ? myRequestList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvRequestId1,tvRequestBy1,tvRequestType1,tvAssignedTo1;
        private TextView tvRequestId,tvRequestBy,tvRequestType,tvAssignedTo,tvDate,tvStatus;
        private CardView cardPersonalDetails;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            cardPersonalDetails = itemView.findViewById(R.id.cardPersonalDetails);
            tvRequestId1 = itemView.findViewById(R.id.tvRequestId1);
            tvRequestBy1 = itemView.findViewById(R.id.tvRequestBy1);
            tvRequestType1 = itemView.findViewById(R.id.tvRequestType1);
            tvAssignedTo1 = itemView.findViewById(R.id.tvAssignedTo1);

            tvRequestId = itemView.findViewById(R.id.tvRequestId);
            tvRequestBy = itemView.findViewById(R.id.tvRequestBy);
            tvRequestType = itemView.findViewById(R.id.tvRequestType);
            tvAssignedTo = itemView.findViewById(R.id.tvAssignedTo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            tvRequestId1.setText(translationManager.REQUEST_ID_KEY);
            tvRequestBy1.setText(translationManager.REQUEST_BY_KEY);
            tvRequestType1.setText(translationManager.REQUEST_TYPE_KEY);
            tvAssignedTo1.setText(translationManager.REQUEST_ASSIGNED_TO_KEY);
        }
    }
}
