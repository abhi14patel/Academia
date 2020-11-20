package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DisciplinaryAction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Exam.Models.Marksheet_Dto;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.DocumentsDetailActivity;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.databinding.DisciplinaryActionListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek 25/09/20
 * View Binding
 */
public class DisciplinaryActionListActivity extends BaseActivity {

    DisciplinaryActionListBinding binding;
    private Context mContext;

    LinearLayoutManager linearLayoutManager;
    ArrayList<DisciplinaryAction_Dto> showList;
    ArrayList<DisciplinaryDocument_Dto> disciplinaryDocList;
    DisciplinaryActionAdapter disciplinaryActionAdapter;

    private final String TAG = DisciplinaryActionListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DisciplinaryActionListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = DisciplinaryActionListActivity.this;

        Initialize();

        populateContents();
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_DISCIPLINARY_ACTION_LIST).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            binding.includeRV.recyclerView.setVisibility(View.INVISIBLE);
            binding.includeRV.superStateView.setVisibility(View.VISIBLE);
            binding.includeRV.superStateView.setSubTitleText("No details found");
        }
        else
        {
            binding.includeRV.recyclerView.setVisibility(View.VISIBLE);
            binding.includeRV.superStateView.setVisibility(View.INVISIBLE);
        }
    }

    private void Initialize() {

        binding.includeTB.groupToolbar.setTitle(translationManager.DISCIPLINARY_ACTION_KEY.toUpperCase());
        setSupportActionBar(binding.includeTB.groupToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorPrimary, binding.includeTB.groupToolbar, this); }

        linearLayoutManager = new LinearLayoutManager(mContext);
        binding.includeRV.recyclerView.setLayoutManager(linearLayoutManager);
        binding.includeRV.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.includeRV.recyclerView.setNestedScrollingEnabled(false);
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

            case KEYS.SWITCH_DISCIPLINARY_ACTION_LIST:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");

                    String code = "";
                    String value = "";

                    if(jsonArray != null && jsonArray.length() > 0) {

                        checkEmpty(false);
                        showList = new ArrayList<>();

                        for(int i = 0 ; i<jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            JSONObject incidentTypeJson = jsonObject1.optJSONObject("incidentType");
                            String incidentType = incidentTypeJson.optString("value");

                            String incidentDetails = jsonObject1.optString("details");
                            JSONObject reportedByUser = jsonObject1.optJSONObject("reportedByUser");
                            if(reportedByUser != null){
                                code = reportedByUser.optString("code");
                                value = reportedByUser.optString("value");
                            }else{
                                code = "";
                                value = "";
                            }
                            String reporterName = jsonObject1.optString("reportedByUserName");
                            String actionTaken = jsonObject1.optString("actionTaken");
                            String remarks = jsonObject1.optString("remarks");
                            Long dateOfAction = jsonObject1.optLong("dateOfAction");
                            Long incidentDate = jsonObject1.optLong("incidentDate");

                            JSONArray documents = jsonObject1.optJSONArray("documents");

                            disciplinaryDocList = new ArrayList<>();

                            if(documents != null && documents.length() > 0){

                                for(int j = 0 ; j < documents.length() ; j++){

                                    JSONObject jsonObject2 = documents.optJSONObject(j);

                                    int id = jsonObject2.optInt("id");
                                    String name = jsonObject2.optString("name");
                                    String path = jsonObject2.optString("path");

                                    disciplinaryDocList.add(new DisciplinaryDocument_Dto(id,name,path));
                                }
                            }

                            DisciplinaryAction_Dto list = new DisciplinaryAction_Dto(incidentType,incidentDetails,reporterName,actionTaken,remarks,dateOfAction,incidentDate,code,value,disciplinaryDocList);
                            showList.add(list);
                        }

                        disciplinaryActionAdapter = new DisciplinaryActionAdapter(mContext,showList);
                        binding.includeRV.recyclerView.setAdapter(disciplinaryActionAdapter);

                        disciplinaryActionAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                DisciplinaryAction_Dto item = showList.get(position);

                                Intent intent = new Intent(mContext, DisciplinaryActionDetailsActivity.class);
                                intent.putExtra(Consts.SELECTED_DATA,item);
                                startActivity(intent);
                            }
                        });

                    }else{
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