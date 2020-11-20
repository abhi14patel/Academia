package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.ParentDetails_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Widgets.CircularImage;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ParentDetailsAdapter extends RecyclerView.Adapter<ParentDetailsAdapter.MyViewHolder> {

    Context context;
    ArrayList<ParentDetails_Dto> parentDetailsList;
    TranslationManager translationManager;

    private boolean isView = false;
    String relation = "";

    public ParentDetailsAdapter(Context context, ArrayList<ParentDetails_Dto> parentDetailsList) {
        this.context = context;
        this.parentDetailsList = parentDetailsList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parents_detail_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ParentDetails_Dto list = parentDetailsList.get(position);

        String relationship = ProjectUtils.getCorrectedString(list.getRelationship());
        if(!relationship.equalsIgnoreCase("")){

            if(relationship.equalsIgnoreCase("FATHER")){
                relation = translationManager.FATHER_DETAILS_KEY;
                holder.llTop.setVisibility(View.GONE);
            }else if(relationship.equalsIgnoreCase("MOTHER")){
                relation = translationManager.MOTHER_DETAILS_KEY;
                holder.llTop.setVisibility(View.GONE);
            }else if(relationship.equalsIgnoreCase("GUARDIAN")){
                relation = translationManager.GUARDIAN_DETAILS_KEY;
                holder.llTop.setVisibility(View.GONE);
            }else if(relationship.equalsIgnoreCase("LOCAL_GUARDIAN")){
                relation = translationManager.LOCAL_GUARDIAN_DETAILS_KEY;
                holder.llTop.setVisibility(View.GONE);
            }else if(relationship.contains("VISITOR")){

                if(relationship.equalsIgnoreCase("VISITOR1")){
                    relation = context.getString(R.string.visitor1);
                }else if(relationship.equalsIgnoreCase("VISITOR2")){
                    relation = context.getString(R.string.visitor2);
                }else if(relationship.equalsIgnoreCase("VISITOR3")){
                    relation = context.getString(R.string.visitor3);
                }else if(relationship.equalsIgnoreCase("VISITOR4")){
                    relation = context.getString(R.string.visitor4);
                }

                if(!isView){
                    holder.llTop.setVisibility(View.VISIBLE);
                    isView = true;
                }else{
                    holder.llTop.setVisibility(View.GONE);
                }

            }else{
                holder.llTop.setVisibility(View.GONE);
            }

            holder.tvRelationship.setText(relation);
        }

        String parentName = ProjectUtils.getCorrectedString(list.getParentName());
        if(!parentName.equalsIgnoreCase("")){
            holder.tvParentName.setText(parentName);
        }

        String userCode = ProjectUtils.getCorrectedString(list.getParentUserCode());
        if(!userCode.equalsIgnoreCase("")){
            holder.llUserCode.setVisibility(View.VISIBLE);
            holder.tvUserCode.setText(userCode);
        }else{
            holder.llUserCode.setVisibility(View.GONE);
        }

        String parentEmail = ProjectUtils.getCorrectedString(list.getRelatedPersonEmailId());
        if(!parentEmail.equalsIgnoreCase("")){
            holder.tvParentEmail.setText(parentEmail);
        }

        String parentMobile = ProjectUtils.getCorrectedString(list.getRelatedPersonMobile());
        if(!parentMobile.equalsIgnoreCase("")){
            holder.tvParentMobile.setText(parentMobile);
        }

        String photoUrl = ProjectUtils.getCorrectedString(list.getPhotoUrl());
        if(!photoUrl.equalsIgnoreCase("")){
            String image = BaseURL.BASE_URL + KEYS.ULTIMATE_GALLERY_IMAGE_METHOD + photoUrl;
            Picasso.with(context).load(image).placeholder(R.drawable.icon_user).into(holder.ivParent);
        }

        boolean isEmergencyContact = list.isEmergencyContact();
        if(isEmergencyContact){
            holder.ivHelp.setVisibility(View.VISIBLE);
        }else{
            holder.ivHelp.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {

        return (null != parentDetailsList ? parentDetailsList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvRelationship,tvParentName,tvUserCode1,tvUserCode,tvParentEmail,tvParentMobile;
        private ImageView ivHelp;
        private CircularImage ivParent;
        private LinearLayout llTop,llUserCode,llEmail,llMobile;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvRelationship = itemView.findViewById(R.id.tvRelationship);
            tvParentName = itemView.findViewById(R.id.tvParentName);
            tvUserCode1 = itemView.findViewById(R.id.tvUserCode1);
            tvUserCode = itemView.findViewById(R.id.tvUserCode);
            tvParentEmail = itemView.findViewById(R.id.tvParentEmail);
            tvParentMobile = itemView.findViewById(R.id.tvParentMobile);
            ivParent = itemView.findViewById(R.id.ivParent);
            ivHelp = itemView.findViewById(R.id.ivHelp);
            llTop = itemView.findViewById(R.id.llTop);
            llUserCode = itemView.findViewById(R.id.llUserCode);
            llEmail = itemView.findViewById(R.id.llEmail);
            llMobile = itemView.findViewById(R.id.llMobile);

            tvUserCode1.setText(translationManager.USER_CODE_KEY);
        }
    }
}
