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
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDiaryClub_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDiary_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.SessionDiaryCourseListActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;
import java.util.ArrayList;

/**
 * Created by Abhishek on October 24 2019.
 */

public class SessionDiaryCourseAdapter extends RecyclerView.Adapter<SessionDiaryCourseAdapter.MyViewHolder>{

    Context context;
    ArrayList<SessionDiaryClub_Dto> sessionDiaryClubList;

    public SessionDiaryCourseAdapter(Context context, ArrayList<SessionDiaryClub_Dto> sessionDiaryClubList)
    {
        this.context = context;
        this.sessionDiaryClubList = sessionDiaryClubList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.session_diary_course_items, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i)
    {
        SessionDiaryClub_Dto list = sessionDiaryClubList.get(i);

        String name = ProjectUtils.getCorrectedString(list.getCourseName());
        if(!name.equalsIgnoreCase(""))
        {
            holder.tvCourseName.setText(name);
        }

        ArrayList<SessionDiary_Dto> sessionDiaryList = list.getSessionDiaryList();
        if(sessionDiaryList != null && sessionDiaryList.size() > 0)
        {
            int count = sessionDiaryList.size();
            holder.tvCount.setText(""+count);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SessionDiaryCourseListActivity.class);
                intent.putExtra(Consts.SESSION_DIARY_LIST,list);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return (null != sessionDiaryClubList ? sessionDiaryClubList.size() : 0);
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
