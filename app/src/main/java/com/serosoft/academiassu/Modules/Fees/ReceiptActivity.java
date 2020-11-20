package com.serosoft.academiassu.Modules.Fees;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Fees.Adapters.ReceiptAdapter;
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
 * Created by Abhishek on September 01 2020.
 */

public class ReceiptActivity extends BaseActivity {

    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    private List<JSONObject> receiptList;
    ReceiptAdapter receiptAdapter;

    private String receiptID;
    private String viewOrDownload;
    String receiptStatus = "";

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private final String TAG = ReceiptActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fees_receipt_list_activity);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = ReceiptActivity.this;

        Initialize();

        populateContents();
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_STUDENT_RECEIPTS).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.RECEIPT_KEY.toUpperCase());
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

    @Override
    public void onTaskComplete(HashMap<String, String> result) {
        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);
        JSONArray arr;

        switch (callFor) {
            case KEYS.SWITCH_STUDENT_RECEIPTS:
                hideProgressDialog();
                receiptList = new ArrayList<>();
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
                            receiptList.add(obj);
                        }

                        receiptAdapter = new ReceiptAdapter(mContext, receiptList);
                        recyclerView.setAdapter(receiptAdapter);

                        receiptAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                ProjectUtils.preventTwoClick(view);

                                JSONObject jsonObject = receiptList.get(position);
                                String receiptID = String.valueOf(jsonObject.optInt("receiptHeaderId"));

                                receiptStatus = jsonObject.optString("receiptStatus");
                                downloadReceipt(receiptID, KEYS.TRUE);
                            }
                        });

                    }else{
                        checkEmpty(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    checkEmpty(true);
                }
                break;

            case KEYS.SWITCH_RECEIPT_DOWNLOAD:
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
                            intent.putExtra("screenName",translationManager.RECEIPT_KEY);
                            intent.putExtra("idForDocument",receiptID);
                            intent.putExtra("folderName", Consts.FEES);
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

                        Boolean downloaded = jsonObject.optBoolean("downloaded");
                        if (downloaded)
                        {
                            String path = jsonObject.optString("path");
                            openFile(new File(path), this);
                        }
                    }
                    catch (Exception e)
                    {
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

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            superStateView.setVisibility(View.VISIBLE);
            superStateView.setSubTitleText("No Receipt Found!");
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.INVISIBLE);
        }
    }

    public void downloadReceipt(String receiptID, String viewOrDownload) {

        this.receiptID = receiptID;
        this.viewOrDownload = viewOrDownload;

        if (ProjectUtils.hasPermissionInManifest(ReceiptActivity.this,PERMISSION_CODE,permission))
        {
            callAPIWithPermission();
        }
    }

    private void callAPIWithPermission() {

        showProgressDialog(mContext);
        new OptimizedServerCallAsyncTask(mContext,
                this, KEYS.SWITCH_RECEIPT_DOWNLOAD).execute(String.valueOf(receiptID), viewOrDownload, receiptStatus);
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

                            if (ProjectUtils.hasPermissionInManifest(ReceiptActivity.this,PERMISSION_CODE,permission))
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
}
