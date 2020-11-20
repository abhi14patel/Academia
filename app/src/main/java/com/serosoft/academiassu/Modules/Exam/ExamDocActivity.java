package com.serosoft.academiassu.Modules.Exam;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamDocActivity extends BaseActivity implements AsyncTaskCompleteListener, View.OnClickListener {

    private Context mContext;
    public Toolbar toolbar;

    private CardView cardMarksheet,cardHallTicket;
    private TextView tvMarksheet,tvHallTicket;
    private LinearLayout layoutEmpty;
    private TextView tvErrorDetails;

    ArrayList<Integer> list = new ArrayList<>();

    private final String TAG = ExamDocActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_doc_activity);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = ExamDocActivity.this;

        Initialize();

        if(Flags.DISEABLE_PERMISSION_FOR_TAIPEI){
            cardMarksheet.setVisibility(View.VISIBLE);
            cardHallTicket.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }else {
            permissionSetup();
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.EXAM_DOC_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorExamMark, toolbar, this); }

        cardMarksheet = findViewById(R.id.cardMarksheet);
        cardHallTicket = findViewById(R.id.cardHallTicket);
        tvMarksheet = findViewById(R.id.tvMarksheet);
        tvHallTicket = findViewById(R.id.tvHallTicket);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        tvErrorDetails = findViewById(R.id.tvErrorDetails);

        tvErrorDetails.setText("No Records Found");
        tvMarksheet.setText(translationManager.MARKSHEET_KEY);
        tvHallTicket.setText(translationManager.HALLTICKET_KEY);

        cardMarksheet.setOnClickListener(this);
        cardHallTicket.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.cardMarksheet:{
                ProjectUtils.preventTwoClick(cardMarksheet);
                Intent intent = new Intent(mContext, MarksheetActivity.class);
                startActivity(intent);
            }break;

            case R.id.cardHallTicket:{
                ProjectUtils.preventTwoClick(cardHallTicket);
                Intent intent = new Intent(mContext, HallTicketActivity.class);
                startActivity(intent);
            }break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:{
                onBackPressed();
            }break;

            case R.id.dashboardMenu:{
                onBackPressed();
            }break;

            case R.id.refresh:{
                getNotifications();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        if (callFor == null) {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), getString(R.string.unexpected_error));
            return;
        }
        switch (callFor) {
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

    private void permissionSetup() {

        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        cardHallTicket.setVisibility(View.GONE);
        cardMarksheet.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i)) {
                case Permissions.PARENT_EXAMINATION_MARKSHEET_VIEW:
                case Permissions.STUDENT_EXAMINATION_MARKSHEET_VIEW:
                    cardMarksheet.setVisibility(View.VISIBLE);
                    layoutEmpty.setVisibility(View.GONE);
                    break;
                case Permissions.PARENT_EXAMINATION_HALLTICKET_VIEW:
                case Permissions.STUDENT_EXAMINATION_HALLTICKET_VIEW:
                    cardHallTicket.setVisibility(View.VISIBLE);
                    layoutEmpty.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
