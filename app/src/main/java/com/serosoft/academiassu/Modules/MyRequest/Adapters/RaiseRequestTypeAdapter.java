package com.serosoft.academiassu.Modules.MyRequest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.serosoft.academiassu.Modules.MyRequest.Models.MyRequestType_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on October 24 2019.
 */

public class RaiseRequestTypeAdapter extends ArrayAdapter<MyRequestType_Dto> {

    private Context context;
    private ArrayList<MyRequestType_Dto> myRequestTypeList;

    public RaiseRequestTypeAdapter(Context context, ArrayList<MyRequestType_Dto> myRequestTypeList) {
        super(context, 0,myRequestTypeList);
        this.context = context;
        this.myRequestTypeList = myRequestTypeList;
    }

    @Override
    public int getCount() {

        return (null != myRequestTypeList ? myRequestTypeList.size() : 0);
    }

    @Override
    public MyRequestType_Dto getItem(int position) {

        return myRequestTypeList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.myraiserequest_items, parent, false);
        }

        MyRequestType_Dto list = myRequestTypeList.get(position);

        TextView tvRequestName = convertView.findViewById(R.id.tvRequestName);
        String value = ProjectUtils.getCorrectedString(list.getValue());

        if(!value.equalsIgnoreCase("")){
            tvRequestName.setText(value);
        }

        return convertView;
    }
}
