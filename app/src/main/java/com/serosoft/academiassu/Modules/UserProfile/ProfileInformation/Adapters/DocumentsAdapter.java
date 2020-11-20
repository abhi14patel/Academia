package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DocumentsDetailActivity;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.Document_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;
import java.util.ArrayList;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.MyViewHolder> {

    Context context;
    ArrayList<Document_Dto> documentList;
    TranslationManager translationManager;

    public DocumentsAdapter(Context context, ArrayList<Document_Dto> documentList) {
        this.context = context;
        this.documentList = documentList;
        translationManager = new TranslationManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.documents_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(documentList.get(position));
    }

    @Override
    public int getItemCount() {

        return (null != documentList ? documentList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvDocumentName,tvDate,tvStatus;
        private AppCompatImageView ivDocument;
        private RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvDocumentName = itemView.findViewById(R.id.tvDocumentName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivDocument = itemView.findViewById(R.id.ivDocument);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }

        public void bind(final Document_Dto item) {

            boolean isPreDefined = item.isPreDefined();

            //Here check isPredefined and get value for document name
            if(isPreDefined){
                String name = ProjectUtils.getCorrectedString(item.getValue());
                if(!name.equalsIgnoreCase("")) {

                    tvDocumentName.setText(name);
                }
            }else{
                String name = ProjectUtils.getCorrectedString(item.getDocumentName());
                if(!name.equalsIgnoreCase("")) {

                    tvDocumentName.setText(name);
                }
            }

            long submissionDate = item.getSubmissionDate();
            String date = ProjectUtils.convertTimestampToDate(submissionDate,context);

            if(!date.equalsIgnoreCase(""))
            {
                tvDate.setText(translationManager.SUBMISSION_DATE_KEY+" - "+date);
            }

            String status = ProjectUtils.getCorrectedString(item.getStatus());
            if(!status.equalsIgnoreCase("")){

                tvStatus.setText(ProjectUtils.capitalize(status));
            }

            String docFileName = ProjectUtils.getCorrectedString(item.getDocPath());
            if(!docFileName.equalsIgnoreCase("")){

                ivDocument.setImageResource(ProjectUtils.showDocIcon(docFileName));
            }

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, DocumentsDetailActivity.class);
                    intent.putExtra(Consts.DOCUMENT_LIST,item);
                    context.startActivity(intent);
                }
            });
        }
    }
}
