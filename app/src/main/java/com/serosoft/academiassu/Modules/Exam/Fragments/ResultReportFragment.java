package com.serosoft.academiassu.Modules.Exam.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Exam.Adapters.ResultReportAdapter1;
import com.serosoft.academiassu.Modules.Exam.Models.ExamResult_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReportEvent_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReportMethod_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReportSubType_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReportType_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.ResultReport_Dto;
import com.serosoft.academiassu.Modules.Exam.ResultReportDetailActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultReportFragment extends Fragment implements AsyncTaskCompleteListener {

    private Context mContext;
    private AppCompatImageView ivFilter;
    private TextView tvProgramName,tvBatch,tvPeriod;
    private TextView tvBatch1,tvPeriod1;
    private LinearLayout llPeriod;

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private View emptyView;
    private TextView tvEmptyDetails;

    private ArrayList<ResultReport_Dto> resultReportList;
    private ArrayList<ResultReportType_Dto> resultReportTypeList;
    private ArrayList<ResultReportSubType_Dto> resultReportSubTypeList;
    private ArrayList<ResultReportMethod_Dto> resultReportMethodList;
    private ArrayList<ResultReportEvent_Dto> resultReportEventList;

    TranslationManager translationManager;
    ResultReportAdapter1 resultReportAdapter1;
    ExamResult_Dto examResult_dto;

    BaseActivity baseActivity;
    SharedPrefrenceManager sharedPrefrenceManager;

    private static final int FILTER_DIALOG = 1001;

    private final String TAG = ResultReportFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.exam_result_report, container, false);
        ProjectUtils.showLog(TAG,"onCreateView");

        mContext = getActivity();

        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        translationManager = new TranslationManager(mContext);

        Initialize(v);

        baseActivity = new BaseActivity();

        setValue();

        return v;
    }

    public void setValue() {

        examResult_dto = sharedPrefrenceManager.getExamResult_Dto(Consts.EXAM_RESULT);
        setExamResultData(examResult_dto);
    }

    private void Initialize(View v) {

        ivFilter = v.findViewById(R.id.ivFilter);
        tvProgramName = v.findViewById(R.id.tvProgramName);
        tvBatch = v.findViewById(R.id.tvBatch);
        tvPeriod = v.findViewById(R.id.tvPeriod);
        recyclerView = v.findViewById(R.id.recyclerView);
        emptyView = v.findViewById(R.id.emptyView);
        tvEmptyDetails = v.findViewById(R.id.tvEmptyDetails);

        tvBatch1 = v.findViewById(R.id.tvBatch1);
        tvPeriod1 = v.findViewById(R.id.tvPeriod1);
        llPeriod = v.findViewById(R.id.llPeriod);

        tvBatch1.setText(translationManager.BATCH_KEY);
        tvPeriod1.setText(translationManager.PERIOD_KEY);

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectUtils.preventTwoClick(ivFilter);
                Intent intent = new Intent(mContext, ExamFilterDialog.class);
                intent.putExtra(Consts.EXAM_RESULT,examResult_dto);
                startActivityForResult(intent,FILTER_DIALOG);
            }
        });

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FILTER_DIALOG && resultCode == getActivity().RESULT_OK && data != null)
        {
            examResult_dto = (ExamResult_Dto) data.getSerializableExtra(Consts.EXAM_RESULT);

            setExamResultNewData(examResult_dto);
        }
    }

    private void setExamResultData(ExamResult_Dto examResult_dto) {

        String programName = ProjectUtils.getCorrectedString(examResult_dto.getProgramName());
        if(!programName.equalsIgnoreCase("")){
            tvProgramName.setText(programName);
        }

        String batchPrintName = ProjectUtils.getCorrectedString(examResult_dto.getBatchPrintName());
        String batch = ProjectUtils.getCorrectedString(examResult_dto.getBatch());
        if(!batchPrintName.equalsIgnoreCase("")){
            tvBatch.setText(batchPrintName);
        }else if(!batch.equalsIgnoreCase("")){
            tvBatch.setText(batch);
        }

        String academyType = sharedPrefrenceManager.getAcademyTypeFromKey();
        if(academyType.equalsIgnoreCase(Consts.SCHOOL)){
            llPeriod.setVisibility(View.GONE);
        }else{
            llPeriod.setVisibility(View.VISIBLE);

            String periodPrintName = ProjectUtils.getCorrectedString(examResult_dto.getPeriodPrintName());
            String period = ProjectUtils.getCorrectedString(examResult_dto.getPeriod());
            if(!periodPrintName.equalsIgnoreCase("")){
                tvPeriod.setText(periodPrintName);
            }else if(!period.equalsIgnoreCase("")){
                tvPeriod.setText(period);
            }
        }

        String programId = examResult_dto.getProgramId();
        String batchId = examResult_dto.getBatchId();
        String periodId = examResult_dto.getPeriodId();

        getResultReport(programId,batchId,periodId);
    }

    private void setExamResultNewData(ExamResult_Dto examResult_dto) {

        String programName = ProjectUtils.getCorrectedString(examResult_dto.getProgramName());
        if(!programName.equalsIgnoreCase("")){
            tvProgramName.setText(programName);
        }

        String batch = ProjectUtils.getCorrectedString(examResult_dto.getBatch());
        if(!batch.equalsIgnoreCase("")){
            tvBatch.setText(batch);
        }

        String academyType = sharedPrefrenceManager.getAcademyTypeFromKey();
        if(academyType.equalsIgnoreCase(Consts.SCHOOL)){
            llPeriod.setVisibility(View.GONE);
        }else{
            llPeriod.setVisibility(View.VISIBLE);

            String period = ProjectUtils.getCorrectedString(examResult_dto.getPeriod());
            if(!period.equalsIgnoreCase("")){
                tvPeriod.setText(period);
            }
        }

        String programId = examResult_dto.getProgramId();
        String batchId = examResult_dto.getBatchId();
        String periodId = examResult_dto.getPeriodId();

        getResultReport(programId,batchId,periodId);
    }

    private void getResultReport(String programId, String batchId, String periodId) {

        if (ConnectionDetector.isConnectingToInternet(mContext)) {

            baseActivity.showProgressDialog(mContext);
            new OptimizedServerCallAsyncTask(mContext,
                    this, KEYS.SWITCH_RESULT_REPORT).execute(programId,batchId,periodId);
        } else {
            Toast.makeText(mContext, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        switch (callFor)
        {
            case KEYS.SWITCH_RESULT_REPORT:
                baseActivity.hideProgressDialog();

                try {
                    JSONObject responseObject = new JSONObject(responseResult.toString());

                    if(responseObject.has("children")){
                        JSONArray arr = responseObject.getJSONArray("children");

                        if (arr != null && arr.length() > 0) {

                            checkEmpty(false);
                            resultReportList = new ArrayList<>();

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject jsonObject = arr.optJSONObject(i);

                                String treeNode = jsonObject.optString("treeNode");
                                String maxMarksOrGrade = jsonObject.optString("maxMarksOrGrade");
                                String obtainedMarksGrade = jsonObject.optString("obtainedMarksGrade");
                                String effetiveMarksGrade = jsonObject.optString("effetiveMarksGrade");
                                String moderationPoints = jsonObject.optString("moderationMarksGrade");
                                double weightage = jsonObject.optDouble("weightage");

                                JSONArray type = jsonObject.optJSONArray("children");
                                resultReportTypeList = new ArrayList<>();

                                if(type != null && type.length() > 0){

                                    for(int j = 0 ; j < type.length() ; j++){
                                        JSONObject jsonObject1 = type.optJSONObject(j);

                                        String treeNode1 = jsonObject1.optString("treeNode");
                                        String maxMarksOrGrade1 = jsonObject1.optString("maxMarksOrGrade");
                                        String obtainedMarksGrade1 = jsonObject1.optString("obtainedMarksGrade");
                                        String effetiveMarksGrade1 = jsonObject1.optString("effetiveMarksGrade");
                                        String moderationPoints1 = jsonObject1.optString("moderationMarksGrade");
                                        double weightage1 = jsonObject1.optDouble("weightage");

                                        JSONArray subType = jsonObject1.optJSONArray("children");
                                        resultReportSubTypeList = new ArrayList<>();

                                        if(subType != null && subType.length() > 0){

                                            for(int k = 0 ; k < subType.length() ; k++){
                                                JSONObject jsonObject2 = subType.optJSONObject(k);

                                                String treeNode2 = jsonObject2.optString("treeNode");
                                                String maxMarksOrGrade2 = jsonObject2.optString("maxMarksOrGrade");
                                                String obtainedMarksGrade2 = jsonObject2.optString("obtainedMarksGrade");
                                                String effetiveMarksGrade2 = jsonObject2.optString("effetiveMarksGrade");
                                                String moderationPoints2 = jsonObject2.optString("moderationMarksGrade");
                                                double weightage2 = jsonObject2.optDouble("weightage");

                                                JSONArray method = jsonObject2.optJSONArray("children");
                                                resultReportMethodList = new ArrayList<>();

                                                if(method != null && method.length() > 0){

                                                    for(int l = 0 ; l < method.length() ; l++){
                                                        JSONObject jsonObject3 = method.optJSONObject(l);

                                                        String treeNode3 = jsonObject3.optString("treeNode");
                                                        String maxMarksOrGrade3 = jsonObject3.optString("maxMarksOrGrade");
                                                        String obtainedMarksGrade3 = jsonObject3.optString("obtainedMarksGrade");
                                                        String effetiveMarksGrade3 = jsonObject3.optString("effetiveMarksGrade");
                                                        String moderationPoints3 = jsonObject3.optString("moderationMarksGrade");
                                                        double weightage3 = jsonObject3.optDouble("weightage");

                                                        JSONArray event = jsonObject3.optJSONArray("children");
                                                        resultReportEventList = new ArrayList<>();

                                                        if(event != null && event.length() > 0){

                                                            for(int m = 0 ; m < event.length() ; m++){
                                                                JSONObject jsonObject4 = event.optJSONObject(m);

                                                                String treeNode4 = jsonObject4.optString("treeNode");
                                                                String maxMarksOrGrade4 = jsonObject4.optString("maxMarksOrGrade");
                                                                String obtainedMarksGrade4 = jsonObject4.optString("obtainedMarksGrade");
                                                                String effetiveMarksGrade4 = jsonObject4.optString("effetiveMarksGrade");
                                                                String moderationPoints4 = jsonObject4.optString("moderationMarksGrade");
                                                                double weightage4 = jsonObject4.optDouble("weightage");

                                                                ResultReportEvent_Dto resultReportEvent_dto = new ResultReportEvent_Dto(treeNode4,maxMarksOrGrade4,effetiveMarksGrade4,obtainedMarksGrade4,moderationPoints4,weightage4);
                                                                resultReportEventList.add(resultReportEvent_dto);
                                                            }
                                                        }

                                                        ResultReportMethod_Dto resultReportMethod_dto = new ResultReportMethod_Dto(treeNode3,maxMarksOrGrade3,effetiveMarksGrade3,obtainedMarksGrade3,moderationPoints3,weightage3,resultReportEventList);
                                                        resultReportMethodList.add(resultReportMethod_dto);
                                                    }
                                                }

                                                ResultReportSubType_Dto resultReportSubType_dto = new ResultReportSubType_Dto(treeNode2,maxMarksOrGrade2,effetiveMarksGrade2,obtainedMarksGrade2,moderationPoints2,weightage2,resultReportMethodList);
                                                resultReportSubTypeList.add(resultReportSubType_dto);
                                            }
                                        }

                                        ResultReportType_Dto resultReportType_dto = new ResultReportType_Dto(treeNode1,maxMarksOrGrade1,effetiveMarksGrade1,obtainedMarksGrade1,moderationPoints1,weightage1,resultReportSubTypeList);
                                        resultReportTypeList.add(resultReportType_dto);
                                    }
                                }

                                ResultReport_Dto resultReport_dto = new ResultReport_Dto(treeNode,maxMarksOrGrade,effetiveMarksGrade,obtainedMarksGrade,moderationPoints,weightage,resultReportTypeList);
                                resultReportList.add(resultReport_dto);
                            }

                            resultReportAdapter1 = new ResultReportAdapter1(mContext,resultReportList);
                            recyclerView.setAdapter(resultReportAdapter1);
                            resultReportAdapter1.notifyDataSetChanged();

                            resultReportAdapter1.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    ProjectUtils.preventTwoClick(view);

                                    ResultReport_Dto list = resultReportList.get(position);

                                    Intent intent = new Intent(mContext, ResultReportDetailActivity.class);
                                    intent.putExtra(Consts.RESULT_REPORT_LIST,list);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            checkEmpty(true);
                        }
                    }else{
                        checkEmpty(true);
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    checkEmpty(true);
                }
                break;
        }
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
            tvEmptyDetails.setText(getString(R.string.result_not_yet_published));
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        }
    }

}
