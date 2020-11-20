package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Models.Disabilities_Dto;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Models.Profile_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;
import java.util.List;

public class DisabilitiesAdapter extends ArrayAdapter<Disabilities_Dto> {

    private Context mContext;
    private ArrayList<Disabilities_Dto> disabilitiesList;
    private ArrayList<Disabilities_Dto> selectedList;

    public DisabilitiesAdapter(Context mContext, ArrayList<Disabilities_Dto> disabilitiesList,ArrayList<Disabilities_Dto> selectedList){
        super(mContext,0,disabilitiesList);

        this.mContext = mContext;
        this.disabilitiesList = disabilitiesList;
        this.selectedList = selectedList;
    }

    @Override
    public int getCount() {

        return (null != disabilitiesList ? disabilitiesList.size() : 0);
    }

    //Here get selected item arraylist
    public ArrayList<Disabilities_Dto> getSelectedIds()
    {
        return selectedList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position,convertView,parent);
    }

    public View getCustomView(int position,@Nullable View convertView, @Nullable ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.profile_disablities_items, parent, false);
        }

        Disabilities_Dto list = disabilitiesList.get(position);

        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        LinearLayout llCheckbox = convertView.findViewById(R.id.llCheckbox);

        String value = ProjectUtils.getCorrectedString(list.getValue());

        if(!value.equalsIgnoreCase("")){
            checkBox.setText(value);
        }

        checkBox.setChecked(list.isSelected());

        llCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedList.contains(disabilitiesList.get(position)))
                {
                    checkBox.setChecked(false);
                    list.setSelected(false);
                    disabilitiesList.set(position,list);
                    selectedList.remove(disabilitiesList.get(position));
                }
                else
                {
                    checkBox.setChecked(true);
                    list.setSelected(true);
                    disabilitiesList.set(position,list);
                    selectedList.add(disabilitiesList.get(position));
                }
            }
        });

        return convertView;
    }
}
