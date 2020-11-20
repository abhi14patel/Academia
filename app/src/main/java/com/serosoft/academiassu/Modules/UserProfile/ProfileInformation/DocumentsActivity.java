package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.AcademiaApp;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters.DocumentsAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.Document_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DocumentsActivity extends BaseActivity {

    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;
    private AppCompatImageView ivAdd;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    DocumentsAdapter documentsAdapter;
    ArrayList<Document_Dto> documentList;

    private final String TAG = DocumentsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documents_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = DocumentsActivity.this;

        Initialize();

        populateContents();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(AcademiaApp.IS_UPDATE){
            populateContents();
        }

        AcademiaApp.IS_UPDATE = false;
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_DOCUMENT_LIST).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            superStateView.setVisibility(View.VISIBLE);
            superStateView.setSubTitleText("No documents found");
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.INVISIBLE);
        }
    }

    private void Initialize() {
        superStateView = findViewById(R.id.superStateView);
        toolbar = findViewById(R.id.group_toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        ivAdd = findViewById(R.id.ivAdd);

        toolbar.setTitle(translationManager.DOCUMENTS_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorPrimary, toolbar, this); }

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(mContext, DocumentUploadActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && ivAdd.getVisibility() == View.VISIBLE) {
                    ivAdd.setVisibility(View.INVISIBLE);
                } else if (dy < 0 && ivAdd.getVisibility() != View.VISIBLE) {
                    ivAdd.setVisibility(View.VISIBLE);
                }
            }
        });
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
                populateContents();
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

            case KEYS.SWITCH_DOCUMENT_LIST:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");

                    if(jsonArray != null && jsonArray.length() > 0) {

                        checkEmpty(false);
                        documentList = new ArrayList<>();

                        String value = "";

                        for(int i = 0 ; i<jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            int documentId = jsonObject1.optInt("documentId");
                            long submissionDate = jsonObject1.optLong("submissionDate");
                            long inspectionDate = jsonObject1.optLong("inspectionDate");
                            String documentName = jsonObject1.optString("documentName");
                            String path = jsonObject1.optString("path");
                            String status = jsonObject1.optString("status");
                            String remark = jsonObject1.optString("remark");
                            String programName = jsonObject1.optString("programName");
                            String seatType = jsonObject1.optString("seatType");
                            boolean isPreDefined = jsonObject1.optBoolean("isPreDefined");

                            JSONObject jsonObject2 = jsonObject1.optJSONObject("documentType");
                            if(jsonObject2!= null && jsonObject2.length() >0){
                                value = jsonObject2.optString("value");
                            }

                            Document_Dto document_dto = new Document_Dto(documentId, submissionDate, inspectionDate,isPreDefined,documentName, path, status, remark, programName, seatType, value);
                            documentList.add(document_dto);
                        }

                        Collections.reverse(documentList);

                        documentsAdapter = new DocumentsAdapter(mContext,documentList);
                        recyclerView.setAdapter(documentsAdapter);
                        documentsAdapter.notifyDataSetChanged();
                    }else
                    {
                        checkEmpty(true);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                    checkEmpty(true);
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
