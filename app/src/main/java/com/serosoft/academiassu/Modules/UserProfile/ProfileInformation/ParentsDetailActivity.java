package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Adapters.ParentDetailsAdapter;
import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models.ParentDetails_Dto;
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

public class ParentsDetailActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    private ArrayList<ParentDetails_Dto> parentDetailList;
    ParentDetailsAdapter parentDetailsAdapter;

    private final String TAG = ParentsDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parents_detail_list);

        ProjectUtils.showLog(TAG,"onCreate");

        mContext = ParentsDetailActivity.this;

        Initialize();

        populateContents();
    }

    private void Initialize() {

        superStateView = findViewById(R.id.superStateView);
        toolbar = findViewById(R.id.group_toolbar);
        recyclerView = findViewById(R.id.recyclerView);

        toolbar.setTitle(translationManager.PARENTS_DETAILS_KEY.toUpperCase());
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
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            showProgressDialog(mContext);
            new OptimizedServerCallAsyncTask(mContext, ParentsDetailActivity.this,
                    KEYS.SWITCH_PARENT_DETAILS).execute();
        }
        else{
            showAlertDialog(this, getString(R.string.network_error), getString(R.string.please_check_your_network_connection));
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response)
    {
        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        if (callFor == null)
        {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), getString(R.string.unexpected_error));
            return;
        }

        switch (callFor)
        {
            case KEYS.SWITCH_PARENT_DETAILS:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0) {
                        checkEmpty(false);
                        parentDetailList = new ArrayList<>();

                        for(int i = 0 ; i<jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            JSONObject jsonObject2 = jsonObject1.getJSONObject("relatedPerson");
                            String parentName = jsonObject2.optString("value");

                            String relationship = jsonObject1.optString("relationship");
                            String parentUserCode = jsonObject1.optString("parentUserCode");
                            String relatedPersonEmailId = jsonObject1.optString("relatedPersonEmailId");
                            String relatedPersonMobile = jsonObject1.optString("relatedPersonMobile");
                            String photoUrl = jsonObject1.optString("photoUrl");

                            boolean relatedPersonHasAccessToStudent = jsonObject1.optBoolean("relatedPersonHasAccessToStudent");
                            boolean isEmergencyContact = jsonObject1.optBoolean("isEmergencyContact");

                            ParentDetails_Dto parentDetails_dto = new ParentDetails_Dto(parentName,relationship,parentUserCode,relatedPersonEmailId,relatedPersonMobile,photoUrl,relatedPersonHasAccessToStudent,isEmergencyContact);
                            parentDetailList.add(parentDetails_dto);
                        }

                        sortParents(parentDetailList);

                    }else
                    {
                        checkEmpty(true);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    checkEmpty(true);
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                }
                break;
            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;
            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;
        }
    }

    private void sortParents(ArrayList<ParentDetails_Dto> list) {

        ArrayList<ParentDetails_Dto> tempList = new ArrayList<>();
        ArrayList<ParentDetails_Dto> tempList1 = new ArrayList<>();
        ArrayList<ParentDetails_Dto> tempList2 = new ArrayList<>();
        ArrayList<ParentDetails_Dto> tempList3 = new ArrayList<>();
        ArrayList<ParentDetails_Dto> tempList4 = new ArrayList<>();
        ArrayList<ParentDetails_Dto> tempList5 = new ArrayList<>();

        for(ParentDetails_Dto parentDetails_dto : list){

            if(parentDetails_dto.getRelationship().equalsIgnoreCase("FATHER")){
                tempList1.add(parentDetails_dto);
            }else if(parentDetails_dto.getRelationship().equalsIgnoreCase("MOTHER")){
                tempList2.add(parentDetails_dto);
            }else if(parentDetails_dto.getRelationship().equalsIgnoreCase("GUARDIAN")){
                tempList3.add(parentDetails_dto);
            }else if(parentDetails_dto.getRelationship().equalsIgnoreCase("LOCAL_GUARDIAN")){
                tempList4.add(parentDetails_dto);
            }else{
                tempList5.add(parentDetails_dto);
            }
        }

        tempList.addAll(tempList1);
        tempList.addAll(tempList2);
        tempList.addAll(tempList3);
        tempList.addAll(tempList4);
        tempList.addAll(tempList5);

        parentDetailsAdapter = new ParentDetailsAdapter(mContext,tempList);
        recyclerView.setAdapter(parentDetailsAdapter);
        parentDetailsAdapter.notifyDataSetChanged();
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            superStateView.setVisibility(View.VISIBLE);
            superStateView.setSubTitleText("No details found");
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
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
}
