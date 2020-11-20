package com.serosoft.academiassu.Modules.Exam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReport_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on January 20 2020.
 */

public class ResultReportAdapter1 extends RecyclerView.Adapter<ResultReportAdapter1.MyViewHolder>
{
    Context context;
    ArrayList<ResultReport_Dto> resultReportList;
    private OnItemClickListener mItemClickListener;
    TranslationManager translationManager;

    public ResultReportAdapter1(Context context, ArrayList<ResultReport_Dto> resultReportList) {
        this.context = context;
        this.resultReportList = resultReportList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_result_report_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ResultReport_Dto list = resultReportList.get(position);

        String nodeName = ProjectUtils.getCorrectedString(list.getTreeNode());
        if(!nodeName.equalsIgnoreCase(""))
        {
            holder.tvNodeName.setText(nodeName);
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

        holder.tvMarks1.setText(translationManager.OBTAINEDMARKS_KEY+" / "+translationManager.MAXMARKS_KEY);
    }

    @Override
    public int getItemCount() {

        return (null != resultReportList ? resultReportList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tvNodeName,tvMarks;
        private TextView tvMarks1;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvNodeName = itemView.findViewById(R.id.tvNodeName);
            tvMarks = itemView.findViewById(R.id.tvMarks);
            tvMarks1 = itemView.findViewById(R.id.tvMarks1);

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
