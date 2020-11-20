package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.PersonalDetails.Models.CurrentEducation_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class CurrentEducationAdapter extends ArrayAdapter<CurrentEducation_Dto> {

    private Context mContext;
    private ArrayList<CurrentEducation_Dto> currentEducationList;
    private ArrayList<CurrentEducation_Dto> selectedList;

    public CurrentEducationAdapter(Context mContext, ArrayList<CurrentEducation_Dto> currentEducationList, ArrayList<CurrentEducation_Dto> selectedList){
        super(mContext,0,currentEducationList);

        this.mContext = mContext;
        this.currentEducationList = currentEducationList;
        this.selectedList = selectedList;
    }

    @Override
    public int getCount() {

        return (null != currentEducationList ? currentEducationList.size() : 0);
    }

    //Here get selected item arraylist
    public ArrayList<CurrentEducation_Dto> getSelectedIds()
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

        CurrentEducation_Dto list = currentEducationList.get(position);

        AppCompatCheckBox checkBox = convertView.findViewById(R.id.checkBox);
        LinearLayout llCheckbox = convertView.findViewById(R.id.llCheckbox);

        String value = ProjectUtils.getCorrectedString(list.getValue());

        if(!value.equalsIgnoreCase("")){
            checkBox.setText(value);
        }

        checkBox.setChecked(list.isSelected());

        llCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedList.contains(currentEducationList.get(position)))
                {
                    checkBox.setChecked(false);
                    list.setSelected(false);
                    currentEducationList.set(position,list);
                    selectedList.remove(currentEducationList.get(position));
                }
                else
                {
                    checkBox.setChecked(true);
                    list.setSelected(true);
                    currentEducationList.set(position,list);
                    selectedList.add(currentEducationList.get(position));
                }
            }
        });

        return convertView;
    }
}
