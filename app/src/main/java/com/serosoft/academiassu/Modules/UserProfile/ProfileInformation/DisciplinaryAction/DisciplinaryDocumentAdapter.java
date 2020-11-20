package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DisciplinaryAction;

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
 * Created by Abhishek on September 29 2020.
 */

public class DisciplinaryDocumentAdapter extends ArrayAdapter<DisciplinaryDocument_Dto> {

    private Context context;
    private ArrayList<DisciplinaryDocument_Dto> disciplinaryDocument_dtos;
    String docFileName = "";

    public DisciplinaryDocumentAdapter(Context context, ArrayList<DisciplinaryDocument_Dto> disciplinaryDocument_dtos) {
        super(context, 0,disciplinaryDocument_dtos);
        this.context = context;
        this.disciplinaryDocument_dtos = disciplinaryDocument_dtos;
    }

    @Override
    public int getCount() {

        return (null != disciplinaryDocument_dtos ? disciplinaryDocument_dtos.size() : 0);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.assignment_document_items, parent, false);
        }

        DisciplinaryDocument_Dto list = disciplinaryDocument_dtos.get(position);

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
