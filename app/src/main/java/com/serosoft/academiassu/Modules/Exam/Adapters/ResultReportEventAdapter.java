package com.serosoft.academiassu.Modules.Exam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReportEvent_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on January 29 2020.
 */

public class ResultReportEventAdapter extends RecyclerView.Adapter<ResultReportEventAdapter.MyViewHolder>
{
    Context context;
    ArrayList<ResultReportEvent_Dto> resultReportEventList;
    TranslationManager translationManager;

    public ResultReportEventAdapter(Context context, ArrayList<ResultReportEvent_Dto> resultReportEventList) {
        this.context = context;
        this.resultReportEventList = resultReportEventList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_result_report_detail_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ResultReportEvent_Dto list = resultReportEventList.get(position);

        String programName = ProjectUtils.getCorrectedString(list.getTreeNode());
        if(!programName.equalsIgnoreCase("")){
            holder.tvProgramName.setText(programName);
        }

        String obtainedMarks = ProjectUtils.getCorrectedString(list.getObtainedMarksGrade());

        if(obtainedMarks.contains("/")){

            String[] separated = obtainedMarks.split("/");
            String str1 = separated[0];
            String str2 = separated[1];

            obtainedMarks = ("("+str1+")"+str2).trim();
        }

        String maxMarks = ProjectUtils.getCorrectedString(list.getMaxMarksOrGrade());
        if(!obtainedMarks.equalsIgnoreCase("") && !maxMarks.equalsIgnoreCase(""))
        {
            holder.tvMarks.setText(obtainedMarks+" / "+maxMarks);
        }

        double weightage = list.getWeightage();
        holder.tvWeightage.setText(""+weightage);

        String effectiveMarks = ProjectUtils.getCorrectedString(list.getEffetiveMarksGrade());
        if(!effectiveMarks.equalsIgnoreCase("")){
            holder.tvEffectiveMarks.setText(effectiveMarks);
        }

        String moderationMarks = ProjectUtils.getCorrectedString(list.getModerationPoints());
        if(!effectiveMarks.equalsIgnoreCase("")){
            holder.tvModerationMarks.setText(moderationMarks);
        }else{
            holder.tvModerationMarks.setText("-");
        }

        holder.ivArrow.setImageResource(R.drawable.arrow_red);
        holder.ivHeaderIndicator.setVisibility(View.INVISIBLE);

        holder.tvMarks1.setText(translationManager.OBTAINEDMARKS_KEY+" / "+translationManager.MAXMARKS_KEY);
        holder.tvWeightage1.setText(translationManager.WEIGHTAGE_KEY);
        holder.tvEffectiveMarks1.setText(translationManager.EFFECTIVEMARKS_KEY);
        holder.tvModerationMarks1.setText(translationManager.MODERATIONMARKS_KEY);
    }

    @Override
    public int getItemCount() {

        return (null != resultReportEventList ? resultReportEventList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvProgramName,tvMarks,tvWeightage,tvEffectiveMarks,tvModerationMarks;
        private TextView tvMarks1,tvMarks2,tvWeightage1,tvEffectiveMarks1,tvModerationMarks1;
        private AppCompatImageView ivArrow,ivHeaderIndicator;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvProgramName = itemView.findViewById(R.id.tvProgramName);
            tvMarks = itemView.findViewById(R.id.tvMarks);
            tvWeightage = itemView.findViewById(R.id.tvWeightage);
            tvEffectiveMarks = itemView.findViewById(R.id.tvEffectiveMarks);
            tvModerationMarks = itemView.findViewById(R.id.tvModerationMarks);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            ivHeaderIndicator = itemView.findViewById(R.id.ivHeaderIndicator);

            tvMarks1 = itemView.findViewById(R.id.tvMarks1);
            tvMarks2 = itemView.findViewById(R.id.tvMarks2);
            tvWeightage1 = itemView.findViewById(R.id.tvWeightage1);
            tvEffectiveMarks1 = itemView.findViewById(R.id.tvEffectiveMarks1);
            tvModerationMarks1 = itemView.findViewById(R.id.tvModerationMarks1);
        }
    }
}
