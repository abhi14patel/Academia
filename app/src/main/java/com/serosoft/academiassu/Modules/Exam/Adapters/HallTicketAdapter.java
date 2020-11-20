package com.serosoft.academiassu.Modules.Exam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Exam.Models.HallTicket_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on October 31 2019.
 */

public class HallTicketAdapter extends RecyclerView.Adapter<HallTicketAdapter.MyViewHolder>
{
    Context context;
    ArrayList<HallTicket_Dto> hallTicketList;
    SharedPrefrenceManager sharedPrefrenceManager;
    TranslationManager translationManager;
    String currencySymbol = "";
    private OnItemClickListener mItemClickListener;

    public HallTicketAdapter(Context context, ArrayList<HallTicket_Dto> hallTicketList) {
        this.context = context;
        this.hallTicketList = hallTicketList;
        sharedPrefrenceManager = new SharedPrefrenceManager(context);
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        String academyType = sharedPrefrenceManager.getAcademyTypeFromKey();
        if(academyType.equalsIgnoreCase(Consts.SCHOOL)){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_hall_ticket_items2, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_hall_ticket_items, parent, false);
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HallTicket_Dto list = hallTicketList.get(position);

        String hallTicket = ProjectUtils.getCorrectedString(list.getHallticket());
        if(!hallTicket.equalsIgnoreCase(""))
        {
            holder.tvHallTicketNo.setText(hallTicket);
        }

        String programName = ProjectUtils.getCorrectedString(list.getProgramName());
        if(!programName.equalsIgnoreCase(""))
        {
            holder.tvProgramName.setText(programName);
        }

        String batchName = ProjectUtils.getCorrectedString(list.getBatchName());
        if(!batchName.equalsIgnoreCase(""))
        {
            holder.tvBatch.setText(batchName);
        }

        String periodName = ProjectUtils.getCorrectedString(list.getPeriodName());
        if(!periodName.equalsIgnoreCase(""))
        {
            holder.tvPeriod.setText(periodName);
        }

        String assessmentGroup = ProjectUtils.getCorrectedString(list.getEvaluationGroupCode());
        if(!assessmentGroup.equalsIgnoreCase(""))
        {
            holder.tvAssessmentGroup.setText(assessmentGroup);
        }

        boolean showAmount = list.isShowAmount();
        if(showAmount){
            holder.llTotalOutstanding.setVisibility(View.VISIBLE);
            String curCode = ProjectUtils.getCorrectedString(list.getCurrencyCode());
            if(!curCode.equalsIgnoreCase("")){
                currencySymbol = BaseActivity.Utils.getCurrencySymbol(curCode);
            }else{
                SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
                currencySymbol = sharedPrefrenceManager.getCurrencySymbolFromKey();
            }
        }else{
            holder.llTotalOutstanding.setVisibility(View.GONE);
        }

        int totalOutstanding = list.getTotalOutstanding();
        if(totalOutstanding != -1)
        {
            holder.tvTotalOutstanding.setText(currencySymbol+totalOutstanding);
        }

        String docFileName = ProjectUtils.getCorrectedString(list.getHallTicketPath());
        if(!docFileName.equalsIgnoreCase("")){

            holder.ivDocument.setImageResource(ProjectUtils.showDocIcon(docFileName));
        }

        holder.tvBatch1.setText(translationManager.BATCH_KEY);
        holder.tvPeriod1.setText(translationManager.PERIOD_KEY);
        holder.tvHallTicketNo1.setText(translationManager.HALL_TICKETNO_KEY);
        holder.tvAssessmentGroup1.setText(translationManager.ASSESSEMENT_GROUP_KEY);
        holder.tvTotalOutstanding1.setText(translationManager.TOTAL_OUTSTANDING_KEY);
    }

    @Override
    public int getItemCount() {

        return (null != hallTicketList ? hallTicketList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tvHallTicketNo,tvProgramName,tvBatch,tvPeriod,tvAssessmentGroup,tvTotalOutstanding;
        private TextView tvHallTicketNo1,tvBatch1,tvPeriod1,tvAssessmentGroup1,tvTotalOutstanding1;
        private AppCompatImageView ivDocument;
        private LinearLayout llTotalOutstanding;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvProgramName = itemView.findViewById(R.id.tvProgramName);
            tvBatch = itemView.findViewById(R.id.tvBatch);
            tvPeriod = itemView.findViewById(R.id.tvPeriod);
            tvHallTicketNo = itemView.findViewById(R.id.tvHallTicketNo);
            tvAssessmentGroup = itemView.findViewById(R.id.tvAssessmentGroup);
            tvTotalOutstanding = itemView.findViewById(R.id.tvTotalOutstanding);

            tvHallTicketNo1 = itemView.findViewById(R.id.tvHallTicketNo1);
            tvBatch1 = itemView.findViewById(R.id.tvBatch1);
            tvPeriod1 = itemView.findViewById(R.id.tvPeriod1);
            tvAssessmentGroup1 = itemView.findViewById(R.id.tvAssessmentGroup1);
            tvTotalOutstanding1 = itemView.findViewById(R.id.tvTotalOutstanding1);

            ivDocument = itemView.findViewById(R.id.ivDocument);
            llTotalOutstanding = itemView.findViewById(R.id.llTotalOutstanding);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
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
