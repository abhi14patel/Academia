package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.FeePayers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.databinding.FeePayerDetailBinding;

import java.util.HashMap;

public class FeePayerDetailActivity extends BaseActivity {

    FeePayerDetailBinding binding;
    private Context mContext;
    FeePayer_Dto feePayer_dto;
    String emailId = "-";

    private final String TAG = FeePayerDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FeePayerDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = FeePayerDetailActivity.this;

        Initialize();

        Intent intent = getIntent();
        feePayer_dto = (FeePayer_Dto) intent.getSerializableExtra(Consts.SELECTED_DATA);

        setData(feePayer_dto);
    }

    private void setData(FeePayer_Dto item) {

        String payerName = ProjectUtils.getCorrectedString(item.getPayerName());
        binding.tvFeePayerName.setText(payerName);

        String payerType = ProjectUtils.getCorrectedString(item.getPayerType());
        binding.tvFeePayerType.setText(payerType);

        if(payerType.equalsIgnoreCase("Person")){
            emailId = ProjectUtils.getCorrectedString(item.getEmailHome());
        }else{
            emailId = ProjectUtils.getCorrectedString(item.getEmailWork());
        }
        binding.tvEmailId.setText(emailId);

        String workPhone = ProjectUtils.getCorrectedString(item.getWorkTelephone());
        if(!workPhone.equalsIgnoreCase("")){
            binding.tvWorkTelephone.setText(workPhone);
        }else{
            binding.tvWorkTelephone.setText("-");
        }

        String mobileNumber = ProjectUtils.getCorrectedString(item.getMobileNumber());
        binding.tvMobileNumber.setText(mobileNumber);

        String address = ProjectUtils.getCorrectedString(item.getAddress());
        if(!address.equalsIgnoreCase("")){
            binding.tvAddress.setText(address);
        }else{
            binding.tvAddress.setText("-");
        }

        Boolean isConsent = item.getFeePayerConsentToCreditCheck();
        if(isConsent){
            binding.tvCheckCredit.setText("Yes");
        }else {
            binding.tvCheckCredit.setText("No");
        }

        String pathName = ProjectUtils.getCorrectedString(item.getDocName());
        if(!pathName.equalsIgnoreCase(""))
        {
            binding.llDocument.setVisibility(View.VISIBLE);
            binding.tvAttachment.setText(pathName);
            binding.ivAttachment.setImageResource(ProjectUtils.showDocIcon(pathName));
        }else{
            binding.llDocument.setVisibility(View.GONE);
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
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}