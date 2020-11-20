package com.serosoft.academiassu.Modules.Exam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReportSubType_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReportType_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on January 29 2020.
 */

public class ResultReportTypeAdapter extends RecyclerView.Adapter<ResultReportTypeAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<ResultReportType_Dto> resultReportTypeList;
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();

    TranslationManager translationManager;

    LinearLayoutManager linearLayoutManager;
    private ArrayList<ResultReportSubType_Dto> resultReportSubTypeList;
    ResultReportSubTypeAdapter resultReportSubTypeAdapter;

    public ResultReportTypeAdapter(Context context, ArrayList<ResultReportType_Dto> resultReportTypeList) {
        this.context = context;
        this.resultReportTypeList = resultReportTypeList;
        expansionsCollection.openOnlyOne(false);
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

        holder.bind(resultReportTypeList.get(position));

        expansionsCollection.add(holder.getExpansionLayout());
    }

    @Override
    public int getItemCount() {

        return (null != resultReportTypeList ? resultReportTypeList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvProgramName,tvMarks,tvWeightage,tvEffectiveMarks,tvModerationMarks;
        private TextView tvMarks1,tvMarks2,tvWeightage1,tvEffectiveMarks1,tvModerationMarks1;
        private AppCompatImageView ivArrow,ivHeaderIndicator;
        private RecyclerView recyclerView;
        ExpansionLayout expansionLayout;

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
            expansionLayout = itemView.findViewById(R.id.expansionLayout);

            recyclerView = itemView.findViewById(R.id.recyclerView);
            linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(false);

            tvMarks1 = itemView.findViewById(R.id.tvMarks1);
            tvMarks2 = itemView.findViewById(R.id.tvMarks2);
            tvWeightage1 = itemView.findViewById(R.id.tvWeightage1);
            tvEffectiveMarks1 = itemView.findViewById(R.id.tvEffectiveMarks1);
            tvModerationMarks1 = itemView.findViewById(R.id.tvModerationMarks1);
        }

        public void bind(ResultReportType_Dto resultReportType_dto) {

            expansionLayout.collapse(false);

            String programName = ProjectUtils.getCorrectedString(resultReportType_dto.getTreeNode());
            if(!programName.equalsIgnoreCase("")){
                tvProgramName.setText(programName);
            }

            String obtainedMarks = ProjectUtils.getCorrectedString(resultReportType_dto.getObtainedMarksGrade());

            if(obtainedMarks.contains("/")){

                String[] separated = obtainedMarks.split("/");
                String str1 = separated[0];
                String str2 = separated[1];

                obtainedMarks = ("("+str1+")"+str2).trim();
            }

            String maxMarks = ProjectUtils.getCorrectedString(resultReportType_dto.getMaxMarksOrGrade());
            if(!obtainedMarks.equalsIgnoreCase("") && !maxMarks.equalsIgnoreCase(""))
            {
                tvMarks.setText(obtainedMarks+" / "+maxMarks);
            }

            double weightage = resultReportType_dto.getWeightage();
            tvWeightage.setText(""+weightage);

            String effectiveMarks = ProjectUtils.getCorrectedString(resultReportType_dto.getEffetiveMarksGrade());
            if(!effectiveMarks.equalsIgnoreCase("")){
                tvEffectiveMarks.setText(effectiveMarks);
            }

            String moderationMarks = ProjectUtils.getCorrectedString(resultReportType_dto.getModerationPoints());
            if(!effectiveMarks.equalsIgnoreCase("")){
                tvModerationMarks.setText(moderationMarks);
            }else{
                tvModerationMarks.setText("-");
            }

            ivArrow.setImageResource(R.drawable.arrow_blue);

            resultReportSubTypeList = new ArrayList<>();
            resultReportSubTypeList = resultReportType_dto.getSubTypeList();
            if(resultReportSubTypeList != null && resultReportSubTypeList.size() > 0){

                ivHeaderIndicator.setVisibility(View.VISIBLE);
                resultReportSubTypeAdapter = new ResultReportSubTypeAdapter(context,resultReportSubTypeList);
                recyclerView.setAdapter(resultReportSubTypeAdapter);
                resultReportSubTypeAdapter.notifyDataSetChanged();
            }else{
                ivHeaderIndicator.setVisibility(View.INVISIBLE);
            }

            tvMarks1.setText(translationManager.OBTAINEDMARKS_KEY+" / "+translationManager.MAXMARKS_KEY);
            tvWeightage1.setText(translationManager.WEIGHTAGE_KEY);
            tvEffectiveMarks1.setText(translationManager.EFFECTIVEMARKS_KEY);
            tvModerationMarks1.setText(translationManager.MODERATIONMARKS_KEY);
        }

        public ExpansionLayout getExpansionLayout() {
            return expansionLayout;
        }
    }
}
