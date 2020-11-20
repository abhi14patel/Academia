package com.serosoft.academiassu.Modules.Fees;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;

import com.serosoft.academiassu.Modules.Fees.Adapters.InvoiceAdapter;
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
import java.util.List;

/**
 * Created by Abhishek on September 01 2020.
 */

public class InvoiceActivity extends BaseActivity {

    Context mContext;

    private List<JSONObject> feeList;
    private List<JSONObject> dueFeeList;
    private List<JSONObject> paidFeeList;

    public Toolbar toolbar;
    private SuperStateView superStateView;
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    InvoiceAdapter invoiceAdapter;

    private RadioGroup radioGroup;
    private RadioButton rbAll;
    private RadioButton rbDue;
    private RadioButton rbPaid;
    private Button btnForSelectOrCancel;
    private RelativeLayout rlFeeGroup;

    private int screenMode;                 // 0 for ALL, 1 for Due, 2 for Paid

    String env = "";

    private boolean isPaymentEmpty = false;
    ArrayList<Integer> list = new ArrayList<>();
    int PAY_NOW_VIEW = 0,MAKE_PAYMENT_VIEW = 0;

    private final String TAG = InvoiceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fees_invoice_list_activity);
        ProjectUtils.showLog(TAG,"onCreate Start");

        mContext = InvoiceActivity.this;

        Initialize();

        permissionSetup();

        callEvents();

        populateContents();

        if(PAY_NOW_VIEW == 0){
            btnForSelectOrCancel.setVisibility(View.GONE);
        }else{
            btnForSelectOrCancel.setVisibility(View.VISIBLE);
        }
    }

    private void Initialize()
    {
        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.FEES_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorFees, toolbar, this); }

        superStateView = findViewById(R.id.superStateView);
        recyclerView = findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        rlFeeGroup = findViewById(R.id.rlFeeGroup);
        radioGroup = findViewById(R.id.radioGroup);
        rbAll = findViewById(R.id.rbAll);
        rbDue = findViewById(R.id.rbDue);
        rbPaid = findViewById(R.id.rbPaid);
        btnForSelectOrCancel = findViewById(R.id.btnForSelectOrCancel);
    }

    private void callEvents() {

        btnForSelectOrCancel.setText(translationManager.PAY_KEY);
        rbAll.setText(translationManager.ALL_KEY);
        rbDue.setText(translationManager.DUE_KEY);
        rbPaid.setText(translationManager.PAID_KEY);

        btnForSelectOrCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!isPaymentEmpty){

                    if(MAKE_PAYMENT_VIEW == 1){

                        //Here redirect to make payment tabs
                        Intent intent = new Intent(mContext,MakePaymentActivity.class);
                        startActivity(intent);

                    }else{

                        //Here redirect to make payment tabs
                        Intent intent = new Intent(mContext,MakePaymentActivity2.class);
                        startActivity(intent);
                    }

                }else{
                    showAlertDialog(mContext, getString(R.string.oops), getString(R.string.payment_settings_not_configured_kindly_contact_the_admin));
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                if (rb == rbAll)
                {
                    screenMode = 0;
                    setAdapter();
                }
                else if (rb == rbDue)
                {
                    if (dueFeeList != null && dueFeeList.size() > 0) {
                        screenMode = 1;
                        setAdapter();
                    } else {
                        rbAll.setChecked(true);
                        showAlertDialog(mContext, "", getString(R.string.no_invoices_due));
                    }
                }else {
                    if (paidFeeList != null && paidFeeList.size() > 0) {
                        screenMode = 2;
                        setAdapter();
                    }else{
                        rbAll.setChecked(true);
                        showAlertDialog(mContext, "", getString(R.string.no_invoices_paid));
                    }
                }
            }
        });

        screenMode = 0;
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_STUDENT_INVOICE).execute();
        } else {
            ProjectUtils.showLong(mContext, KEYS.NET_ERROR_MESSAGE);
        }
    }

    private void setAdapter()
    {
        if (feeList != null && feeList.size() != 0)
        {
            rlFeeGroup.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.GONE);
            populateOtherFeeLists();

            if (screenMode == 0)
            {
                setAllInvoicesInAdapter(feeList);
            }
            else if (screenMode == 1)
            {
                setDueInvoicesInAdapter(dueFeeList);
            }
            else if (screenMode == 2)
            {
                setPaidInvoicesInAdapter(paidFeeList);
            }

            populatePaymentGatewayDetails();
        }
        else
        {
            hideProgressDialog();
            rlFeeGroup.setVisibility(View.GONE);
            superStateView.setVisibility(View.VISIBLE);
        }
    }

    private void populatePaymentGatewayDetails() {

        if (Flags.ENABLE_PAYTM_PRODUCTION) {
            env = "LIVE";
        } else {
            env = "STAGING";
        }
        if (ConnectionDetector.isConnectingToInternet(this)) {
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_PAYMENT_GATEWAY).execute(env);
        } else {
            ProjectUtils.showLong(mContext, KEYS.NET_ERROR_MESSAGE);
        }
    }

    private void setAllInvoicesInAdapter(List<JSONObject> feeList)
    {
        invoiceAdapter = new InvoiceAdapter(mContext, feeList);
        recyclerView.setAdapter(invoiceAdapter);

        invoiceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {

                Intent intent = new Intent(mContext, InvoiceDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                JSONObject jsonObject = feeList.get(pos);
                int billId = jsonObject.optInt("billId");
                intent.putExtra("billId", billId);
                intent.putExtra("jsonObject", jsonObject.toString());
                startActivity(intent);
            }
        });
    }

    private void setPaidInvoicesInAdapter(List<JSONObject> feeList)
    {
        invoiceAdapter = new InvoiceAdapter(mContext, paidFeeList);
        recyclerView.setAdapter(invoiceAdapter);

        invoiceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {

                Intent intent = new Intent(mContext, InvoiceDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                JSONObject jsonObject = feeList.get(pos);
                int billId = jsonObject.optInt("billId");
                intent.putExtra("billId", billId);
                intent.putExtra("jsonObject", jsonObject.toString());
                startActivity(intent);
            }
        });
    }

    private void setDueInvoicesInAdapter(List<JSONObject> feeList)
    {
        invoiceAdapter = new InvoiceAdapter(mContext, feeList);
        recyclerView.setAdapter(invoiceAdapter);

        invoiceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {

                Intent intent = new Intent(mContext, InvoiceDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                JSONObject jsonObject = dueFeeList.get(pos);
                int billId = jsonObject.optInt("billId");
                intent.putExtra("billId", billId);
                intent.putExtra("jsonObject", jsonObject.toString());
                startActivity(intent);
            }
        });
    }

    private void populateOtherFeeLists()
    {
        paidFeeList = new ArrayList<>();
        dueFeeList = new ArrayList<>();

        for (int i = 0 ; i < feeList.size() ; ++i)
        {
            JSONObject jsonObject = feeList.get(i);

            Double balanceAmount = jsonObject.optDouble("balanceAmount");

            if (balanceAmount.doubleValue() == 0.0)
            {
                paidFeeList.add(jsonObject);
            } else {
                dueFeeList.add(jsonObject);
            }
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result) {

        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);
        JSONArray arr;

        switch (callFor) {

            case KEYS.SWITCH_STUDENT_INVOICE:
                hideProgressDialog();
                feeList = new ArrayList<>();

                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("whatever")) {
                        arr = responseObject.getJSONArray("whatever");
                    } else {
                        arr = responseObject.getJSONArray("rows");
                    }

                    if (arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            feeList.add(obj);
                        }
                    }

                    setAdapter();

                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgressDialog();
                }
                break;

            case KEYS.SWITCH_PAYMENT_GATEWAY:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult);

                    if (jsonObject.has("environment")) {

                        isPaymentEmpty = false;
                    }

                }catch (Exception ex){
                    ex.printStackTrace();

                    isPaymentEmpty = true;
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

    private void permissionSetup() {

        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        PAY_NOW_VIEW = 0;
        MAKE_PAYMENT_VIEW = 0;

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i)) {
                case Permissions.PARENT_PERMISSION_FEES_INVOICE_MAKEPAYMENT:
                case Permissions.STUDENT_PERMISSION_FEES_INVOICE_MAKEPAYMENT:
                    PAY_NOW_VIEW = 1;
                    break;
                case Permissions.PARENT_MAKE_PAYMENT_VIEW:
                case Permissions.STUDENT_MAKE_PAYMENT_VIEW:
                    MAKE_PAYMENT_VIEW = 1;
                    break;
            }
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
}
