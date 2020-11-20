package com.serosoft.academiassu.Modules.Assignments.Assignment.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.AssignmentType_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class AssignmentTypeAdapter extends ArrayAdapter<AssignmentType_Dto> {

    private Context mContext;
    private ArrayList<AssignmentType_Dto> assignmentTypeList;

    public AssignmentTypeAdapter(Context mContext, ArrayList<AssignmentType_Dto> assignmentTypeList){
        super(mContext,0,assignmentTypeList);

        this.mContext = mContext;
        this.assignmentTypeList = assignmentTypeList;
    }

    @Override
    public int getCount() {

        return (null != assignmentTypeList ? assignmentTypeList.size() : 0);
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

        AssignmentType_Dto list = assignmentTypeList.get(position);

        TextView tvItem = convertView.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getValue());

        if(!value.equalsIgnoreCase("")){
            tvItem.setText(value);
        }

        return convertView;
    }
}
