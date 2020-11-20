package com.serosoft.academiassu.Modules.MyRequest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.MyRequest.Models.RequestType_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on May 15 2020.
 */

public class RequestTypeAdapter extends ArrayAdapter<RequestType_Dto> {

    private Context mContext;
    private ArrayList<RequestType_Dto> requestTypeList;

    public RequestTypeAdapter(Context mContext, ArrayList<RequestType_Dto> requestTypeList){
        super(mContext,0,requestTypeList);

        this.mContext = mContext;
        this.requestTypeList = requestTypeList;
    }

    @Override
    public int getCount() {

        return (null != requestTypeList ? requestTypeList.size() : 0);
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

        RequestType_Dto list = requestTypeList.get(position);

        TextView tvItem = convertView.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getValue());

        if(!value.equalsIgnoreCase("")){

            tvItem.setText(value);
        }

        return convertView;
    }
}
