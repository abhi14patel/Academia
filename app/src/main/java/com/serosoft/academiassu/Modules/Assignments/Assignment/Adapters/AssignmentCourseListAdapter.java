package com.serosoft.academiassu.Modules.Assignments.Assignment.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Assignments.Assignment.AssignmentDetailActivity;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.Assignment_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on October 31 2019.
 */

public class AssignmentCourseListAdapter extends RecyclerView.Adapter<AssignmentCourseListAdapter.MyViewHolder>{

    Context context;
    ArrayList<Assignment_Dto> assignmentList;
    TranslationManager translationManager;

    public AssignmentCourseListAdapter(Context context, ArrayList<Assignment_Dto> assignmentList) {
        this.context = context;
        this.assignmentList = assignmentList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_course_list_items, parent, false);

        return new MyViewHolder(view);
    }

    public void updateResults(ArrayList<Assignment_Dto> results)
    {
        assignmentList = results;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Assignment_Dto list = assignmentList.get(position);

        String assignmentName = ProjectUtils.getCorrectedString(list.getAssignmentName());
        if(!assignmentName.equalsIgnoreCase(""))
        {
            holder.tvAssignment.setText(assignmentName);
        }

        String facultyName = ProjectUtils.getCorrectedString(list.getFacultyName());
        if(!facultyName.equalsIgnoreCase(""))
        {
            holder.tvFacultyName.setText(translationManager.FACULTY_KEY+" : "+facultyName);
        }

        long longDate = list.getSubmission_last_date_long();
        String date = ProjectUtils.convertTimestampToDate(longDate,context);
        String time = ProjectUtils.convertTimestampToTime(longDate,context);

        if(!date.equalsIgnoreCase(""))
        {
            holder.tvDueDate.setText(translationManager.DUE_ON_KEY+" : "+date+" "+time);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AssignmentDetailActivity.class);
                intent.putExtra(Consts.ASSIGNMENT_LIST,list);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return (null != assignmentList ? assignmentList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvAssignment,tvFacultyName,tvDueDate;
        private LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvAssignment = itemView.findViewById(R.id.tvAssignment);
            tvFacultyName = itemView.findViewById(R.id.tvFacultyName);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
