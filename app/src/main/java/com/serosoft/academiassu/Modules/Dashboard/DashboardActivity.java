package com.serosoft.academiassu.Modules.Dashboard;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cipolat.superstateview.SuperStateView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.OnItemClickListener;
import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.AcademiaDrive.DriveMainActivity;
import com.serosoft.academiassu.Modules.Assignments.Assignment.AssignmentCourseActivity;
import com.serosoft.academiassu.Modules.Assignments.AssignmentMainActivity;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.SessionDiaryCourseActivity;
import com.serosoft.academiassu.Modules.Attendance.AttendanceMainActivity;
import com.serosoft.academiassu.Modules.Attendance.MultipleAttendanceMainActivity;
import com.serosoft.academiassu.Modules.Circular.CircularActivity;
import com.serosoft.academiassu.Modules.Dashboard.Models.CurrencyDto;
import com.serosoft.academiassu.Modules.Dashboard.Models.ModuleData;
import com.serosoft.academiassu.Modules.Event.EventsActivity;
import com.serosoft.academiassu.Modules.Exam.ExamDocActivity;
import com.serosoft.academiassu.Modules.Exam.ExamMainActivity;
import com.serosoft.academiassu.Modules.Exam.MarksheetActivity;
import com.serosoft.academiassu.Modules.Exam.ResultReportActivity;
import com.serosoft.academiassu.Modules.Fees.FeesMainActivity;
import com.serosoft.academiassu.Modules.Login.LoginActivity;
import com.serosoft.academiassu.Modules.MyCourses.MyCoursesListActivity;
import com.serosoft.academiassu.Modules.MyRequest.MyRequestMainActivity;
import com.serosoft.academiassu.Modules.TimeTable.TimeTableViewTypeActivity;
import com.serosoft.academiassu.Modules.UserProfile.UserInfoActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Widgets.ProfileCircularImage;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.Utils.Version;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Abhishek on October 14 2019.
 */

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncTaskCompleteListener
{
    private Context mContext;
    public Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView = null;
    private TextView tvVersion;

    private ImageView new_instituteIcon;
    private ProfileCircularImage profilePicHeader;
    private TextView userNameDrawer,userEmailIdHeader;

    SuperStateView superStateView;

    ArrayList<Integer> moduleCodeList = new ArrayList<>();
    ArrayList<ModuleData> moduleList;
    ArrayList<ModuleData> moduleTempList;

    Menu menu;
    LinearLayout linearLayout;
    private RecyclerView rvModules;
    LinearLayoutManager linearLayoutManager;
    DashboardAdapter dashboardAdapter;

    ArrayList<CurrencyDto> currencyList;

    ArrayList<Integer> list = new ArrayList<>();
    int HOMEWORK_VIEW = 0, SESSION_DIARY_VIEW = 0,RESULT_REPORT_VIEW = 0,EXAM_DOCS_VIEW = 0;

    private final String TAG = DashboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ProjectUtils.showLog(TAG,"onCreate start");

        mContext = DashboardActivity.this;
        sharedPrefrenceManager = new SharedPrefrenceManager(mContext);

        if (sharedPrefrenceManager.getIsParentStatusFromKey())
        {
            setContentView(R.layout.dashboard_activity_parent);
        } else {
            setContentView(R.layout.dashboard_activity_student);
        }

        Initialize();

        Glide.with(mContext).load(R.drawable.loader).into(new_instituteIcon);

        //Navigation drawer
        mToggle=new ActionBarDrawerToggle(this, mDrawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        allModules();

        callModulesApi();

        eventDictionary();

        populateContents();

    }

    private void eventDictionary() {

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String curDate = ProjectUtils.convertTimestampToDate(System.currentTimeMillis(),mContext);
        String curTime = ProjectUtils.convertTimestampToTime(System.currentTimeMillis(),mContext);

        String currentDateTime = curDate + " " + curTime;

        String myVersionName = pInfo.versionName;
        Version curVersion = new Version(myVersionName);

        String currentVersion = sharedPrefrenceManager.getCurrentVersionFromKey();

        if(currentVersion.equalsIgnoreCase("")){
            currentVersionEvent(curVersion.get(), currentDateTime);
            sharedPrefrenceManager.setCurrentVersionSP(curVersion.get());
        }else if(Float.parseFloat(curVersion.get()) > Float.parseFloat(currentVersion)){
            currentVersionEvent(curVersion.get(), currentDateTime);
            sharedPrefrenceManager.setCurrentVersionSP(curVersion.get());
        }
    }

    private void currentVersionEvent(String currentVersionString, String currentDate) {

        Bundle bundle = new Bundle();
        bundle.putString("client_instance_url", BaseURL.BASE_URL);
        bundle.putInt("user_id", sharedPrefrenceManager.getUserIDFromKey());
        bundle.putBoolean("is_parent", sharedPrefrenceManager.getIsParentStatusFromKey());
        bundle.putString("first_name", sharedPrefrenceManager.getUserFirstNameFromKey());
        bundle.putString("last_name", sharedPrefrenceManager.getUserLastNameFromKey());
        bundle.putInt("period_id", sharedPrefrenceManager.getPeriodIDFromKey());
        bundle.putString("email_address", sharedPrefrenceManager.getUserEmailFromKey());
        bundle.putString("contact_1", sharedPrefrenceManager.getContact1FromKey());
        bundle.putString("contact_2", sharedPrefrenceManager.getContact2FromKey());
        bundle.putString("current_version_installed", currentVersionString);
        bundle.putInt("academy_location_id", sharedPrefrenceManager.getAcademyLocationIDFromKey());
        bundle.putBoolean("is_course_level", sharedPrefrenceManager.getIsCourseLevelStatusFromKey());
        bundle.putBoolean("is_complete_day", sharedPrefrenceManager.getIsCompleteDayStatusFromKey());
        bundle.putBoolean("is_multiple_session", sharedPrefrenceManager.getIsMultipleSessionStatusFromKey());
        bundle.putString("mother_name", sharedPrefrenceManager.getStudentMothersNameFromKey());
        bundle.putString("father_name", sharedPrefrenceManager.getStudentFathersNameFromKey());
        bundle.putInt("person_id", sharedPrefrenceManager.getPersonIDFromKey());
        bundle.putInt("portal_id", sharedPrefrenceManager.getPortalIDFromKey());
        bundle.putInt("program_id", sharedPrefrenceManager.getProgramIDFromKey());
        bundle.putString("date_of_birth", sharedPrefrenceManager.getDateOfBirthFromKey());
        bundle.putString("date_of_log", currentDate);
        firebaseAnalytics.logEvent(getString(R.string.academia_info), bundle);
    }

    private void callModulesApi()
    {
        if (ConnectionDetector.isConnectingToInternet(this))
        {
            if (sharedPrefrenceManager.getIsParentStatusFromKey())
            {
                showProgressDialog(this);
                new OptimizedServerCallAsyncTask(this,this,
                        KEYS.SWITCH_FEATURE_PRIVILEGES).execute(Consts.PARENT_APP_PORTAL_ID,Consts.PARENT_MOBILE_APP);
            }
            else
            {
                showProgressDialog(this);
                new OptimizedServerCallAsyncTask(this,this,
                        KEYS.SWITCH_FEATURE_PRIVILEGES).execute(Consts.STUDENT_APP_PORTAL_ID,Consts.STUDENT_MOBILE_APP);
            }

        }
        else
        {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void allModules()
    {
        moduleList = new ArrayList<>();

        if (sharedPrefrenceManager.getIsParentStatusFromKey())
        {
            moduleList.add(new ModuleData(Permissions.PARENT_PERMISSION_MY_COURSE_VIEW,getResources().getColor(R.color.colorMyCourse),R.mipmap.mycourses_white_2020,translationManager.MYCOURSE_KEY));
            moduleList.add(new ModuleData(Permissions.PARENT_PERMISSION_ATTENDANCE_VIEW,getResources().getColor(R.color.colorAttendance),R.mipmap.attendance_white_2018,translationManager.ATTENDANCE_KEY));
            moduleList.add(new ModuleData(Permissions.PARENT_PERMISSION_FEES_VIEW,getResources().getColor(R.color.colorFees),R.mipmap.fees_white_2018,translationManager.FEES_KEY));
            moduleList.add(new ModuleData(Permissions.PARENT_PERMISSION_ASSIGNMENT_VIEW,getResources().getColor(R.color.colorAssignment),R.mipmap.assignments_white_2018,translationManager.ASSIGNMENT_KEY));
            moduleList.add(new ModuleData(Permissions.PARENT_PERMISSION_RESULT_VIEW,getResources().getColor(R.color.colorResults),R.mipmap.exam_white_2018,translationManager.EXAMRESULT_KEY));
            moduleList.add(new ModuleData(Permissions.PARENT_PERMISSION_EVENTS_VIEW,getResources().getColor(R.color.colorEvents),R.mipmap.events_white_2018,translationManager.EVENT_KEY));
            moduleList.add(new ModuleData(Permissions.PARENT_PERMISSION_TIMETABLE_VIEW,getResources().getColor(R.color.colorTimetable),R.mipmap.timetable_white_2018,translationManager.TIMETABLE_KEY));
            moduleList.add(new ModuleData(Permissions.PARENT_PERMISSION_CIRCULARS_VIEW,getResources().getColor(R.color.colorCircular),R.mipmap.circulars_white_2018,translationManager.CIRCULAR_KEY));
            moduleList.add(new ModuleData(Permissions.PARENT_PERMISSION_GALLERY_VIEW,getResources().getColor(R.color.colorDrive),R.mipmap.drive_white_2020,translationManager.ACADEMIA_DRIVE_KEY));
        }
        else
        {
            moduleList.add(new ModuleData(Permissions.STUDENT_PERMISSION_MY_COURSE_VIEW,getResources().getColor(R.color.colorMyCourse),R.mipmap.mycourses_white_2020,translationManager.MYCOURSE_KEY));
            moduleList.add(new ModuleData(Permissions.STUDENT_PERMISSION_ATTENDANCE_VIEW,getResources().getColor(R.color.colorAttendance),R.mipmap.attendance_white_2018,translationManager.ATTENDANCE_KEY));
            moduleList.add(new ModuleData(Permissions.STUDENT_PERMISSION_FEES_VIEW,getResources().getColor(R.color.colorFees),R.mipmap.fees_white_2018,translationManager.FEES_KEY));
            moduleList.add(new ModuleData(Permissions.STUDENT_PERMISSION_ASSIGNMENT_VIEW,getResources().getColor(R.color.colorAssignment),R.mipmap.assignments_white_2018,translationManager.ASSIGNMENT_KEY));
            moduleList.add(new ModuleData(Permissions.STUDENT_PERMISSION_RESULT_VIEW,getResources().getColor(R.color.colorResults),R.mipmap.exam_white_2018,translationManager.EXAMRESULT_KEY));
            moduleList.add(new ModuleData(Permissions.STUDENT_PERMISSION_EVENTS_VIEW,getResources().getColor(R.color.colorEvents),R.mipmap.events_white_2018,translationManager.EVENT_KEY));
            moduleList.add(new ModuleData(Permissions.STUDENT_PERMISSION_TIMETABLE_VIEW,getResources().getColor(R.color.colorTimetable),R.mipmap.timetable_white_2018,translationManager.TIMETABLE_KEY));
            moduleList.add(new ModuleData(Permissions.STUDENT_PERMISSION_CIRCULARS_VIEW,getResources().getColor(R.color.colorCircular),R.mipmap.circulars_white_2018,translationManager.CIRCULAR_KEY));
            moduleList.add(new ModuleData(Permissions.STUDENT_PERMISSION_GALLERY_VIEW,getResources().getColor(R.color.colorDrive),R.mipmap.drive_white_2020,translationManager.ACADEMIA_DRIVE_KEY));
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        navigationHeader();
    }

    private void navigationHeader()
    {
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        profilePicHeader = hView.findViewById(R.id.profilePicHeader);
        userNameDrawer = hView.findViewById(R.id.drawerHeaderUserName);
        userEmailIdHeader = hView.findViewById(R.id.drawerHeaderEmail);

        String name = sharedPrefrenceManager.getUserFirstNameFromKey() + " " + sharedPrefrenceManager.getUserLastNameFromKey();
        userNameDrawer.setText(name);
        if (sharedPrefrenceManager.getIsParentStatusFromKey())
        {
            userEmailIdHeader.setText("C/o " + sharedPrefrenceManager.getParentNameFromKey());
        } else {
            userEmailIdHeader.setText(sharedPrefrenceManager.getUserEmailFromKey());
        }

        String stringForImage = sharedPrefrenceManager.getUserImageFromKey();

        if (stringForImage.length() > 0)
        {
            byte[] decodedString = Base64.decode(stringForImage, Base64.DEFAULT);

            Glide.with(mContext)
                    .asBitmap()
                    .load(decodedString)
                    .placeholder(R.drawable.user_icon1)
                    .override(600, 200)
                    .fitCenter()
                    .into(profilePicHeader);
            profilePicHeader.setBorderWidth(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.dashboardMenu).setVisible(false);
        return true;
    }

    private void Initialize()
    {
        navigationView = findViewById(R.id.nav_view);
        new_instituteIcon = findViewById(R.id.new_instituteIcon);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        rvModules = findViewById(R.id.rvModules);
        superStateView = findViewById(R.id.superStateView);
        linearLayout = findViewById(R.id.linearLayout);
        tvVersion =  findViewById(R.id.tvVersion);
        toolbar =  findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.DASHBOARD_TITLE_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        linearLayoutManager = new LinearLayoutManager(mContext);
        rvModules.setLayoutManager(linearLayoutManager);
        rvModules.setItemAnimator(new DefaultItemAnimator());
        rvModules.setNestedScrollingEnabled(false);

        if (navigationView != null) {

            menu = navigationView.getMenu();
            menu.findItem(R.id.home_drawer).setTitle(translationManager.HOME_KEY);
            menu.findItem(R.id.myRequest).setTitle(translationManager.MY_REQUESTS_KEY);
            menu.findItem(R.id.logout_drawer).setTitle(translationManager.LOGOUT_KEY);
            menu.findItem(R.id.myProfile).setTitle(translationManager.MYPROFILE_KEY);
            menu.findItem(R.id.reset_drawer).setTitle(translationManager.RESET_PASSWORD_KEY);
            menu.findItem(R.id.rateReview).setTitle(translationManager.RATE_REVIEW_KEY);
            menu.findItem(R.id.feedback).setTitle(translationManager.FEEDBACK_KEY);

            if (sharedPrefrenceManager.getIsParentStatusFromKey())
            {
                menu.findItem(R.id.switch_student).setTitle(translationManager.SWITCH_STUDENT_KEY);
            }

            permissionSetupSideDrawer();
        }

        //Here set Versions
        String version = sharedPrefrenceManager.getVersionFromKey();
        tvVersion.setText(version);
    }

    public void populateContents()
    {
        if (ConnectionDetector.isConnectingToInternet(this))
        {
            loadDashboardItems();
        }
        else
        {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    public void loadDashboardItems()
    {
        new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_ACADEMY_IMAGE).execute();

        if (Flags.CHECK_VERSION)
        {
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_LATEST_VERSION).execute();
        }

        new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_ALL_CURRENCY).execute();
    }

    private void showUpdatePopUp(JSONObject jsonObject)
    {
        Intent intent = new Intent(DashboardActivity.this, UpdateActivity.class);
        intent.putExtra("json", jsonObject.toString());
        startActivity(intent);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result)
    {
        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);
        String returnResult = result.get(KEYS.RETURN_RESULT);

        if (callFor == null) {return;}

        switch (callFor)
        {
            case KEYS.SWITCH_LOGOUT:
            {
                if(sharedPrefrenceManager.getUserGLoginStatusFromKey()){

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {

                                    if(status.isSuccess()){

                                        logoutFromApp();
                                    }
                                }
                            });
                }else{

                    logoutFromApp();
                }
            }
            break;

            case KEYS.SWITCH_CIRCULARS:
            {
                if ((responseResult != null) && responseResult.equals(KEYS.RESPONSE_SUCCESSFULLY))
                {
                    Intent intent = new Intent(DashboardActivity.this, CircularActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(KEYS.CALL_FOR, DashboardActivity.class.getSimpleName());
                    startActivity(intent);
                }
                else
                {
                    showAlertDialog(this, "INFO", responseResult);
                }
            }
            break;

            case KEYS.SWITCH_REFRESH_LOGIN:
            {
                hideProgressDialog();
                if (returnResult == KEYS.TRUE)
                {
                    loadDashboardItems();
                } else {
                    sharedPrefrenceManager.setUserLoginStatusInSP(false);
                    sharedPrefrenceManager.setUserGLoginStatusInSP(false);
                    finish();
                }
            }
            break;

            case KEYS.SWITCH_ACADEMY_IMAGE:
            {
                try
                {
                    JSONObject responseObject = new JSONObject(responseResult);

                    if (responseObject.has("value"))
                    {
                        setInstituteLogo(responseObject.optString("value"));
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    ProjectUtils.showLog(TAG,""+e.getMessage());
                    new_instituteIcon.setImageResource(R.drawable.academia_new_logo);
                }
            }
            break;

            case KEYS.SWITCH_LATEST_VERSION:
            {
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("latestVersion"))
                    {
                        String latestVersionString = responseObject.optString("latestVersion");
                        PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                        String currentVersionString = pInfo.versionName;

                        Version currentVersion = new Version(currentVersionString);
                        Version latestVersion = new Version(latestVersionString);

                        int compareStatus = currentVersion.compareTo(latestVersion);
                        if (compareStatus == -1)
                        {
                            //Show Update PopUp
                            showUpdatePopUp(responseObject);
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    showAlertDialog(this, getString(R.string.oops), getString(R.string.versioning_api_data_not_set));
                }
            }
            break;

            case KEYS.SWITCH_ASSOCIATED_SIBLINGS:
            {
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    JSONArray jsonArray = responseObject.optJSONArray("whatever");

                    Dialog alertDialog = new Dialog(DashboardActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.switch_student_dialog, null);
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.setContentView(convertView);
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    ArrayList studentList = new ArrayList<JSONObject>();
                    for (int i = 0 ; i < jsonArray.length() ; ++i )
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        studentList.add(obj);
                    }
                    TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
                    ListView lv = (ListView) convertView.findViewById(R.id.listViewForSwitchStudents);
                    AppCompatImageView ivClose = (AppCompatImageView) convertView.findViewById(R.id.ivClose);
                    lv.setAdapter(new SwitchStudentAdapter(DashboardActivity.this, studentList, ""));
                    titleTextView.setText(translationManager.SWITCH_STUDENT_KEY);

                    ivClose.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            alertDialog.dismiss();
                        }
                    });

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            alertDialog.dismiss();
                            try {
                                if (ConnectionDetector.isConnectingToInternet(DashboardActivity.this)) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(position);
                                    Integer studentId = jsonObject.getInt("studentId");
                                    Integer parentPersonId = jsonObject.getInt("parentPersonId");
                                    Integer parentUserId = jsonObject.getInt("parentUserId");
                                    sharedPrefrenceManager.setParentUserIDInSP(parentUserId);
                                    sharedPrefrenceManager.setParentPersonIDInSP(parentPersonId);
                                    sharedPrefrenceManager.setUserIDInSP(studentId);

                                    showProgressDialog(DashboardActivity.this);
                                    new OptimizedServerCallAsyncTask(DashboardActivity.this, DashboardActivity.this, KEYS.SWITCH_SWITCH_STUDENT).execute();
                                }
                                else
                                {
                                    showAlertDialog(DashboardActivity.this, getString(R.string.network_error), getString(R.string.please_check_your_network_connection));
                                }

                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(alertDialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    alertDialog.show();
                    alertDialog.getWindow().setAttributes(lp);

                } catch (Exception e)
                {
                    e.printStackTrace();
                    ProjectUtils.showLog(TAG,""+e.getMessage());
                }
            }
            break;

            case KEYS.SWITCH_SWITCH_STUDENT:
            {
                hideProgressDialog();
                if (returnResult == KEYS.TRUE)
                {
                    populateContents();
                    navigationHeader();
                }
                else
                {
                    showAlertDialog(DashboardActivity.this, getString(R.string.info), getString(R.string.unexpected_error));
                }
            }
            break;

            case KEYS.SWITCH_NOTIFICATIONS:
            {
                populateNotificationsList(responseResult);
            }
            break;

            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
            {
                showNotificationCount(responseResult);
            }
            break;

            case KEYS.SWITCH_ALL_CURRENCY:
            {
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    JSONArray jsonArray = responseObject.getJSONArray("whatever");

                    currencyList = new ArrayList<>();
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = String.valueOf(jsonObject.optInt("id"));
                        String name = jsonObject.optString("name");
                        String currencyCode = jsonObject.optString("currencyCode");

                        CurrencyDto currencyDto = new CurrencyDto(id,name,currencyCode);
                        currencyList.add(currencyDto);
                    }

                    dbHelper.insertAllCurrency(currencyList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }break;

            case KEYS.SWITCH_FEATURE_PRIVILEGES:
            {
                hideProgressDialog();

                try
                {
                    JSONObject responseObject = new JSONObject(responseResult);
                    JSONArray academyLocationPrivileges = responseObject.getJSONArray("academyLocationPrivileges");

                    moduleCodeList = new ArrayList<>();

                    if(academyLocationPrivileges != null && academyLocationPrivileges.length() > 0)
                    {
                        checkEmpty(false);

                        for (int i = 0 ; i < academyLocationPrivileges.length() ; i++)
                        {
                            JSONObject obj = academyLocationPrivileges.getJSONObject(i);

                            JSONArray jsonArray1 = obj.getJSONArray("featurePrivileges");

                            for(int p = 0 ; p<jsonArray1.length() ; p++)
                            {
                                int colorId = jsonArray1.getInt(p);

                                moduleCodeList.add(colorId);
                            }

                            //Here save module permission in sp
                            sharedPrefrenceManager.setModulePermissionList(Permissions.MODULE_PERMISSION,moduleCodeList);

                            moduleTempList = new ArrayList<>();

                            //Here check server module code from moduleCodeList and add in colorListTemp using predefine module list
                            for(int j=0 ; j< moduleCodeList.size() ; j++)
                            {
                                for(int k=0 ; k< moduleList.size() ; k++)
                                {
                                    if(moduleList.get(k).getId() == moduleCodeList.get(j))
                                    {
                                        moduleTempList.add(moduleList.get(k));
                                        break;
                                    }
                                }
                            }

                            Log.d("sfsdf", String.valueOf(moduleTempList));

                            //Here sort colorListTemp before swap position
                            Collections.sort(moduleTempList,ModuleData.sortModuleId);
                            moduleTempList = DashboardSort.sortColors(this, moduleList, moduleTempList);
                            moduleTempList = DashboardSort.sortColors(this, moduleTempList);

//                            for(int m = 0 ; m<moduleTempList.size() ; m++)
//                            {
//                                if(!(m+1 >= moduleTempList.size()))
//                                {
//                                    //Here check 2 nearby module with same color
//                                    if(moduleTempList.get(m).getColor() == moduleTempList.get(m+1).getColor())
//                                    {
//                                        //Here Swip those 2 modules
//                                        Collections.swap(moduleTempList, m+1, moduleTempList.size()-1);
//
//                                        if(m+1 == moduleTempList.size()-1)
//                                        {
//                                            for(int n = 0 ; n<moduleTempList.size() ; n++)
//                                            {
//                                                if(moduleTempList.get(m+1).getColor() != moduleTempList.get(n).getColor())
//                                                {
//                                                    Collections.swap(moduleTempList, m+1, n);
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
                        }

//                        if(moduleTempList.size() > 6){
//
//                            //Here mycourse module always on top
//                            if (sharedPrefrenceManager.getIsParentStatusFromKey())
//                            {
//                                for(int a = 0 ; a <moduleTempList.size() ; a++){
//                                    if(moduleTempList.get(a).getId() == Permissions.PARENT_PERMISSION_MY_COURSE_VIEW){
//                                        int itemPos = a;
//                                        moduleTempList.remove(itemPos);
//                                        moduleTempList.add(0, new ModuleData(Permissions.PARENT_PERMISSION_MY_COURSE_VIEW,getResources().getColor(R.color.colorMyCourse),R.mipmap.mycourses_white_2020,translationManager.MYCOURSE_KEY));
//                                    }
//                                }
//                            }else{
//
//                                for(int a = 0 ; a <moduleTempList.size() ; a++){
//                                    if(moduleTempList.get(a).getId() == Permissions.STUDENT_PERMISSION_MY_COURSE_VIEW){
//                                        int itemPos = a;
//                                        moduleTempList.remove(itemPos);
//                                        moduleTempList.add(0, new ModuleData(Permissions.STUDENT_PERMISSION_MY_COURSE_VIEW,getResources().getColor(R.color.colorMyCourse),R.mipmap.mycourses_white_2020,translationManager.MYCOURSE_KEY));
//                                    }
//                                }
//                            }
//                        }

                        /*if(moduleTempList.size() >= 3){

                            for(int k = 0 ; k<moduleTempList.size() ; k++)
                            {
                                if(!(k+1 >= moduleTempList.size()))
                                {
                                    if(moduleTempList.get(k).getColor() == moduleTempList.get(k+1).getColor())
                                    {
                                        Collections.swap(moduleTempList, k+1, moduleTempList.size()-1);
                                    }
                                }
                            }
                        }*/

                        showModuleData(moduleTempList);
                    }
                    else
                    {
                        checkEmpty(true);
                        sharedPrefrenceManager.setModulePermissionList(Permissions.MODULE_PERMISSION,moduleCodeList);
                    }

                    permissionSetupSideDrawer();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();

                    checkEmpty(true);

                    ProjectUtils.showLog("Error",""+ex.getMessage());
                }
            }
            break;
        }
    }

    private void logoutFromApp() {
        sharedPrefrenceManager.setUserLoginStatusInSP(false);
        sharedPrefrenceManager.setUserGLoginStatusInSP(false);
        sharedPrefrenceManager.clearPreferences(Permissions.MODULE_PERMISSION);
        sharedPrefrenceManager.clearPreferences(Consts.TRANSLATION_KEYS);
        sharedPrefrenceManager.clearPreferences(Consts.REQUEST_ID_LIST);
        dbHelper.deleteCurrencyDB();

        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showModuleData(ArrayList<ModuleData> moduleList)
    {
        if (sharedPrefrenceManager.getIsParentStatusFromKey())
        {
            dashboardAdapter = new DashboardAdapter(DashboardActivity.this,moduleList);
            rvModules.setAdapter(dashboardAdapter);
            dashboardAdapter.notifyDataSetChanged();

            dashboardAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position)
                {
                    //Handle double tap
                    ProjectUtils.preventTwoClick(view);

                    ModuleData moduleData = moduleList.get(position);

                    int id = moduleData.getId();

                    switch (id)
                    {
                        case Permissions.PARENT_PERMISSION_ATTENDANCE_VIEW:
                        {
                            if (Flags.ENABLE_MULTIPLE_ATTENDANCE_TYPES)
                            {
                                if (sharedPrefrenceManager.getAttendanceTypeCountFromKey() == 1)
                                {
                                    Intent intent = new Intent(mContext, AttendanceMainActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(mContext, MultipleAttendanceMainActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else
                            {
                                Intent intent = new Intent(mContext, AttendanceMainActivity.class);
                                startActivity(intent);
                            }
                        }break;

                        case Permissions.PARENT_PERMISSION_MY_COURSE_VIEW:
                        {
                            Intent intent = new Intent(mContext, MyCoursesListActivity.class);
                            startActivity(intent);
                        }break;

                        case Permissions.PARENT_PERMISSION_FEES_VIEW:
                        {
                            Intent intent = new Intent(mContext, FeesMainActivity.class);
                            startActivity(intent);
                        }break;

                        case Permissions.PARENT_PERMISSION_ASSIGNMENT_VIEW:
                        {
                            permissionSetupAssignment();
                        }break;

                        case Permissions.PARENT_PERMISSION_RESULT_VIEW:
                        {
                            permissionSetupExamination();
                        }break;

                        case Permissions.PARENT_PERMISSION_EVENTS_VIEW:
                        {
                            Intent intent = new Intent(mContext, EventsActivity.class);
                            startActivity(intent);
                        }break;

                        case Permissions.PARENT_PERMISSION_TIMETABLE_VIEW:
                        {
                            Intent intent = new Intent(mContext, TimeTableViewTypeActivity.class);
                            startActivity(intent);
                        }break;

                        case Permissions.PARENT_PERMISSION_CIRCULARS_VIEW:
                        {
                            Intent intent = new Intent(mContext, CircularActivity.class);
                            startActivity(intent);
                        }break;

                        case Permissions.PARENT_PERMISSION_GALLERY_VIEW:
                        {
                            Intent intent = new Intent(mContext, DriveMainActivity.class);
                            startActivity(intent);
                        }break;
                    }
                }
            });

        }
        else
        {
            dashboardAdapter = new DashboardAdapter(DashboardActivity.this,moduleList);
            rvModules.setAdapter(dashboardAdapter);
            dashboardAdapter.notifyDataSetChanged();

            dashboardAdapter.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    //Handle double tap
                    ProjectUtils.preventTwoClick(view);

                    ModuleData moduleData = moduleList.get(position);

                    int id = moduleData.getId();

                    switch (id)
                    {
                        case Permissions.STUDENT_PERMISSION_ATTENDANCE_VIEW:
                        {
                            if (Flags.ENABLE_MULTIPLE_ATTENDANCE_TYPES)
                            {
                                if (sharedPrefrenceManager.getAttendanceTypeCountFromKey() == 1)
                                {
                                    Intent intent = new Intent(mContext, AttendanceMainActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(mContext, MultipleAttendanceMainActivity.class);
                                    startActivity(intent);
                                }
                            }
                            else
                            {
                                Intent intent = new Intent(mContext, AttendanceMainActivity.class);
                                startActivity(intent);
                            }
                        }break;

                        case Permissions.STUDENT_PERMISSION_MY_COURSE_VIEW:
                        {
                            Intent intent = new Intent(mContext, MyCoursesListActivity.class);
                            startActivity(intent);
                        }break;

                        case Permissions.STUDENT_PERMISSION_FEES_VIEW:
                        {
                            Intent intent = new Intent(mContext, FeesMainActivity.class);
                            startActivity(intent);
                        }break;

                        case Permissions.STUDENT_PERMISSION_ASSIGNMENT_VIEW:
                        {
                            permissionSetupAssignment();
                        }break;

                        case Permissions.STUDENT_PERMISSION_RESULT_VIEW:
                        {
                            permissionSetupExamination();
                        }break;

                        case Permissions.STUDENT_PERMISSION_EVENTS_VIEW:
                        {
                            Intent intent = new Intent(mContext, EventsActivity.class);
                            startActivity(intent);
                        }break;

                        case Permissions.STUDENT_PERMISSION_TIMETABLE_VIEW:
                        {
                            Intent intent = new Intent(mContext, TimeTableViewTypeActivity.class);
                            startActivity(intent);
                        }break;

                        case Permissions.STUDENT_PERMISSION_CIRCULARS_VIEW:
                        {
                            Intent intent = new Intent(mContext, CircularActivity.class);
                            startActivity(intent);
                        }break;

                        case Permissions.STUDENT_PERMISSION_GALLERY_VIEW:
                        {
                            Intent intent = new Intent(mContext, DriveMainActivity.class);
                            startActivity(intent);
                        }break;
                    }
                }
            });
        }
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            rvModules.setVisibility(View.INVISIBLE);
            superStateView.setVisibility(View.VISIBLE);
        }
        else
        {
            rvModules.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.refresh:
            {
                populateContents();
                callModulesApi();
                getNotifications();
            }break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        int id = menuItem.getItemId();

        switch (id)
        {
            case R.id.home_drawer:
            {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            break;

            case R.id.myProfile:
            {
                Intent intent = new Intent(DashboardActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.myRequest:
            {
                Intent intent = new Intent(DashboardActivity.this, MyRequestMainActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.reset_drawer:
            {
                Intent intent1 = new Intent(DashboardActivity.this, ResetPassword.class);
                startActivity(intent1);
            }
            break;

            case R.id.switch_student:
            {
                if (ConnectionDetector.isConnectingToInternet(DashboardActivity.this))
                {
                    showProgressDialog(this);
                    new OptimizedServerCallAsyncTask(DashboardActivity.this, DashboardActivity.this, KEYS.SWITCH_ASSOCIATED_SIBLINGS).execute();
                }
                else
                {
                    showAlertDialog(DashboardActivity.this, KEYS.NET_ERROR_HEAD, KEYS.NET_ERROR_MESSAGE);
                }
            }
            break;

            case R.id.rateReview:
            {
                if(Flags.IS_APP_LIVE){
                    Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
                    }
                }else{
                    ProjectUtils.openBrowser(mContext,BaseURL.BASE_URL);
                }
            }
            break;

            case R.id.feedback:
            {
                Intent intent = new Intent(mContext, FeedbackActivity.class);
                startActivity(intent);
            }break;

            case R.id.share_app:
            {
                ProjectUtils.shareApp(mContext,translationManager.SHARE_APP_CONTENT_KEY);
            }break;

            case R.id.logout_drawer:
            {
                if (ConnectionDetector.isConnectingToInternet(DashboardActivity.this))
                {
                    showProgressDialog(this);
                    new OptimizedServerCallAsyncTask(DashboardActivity.this, DashboardActivity.this, KEYS.SWITCH_FCM_LOGOUT).execute();
                    new OptimizedServerCallAsyncTask(DashboardActivity.this, DashboardActivity.this, KEYS.SWITCH_LOGOUT).execute();
                }
                else
                {
                    showAlertDialog(DashboardActivity.this, KEYS.NET_ERROR_HEAD, KEYS.NET_ERROR_MESSAGE);
                }
            }
            break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    public void setInstituteLogo(String imageString)
    {
        if (imageString.isEmpty() || imageString.length() == 0)
        {
            new_instituteIcon.setImageResource(R.drawable.academia_new_logo);
        }
        else
        {
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);

            Glide.with(mContext)
                    .asBitmap()
                    .load(decodedString)
                    .placeholder(R.drawable.academia_new_logo)
                    .into(new_instituteIcon);
        }
    }

    //Backpress double click
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            DashboardActivity.this.finish();
            android.os.Process.killProcess(android.os.Process.myPid());

            super.onBackPressed();
            return;
        }

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        this.doubleBackToExitPressedOnce = true;

        Snackbar.make(linearLayout,getString(R.string.press_again_to_exit),Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void permissionSetupAssignment()
    {
        //Here set Assignment module permission
        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        HOMEWORK_VIEW = 0;
        SESSION_DIARY_VIEW = 0;

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i)) {
                case Permissions.PARENT_PERMISSION_ASSIGNMENT_HOMEWORK_VIEW:
                case Permissions.STUDENT_PERMISSION_ASSIGNMENT_HOMEWORK_VIEW:
                    HOMEWORK_VIEW = 1;
                    break;
                case Permissions.PARENT_PERMISSION_ASSIGNMENT_SESSION_DIARY_VIEW:
                case Permissions.STUDENT_PERMISSION_ASSIGNMENT_SESSION_DIARY_VIEW:
                    SESSION_DIARY_VIEW = 1;
                    break;
            }
        }

        if(HOMEWORK_VIEW != 0 && SESSION_DIARY_VIEW != 0)
        {
            Intent intent = new Intent(mContext, AssignmentMainActivity.class);
            startActivity(intent);
        }
        else if (SESSION_DIARY_VIEW != 0)
        {
            Intent intent = new Intent(mContext, SessionDiaryCourseActivity.class);
            startActivity(intent);
        }
        else if (HOMEWORK_VIEW != 0)
        {
            Intent intent = new Intent(mContext, AssignmentCourseActivity.class);
            startActivity(intent);
        }
        else {
            showAlertDialog(DashboardActivity.this, getString(R.string.info), getString(R.string.permission_for_submodule_not_set));
        }
    }

    private void permissionSetupExamination()
    {
        if(Flags.IS_CLIENT_VELOCITY){

            Intent intent = new Intent(mContext, MarksheetActivity.class);
            startActivity(intent);

        }else{

            if(Flags.DISEABLE_PERMISSION_FOR_TAIPEI){

                Intent intent = new Intent(mContext, ExamMainActivity.class);
                startActivity(intent);

            }else{

                list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

                RESULT_REPORT_VIEW = 0;
                EXAM_DOCS_VIEW = 0;

                for (int i = 0; i < list.size(); i++) {

                    switch (list.get(i)) {
                        case Permissions.PARENT_EXAMINATION_RESULT_REPORT_VIEW:
                        case Permissions.STUDENT_EXAMINATION_RESULT_REPORT_VIEW:
                            RESULT_REPORT_VIEW = 1;
                            break;
                        case Permissions.PARENT_EXAMINATION_EXAM_DOCS_VIEW:
                        case Permissions.STUDENT_EXAMINATION_EXAM_DOCS_VIEW:
                            EXAM_DOCS_VIEW = 1;
                            break;
                    }
                }

                if(RESULT_REPORT_VIEW != 0 && EXAM_DOCS_VIEW != 0)
                {
                    Intent intent = new Intent(mContext, ExamMainActivity.class);
                    startActivity(intent);
                }
                else if (RESULT_REPORT_VIEW != 0)
                {
                    Intent intent = new Intent(mContext, ResultReportActivity.class);
                    startActivity(intent);
                }
                else if (EXAM_DOCS_VIEW != 0)
                {
                    Intent intent = new Intent(mContext, ExamDocActivity.class);
                    startActivity(intent);
                }
                else {
                    showAlertDialog(DashboardActivity.this, getString(R.string.info), getString(R.string.permission_for_submodule_not_set));
                }
            }
        }
    }

    private void permissionSetupSideDrawer() {
        //Here set side drawer module permission
        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        menu.findItem(R.id.myRequest).setVisible(false);
        menu.findItem(R.id.rateReview).setVisible(false);
        menu.findItem(R.id.feedback).setVisible(false);

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i)) {
                case Permissions.PARENT_MY_REQUEST_VIEW:
                case Permissions.STUDENT_MY_REQUEST_VIEW:
                    menu.findItem(R.id.myRequest).setVisible(true);
                    break;
                case Permissions.PARENT_RATE_AND_REVIEW_VIEW:
                case Permissions.STUDENT_RATE_AND_REVIEW_VIEW:
                    menu.findItem(R.id.rateReview).setVisible(true);
                    break;
                case Permissions.PARENT_FEEDBACK_VIEW:
                case Permissions.STUDENT_FEEDBACK_VIEW:
                    menu.findItem(R.id.feedback).setVisible(true);
                    break;
            }
        }
    }
}
