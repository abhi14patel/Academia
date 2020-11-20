package com.serosoft.academiassu.Modules.MyRequest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.MyRequest.Models.RequestStatus_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on May 14 2020.
 */

public class RequestStatusAdapter extends ArrayAdapter<RequestStatus_Dto> {

    private Context mContext;
    private ArrayList<RequestStatus_Dto> requestStatusList;

    public RequestStatusAdapter(Context mContext, ArrayList<RequestStatus_Dto> requestStatusList){
        super(mContext,0,requestStatusList);

        this.mContext = mContext;
        this.requestStatusList = requestStatusList;
    }

    @Override
    public int getCount() {

        return (null != requestStatusList ? requestStatusList.size() : 0);
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

        RequestStatus_Dto list = requestStatusList.get(position);

        TextView tvItem = convertView.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getValue());

        if(!value.equalsIgnoreCase("")){

            tvItem.setText(value);
        }

        return convertView;
    }
}
