package com.serosoft.academiassu.Modules.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Utils.GMailSender;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.Widgets.RatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FeedbackActivity extends BaseActivity implements AsyncTaskCompleteListener {

    Context mContext;
    public Toolbar toolbar;
    private RatingBar ratingBar;
    private EditText etFeedback;
    private Button btnSubmit;
    private LinearLayout llFeedback;

    ArrayList<String> emailList;
    ArrayList<Integer> list = new ArrayList<>();
    int RATING_STAR_VIEW = 0, FEEDBACK_FIELD_VIEW = 0;

    String textEmailId = "",academyLocation = "", textMobileNo = "";
    String textSender, textSubject, textBody, textFeedback = "No Message";

    private final String TAG = FeedbackActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ProjectUtils.showLog(TAG,"onCreate start");

        mContext = FeedbackActivity.this;

        Initialize();

        permissionSetup();

        populateContent();
    }

    private void populateContent() {
        if (ConnectionDetector.isConnectingToInternet(this))
        {
            showProgressDialog(mContext);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_FEEDBACK_EMAILS).execute();
        }
        else
        {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private String getDateTime() {

        String formatDate = ProjectUtils.getTimestampToDate(mContext);
        String formatTime = ProjectUtils.getTimestampToTime(mContext);

        DateFormat dateFormat = new SimpleDateFormat(formatDate+" "+formatTime);
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);
        ratingBar = findViewById(R.id.ratingBar);
        etFeedback = findViewById(R.id.etFeedback);
        btnSubmit = findViewById(R.id.btnSubmit);
        llFeedback = findViewById(R.id.llFeedback);

        toolbar.setTitle(translationManager.FEEDBACK_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorPrimary, toolbar, this); }

        etFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(count>0){
                    buttonEnable(true);
                }else{
                    buttonEnable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textSender = "mobile@serosoft.com";
                textSubject = "Student Mobile Application Feedback";
                textFeedback = etFeedback.getText().toString().trim();

                academyLocation = sharedPrefrenceManager.getAcademyLocationNameInSP();
                textEmailId = sharedPrefrenceManager.getUserEmailFromKey();
                String mobileNumber = sharedPrefrenceManager.getContact1FromKey();
                textMobileNo = mobileNumber.replace("-"," ");

                textBody = "Hello,"+"\n\n"
                        +"The following feedback was shared- "+"\n\n"
                        +"Date: "+getDateTime()+"\n"
                        +"Academy Location: "+academyLocation+"\n"
                        +"User ID/Name: "+sharedPrefrenceManager.getUserNameFromKey()+"/"+sharedPrefrenceManager.getUserFirstNameFromKey()+" "+sharedPrefrenceManager.getUserLastNameFromKey()+"\n"
                        +"Email Address: "+textEmailId+"\n"
                        +"Mobile Number: "+textMobileNo+"\n"
                        +"Rating: "+ratingBar.getCount()+" out of 5\n"
                        +"Message: "+textFeedback+"\n\n"
                        +"Regards,\n"
                        +"Administrator";

                if(emailList != null && emailList.size() > 0){
                    sendEmail(textSender,emailList,textSubject,textBody.trim());
                }else{
                    emailList = new ArrayList<>();
                    emailList.add("mobile@serosoft.com");
                    sendEmail(textSender,emailList,textSubject,textBody.trim());
                }
            }
        });
    }

    private void buttonEnable(boolean b) {

        if(b){
            btnSubmit.setEnabled(true);
            btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit));
        }else{
            btnSubmit.setEnabled(false);
            btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_submit_disable));
        }
    }

    private void sendEmail(String textSender, ArrayList<String> recipientEmailList, String textSubject, String textBody) {

        showProgressDialog(mContext);

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String idList = recipientEmailList.toString();
                    String recipient = idList.substring(1, idList.length() - 1).replace(", ", ",");
                    GMailSender sender = new GMailSender(textSender, "Milinda@6554");
                    sender.sendMail(textSubject,
                                textBody,
                                textSender,
                                recipient);

                    hideProgressDialog();
                    showToast("Feedback Submitted Successfully");

                } catch (Exception e) {
                    ProjectUtils.showLog("mylog", "Error: " + e.getMessage());
                    hideProgressDialog();
                }
            }
        });
        sender.start();
    }

    public void showToast(final String toast)
    {
        runOnUiThread(() -> Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show());
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.refresh);
        item.setVisible(false);

        return true;
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
                onBackPressed();
            }break;

            case R.id.refresh:
            {
                getNotifications();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> response)
    {
        String callFor = response.get(KEYS.CALL_FOR);
        String responseResult = response.get(KEYS.RETURN_RESPONSE);
        Intent intent;

        if (callFor == null)
        {
            hideProgressDialog();
            showAlertDialog(this, getString(R.string.oops), "Unexpected Error at " + this.getLocalClassName());
            return;
        }

        switch (callFor)
        {
            case KEYS.SWITCH_FEEDBACK_EMAILS:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    JSONArray jsonArray = responseObject.getJSONArray("whatever");

                    emailList = new ArrayList<>();

                    if(jsonArray != null && jsonArray.length() > 0){

                        for(int i = 0 ; i<jsonArray.length() ; i++)
                        {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            String email = jsonObject.optString("emailAddress");
                            boolean status = jsonObject.optBoolean("status");
                            if(status){
                                emailList.add(email);
                            }
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private void permissionSetup()
    {
        //Here set module permission without api response
        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        RATING_STAR_VIEW = 0;
        FEEDBACK_FIELD_VIEW = 0;

        for (int i = 0 ; i < list.size() ; i++){

            switch (list.get(i)) {
                case Permissions.PARENT_STAR_RATING_VIEW:
                case Permissions.STUDENT_STAR_RATING_VIEW:
                    RATING_STAR_VIEW = 1;
                    break;
                case Permissions.PARENT_ENTER_FEEDBACK_VIEW:
                case Permissions.STUDENT_ENTER_FEEDBACK_VIEW:
                    FEEDBACK_FIELD_VIEW = 1;
                    break;
            }
        }

        int value = validateFields();

        if(value == 1)
        {
            ratingBar.setVisibility(View.VISIBLE);
            llFeedback.setVisibility(View.VISIBLE);
            buttonEnable(false);
        }
        else if(value == 2)
        {
            ratingBar.setVisibility(View.VISIBLE);
            llFeedback.setVisibility(View.GONE);
            buttonEnable(true);
        }
        else if(value == 3)
        {
            ratingBar.setVisibility(View.GONE);
            llFeedback.setVisibility(View.VISIBLE);
            buttonEnable(false);
        }
    }

    public int validateFields()
    {
        if(RATING_STAR_VIEW != 0 && FEEDBACK_FIELD_VIEW != 0)
        {
            return 1;
        }
        else if (RATING_STAR_VIEW != 0)
        {
            return 2;
        }
        else if (FEEDBACK_FIELD_VIEW != 0)
        {
            return 3;
        }
        return 0;
    }

}
