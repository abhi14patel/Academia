package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DisciplinaryAction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Modules.Assignments.Assignment.AssignmentDetailActivity;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.AssignmentDocument_Dto;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DocumentsDetailActivity;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Utils.PDFActivity2;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.databinding.DisciplinaryActionDetailsBinding;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek 28/09/20
 * View Binding
 */

public class DisciplinaryActionDetailsActivity extends BaseActivity {

    DisciplinaryActionDetailsBinding binding;
    private Context mContext;
    DisciplinaryAction_Dto disciplinaryAction_dto;

    ArrayList<DisciplinaryDocument_Dto> disciplinaryDocumentList;
    DisciplinaryDocumentAdapter disciplinaryDocumentAdapter;

    String docFileName = "",documentId = "";
    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = DisciplinaryActionDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DisciplinaryActionDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = DisciplinaryActionDetailsActivity.this;

        Initialize();

        Intent intent = getIntent();
        disciplinaryAction_dto = (DisciplinaryAction_Dto) intent.getSerializableExtra(Consts.SELECTED_DATA);

        setData(disciplinaryAction_dto);
    }

    private void setData(DisciplinaryAction_Dto item) {

        String incidentType = ProjectUtils.getCorrectedString(item.getIncidentType());
        binding.tvTypeIncident.setText(incidentType);

        String actionType = ProjectUtils.getCorrectedString(item.getActionTaken());
        binding.tvActionTaken.setText(actionType);

        Long date_of_incident = item.getIncidentDate();
        String incidentDate = ProjectUtils.convertTimestampToDate(date_of_incident,mContext);
        binding.tvDOI.setText(incidentDate);

        Long date_of_action = item.getDateOfAction();
        String actionDate = ProjectUtils.convertTimestampToDate(date_of_action,mContext);
        binding.tvDOA.setText(actionDate);

        String code = ProjectUtils.getCorrectedString(item.getCode());
        String value = ProjectUtils.getCorrectedString(item.getValue());
        if(!code.equalsIgnoreCase("")){

            binding.tvReportedBy.setText("User");
            binding.tvReporterName.setText(code+"/"+value);

        }else{
            binding.tvReportedBy.setText("Others");

            String reporterName = ProjectUtils.getCorrectedString(item.getReporterName());
            binding.tvReporterName.setText(reporterName);
        }

        String incidentDetail = ProjectUtils.getCorrectedString(item.getIncidentDetails());
        binding.tvIncidentDetail.setText(incidentDetail);

        String remark = ProjectUtils.getCorrectedString(item.getRemarks());
        binding.tvRemark.setText(remark);

        disciplinaryDocumentList = item.getDisciplinaryDocument_dtos();
        if(disciplinaryDocumentList != null && disciplinaryDocumentList.size() > 0){
            binding.llDocuments.setVisibility(View.VISIBLE);
            disciplinaryDocumentAdapter = new DisciplinaryDocumentAdapter(mContext,disciplinaryDocumentList);
            binding.lvDocument.setAdapter(disciplinaryDocumentAdapter);

            binding.lvDocument.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DisciplinaryDocument_Dto disciplinaryDocument_dto = disciplinaryDocumentList.get(position);

                    downloadDocument(disciplinaryDocument_dto);
                }
            });

        }else{
            binding.llDocuments.setVisibility(View.GONE);
        }
    }

    private void downloadDocument(DisciplinaryDocument_Dto disciplinaryDocument_dto) {

        documentId = String.valueOf(disciplinaryDocument_dto.getId());

        String docName = ProjectUtils.getCorrectedString(disciplinaryDocument_dto.getPath());
        if(!docName.equalsIgnoreCase(""))
        {
            int index = docName.lastIndexOf("/");
            docFileName = docName.substring(index + 1);
        }

        if (ProjectUtils.hasPermissionInManifest(DisciplinaryActionDetailsActivity.this,PERMISSION_CODE,permission))
        {
            callAPIWithPermission();
        }
    }

    private void callAPIWithPermission() {

        showProgressDialog(mContext);
        new OptimizedServerCallAsyncTask(DisciplinaryActionDetailsActivity.this, DisciplinaryActionDetailsActivity.this,
                KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD).execute(documentId, docFileName, Consts.DOCUMENTS);
    }

    private void Initialize() {

        binding.includeTB.groupToolbar.setTitle(translationManager.INCIDENT_DETAILS_KEY.toUpperCase());
        setSupportActionBar(binding.includeTB.groupToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorPrimary, binding.includeTB.groupToolbar, this); }

        binding.tvTypeIncident1.setText(translationManager.TYPE_OF_INCIDENT_KEY);
        binding.tvDOI1.setText(translationManager.DATE_OF_INCIDENT_KEY);
        binding.tvDOA1.setText(translationManager.DATE_OF_ACTION_KEY);
        binding.tvRemark1.setText(translationManager.REMARKS_KEY);
        binding.tvDocument1.setText(translationManager.DOCUMENT_UPLOADED_KEY);
        binding.tvIncidentDetail1.setText(translationManager.INCIDENT_DETAILS_KEY);
        binding.tvActionTaken1.setText(translationManager.ACTION_TAKEN_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.refresh);
        item.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                onBackPressed();
            }break;

            case R.id.dashboardMenu:
            {
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }break;

            case R.id.refresh:
            {
                getNotifications();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response)
    {
        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        if (callFor == null)
        {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), "Unexpected Error at " + this.getLocalClassName());
            return;
        }

        switch (callFor)
        {
            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;

            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;

            case KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    Boolean downloaded = jsonObject.optBoolean("downloaded");
                    String path = jsonObject.optString("path");
                    String extension = FilenameUtils.getExtension(docFileName).toUpperCase();
                    String message = jsonObject.optString("message");

                    if (extension.equalsIgnoreCase("PDF"))
                    {
                        if (downloaded)
                        {
                            Intent intent=new Intent(mContext, PDFActivity2.class);
                            intent.putExtra("filePath",path);
                            intent.putExtra("folderName",Consts.DOCUMENTS);
                            intent.putExtra("screenName","Document");
                            intent.putExtra("idForDocument",documentId);
                            intent.putExtra("headerColor",R.color.colorAssignment);
                            startActivity(intent);
                        }
                        else
                        {
                            ProjectUtils.showLong(mContext,message);
                        }
                    }
                    else
                    {
                        ProjectUtils.showLong(mContext,message);
                        if (downloaded)
                        {
                            openFile(new File(path), this);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}