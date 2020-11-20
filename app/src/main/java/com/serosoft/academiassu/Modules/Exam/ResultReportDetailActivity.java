package com.serosoft.academiassu.Modules.Exam;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Exam.Adapters.ResultReportTypeAdapter;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReportType_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReport_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultReportDetailActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    public Toolbar toolbar;
    private SuperStateView superStateView;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private TextView tvProgramName,tvMarks,tvWeightage,tvEffectiveMarks,tvModerationMarks;
    private TextView tvMarks1,tvWeightage1,tvEffectiveMarks1,tvModerationMarks1;

    ResultReport_Dto resultReport_dto;
    ArrayList<ResultReportType_Dto> typeList = new ArrayList<>();
    ResultReportTypeAdapter resultReportTypeAdapter;

    private final String TAG = ResultReportDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_result_report_detail_list);
        ProjectUtils.showLog(TAG,"onCreate");
        mContext = ResultReportDetailActivity.this;

        Initialize();

        Intent intent = getIntent();
        resultReport_dto = (ResultReport_Dto) intent.getSerializableExtra(Consts.RESULT_REPORT_LIST);

        setExamResultData(resultReport_dto);
    }

    private void setExamResultData(ResultReport_Dto resultReport_dto) {

        String programName = ProjectUtils.getCorrectedString(resultReport_dto.getTreeNode());
        if(!programName.equalsIgnoreCase("")){
            tvProgramName.setText(programName);
        }

        String obtainedMarks = ProjectUtils.getCorrectedString(resultReport_dto.getObtainedMarksGrade());

        if(obtainedMarks.contains("/")){

            String[] separated = obtainedMarks.split("/");
            String str1 = separated[0];
            String str2 = separated[1];

            obtainedMarks = ("("+str1+")"+str2).trim();
        }

        String maxMarks = ProjectUtils.getCorrectedString(resultReport_dto.getMaxMarksOrGrade());
        if(!obtainedMarks.equalsIgnoreCase("") && !maxMarks.equalsIgnoreCase(""))
        {
            tvMarks.setText(obtainedMarks+" / "+maxMarks);
        }

        double weightage = resultReport_dto.getWeightage();
        tvWeightage.setText(""+weightage);

        String effectiveMarks = ProjectUtils.getCorrectedString(resultReport_dto.getEffetiveMarksGrade());
        if(!effectiveMarks.equalsIgnoreCase("")){
            tvEffectiveMarks.setText(effectiveMarks);
        }

        String moderationMarks = ProjectUtils.getCorrectedString(resultReport_dto.getModerationPoints());
        if(!effectiveMarks.equalsIgnoreCase("")){
            tvModerationMarks.setText(moderationMarks);
        }else{
            tvModerationMarks.setText("-");
        }

        typeList = resultReport_dto.getTypeList();
        if(typeList != null && typeList.size() > 0){

            checkEmpty(false);
            resultReportTypeAdapter = new ResultReportTypeAdapter(mContext,typeList);
            recyclerView.setAdapter(resultReportTypeAdapter);
            resultReportTypeAdapter.notifyDataSetChanged();
        }else{
            checkEmpty(true);
        }
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.refresh).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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

    private void Initialize() {

        superStateView = findViewById(R.id.superStateView);
        toolbar = findViewById(R.id.group_toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        tvProgramName = findViewById(R.id.tvProgramName);
        tvMarks = findViewById(R.id.tvMarks);
        tvWeightage = findViewById(R.id.tvWeightage);
        tvEffectiveMarks = findViewById(R.id.tvEffectiveMarks);
        tvModerationMarks = findViewById(R.id.tvModerationMarks);

        tvMarks1 = findViewById(R.id.tvMarks1);
        tvWeightage1 = findViewById(R.id.tvWeightage1);
        tvEffectiveMarks1 = findViewById(R.id.tvEffectiveMarks1);
        tvModerationMarks1 = findViewById(R.id.tvModerationMarks1);

        toolbar.setTitle(translationManager.RESULT_REPORT_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorExamMark, toolbar, this); }

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        tvMarks1.setText(translationManager.OBTAINEDMARKS_KEY+" / "+translationManager.MAXMARKS_KEY);
        tvWeightage1.setText(translationManager.WEIGHTAGE_KEY);
        tvEffectiveMarks1.setText(translationManager.EFFECTIVEMARKS_KEY);
        tvModerationMarks1.setText(translationManager.MODERATIONMARKS_KEY);
    }
}
