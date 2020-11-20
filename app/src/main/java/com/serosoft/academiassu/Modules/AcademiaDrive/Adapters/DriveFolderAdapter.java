package com.serosoft.academiassu.Modules.AcademiaDrive.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.serosoft.academiassu.Modules.AcademiaDrive.Models.DriveFolder_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;
import java.util.ArrayList;

public class DriveFolderAdapter extends RecyclerView.Adapter<DriveFolderAdapter.MyViewHolder>{

    Context context;
    ArrayList<DriveFolder_Dto> driveFolderList;

    public DriveFolderAdapter(Context context, ArrayList<DriveFolder_Dto> driveFolderList) {
        this.context = context;
        this.driveFolderList = driveFolderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drive_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DriveFolder_Dto list = driveFolderList.get(position);

        String eventName = ProjectUtils.getCorrectedString(list.getAlbumName());
        if(!eventName.equalsIgnoreCase("")){

            holder.tvFolderName.setText(eventName);
        }
    }

    @Override
    public int getItemCount() {
        return (null != driveFolderList ? driveFolderList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvFolderName;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvFolderName = itemView.findViewById(R.id.tvFolderName);
        }
    }
}
