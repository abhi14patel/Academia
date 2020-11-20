package com.serosoft.academiassu.Modules.Attendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by opj1 on 26/08/2016.
 */
public class MultipleAttendanceDialogAdapter extends ArrayAdapter<JSONObject> {
    Context context;
    List<JSONObject> switchStudentList;
    String attendanceType;
    public TranslationManager translationManager;

    public MultipleAttendanceDialogAdapter(Context context, List<JSONObject> arrayList, String callBy) {
        super(context, 0, arrayList);
        this.context = context;
        this.switchStudentList = arrayList;
        translationManager = new TranslationManager(context);
    }

    public MultipleAttendanceDialogAdapter(Context context, List<JSONObject> arrayList, String callBy, String at) {
        super(context, 0, arrayList);
        this.context = context;
        this.switchStudentList = arrayList;
        this.attendanceType = at;
        translationManager = new TranslationManager(context);
    }

    @Override
    public int getCount() {
        return switchStudentList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return switchStudentList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            if (attendanceType.equals(KEYS.MULTIPLE_SESSION)) {
                convertView = LayoutInflater.from(context).inflate(R.layout.multiple_attendance_multiple_session_item, parent, false);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.multiple_attendance_course_level_item, parent, false);
            }
        }

        if (attendanceType.equals(KEYS.MULTIPLE_SESSION)) {

            //MULTIPLE_SESSION:

            TextView textViewForSessionName = convertView.findViewById(R.id.sessionName);
            TextView textViewForFacultyName = convertView.findViewById(R.id.facultyName);
            TextView textViewForAttendanceStatus = convertView.findViewById(R.id.attendanceStatus);
            ImageView greenTick = convertView.findViewById(R.id.greenTick);
            TextView facultyLabel = convertView.findViewById(R.id.facultyLabel);
            LinearLayout llRemark = convertView.findViewById(R.id.llRemark);
            TextView tvRemark1 = convertView.findViewById(R.id.tvRemark1);
            TextView tvRemark = convertView.findViewById(R.id.tvRemark);

            tvRemark1.setText(translationManager.REMARK_KEY +":");
            facultyLabel.setText(translationManager.FACULTY_KEY +":");

            JSONObject jsonObject = getItem(position);

            try {
                Integer sessionNo = jsonObject.optInt("sessionNo");
                String sessionName = "Session " + String.valueOf(sessionNo);
                textViewForSessionName.setText(sessionName);

                String facultyName = jsonObject.optString("facultyName");
                textViewForFacultyName.setText(facultyName);
                String attendanceStatus = jsonObject.optString("attendanceStatus");

                if (attendanceStatus.equals("Present") || attendanceStatus.equals("PRESENT")) {
                    textViewForAttendanceStatus.setText(translationManager.PRESENT_KEY);
                    greenTick.setImageResource(R.drawable.asign_check);
                } else if (attendanceStatus.equals("Absent") || attendanceStatus.equals("ABSENT")) {
                    textViewForAttendanceStatus.setText(translationManager.ABSENT_KEY);
                    greenTick.setImageResource(R.drawable.asign_cross);
                } else {
                    textViewForAttendanceStatus.setText(attendanceStatus);
                    greenTick.setImageResource(R.drawable.leave_icon);
                }

                String remark = ProjectUtils.getCorrectedString(jsonObject.optString("attendanceComment"));
                if(!remark.equalsIgnoreCase("")){
                    llRemark.setVisibility(View.VISIBLE);
                    tvRemark.setText(remark);
                }else{
                    llRemark.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        } else {

            //COURSE_LEVEL:

            TextView textViewForDuration = convertView.findViewById(R.id.tetViewForDuration);
            TextView textViewForFacultyName = convertView.findViewById(R.id.facultyName);
            TextView textViewForAttendanceStatus = convertView.findViewById(R.id.attendanceStatus);
            ImageView greenTick = convertView.findViewById(R.id.greenTick);
            TextView facultyLabel = convertView.findViewById(R.id.facultyLabel);
            LinearLayout llRemark = convertView.findViewById(R.id.llRemark);
            TextView tvRemark1 = convertView.findViewById(R.id.tvRemark1);
            TextView tvRemark = convertView.findViewById(R.id.tvRemark);

            tvRemark1.setText(translationManager.REMARKS_KEY +":");
            facultyLabel.setText(translationManager.FACULTY_KEY+":");

            JSONObject jsonObject = getItem(position);

            try {
                Integer startTimeLong = jsonObject.optInt("fromTime");
                Integer endTimeLong = jsonObject.optInt("toTime");

                SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm a");
                String startTimeString = targetFormat.format(startTimeLong);
                String endTimeString = targetFormat.format(endTimeLong);

                textViewForDuration.setText(startTimeString + " - " + endTimeString);

                String facultyName = jsonObject.optString("facultyName");
                textViewForFacultyName.setText(facultyName);

                String attendanceStatus = jsonObject.optString("attendanceStatus");

                if (attendanceStatus.equals("Present") || attendanceStatus.equals("PRESENT")) {
                    textViewForAttendanceStatus.setText(translationManager.PRESENT_KEY);
                    greenTick.setImageResource(R.drawable.asign_check);
                } else if (attendanceStatus.equals("Absent") || attendanceStatus.equals("ABSENT")) {
                    textViewForAttendanceStatus.setText(translationManager.ABSENT_KEY);
                    greenTick.setImageResource(R.drawable.asign_cross);
                } else {
                    textViewForAttendanceStatus.setText(attendanceStatus);
                    greenTick.setImageResource(R.drawable.leave_icon);
                }

                String remark = ProjectUtils.getCorrectedString(jsonObject.optString("attendanceComment"));
                if(!remark.equalsIgnoreCase("")){
                    llRemark.setVisibility(View.VISIBLE);
                    tvRemark.setText(remark);
                }else{
                    llRemark.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
