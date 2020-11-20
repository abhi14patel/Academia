package com.serosoft.academiassu.Modules.Circular;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CircularActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private List<JSONObject> circularList;
    private ListView circularLV;
    private CircularListAdapter adapter;
    private LinearLayout layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circular_list);

        Initialize();

        populateContents();

        Bundle bundle = new Bundle();
        bundle.putInt("StudentId", sharedPrefrenceManager.getUserIDFromKey());
        firebaseAnalytics.logEvent(getString(R.string.circular), bundle);
    }

    private void Initialize() {
        Toolbar toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.CIRCULAR_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorNotification, toolbar, this); }
    }

    private void populateCircularList() {
        layoutEmpty = findViewById(R.id.layoutEmpty);
        circularLV = findViewById(R.id.circularLV);
        if ((circularList != null) && (circularList.size() > 0)) {
            setCircular(circularList);
        }else {
            layoutEmpty.setVisibility(View.VISIBLE);
            circularLV.setVisibility(View.GONE);
        }
    }

    private void populateContents() {
        if (ConnectionDetector.isConnectingToInternet(this)) {
            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_CIRCULARS).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }


    private void setCircular(final List<JSONObject> circularList) {
        final List<JSONObject> finalCircularList = circularList;
        circularLV.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
        if(null==adapter){
            adapter = new CircularListAdapter(CircularActivity.this, finalCircularList);
            circularLV.setAdapter(adapter);
        }else {
            adapter.upDateNotificationDBList(finalCircularList);
        }

        circularLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(CircularActivity.this, CirculerDetailActivity.class);

                JSONObject jsonObject = circularList.get(i);
                intent.putExtra("circularObject", jsonObject.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.dashboardMenu:
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
    public void onTaskComplete(HashMap<String, String> response) {
        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        if (callFor == null) {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), "Unexpected Error at " + this.getLocalClassName());
            return;
        }
        switch (callFor) {
            case KEYS.SWITCH_CIRCULARS:
                hideProgressDialog();
                circularList = new ArrayList<>();
                JSONArray arr = new JSONArray();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("rows")) {
                        arr = responseObject.getJSONArray("rows");

                    }
                    if (arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            circularList.add(obj);
                        }
                    }
                    populateCircularList();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlertDialog(this, getString(R.string.oops), e.getMessage());
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
