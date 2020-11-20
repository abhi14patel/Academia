package com.serosoft.academiassu.Modules.Attendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Modules.Attendance.Models.AttendanceType_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class MultipleAttendanceTypeAdapter extends RecyclerView.Adapter<MultipleAttendanceTypeAdapter.MyViewHolder> {

    Context context;
    ArrayList<AttendanceType_Dto> attendanceTypeList;
    private OnItemClickListener mItemClickListener;

    String attendanceType = "";

    public MultipleAttendanceTypeAdapter(Context context, ArrayList<AttendanceType_Dto> attendanceTypeList,String attendanceType) {
        this.context = context;
        this.attendanceTypeList = attendanceTypeList;
        this.attendanceType = attendanceType;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_main_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AttendanceType_Dto list = attendanceTypeList.get(position);

        String facultyName = ProjectUtils.getCorrectedString(list.getFacultyName());

        if(attendanceType.equalsIgnoreCase(KEYS.COMPLETE_DAY) || attendanceType.equalsIgnoreCase(KEYS.MULTIPLE_SESSION)){

            String programName = ProjectUtils.getCorrectedString(list.getProgramName());
            String sectionCode = ProjectUtils.getCorrectedString(list.getSectionCode());
            int totalRecords = list.getTotalRecords();
            int presentRecords = list.getPresentRecords();

            holder.ivIcon.setImageResource(R.drawable.program_attendance_icon);
            holder.tvName.setText(programName+" - "+sectionCode);
            holder.tvFacultyName.setText(facultyName);
            holder.tvCount.setText(presentRecords + "/" + totalRecords);

        }else{

            String courseName = ProjectUtils.getCorrectedString(list.getCourseName());
            int totalRecords = list.getTotalRecords();
            int presentRecords = list.getPresentRecords();

            holder.ivIcon.setImageResource(R.drawable.course_attendance_icon);
            holder.tvName.setText(courseName);

            if(!facultyName.equalsIgnoreCase("")){
                holder.tvFacultyName.setText(facultyName);
            }else{
                holder.tvFacultyName.setVisibility(View.GONE);
            }

            holder.tvCount.setText(presentRecords + "/" + totalRecords);
        }
    }

    @Override
    public int getItemCount() {
        return (null != attendanceTypeList ? attendanceTypeList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tvName,tvFacultyName,tvCount;
        private ImageView ivIcon;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvFacultyName = itemView.findViewById(R.id.tvFacultyName);
            tvCount = itemView.findViewById(R.id.tvCount);
            ivIcon = itemView.findViewById(R.id.ivIcon);

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
