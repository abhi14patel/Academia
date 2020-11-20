package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.FeePayers.AddFeePayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<Country_Dto> {

    private Context mContext;
    private ArrayList<Country_Dto> countryList;

    public CountryAdapter(Context mContext, ArrayList<Country_Dto> countryList){
        super(mContext,0,countryList);

        this.mContext = mContext;
        this.countryList = countryList;
    }

    @Override
    public int getCount() {

        return (null != countryList ? countryList.size() : 0);
    }

    @Override
    public Country_Dto getItem(int position) {

        return countryList.get(position);
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

        Country_Dto list = countryList.get(position);

        TextView tvItem = convertView.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getCountryName());

        if(!value.equalsIgnoreCase("")){
            tvItem.setText(value);
        }

        return convertView;
    }
}
