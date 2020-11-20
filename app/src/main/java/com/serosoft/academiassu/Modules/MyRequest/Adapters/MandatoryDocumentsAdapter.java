package com.serosoft.academiassu.Modules.MyRequest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.serosoft.academiassu.Modules.MyRequest.Models.RequestDocuments_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class MandatoryDocumentsAdapter extends ArrayAdapter<RequestDocuments_Dto> {

    private Context mContext;
    private ArrayList<RequestDocuments_Dto> documentsList;

    public MandatoryDocumentsAdapter(Context mContext, ArrayList<RequestDocuments_Dto> documentsList){
        super(mContext,0,documentsList);

        this.mContext = mContext;
        this.documentsList = documentsList;
    }

    @Override
    public int getCount() {

        return (null != documentsList ? documentsList.size() : 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.myrequest_document_items, parent, false);
        }

        RequestDocuments_Dto list = documentsList.get(position);

        LinearLayout ll1 = convertView.findViewById(R.id.ll1);
        ll1.setVisibility(View.INVISIBLE);

        ImageView ivEdit = convertView.findViewById(R.id.ivEdit);
        ImageView ivDownload = convertView.findViewById(R.id.ivDownload);

        TextView tvItem = convertView.findViewById(R.id.tvItem);
        String value = ProjectUtils.getCorrectedString(list.getValue());

        if(!value.equalsIgnoreCase("")){
            tvItem.setText(value);
        }

        return convertView;
    }

}
