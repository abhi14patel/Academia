package com.serosoft.academiassu.Modules.Assignments.SessionDiary.Adapters;

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
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDiary_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.SessionDiaryDetailActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on October 24 2019.
 */

public class SessionDiaryCourseListAdapter extends RecyclerView.Adapter<SessionDiaryCourseListAdapter.MyViewHolder>{

    Context context;
    ArrayList<SessionDiary_Dto> sessionDiaryList;
    TranslationManager translationManager;

    public SessionDiaryCourseListAdapter(Context context, ArrayList<SessionDiary_Dto> sessionDiaryList) {
        this.context = context;
        this.sessionDiaryList = sessionDiaryList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_diary_course_list_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SessionDiary_Dto list = sessionDiaryList.get(position);

        String sessionNo = ProjectUtils.getCorrectedString(list.getSessionNo());
        if(!sessionNo.equalsIgnoreCase(""))
        {
            holder.tvSession.setText(translationManager.SESSION_NUMBER_KEY+" "+sessionNo);
        }

        String facultyName = ProjectUtils.getCorrectedString(list.getFacultyName());
        if(!facultyName.equalsIgnoreCase(""))
        {
            holder.tvFacultyName.setText(translationManager.FACULTY_KEY+" "+facultyName);
        }

        long longDate = list.getDate_long();
        String date = ProjectUtils.convertTimestampToDate(longDate,context);
        String time = ProjectUtils.convertTimestampToTime(longDate,context);

        if(!date.equalsIgnoreCase(""))
        {
            holder.tvPublishedDate.setText("Published Date : "+date);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SessionDiaryDetailActivity.class);
                intent.putExtra(Consts.SESSION_DIARY_LIST,list);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return (null != sessionDiaryList ? sessionDiaryList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvSession,tvFacultyName,tvPublishedDate;
        private LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvSession = itemView.findViewById(R.id.tvSession);
            tvFacultyName = itemView.findViewById(R.id.tvFacultyName);
            tvPublishedDate = itemView.findViewById(R.id.tvPublishedDate);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
