package com.serosoft.academiassu.Modules.Exam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Exam.Models.Marksheet_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on October 31 2019.
 */

public class MarksheetAdapter extends RecyclerView.Adapter<MarksheetAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<Marksheet_Dto> marksheetList;
    SharedPrefrenceManager sharedPrefrenceManager;
    TranslationManager translationManager;
    private OnItemClickListener mItemClickListener;

    public MarksheetAdapter(Context context, ArrayList<Marksheet_Dto> marksheetList) {
        this.context = context;
        this.marksheetList = marksheetList;
        sharedPrefrenceManager = new SharedPrefrenceManager(context);
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        String academyType = sharedPrefrenceManager.getAcademyTypeFromKey();
        if(academyType.equalsIgnoreCase(Consts.SCHOOL)){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_marksheet_items2, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_marksheet_items, parent, false);
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Marksheet_Dto list = marksheetList.get(position);

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

        String sectionName = ProjectUtils.getCorrectedString(list.getSectionName());
        if(!sectionName.equalsIgnoreCase(""))
        {
            holder.tvSection.setText(sectionName);
        }

        String docFileName = ProjectUtils.getCorrectedString(list.getMarksheetPath());
        if(!docFileName.equalsIgnoreCase("")){

            holder.ivDocument.setImageResource(ProjectUtils.showDocIcon(docFileName));
        }

        holder.tvBatch1.setText(translationManager.BATCH_KEY);
        holder.tvSection1.setText(translationManager.SECTION_KEY);
        holder.tvPeriod1.setText(translationManager.PERIOD_KEY);
    }

    @Override
    public int getItemCount() {

        return (null != marksheetList ? marksheetList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tvProgramName,tvBatch,tvPeriod,tvSection;
        private TextView tvBatch1,tvPeriod1,tvSection1;
        private AppCompatImageView ivDocument;
        private RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvProgramName = itemView.findViewById(R.id.tvProgramName);
            tvBatch = itemView.findViewById(R.id.tvBatch);
            tvPeriod = itemView.findViewById(R.id.tvPeriod);
            tvSection = itemView.findViewById(R.id.tvSection);

            tvBatch1 = itemView.findViewById(R.id.tvBatch1);
            tvPeriod1 = itemView.findViewById(R.id.tvPeriod1);
            tvSection1 = itemView.findViewById(R.id.tvSection1);

            ivDocument = itemView.findViewById(R.id.ivDocument);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

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
