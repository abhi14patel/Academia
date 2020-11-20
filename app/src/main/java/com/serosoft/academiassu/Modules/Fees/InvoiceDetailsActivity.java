package com.serosoft.academiassu.Modules.Fees;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Manager.FeeManager;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.Assignment_Dto;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Dashboard.Models.CurrencyDto;

import com.serosoft.academiassu.Modules.Fees.Adapters.InvoiceDetailAdapter;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.PDFActivity;
import com.serosoft.academiassu.Utils.PDFActivity2;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Abhishek on September 02 2020.
 */

public class InvoiceDetailsActivity extends BaseActivity {

    Context mContext;

    private FeeManager feeManager;
    private ArrayList<InvoiceDetails_Dto> feeList;
    private ArrayList<InvoiceDetails_Dto> tempList;
    InvoiceDetailAdapter invoiceDetailAdapter;

    public Toolbar toolbar;
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    private TextView tvInvoiceTitle,tvInvoiceStatus,tvBillDate,tvDueDate,tvTotalAmount,tvBalanceAmount;
    private TextView tvTotalAmount1,tvBalanceAmount1;
    private RelativeLayout rlTopView,rlFeeGroup;
    private TextView tvError;

    private String viewOrDownload;
    private String billId;
    private String jsonObject = "";

