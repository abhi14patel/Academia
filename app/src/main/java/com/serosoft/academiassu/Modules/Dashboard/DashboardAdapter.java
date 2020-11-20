package com.serosoft.academiassu.Modules.Dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Modules.Dashboard.Models.ModuleData;
import com.serosoft.academiassu.R;
import java.util.ArrayList;

/**
 * Created by Abhishek on October 14 2019.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<ModuleData> moduleList;
    private OnItemClickListener mItemClickListener;

    DashboardAdapter(Context context, ArrayList<ModuleData> moduleList)
    {
        this.context = context;
        this.moduleList = moduleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_dashboard_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final ModuleData moduleData = moduleList.get(position);

        holder.tvModuleName.setText(moduleData.getName());
        holder.rl1.setBackgroundColor(moduleData.getColor());
        holder.ivModuleIcon.setImageResource(moduleData.getIcon());
    }

    @Override
    public int getItemCount()
    {
        return moduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private AppCompatImageView ivModuleIcon;
        private TextView tvModuleName,tvCount;
        RelativeLayout rl1;

        ViewHolder(View view)
        {
            super(view);

            ivModuleIcon = view.findViewById(R.id.ivModuleIcon);
            tvModuleName = view.findViewById(R.id.tvModuleName);
            tvCount = view.findViewById(R.id.tvCount);
            rl1 = view.findViewById(R.id.rl1);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if(mItemClickListener != null)
            {
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    void setOnItemClickListener(OnItemClickListener mItemClickListener)
    {
        this.mItemClickListener = mItemClickListener;
    }

}
