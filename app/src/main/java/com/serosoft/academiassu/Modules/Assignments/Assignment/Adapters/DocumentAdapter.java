package com.serosoft.academiassu.Modules.Assignments.Assignment.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.AssignmentDocument_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by Abhishek on October 24 2019.
 */

public class DocumentAdapter extends ArrayAdapter<AssignmentDocument_Dto> {

    private Context context;
    private ArrayList<AssignmentDocument_Dto> attachmentList;
    String docFileName = "";

    public DocumentAdapter(Context context, ArrayList<AssignmentDocument_Dto> attachmentList) {
        super(context, 0,attachmentList);
        this.context = context;
        this.attachmentList = attachmentList;
    }

    @Override
    public int getCount() {

        return (null != attachmentList ? attachmentList.size() : 0);
    }

    @Override
    public AssignmentDocument_Dto getItem(int position) {

        return attachmentList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.assignment_document_items, parent, false);
        }

        AssignmentDocument_Dto list = attachmentList.get(position);

        TextView tvAttachment = convertView.findViewById(R.id.tvAttachment);
        TextView tvDownload = convertView.findViewById(R.id.tvDownload);
        AppCompatImageView ivAttachment = convertView.findViewById(R.id.ivAttachment);

        String pathName = ProjectUtils.getCorrectedString(list.getPath());
        if(!pathName.equalsIgnoreCase(""))
        {
            int index = pathName.lastIndexOf("/");
            docFileName = pathName.substring(index + 1);

            tvAttachment.setText(docFileName);
        }else{
            tvAttachment.setText("-");
        }

        ivAttachment.setImageResource(ProjectUtils.showDocIcon(docFileName));

        return convertView;
    }
}
