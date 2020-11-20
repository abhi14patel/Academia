package com.serosoft.academiassu.Modules.AcademiaDrive.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Modules.AcademiaDrive.Models.DriveFile_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DriveMediaAdapter extends RecyclerView.Adapter<DriveMediaAdapter.MyViewHolder>{

    Context context;
    ArrayList<DriveFile_Dto> driveFileList;
    String docFileName = "";

    public DriveMediaAdapter(Context context, ArrayList<DriveFile_Dto> driveFileList) {
        this.context = context;
        this.driveFileList = driveFileList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drive_file_media_items, parent, false);

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

            String imageURL = pathName;
            imageURL = BaseURL.BASE_URL + KEYS.ULTIMATE_GALLERY_IMAGE_METHOD + pathName;
            Picasso.with(context).load(imageURL).into(holder.imgFile);
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
        private AppCompatImageView imgFile;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvFileName = itemView.findViewById(R.id.tvFileName);
            imgFile = itemView.findViewById(R.id.imgFile);
        }
    }
}
