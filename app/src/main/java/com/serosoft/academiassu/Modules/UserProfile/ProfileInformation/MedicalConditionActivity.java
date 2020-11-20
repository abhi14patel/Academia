package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Helpers.AcademiaApp;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;

import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters.MedicalConditionAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.MedicalCondition_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MedicalConditionActivity extends BaseActivity {

    private Context mContext;
    public Toolbar toolbar;
    private LinearLayout layoutEmpty;
    private AppCompatImageView ivAdd;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    MedicalConditionAdapter medicalConditionAdapter;
    ArrayList<MedicalCondition_Dto> medicalConditionList;

    private final String TAG = MedicalConditionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_condition_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = MedicalConditionActivity.this;

        Initialize();

        populateContents();
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_MEDICAL_CONDITION_LIST).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(AcademiaApp.IS_UPDATE){
            populateContents();
        }

        AcademiaApp.IS_UPDATE = false;
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            layoutEmpty.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.INVISIBLE);
        }
    }

    private void Initialize() {

        layoutEmpty = findViewById(R.id.layoutEmpty);
        toolbar = findViewById(R.id.group_toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        ivAdd = findViewById(R.id.ivAdd);

        toolbar.setTitle(translationManager.MEDICAL_CONDITIONS_KEY.toUpperCase());
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

                Intent intent1 = new Intent(mContext, MedicalConditionUploadActivity.class);
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

        switch (callFor){

            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;

            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;

            case KEYS.SWITCH_MEDICAL_CONDITION_LIST:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");

                    if(jsonArray != null && jsonArray.length() > 0) {

                        checkEmpty(false);
                        medicalConditionList = new ArrayList<>();

                        String value = "";

                        for(int i = 0 ; i<jsonArray.length() ; i++) {

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            long dateSince = jsonObject1.optLong("dateSince");
                            String medicalCondition = jsonObject1.optString("medicalCondition");
                            String consultingDoctor = jsonObject1.optString("consultingDoctor");
                            String remarks = jsonObject1.optString("remarks");
                            String doctorTelephoneCountryCode = jsonObject1.optString("doctorTelephoneCountryCode");
                            String doctorTelephoneNo = jsonObject1.optString("doctorTelephoneNo");

                            JSONObject jsonObject2 = jsonObject1.optJSONObject("conditionType");
                            if(jsonObject2!= null && jsonObject2.length() >0){
                                value = jsonObject2.optString("value");
                            }

                            MedicalCondition_Dto medicalCondition_dto = new MedicalCondition_Dto(medicalCondition,value,dateSince,consultingDoctor,doctorTelephoneCountryCode,doctorTelephoneNo,remarks);
                            medicalConditionList.add(medicalCondition_dto);
                        }

                        medicalConditionAdapter = new MedicalConditionAdapter(mContext,medicalConditionList);
                        recyclerView.setAdapter(medicalConditionAdapter);
                        medicalConditionAdapter.notifyDataSetChanged();
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