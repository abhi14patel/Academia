package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DisciplinaryAction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.databinding.DisciplinaryActionItemsBinding;

import java.util.ArrayList;

/**
 * Created by Abhishek 25/09/20
 * View Binding
 */
public class DisciplinaryActionAdapter extends RecyclerView.Adapter<DisciplinaryActionAdapter.MyViewHolder> {

    Context context;
    ArrayList<DisciplinaryAction_Dto> disciplinaryActionList;
    private OnItemClickListener mItemClickListener;
    TranslationManager translationManager;

    public DisciplinaryActionAdapter(Context context, ArrayList<DisciplinaryAction_Dto> disciplinaryActionList){

        this.context = context;
        this.disciplinaryActionList = disciplinaryActionList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        DisciplinaryActionItemsBinding disciplinaryActionItemsBinding = DisciplinaryActionItemsBinding.inflate(layoutInflater,parent,false);

        return new MyViewHolder(disciplinaryActionItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(disciplinaryActionList.get(position));
    }

    @Override
    public int getItemCount() {
        return disciplinaryActionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        DisciplinaryActionItemsBinding binding;

        public MyViewHolder(@NonNull DisciplinaryActionItemsBinding disciplinaryActionItemsBinding){
            super(disciplinaryActionItemsBinding.getRoot());

            this.binding = disciplinaryActionItemsBinding;

            View view = binding.getRoot();
            view.setOnClickListener(this);

            binding.tvTypeIncident1.setText(translationManager.TYPE_OF_INCIDENT_KEY);
            binding.tvDOI1.setText(translationManager.DATE_OF_INCIDENT_KEY);
            binding.tvDOA1.setText(translationManager.DATE_OF_ACTION_KEY);
            binding.tvActionTaken1.setText(translationManager.ACTION_TAKEN_KEY);
        }

        public void bind(DisciplinaryAction_Dto item) {

            String incidentType = ProjectUtils.getCorrectedString(item.getIncidentType());
            binding.tvTypeIncident.setText(incidentType);

            String actionType = ProjectUtils.getCorrectedString(item.getActionTaken());
            binding.tvActionTaken.setText(actionType);

            Long date_of_incident = item.getIncidentDate();
            String incidentDate = ProjectUtils.convertTimestampToDate(date_of_incident,context);
            binding.tvDOI.setText(incidentDate);

            Long date_of_action = item.getIncidentDate();
            String actionDate = ProjectUtils.convertTimestampToDate(date_of_action,context);
            binding.tvDOA.setText(actionDate);
        }

        @Override
        public void onClick(View v) {

            if(mItemClickListener != null)
            {
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener)
    {
        this.mItemClickListener = mItemClickListener;
    }
}
