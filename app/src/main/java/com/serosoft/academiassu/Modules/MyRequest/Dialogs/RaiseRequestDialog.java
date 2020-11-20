package com.serosoft.academiassu.Modules.MyRequest.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;

import com.serosoft.academiassu.Modules.MyRequest.Adapters.RaiseRequestTypeAdapter;
import com.serosoft.academiassu.Modules.MyRequest.Models.MyRequestType_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class RaiseRequestDialog extends Activity implements AsyncTaskCompleteListener, View.OnClickListener {

    LayoutInflater inflater;
    View view;
    BaseActivity baseActivity;

    private Context mContext;
    SharedPrefrenceManager sharedPrefrenceManager;
    TranslationManager translationManager;

    private ListView lvRequestType;
    private TextView tvTitle;
    private AppCompatImageView ivClose;

    ArrayList<MyRequestType_Dto> myRequestTypeList = new ArrayList<>();
    RaiseRequestTypeAdapter raiseRequestTypeAdapter;

    private static final String TAG = RaiseRequestDialog.class.getSimpleName();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.showLog(TAG,"onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = RaiseRequestDialog.this;
        baseActivity = new BaseActivity();

        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        setFinishOnTouchOutside(false);
        inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.myraiserequest_layout,null,false);

        setContentView(view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        IntializeView();

        Intent intent = getIntent();
        myRequestTypeList = (ArrayList<MyRequestType_Dto>) intent.getSerializableExtra(Consts.MY_REQUEST_TYPE_LIST);

        if(myRequestTypeList != null && myRequestTypeList.size() > 0){

            raiseRequestTypeAdapter = new RaiseRequestTypeAdapter(mContext,myRequestTypeList);
            lvRequestType.setAdapter(raiseRequestTypeAdapter);
        }
    }

    private void IntializeView() {

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        translationManager = new TranslationManager(this);

        tvTitle = findViewById(R.id.tvTitle);
        ivClose = findViewById(R.id.ivClose);
        lvRequestType = findViewById(R.id.lvRequestType);

        ivClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.ivClose:{
                finish();
            }break;
        }
    }

    @Override
    public void onBackPressed() {}

    @Override
    public void onTaskComplete(HashMap<String, String> response) {

        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);

        switch (callFor)
        {

        }
    }
}
