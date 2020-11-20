package com.serosoft.academiassu.Modules.Fees;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.serosoft.academiassu.Manager.FeeManager;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.Models.CurrencyDto;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Abhishek on September 01 2020.
 */

public class FeesMainActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;
    private FeeManager feeManager;

    private List<JSONObject> feeList;
    private List<JSONObject> receiptList;

    private TextView tvBilled,tvReceipt,tvTotalOutStanding;
    private TextView tvInvoiceTotal,tvReceiptTotal,tvTotalOutStandingAmount;
    private AppCompatImageView ivInvoice,ivReceipt;
    private LinearLayout llBilled,llReceipt,llTotalOutstanding;

    private double totalFeeAmount = 0;
    private double totalReceipt = 0;
    private double totalOutStandingDouble = 0;

    String currencySymbol = "";

    private final String TAG = FeesMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fees_main);
        ProjectUtils.showLog(TAG,"onCreate start");
        mContext = FeesMainActivity.this;

        Initialize();

        feeManager = new FeeManager(this);

        populateContents();

        Bundle bundle = new Bundle();
        bundle.putInt("StudentId", sharedPrefrenceManager.getUserIDFromKey());
        firebaseAnalytics.logEvent(getString(R.string.fees), bundle);
    }

    private void populateContents()
    {
        if (ConnectionDetector.isConnectingToInternet(this))
        {
            llBilled.setVisibility(View.GONE);
            llReceipt.setVisibility(View.GONE);
            llTotalOutstanding.setVisibility(View.GONE);

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_STUDENT_FEES).execute();
        }
        else
        {
            ProjectUtils.showLong(mContext, KEYS.NET_ERROR_MESSAGE);
        }
    }

    private void Initialize()
    {
        toolbar = findViewById(R.id.group_toolbar);
        tvBilled = findViewById(R.id.tvBilled);
        tvReceipt = findViewById(R.id.tvReceipt);
        tvTotalOutStanding = findViewById(R.id.tvTotalOutStanding);

        tvInvoiceTotal = findViewById(R.id.tvInvoiceTotal);
        tvReceiptTotal = findViewById(R.id.tvReceiptTotal);
        tvTotalOutStandingAmount = findViewById(R.id.tvTotalOutStandingAmount);

        ivInvoice = findViewById(R.id.ivInvoice);
        ivReceipt = findViewById(R.id.ivReceipt);

        llBilled = findViewById(R.id.llBilled);
        llReceipt = findViewById(R.id.llReceipt);
        llTotalOutstanding = findViewById(R.id.llTotalOutstanding);

        toolbar.setTitle(translationManager.FEES_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorFees, toolbar, this); }

        tvBilled.setText(translationManager.BILLED_KEY);
        tvReceipt.setText(translationManager.RECEIPT_KEY);
        tvTotalOutStanding.setText(translationManager.TOTAL_OUTSTANDING_KEY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.dashboardMenu:
                onBackPressed();
                return true;
            case R.id.refresh:
                populateContents();
                getNotifications();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result)
    {
        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);

        switch (callFor)
        {
            case KEYS.SWITCH_STUDENT_FEES:

                feeList = new ArrayList<>();
                JSONArray arr;
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("whatever"))
                    {
                        arr = responseObject.getJSONArray("whatever");
                    }
                    else
                    {
                        arr = responseObject.getJSONArray("rows");
                    }

                    if (arr.length() > 0)
                    {
                        for (int i = 0; i < arr.length(); i++)
                        {
                            JSONObject obj = arr.getJSONObject(i);
                            feeList.add(obj);
                        }
                    }

                    if (ConnectionDetector.isConnectingToInternet(this))
                    {
                        new OptimizedServerCallAsyncTask(this,
                                this, KEYS.SWITCH_STUDENT_RECEIPTS).execute();
                    } else {
                        hideProgressDialog();
                        ProjectUtils.showLong(mContext, KEYS.NET_ERROR_MESSAGE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    hideProgressDialog();
                    showAlertDialog(this, getString(R.string.oops), getString(R.string.something_went_wrong));
                }
                break;

            case KEYS.SWITCH_STUDENT_RECEIPTS:
                hideProgressDialog();
                receiptList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("whatever"))
                    {
                        arr = responseObject.getJSONArray("whatever");
                    } else {
                        arr = responseObject.getJSONArray("rows");
                    }
                    if (arr.length() > 0)
                    {
                        for (int i = 0; i < arr.length(); i++)
                        {
                            JSONObject obj = arr.getJSONObject(i);
                            receiptList.add(obj);
                        }
                    }

                    setAdapter();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    hideProgressDialog();
                    showAlertDialog(this, getString(R.string.oops), getString(R.string.something_went_wrong));
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

    public double getTotalOutStanding() {
        double tos = 0;
        for (int i = 0 ; i < feeList.size() ; ++i) {
            JSONObject jsonObject = feeList.get(i);
            double os = jsonObject.optDouble("balanceAmount");
            tos = tos + os;
        }
        return tos;
    }


    private void setAdapter() {

        setFeeAndReceipt(feeList, receiptList);
    }

    private void setFeeAndReceipt(List<JSONObject> feeList, List<JSONObject> receiptList) {

        totalFeeAmount = 0;
        totalReceipt = 0;

        ArrayList<Integer> currencyIds = new ArrayList<>();

        if (null != feeList) {

            for (int i = 0; i < feeList.size(); i++) {

                totalFeeAmount += feeManager.getFeeAmount(feeList.get(i));

                currencyIds.add(feeManager.getCurrencyId(feeList.get(i)));
            }

            llBilled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FeesMainActivity.this, InvoiceActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (null != receiptList) {

            for (int i = 0; i < receiptList.size(); i++)
            {
                totalReceipt += feeManager.getReceiptAmount(receiptList.get(i));

                currencyIds.add(feeManager.getCurrencyId(receiptList.get(i)));
            }

            llReceipt.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(FeesMainActivity.this, ReceiptActivity.class);
                    startActivity(intent);
                }
            });
        }

        if(currencyIds != null && currencyIds.size() > 0){

            String currencyId = "";

            //Converting ArrayList to HashSet to remove duplicates
            HashSet<Integer> listToSet = new HashSet<Integer>(currencyIds);

            if(listToSet.size() == 1){

                updateUI(false);

                //Here get currency according to currencycode
                Iterator iterator = listToSet.iterator();

                // check values
                while (iterator.hasNext()) {
                    currencyId  = String.valueOf(iterator.next());
                }

                ArrayList<CurrencyDto> currencyList = dbHelper.getAllData();

                if(currencyList != null && currencyList.size() > 0){
                    for(CurrencyDto currencyDto : currencyList){
                        if(currencyDto.getId().contains(currencyId)){
                            String curCode = currencyDto.getCurrencyCode();
                            currencySymbol = Utils.getCurrencySymbol(curCode);
                        }
                    }
                }
                else{
                    currencySymbol = sharedPrefrenceManager.getCurrencySymbolFromKey();
                }

                if (totalFeeAmount != 0) {

                    tvInvoiceTotal.setText(currencySymbol + BaseActivity.doublePrecision(totalFeeAmount));
                }
                else if (totalFeeAmount == 0) {

                    tvInvoiceTotal.setText(currencySymbol + BaseActivity.doublePrecision(totalFeeAmount));
                }

                if (totalReceipt != 0) {

                    tvReceiptTotal.setText(currencySymbol + BaseActivity.doublePrecision(totalReceipt));
                }
                else if (totalReceipt == 0) {

                    tvReceiptTotal.setText(currencySymbol + BaseActivity.doublePrecision(totalReceipt));
                }

                totalOutStandingDouble = getTotalOutStanding();
                if (totalOutStandingDouble >= 0) {

                    tvTotalOutStandingAmount.setText(currencySymbol + BaseActivity.doublePrecision(totalOutStandingDouble));
                }
                else if ((totalFeeAmount - totalReceipt) < 0) {

                    tvTotalOutStandingAmount.setVisibility(View.GONE);
                }

            }else{
                updateUI(true);
            }
        }else {
            updateUI(false);
        }

    }

    private void updateUI(boolean b) {

        llBilled.setVisibility(View.VISIBLE);
        llReceipt.setVisibility(View.VISIBLE);

        if(b){
            tvInvoiceTotal.setVisibility(View.GONE);
            ivInvoice.setVisibility(View.VISIBLE);
            tvReceiptTotal.setVisibility(View.GONE);
            ivReceipt.setVisibility(View.VISIBLE);
            llTotalOutstanding.setVisibility(View.GONE);
        }else{
            tvInvoiceTotal.setVisibility(View.VISIBLE);
            ivInvoice.setVisibility(View.GONE);
            tvReceiptTotal.setVisibility(View.VISIBLE);
            ivReceipt.setVisibility(View.GONE);
            llTotalOutstanding.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}