    String currencySymbol = "";
    String amountStatus = "";

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = InvoiceDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fees_invoice_detail_list_activity);
        ProjectUtils.showLog(TAG,"onCreate Start");

        mContext = InvoiceDetailsActivity.this;

        Initialize();

        feeManager = new FeeManager(this);

        Intent intent = getIntent();
        billId = String.valueOf(intent.getIntExtra("billId", -1));
        jsonObject = intent.getStringExtra("jsonObject");

        getJsonData(jsonObject);

        populateContents();
    }

    private void getJsonData(String jObject) {

        try {
            JSONObject jsonObject = new JSONObject(jObject);

            String invoiceTitle = ProjectUtils.getCorrectedString(feeManager.getFeeHead(jsonObject));
            if (!invoiceTitle.equalsIgnoreCase("")) {
                tvInvoiceTitle.setText(translationManager.BILL_NUMBER_KEY+" "+ invoiceTitle);
            }

            String billDate = ProjectUtils.getCorrectedString(feeManager.getFeeDate(jsonObject));
            if (!billDate.equalsIgnoreCase("")) {
                tvBillDate.setText(translationManager.BILL_DATE_KEY+" "+ billDate);
            }

            String dueDate = ProjectUtils.getCorrectedString(feeManager.getFeeDueDate(jsonObject));
            if (!dueDate.equalsIgnoreCase("")) {
                tvDueDate.setText(translationManager.DUE_DATE_KEY+" "+ dueDate);
            }

            //Here get currency according to currencycode
            String id = String.valueOf(feeManager.getCurrencyId(jsonObject));
            ArrayList<CurrencyDto> currencyList = dbHelper.getAllData();

            if(currencyList != null && currencyList.size() > 0){
                for(CurrencyDto currencyDto : currencyList){
                    if(currencyDto.getId().contains(id)){
                        String curCode = currencyDto.getCurrencyCode();
                        currencySymbol = BaseActivity.Utils.getCurrencySymbol(curCode);
                    }
                }
            }
            else{
                SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
                currencySymbol = sharedPrefrenceManager.getCurrencySymbolFromKey();
            }

            Double balanceAmount = feeManager.getBalanceAmount(jsonObject);
            Double totalAmount = feeManager.getTotalAmount(jsonObject);

            tvBalanceAmount.setText(currencySymbol + balanceAmount);
            tvTotalAmount.setText(currencySymbol + totalAmount);

            String status = feeManager.getFeesStatus(jsonObject);
            if(status.equalsIgnoreCase("Paid")){

                amountStatus = translationManager.PAID_KEY;
            }else if(status.equalsIgnoreCase("Fully Pending")){

                amountStatus = translationManager.FULLY_PENDING_KEY;
            }else if(status.equalsIgnoreCase("Partially Settled")){

                amountStatus = translationManager.PARTIALLY_SETTLED_KEY;
            }
            tvInvoiceStatus.setText(amountStatus);
            tvInvoiceStatus.setTextColor(feeManager.getFeesStatusColor(jsonObject));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.FEES_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorFees, toolbar, this); }

        recyclerView = findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        tvError = findViewById(R.id.tvError);
        tvInvoiceTitle = findViewById(R.id.tvInvoiceTitle);
        tvInvoiceStatus = findViewById(R.id.tvInvoiceStatus);
        tvBillDate = findViewById(R.id.tvBillDate);
        tvDueDate = findViewById(R.id.tvDueDate);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvBalanceAmount = findViewById(R.id.tvBalanceAmount);

        tvTotalAmount1 = findViewById(R.id.tvTotalAmount1);
        tvBalanceAmount1 = findViewById(R.id.tvBalanceAmount1);

        tvTotalAmount1.setText(translationManager.TOTAL_AMOUNT_KEY);
        tvBalanceAmount1.setText(translationManager.BALANCE_AMOUNT_KEY);

        rlTopView = findViewById(R.id.rlTopView);
        rlFeeGroup = findViewById(R.id.rlFeeGroup);

        rlTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadInvoice(String.valueOf(billId), KEYS.TRUE);
            }
        });
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(mContext,this, KEYS.SWITCH_INVOICE_DETAILS).execute(String.valueOf(billId));

        }else{

            ProjectUtils.showLong(mContext, KEYS.NET_ERROR_MESSAGE);
        }
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            rlFeeGroup.setVisibility(View.INVISIBLE);
            tvError.setVisibility(View.VISIBLE);
        }
        else
        {
            rlFeeGroup.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.INVISIBLE);
        }
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
                populateContents();
                getNotifications();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void downloadInvoice(String billId, String viewOrDownload) {

        this.billId = billId;
        this.viewOrDownload = viewOrDownload;

        if (ProjectUtils.hasPermissionInManifest(InvoiceDetailsActivity.this,PERMISSION_CODE,permission))
        {
            callAPIWithPermission();
        }
    }

    private void callAPIWithPermission() {

        showProgressDialog(mContext);
        new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_INVOICE_DOWNLOAD).execute(billId, viewOrDownload);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        boolean hasAllPermissions = false;

        switch (requestCode)
        {
            case PERMISSION_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    hasAllPermissions = true;
                }
                else
                {
                    hasAllPermissions = false;

                    ProjectUtils.showDialog(mContext, getString(R.string.app_name), getString(R.string.permission_msg), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if (ProjectUtils.hasPermissionInManifest(InvoiceDetailsActivity.this,PERMISSION_CODE,permission))
                            {
                                callAPIWithPermission();
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },false);
                }

                if(hasAllPermissions) callAPIWithPermission();
                break;
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result) {

        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);

        switch (callFor) {

            case KEYS.SWITCH_INVOICE_DETAILS:

                hideProgressDialog();
                feeList = new ArrayList<>();
                JSONArray arr;
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("whatever")) {
                        arr = responseObject.getJSONArray("whatever");
                    } else {
                        arr = responseObject.getJSONArray("rows");
                    }

                    if(arr != null && arr.length() > 0) {

                        checkEmpty(false);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            String groupingFeeHeadName = obj.optString("groupingFeeHeadName");
                            String feeHead = obj.optString("feeHead");
                            Double balanceAmount = obj.optDouble("balanceAmount");
                            Double feeAmount = obj.optDouble("feeAmount");
                            Double discountAmount = obj.optDouble("discountAmount");
                            Double adjustedAmount = obj.optDouble("adjustedAmount");
                            Double amountPending = obj.optDouble("amountPending");
                            int currencyId = obj.optInt("currencyId");

                            feeList.add(new InvoiceDetails_Dto(groupingFeeHeadName,feeHead,balanceAmount,feeAmount,discountAmount,adjustedAmount,amountPending,currencyId));
                        }

                        invoiceDetailAdapter = new InvoiceDetailAdapter(mContext, feeList);
                        recyclerView.setAdapter(invoiceDetailAdapter);

                    }else{
                        checkEmpty(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    checkEmpty(true);
                }
                break;

            case KEYS.SWITCH_INVOICE_DOWNLOAD:
                hideProgressDialog();
                if (viewOrDownload.equals(KEYS.TRUE))
                {
                    try {
                        JSONObject jsonObject = new JSONObject(responseResult);
                        Boolean downloaded = jsonObject.optBoolean("downloaded");
                        String message = jsonObject.optString("message");
                        if (downloaded)
                        {
                            String path = jsonObject.optString("path");
                            Intent intent=new Intent(this, PDFActivity.class);
                            intent.putExtra("filePath",path);
                            intent.putExtra("screenName",translationManager.INVOICE_KEY);;
                            intent.putExtra("folderName", Consts.FEES);
                            intent.putExtra("idForDocument",String.valueOf(billId));
                            intent.putExtra("headerColor",R.color.colorFees);
                            startActivity(intent);
                        }else{
                            ProjectUtils.showLong(mContext,message);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        showAlertDialog(this, getString(R.string.oops), getString(R.string.something_went_wrong));
                    }
                }
                else
                {
                    try {
                        JSONObject jsonObject = new JSONObject(responseResult);
                        String message = jsonObject.optString("message");
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                        boolean downloaded = jsonObject.optBoolean("downloaded");
                        if (downloaded)
                        {
                            String path = jsonObject.optString("path");
                            openFile(new File(path), this);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
}
