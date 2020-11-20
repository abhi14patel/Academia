package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.CountryCode_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class CountryCodeAdapter extends ArrayAdapter<CountryCode_Dto> {

    private Context mContext;
    private ArrayList<CountryCode_Dto> countryCodeList;

    public CountryCodeAdapter(Context mContext, ArrayList<CountryCode_Dto> countryCodeList){
        super(mContext,0,countryCodeList);

        this.mContext = mContext;
        this.countryCodeList = countryCodeList;
    }

    @Override
    public int getCount() {

        return (null != countryCodeList ? countryCodeList.size() : 0);
    }

    @Override
    public CountryCode_Dto getItem(int position) {

        return countryCodeList.get(position);
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

        CountryCode_Dto list = countryCodeList.get(position);

        TextView tvItem = convertView.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getIsdCode());

        if(!value.equalsIgnoreCase("")){
            tvItem.setText(value);
        }

        return convertView;
    }
}
