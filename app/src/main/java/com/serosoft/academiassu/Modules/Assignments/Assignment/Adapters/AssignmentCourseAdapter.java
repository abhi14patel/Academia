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
import com.serosoft.academiassu.Modules.Assignments.Assignment.AssignmentCourseListActivity;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.AssignmentClub_Dto;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.Assignment_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on October 31 2019.
 */

public class AssignmentCourseAdapter extends RecyclerView.Adapter<AssignmentCourseAdapter.MyViewHolder>
{
    Context context;
    ArrayList<AssignmentClub_Dto> assignmentClubList;

    public AssignmentCourseAdapter(Context context, ArrayList<AssignmentClub_Dto> assignmentClubList) {
        this.context = context;
        this.assignmentClubList = assignmentClubList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_course_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AssignmentClub_Dto list = assignmentClubList.get(position);

        String name = ProjectUtils.getCorrectedString(list.getCourseName());
        if(!name.equalsIgnoreCase(""))
        {
            holder.tvCourseName.setText(name);
        }

        ArrayList<Assignment_Dto> assignmentList = list.getAssignmentList();
        if(assignmentList != null && assignmentList.size() > 0)
        {
            int count = assignmentList.size();
            holder.tvCount.setText(""+count);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, AssignmentCourseListActivity.class);
                intent.putExtra(Consts.ASSIGNMENT_LIST,list);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return (null != assignmentClubList ? assignmentClubList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvCourseName,tvCount;
        private LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCount = itemView.findViewById(R.id.tvCount);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
