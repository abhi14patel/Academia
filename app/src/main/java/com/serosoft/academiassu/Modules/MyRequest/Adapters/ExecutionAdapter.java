package com.serosoft.academiassu.Modules.MyRequest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.MyRequest.Models.Execution_Dto;
import com.serosoft.academiassu.R;

import java.util.ArrayList;

public class ExecutionAdapter extends ArrayAdapter<Execution_Dto> {

    private Context mContext;
    private ArrayList<Execution_Dto> ExecutionList;

    public ExecutionAdapter(Context mContext, ArrayList<Execution_Dto> ExecutionList){
        super(mContext,0,ExecutionList);

        this.mContext = mContext;
        this.ExecutionList = ExecutionList;
    }

    @Override
    public int getCount() {

        return (null != ExecutionList ? ExecutionList.size() : 0);
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

        TextView tvItem = convertView.findViewById(R.id.tvItem);

        Execution_Dto execution_dto = ExecutionList.get(position);
        tvItem.setText(execution_dto.getName());

        return convertView;
    }
}
