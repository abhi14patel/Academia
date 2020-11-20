package com.serosoft.academiassu.Modules.MyRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.AcademiaApp;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.MyRequest.Adapters.MyRequestAdapter;
import com.serosoft.academiassu.Modules.MyRequest.Dialogs.MyRequestFilterDialog;
import com.serosoft.academiassu.Modules.MyRequest.Dialogs.RaiseRequestDialog;
import com.serosoft.academiassu.Modules.MyRequest.Models.MyRequestType_Dto;
import com.serosoft.academiassu.Modules.MyRequest.Models.MyRequest_Dto;
import com.serosoft.academiassu.Modules.MyRequest.Models.RequestFilter_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.Networking.ServiceCalls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek on May 07 2020.
 */

public class MyRequestMainActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;
    private TextView tvRaiseRequest;
    ServiceCalls serviceCalls;

    private AppCompatImageView ivFilter;
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    private ArrayList<MyRequest_Dto> myRequestList;
    ArrayList<String> requestIdList;
    MyRequestAdapter myRequestAdapter;

    ArrayList<RequestFilter_Dto> requestFilterList;
    ArrayList<MyRequestType_Dto> myRequestTypeList;

    private static final int FILTER_DIALOG = 1001;

    private final String TAG = MyRequestMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myrequest_main_list);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = MyRequestMainActivity.this;
        serviceCalls = new ServiceCalls();

        Initialize();

        getRequesterData();
    }

    private void getRequesterData() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            sharedPrefrenceManager.clearPreferences(Consts.REQUEST_ID_LIST);

            populateContent(requestFilterList);

        }else{
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(AcademiaApp.IS_UPDATE){
            getRequesterData();
        }

        AcademiaApp.IS_UPDATE = false;
    }

    private void populateContent(ArrayList<RequestFilter_Dto> requestFilterList) {

        showProgressDialog(this);

        //Here do api calling
        String url = BaseURL.BASE_URL + KEYS.MY_REQUEST_LIST_METHOD;

        String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
        String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();
        String str = tokenType + " " + accessToken;

        JSONObject jsonObject = serviceCalls.getJSONMyRequestService(mContext,requestFilterList);

        AndroidNetworking.post(url)
                .setOkHttpClient(ProjectUtils.getUnsafeOkHttpClient())
                .addHeaders("Authorization",str)
                .addHeaders("Content-Type","application/json")
                .addJSONObjectBody(jsonObject)                      // posting json
                .addQueryParameter("page","1")
                .addQueryParameter("start","0")
                .addQueryParameter("limit","-1")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();

                        loadRaiseRequest();

                        JSONArray jsonArray = response.optJSONArray("rows");

                        if(jsonArray != null && jsonArray.length() > 0) {
                            checkEmpty(false);
                            myRequestList = new ArrayList<>();
                            requestIdList = new ArrayList<>();

                            for(int i = 0 ; i<jsonArray.length() ; i++){

                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                int id = jsonObject1.optInt("ID");
                                String requestBy = jsonObject1.optString("REQUEST_BY");
                                String requestId = jsonObject1.optString("REQUEST_ID");
                                String requestAssignedTo = jsonObject1.optString("REQUEST_ASSIGNED_TO");
                                String serviceRequestStatus = jsonObject1.optString("SERVICE_REQUEST_STATUS");
                                String requestType = jsonObject1.optString("REQUEST_TYPE");
                                String requestDate = jsonObject1.optString("REQUEST_DATE");

                                MyRequest_Dto list = new MyRequest_Dto(id,requestBy,requestId,requestAssignedTo,serviceRequestStatus,requestType,requestDate);
                                myRequestList.add(list);
                                requestIdList.add(requestId);
                            }

                            myRequestAdapter = new MyRequestAdapter(mContext,myRequestList);
                            recyclerView.setAdapter(myRequestAdapter);
                            myRequestAdapter.notifyDataSetChanged();
                            sharedPrefrenceManager.setRequestIdList(Consts.REQUEST_ID_LIST,requestIdList);
                        }else{
                            checkEmpty(true);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        checkEmpty(true);
                        hideProgressDialog();
                        ProjectUtils.showLog(TAG,""+anError.getMessage());
                        loadRaiseRequest();
                    }
                });
    }

    private void loadRaiseRequest() {

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_RAISE_REQUEST_TYPE).execute(Consts.MY_REQUEST_TYPE_OTHER);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id)
        {
            case R.id.ivFilter:
            {
                ProjectUtils.preventTwoClick(ivFilter);
                Intent intent = new Intent(mContext, MyRequestFilterDialog.class);
                intent.putExtra(Consts.REQUEST_FILTER_LIST,requestFilterList);
                startActivityForResult(intent,FILTER_DIALOG);
            }break;

            case R.id.tvRaiseRequest:
            {
                ProjectUtils.preventTwoClick(tvRaiseRequest);
                Intent intent = new Intent(mContext, RaiseRequestDialog.class);
                intent.putExtra(Consts.MY_REQUEST_TYPE_LIST,myRequestTypeList);
                startActivity(intent);
            }break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FILTER_DIALOG && resultCode == RESULT_OK && data != null)
        {
            requestFilterList = (ArrayList<RequestFilter_Dto>) data.getSerializableExtra("requestFilter");

            populateContent(requestFilterList);
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

            case KEYS.SWITCH_RAISE_REQUEST_TYPE:
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(responseResult.toString());

                    JSONArray jsonArray = jsonObject.optJSONArray("whatever");

                    if(jsonArray != null && jsonArray.length() > 0) {

                        tvRaiseRequest.setVisibility(View.VISIBLE);

                        myRequestTypeList = new ArrayList<>();

                        for(int i = 0 ; i<jsonArray.length() ; i++){

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String value = jsonObject1.optString("value");
                            int id = jsonObject1.optInt("id");

                            myRequestTypeList.add(new MyRequestType_Dto(value,id));
                        }
                    }else{
                        tvRaiseRequest.setVisibility(View.INVISIBLE);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    tvRaiseRequest.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    private void Initialize() {

        requestFilterList = new ArrayList<>();

        superStateView = findViewById(R.id.superStateView);
        toolbar = findViewById(R.id.group_toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        ivFilter = findViewById(R.id.ivFilter);
        tvRaiseRequest = findViewById(R.id.tvRaiseRequest);

        toolbar.setTitle(translationManager.MY_REQUESTS_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorMyRequest, toolbar, this); }

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && ivFilter.getVisibility() == View.VISIBLE) {
                    ivFilter.setVisibility(View.INVISIBLE);
                } else if (dy < 0 && ivFilter.getVisibility() != View.VISIBLE) {
                    ivFilter.setVisibility(View.VISIBLE);
                }
            }
        });

        ivFilter.setOnClickListener(this);
        tvRaiseRequest.setOnClickListener(this);

        tvRaiseRequest.setText(translationManager.RAISE_REQUEST_KEY);
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
                populateContent(requestFilterList);
            }break;
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
