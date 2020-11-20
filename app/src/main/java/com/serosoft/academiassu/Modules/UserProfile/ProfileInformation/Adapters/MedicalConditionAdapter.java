package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.MedicalCondition_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;

public class MedicalConditionAdapter extends RecyclerView.Adapter<MedicalConditionAdapter.MyViewHolder> {

    Context context;
    ArrayList<MedicalCondition_Dto> medicalConditionList;
    TranslationManager translationManager;

    public MedicalConditionAdapter(Context context, ArrayList<MedicalCondition_Dto> medicalConditionList) {
        this.context = context;
        this.medicalConditionList = medicalConditionList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_condition_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(medicalConditionList.get(position));
    }

    @Override
    public int getItemCount() {

        return (null != medicalConditionList ? medicalConditionList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvConditionType1,tvSince1,tvConsultingDoctor1,tvContact1,tvMedicalCondition1,tvPrecuation1;
        private TextView tvConditionType,tvSince,tvConsultingDoctor,tvContact,tvMedicalCondition,tvPrecuation;
        private LinearLayout llSince;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvConditionType1 = itemView.findViewById(R.id.tvConditionType1);
            tvSince1 = itemView.findViewById(R.id.tvSince1);
            tvConsultingDoctor1 = itemView.findViewById(R.id.tvConsultingDoctor1);
            tvContact1 = itemView.findViewById(R.id.tvContact1);
            tvMedicalCondition1 = itemView.findViewById(R.id.tvMedicalCondition1);
            tvPrecuation1 = itemView.findViewById(R.id.tvPrecuation1);

            tvConditionType = itemView.findViewById(R.id.tvConditionType);
            tvSince = itemView.findViewById(R.id.tvSince);
            tvConsultingDoctor = itemView.findViewById(R.id.tvConsultingDoctor);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvMedicalCondition = itemView.findViewById(R.id.tvMedicalCondition);
            tvPrecuation = itemView.findViewById(R.id.tvPrecuation);

            llSince = itemView.findViewById(R.id.llSince);

            tvConditionType1.setText(translationManager.CONDITION_TYPE_KEY);
            tvSince1.setText(translationManager.SINCE_KEY);
            tvConsultingDoctor1.setText(translationManager.CONSULTING_DOCTOR_KEY);
            tvContact1.setText(translationManager.CONTACT_NUMBER_KEY);
            tvMedicalCondition1.setText(translationManager.MEDICAL_CONDITION_KEY);
            tvPrecuation1.setText(translationManager.PRECAUTION_MEDICATION_KEY);
        }

        public void bind(final MedicalCondition_Dto item) {

            long sinceDate = item.getDate();

            if(sinceDate != 0){

                String date = ProjectUtils.convertTimestampToDate(sinceDate,context);

                if(!date.equalsIgnoreCase(""))
                {
                    llSince.setVisibility(View.VISIBLE);
                    tvSince.setText(" : "+date);
                }else{
                    llSince.setVisibility(View.INVISIBLE);
                }
            }
            else{
                llSince.setVisibility(View.INVISIBLE);
            }


            String conditionType = ProjectUtils.getCorrectedString(item.getConditionType());
            if(!conditionType.equalsIgnoreCase("")){

                tvConditionType.setText(conditionType);
            }else{
                tvConditionType.setText(" - ");
            }

            String consultingDoctor = ProjectUtils.getCorrectedString(item.getConsultingDoctor());
            if(!consultingDoctor.equalsIgnoreCase("")){

                tvConsultingDoctor.setText(consultingDoctor);
            }else{
                tvConsultingDoctor.setText(" - ");
            }

            String code = ProjectUtils.getCorrectedString(item.getDoctorTelephoneCountryCode());
            String contact = ProjectUtils.getCorrectedString(item.getDoctorTelephoneNo());
            if(!contact.equalsIgnoreCase("")){

                tvContact.setText(code+" "+contact);
            }else{
                tvContact.setText(" - ");
            }

            String medicalCondition = ProjectUtils.getCorrectedString(item.getMedicalCondition());
            if(!medicalCondition.equalsIgnoreCase("")){

                tvMedicalCondition.setText(medicalCondition);
            }else{
                tvMedicalCondition.setText(" - ");
            }

            String precaution = ProjectUtils.getCorrectedString(item.getPrecaution());
            if(!precaution.equalsIgnoreCase("")){

                tvPrecuation.setText(precaution);
            }else{
                tvPrecuation.setText(" - ");
            }
        }
    }
}
