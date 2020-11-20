package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Models.Profile_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class SalutationAdapter extends ArrayAdapter<Profile_Dto> {

    private Context mContext;
    private ArrayList<Profile_Dto> salutationList;

    public SalutationAdapter(Context mContext, ArrayList<Profile_Dto> salutationList){
        super(mContext,0,salutationList);

        this.mContext = mContext;
        this.salutationList = salutationList;
    }

    @Override
    public int getCount() {

        return (null != salutationList ? salutationList.size() : 0);
    }

    @Override
    public Profile_Dto getItem(int position) {

        return salutationList.get(position);
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

        Profile_Dto list = salutationList.get(position);

        TextView tvItem = convertView.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getValue());

        if(!value.equalsIgnoreCase("")){
            tvItem.setText(value);
        }

        return convertView;
    }
}
