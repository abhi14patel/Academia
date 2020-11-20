package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.DocumentType_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class DocumentTypeAdapter extends ArrayAdapter<DocumentType_Dto> {

    private Context mContext;
    private ArrayList<DocumentType_Dto> documentTypeList;

    public DocumentTypeAdapter(Context mContext,ArrayList<DocumentType_Dto> documentTypeList){
        super(mContext,0,documentTypeList);

        this.mContext = mContext;
        this.documentTypeList = documentTypeList;
    }

    @Override
    public int getCount() {

        return (null != documentTypeList ? documentTypeList.size() : 0);
    }

    @Override
    public DocumentType_Dto getItem(int position) {

        return documentTypeList.get(position);
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

        DocumentType_Dto list = documentTypeList.get(position);

        TextView tvItem = convertView.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getValue());

        if(!value.equalsIgnoreCase("")){
            tvItem.setText(value);
        }

        return convertView;
    }
}
