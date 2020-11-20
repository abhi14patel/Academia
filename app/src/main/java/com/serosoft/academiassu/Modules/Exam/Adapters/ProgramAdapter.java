package com.serosoft.academiassu.Modules.Exam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.Exam.Models.Program_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class ProgramAdapter extends ArrayAdapter<Program_Dto> {

    private Context mContext;
    private ArrayList<Program_Dto> programList;

    public ProgramAdapter(Context mContext, ArrayList<Program_Dto> programList){
        super(mContext,0,programList);

        this.mContext = mContext;
        this.programList = programList;
    }

    @Override
    public int getCount() {

        return (null != programList ? programList.size() : 0);
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

        Program_Dto list = programList.get(position);

        TextView tvItem = convertView.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getProgramName());
        String code = ProjectUtils.getCorrectedString(list.getProgramCode());

        if(!value.equalsIgnoreCase("")){
            if(!code.equalsIgnoreCase("")){
                tvItem.setText(code+"/"+value);
            }else{
                tvItem.setText(value);
            }
        }

        return convertView;
    }
}
