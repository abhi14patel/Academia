package com.serosoft.academiassu.Modules.Attendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.Attendance.Models.AttendanceCourseVariant_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class AttendanceCourseVariantAdapter extends ArrayAdapter<AttendanceCourseVariant_Dto> {

    private Context mContext;
    private ArrayList<AttendanceCourseVariant_Dto> courseVariantList;

    public AttendanceCourseVariantAdapter(Context mContext, ArrayList<AttendanceCourseVariant_Dto> courseVariantList){
        super(mContext,0,courseVariantList);

        this.mContext = mContext;
        this.courseVariantList = courseVariantList;
    }

    @Override
    public int getCount() {

        return (null != courseVariantList ? courseVariantList.size() : 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position,convertView,parent);
    }

    public View getCustomView(int position,@Nullable View convertView, @Nullable ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.document_type_item, parent, false);
        }

        AttendanceCourseVariant_Dto list = courseVariantList.get(position);

        TextView tvItem = convertView.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getCourseVariantCode());

        if(!value.equalsIgnoreCase("")){

            tvItem.setText(value);
        }

        return convertView;
    }
}
