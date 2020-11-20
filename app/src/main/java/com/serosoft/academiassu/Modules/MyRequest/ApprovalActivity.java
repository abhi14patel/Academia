package com.serosoft.academiassu.Modules.MyRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.MyRequest.Adapters.ApprovalAdapter;
import com.serosoft.academiassu.Modules.MyRequest.Models.Approval_Dto;

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

public class ApprovalActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;
    private SuperStateView superStateView;
    private TextView tvUserName;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    ArrayList<Approval_Dto> approvalList;
    ApprovalAdapter approvalAdapter;

    String requestId = "";

    private final String TAG = ApprovalActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_list);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = ApprovalActivity.this;

        Initialize();

        Intent intent = getIntent();
        requestId = intent.getStringExtra(Consts.REQUEST_ID);

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_APPROVAL_DETAILS).execute(requestId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        superStateView = findViewById(R.id.superStateView);
        toolbar = findViewById(R.id.group_toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        tvUserName = findViewById(R.id.tvUserName);

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        toolbar.setTitle(translationManager.APPROVAL_DETAILS_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorMyRequest, toolbar, this); }
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
            case KEYS.SWITCH_APPROVAL_DETAILS:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("rows");

                    if(jsonArray != null && jsonArray.length() > 0){

                        checkEmpty(false);
                        String user = "";
                        approvalList = new ArrayList<>();

                        for(int i = 0 ; i < jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            long actionDate = jsonObject1.optLong("actionDate");
                            String remarks = jsonObject1.optString("remarks");
                            String actionType = jsonObject1.optString("actionType");

                            JSONObject jsonObject2 = jsonObject1.optJSONObject("user");
                            if(jsonObject2 != null){
                                user = jsonObject2.optString("value");
                            }

                            approvalList.add(new Approval_Dto(actionDate,user,remarks,actionType,""));
                        }

                        tvUserName.setText(translationManager.ACTION_TAKEN_BY_KEY+" : "+user);

                        approvalAdapter = new ApprovalAdapter(mContext,approvalList);
                        recyclerView.setAdapter(approvalAdapter);
                        approvalAdapter.notifyDataSetChanged();

                    }else{
                        checkEmpty(true);
                    }

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                    checkEmpty(true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.refresh);
        item.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.dashboardMenu:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            case R.id.refresh:
                getNotifications();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            superStateView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
