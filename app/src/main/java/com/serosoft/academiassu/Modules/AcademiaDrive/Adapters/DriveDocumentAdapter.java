package com.serosoft.academiassu.Modules.AcademiaDrive.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Modules.AcademiaDrive.Models.DriveFile_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class DriveDocumentAdapter extends RecyclerView.Adapter<DriveDocumentAdapter.MyViewHolder>{

    Context context;
    ArrayList<DriveFile_Dto> driveFileList;
    String docFileName = "";

    public DriveDocumentAdapter(Context context, ArrayList<DriveFile_Dto> driveFileList) {
        this.context = context;
        this.driveFileList = driveFileList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drive_file_document_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DriveFile_Dto list = driveFileList.get(position);

        String pathName = ProjectUtils.getCorrectedString(list.getPath());
        if(!pathName.equalsIgnoreCase(""))
        {
            int index = pathName.lastIndexOf("/");
            docFileName = pathName.substring(index + 1);

            holder.imgFile.setImageResource(ProjectUtils.showDocIcon(docFileName));
        }

        String fileName = ProjectUtils.getCorrectedString(list.getImageName());
        if(!fileName.equalsIgnoreCase("")){

            holder.tvFileName.setText(fileName);
        }
    }

    @Override
    public int getItemCount() {
        return (null != driveFileList ? driveFileList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvFileName;
        private ImageView imgFile;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvFileName = itemView.findViewById(R.id.tvFileName);
            imgFile = itemView.findViewById(R.id.imgFile);
        }
    }
}
