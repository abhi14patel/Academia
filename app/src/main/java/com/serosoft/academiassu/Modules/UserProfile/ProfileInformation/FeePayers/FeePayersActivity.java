package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.FeePayers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.serosoft.academiassu.Helpers.AcademiaApp;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;

import com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.FeePayers.AddFeePayer.AddFeePayerActivity;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.databinding.FeePayerListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek 07/10/20
 * View Binding
 */

public class FeePayersActivity extends BaseActivity {

    FeePayerListBinding binding;
    private Context mContext;
    LinearLayoutManager linearLayoutManager;
    ArrayList<FeePayer_Dto> showList;
    FeePayerAdapter feePayerAdapter;

    private final String TAG = FeePayersActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FeePayerListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = FeePayersActivity.this;

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
                    this, KEYS.SWITCH_FEE_PAYER_LIST).execute();
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

        binding.includeTB.groupToolbar.setTitle(getString(R.string.fee_payers).toUpperCase());
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

        binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(mContext, AddFeePayerActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
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

            case KEYS.SWITCH_FEE_PAYER_LIST:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.getJSONArray("rows");

                    if(jsonArray != null && jsonArray.length() > 0) {

                        checkEmpty(false);
                        showList = new ArrayList<>();

                        int id = 0;
                        String name = "";
                        String path = "";

                        for(int i = 0 ; i<jsonArray.length() ; i++) {

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String payerType = jsonObject1.optString("payerType");
                            String payerName = jsonObject1.optString("payerName");

                            String workTelephoneCountryCode = ProjectUtils.getCorrectedString(jsonObject1.optString("workTelephoneCountryCode"));
                            String workTelephone = ProjectUtils.getCorrectedString(jsonObject1.optString("workTelephone"));
                            String workTelephoneNumber = workTelephoneCountryCode+" "+workTelephone;

                            String countryCode = ProjectUtils.getCorrectedString(jsonObject1.optString("countryCode"));
                            String moNumber = ProjectUtils.getCorrectedString(jsonObject1.optString("mobileNumber"));
                            String mobileNumber = countryCode+" "+moNumber;

                            String emailWork = jsonObject1.optString("emailWork");
                            String emailHome = jsonObject1.optString("emailHome");

                            Boolean isFeePayerConsentToCreditCheck = jsonObject1.optBoolean("doesFeePayerConsentToCreditCheck");
                            String address = jsonObject1.optString("addressTextArea");

                            JSONObject documents = jsonObject1.optJSONObject("feePayerId");

                            if(documents != null){

                                id = documents.optInt("id");
                                name = documents.optString("name");
                                path = documents.optString("path");
                            }else{
                                id = 0;
                                name = "";
                                path = "";
                            }

                            FeePayer_Dto list = new FeePayer_Dto(payerType,payerName,workTelephoneNumber,mobileNumber,emailWork,emailHome,isFeePayerConsentToCreditCheck,address,id,path,name);
                            showList.add(list);
                        }

                        feePayerAdapter = new FeePayerAdapter(mContext,showList);
                        binding.includeRV.recyclerView.setAdapter(feePayerAdapter);

                        feePayerAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                FeePayer_Dto item = showList.get(position);

                                Intent intent = new Intent(mContext, FeePayerDetailActivity.class);
                                intent.putExtra(Consts.SELECTED_DATA,item);
                                startActivity(intent);
                            }
                        });
                    }
                    else{
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