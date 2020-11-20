package com.serosoft.academiassu.Modules.Attendance;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Attendance.Adapters.MultipleCalendarAdapter;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class MultipleAttendanceActivity extends BaseActivity implements AsyncTaskCompleteListener {

    public GregorianCalendar cal_month;
    private MultipleCalendarAdapter cal_adapter;

    private TextView tvMonth;
    private LinearLayout llPrevious;
    private LinearLayout llNext;

    private GridView gvCalendar;
    private TextView groupTypeNameTextView;
    private TextView groupNameTextView;
    private TextView groupAdminTextView;
    private ImageView groupIcon;

    private RelativeLayout attendanceRL;
    private TextView noAttendanceDetail;
    private Toolbar toolbar;
    private List<JSONObject> attendanceList;

    private int totalTillDate;
    private int presentTillDate;
    private int absentTillDate;
    private int otherTillDate;

    private TextView tvPresentValue;
    private TextView tvAbsentValue;
    private TextView tvTotalClassesValue;

    private TextView tvPresentValue2;
    private TextView tvAbsentValue2;
    private TextView tvOtherValue2;
    private TextView tvTotalClassesValue2;

    private String startDate="",endDate="";
    private String fStartDate="",fEndDate="";
    private String currentDate = "";

    Date dateStart,dateEnd,dateCurrent;

    private int courseId;
    public String attendanceType;
    public String sectionId = "";
    public String periodId = "";

    RelativeLayout rl1,rl2;
    ImageView ivNext,ivPrevious;
    TextView tvOverall,tvThisMonth;
    TextView tvPresent,tvPresent1,tvPresent2,tvAbsent,tvAbsent1,tvAbsent2,tvOther,tvOther2,tvTotalClasses1,tvTotalClasses2;
    ProgressBar pbPresent1,pbAbsent1,pbPresent2,pbAbsent2,pbOther2;
    TextView tvPresentPercentage1,tvAbsentPercentage1,tvPresentPercentage2,tvAbsentPercentage2,tvOtherPercentage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_attendance2);

        Initialize();

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();

        String title = getIntent().getStringExtra("title");
        String details = getIntent().getStringExtra("details");

        totalTillDate = getIntent().getIntExtra("totalTillDate", 0);
        presentTillDate = getIntent().getIntExtra("presentTillDate", 0);
        absentTillDate = getIntent().getIntExtra("absentTillDate", 0);
        otherTillDate = getIntent().getIntExtra("otherTillDate", 0);
        attendanceType = getIntent().getStringExtra("attendanceType");
        courseId = getIntent().getIntExtra("courseId", 0);
        sectionId = getIntent().getStringExtra("sectionId");
        periodId = getIntent().getStringExtra("periodId");
        fStartDate = ProjectUtils.getCorrectedString(getIntent().getStringExtra("fStartDate"));
        fEndDate = ProjectUtils.getCorrectedString(getIntent().getStringExtra("fEndDate"));

        groupNameTextView.setText(title);
        groupAdminTextView.setText(details);
        groupTypeNameTextView.setVisibility(View.GONE);
        groupIcon.setImageResource(R.mipmap.attendance_white_2018);

        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorAttendance, toolbar, this); }

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(MultipleAttendanceActivity.this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_STUDENT_CALENDAR_DURATION).execute(periodId);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateContents() {

        populateTillDateData();

        Date date = cal_month.getTime();
        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM");

        try {
            dateStart = sdfo.parse(startDate);
            dateEnd = sdfo.parse(endDate);
            currentDate = sdfo.format(date);
            dateCurrent = sdfo.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Here check is current date between start date and end date
        boolean isBetween = ProjectUtils.CheckDates(startDate,endDate,currentDate);

        if(isBetween){
            //If current date between startDate and endDate current date month calender open
            cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        }else{
            //Here start calender from start date
            String[] dateStart = startDate.split("-");
            int y1 = Integer.parseInt(dateStart[0]);
            int m1 = Integer.parseInt(dateStart[1]);

            cal_month.set(y1,m1-1,1);
            ProjectUtils.showLog("TAG",""+cal_month);
        }

        monthAttendanceMethod(cal_month);
    }

    private void setAdapter() {
        //---------- Calendar View----------
        if (null != attendanceList) {
            setAttendance(attendanceList);
        } else if (ConnectionDetector.isConnectingToInternet(MultipleAttendanceActivity.this)) {
            attendanceRL.setVisibility(View.GONE);
            noAttendanceDetail.setVisibility(View.VISIBLE);
        } else {
            attendanceRL.setVisibility(View.GONE);
            noAttendanceDetail.setVisibility(View.VISIBLE);
            noAttendanceDetail.setText(KEYS.NET_ERROR_MESSAGE);
        }
    }

    private void populateTillDateData() {
        tvTotalClassesValue.setText(String.valueOf(totalTillDate));
        tvPresentValue.setText(String.valueOf(presentTillDate));
        tvAbsentValue.setText(String.valueOf(absentTillDate));

        float presentPercentage = (Float.parseFloat(String.valueOf(presentTillDate)) * 100)/Float.parseFloat(String.valueOf(totalTillDate));
        float absentPercentage = (Float.parseFloat(String.valueOf(absentTillDate)) * 100)/Float.parseFloat(String.valueOf(totalTillDate));

        String present = String.format("%.1f", presentPercentage);
        String absent = String.format("%.1f", absentPercentage);

        tvPresentPercentage1.setText(present+"%");
        pbPresent1.setProgress((int) presentPercentage);
        tvAbsentPercentage1.setText(absent+"%");
        pbAbsent1.setProgress((int) absentPercentage);
    }

    private void setAttendance(List<JSONObject> attendanceList) {
        cal_adapter = new MultipleCalendarAdapter(this, cal_month, attendanceList, this.attendanceType);
        tvMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        Date date = cal_month.getTime();
        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM");

        try {
            dateStart = sdfo.parse(startDate);
            dateEnd = sdfo.parse(endDate);
            currentDate = sdfo.format(date);
            dateCurrent = sdfo.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        llPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectUtils.preventTwoClick(llPrevious);
                ProjectUtils.preventTwoClick(llNext);

                if(dateCurrent.after(dateStart)){
                    setPreviousMonth();
                    refreshCalendar();
                }
            }
        });

        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectUtils.preventTwoClick(llPrevious);
                ProjectUtils.preventTwoClick(llNext);

                if(dateCurrent.before(dateEnd)){
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });

        gvCalendar.setAdapter(cal_adapter);
        gvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ((MultipleCalendarAdapter) parent.getAdapter()).setSelected(v, position);
            }
        });
    }

    private void monthAttendanceMethod(GregorianCalendar cal_month) {

        if (ConnectionDetector.isConnectingToInternet(MultipleAttendanceActivity.this)) {

            int iYear = cal_month.get(Calendar.YEAR);
            int iMonth = cal_month.get(Calendar.MONTH) + 1;
            int iDay = 0;
            Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);
            int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

            String month = String.valueOf(cal_month.get(Calendar.MONTH) + 1);
            String year = String.valueOf(cal_month.get(Calendar.YEAR));

            String startDate = year + "-" + month + "-" + "1";
            String endDate = year + "-" + month + "-" + daysInMonth;

            showProgressDialog(this);
            if (attendanceType.equals(KEYS.COMPLETE_DAY)) {
                new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY_MONTHWISE)
                        .execute(startDate, endDate, sectionId);
            } else if (attendanceType.equals(KEYS.MULTIPLE_SESSION)) {
                new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION_MONTHWISE)
                        .execute(startDate, endDate, sectionId);
            } else {
                new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL_MONTHWISE)
                        .execute(String.valueOf(courseId), startDate, endDate);
            }

        } else {
            Toast.makeText(MultipleAttendanceActivity.this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.ATTENDANCE_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorAttendance, toolbar, this); }

        tvMonth = findViewById(R.id.tvMonth);
        llPrevious = findViewById(R.id.llPrevious);
        llNext = findViewById(R.id.llNext);

        gvCalendar = findViewById(R.id.gvCalendar);
        groupIcon = findViewById(R.id.groupIcon);
        groupTypeNameTextView = findViewById(R.id.group_type_userInfo);
        groupNameTextView = findViewById(R.id.group_name_userInfo);
        groupAdminTextView = findViewById(R.id.admin_name_userInfo);

        attendanceRL = findViewById(R.id.attendanceRL);
        noAttendanceDetail = findViewById(R.id.noAttendanceDetail);

        ivNext = findViewById(R.id.ivNext);
        ivPrevious = findViewById(R.id.ivPrevious);

        tvPresent = findViewById(R.id.tvPresent);
        tvAbsent = findViewById(R.id.tvAbsent);
        tvOther = findViewById(R.id.tvOther);

        tvOverall = findViewById(R.id.tvOverall);
        tvTotalClasses1 = findViewById(R.id.tvTotalClasses1);
        tvPresent1 = findViewById(R.id.tvPresent1);
        tvAbsent1 = findViewById(R.id.tvAbsent1);
        tvPresentValue = findViewById(R.id.tvPresentValue);
        tvAbsentValue = findViewById(R.id.tvAbsentValue);
        tvTotalClassesValue = findViewById(R.id.tvTotalClassesValue);

        tvThisMonth = findViewById(R.id.tvThisMonth);
        tvTotalClasses2 = findViewById(R.id.tvTotalClasses2);
        tvPresent2 = findViewById(R.id.tvPresent2);
        tvAbsent2 = findViewById(R.id.tvAbsent2);
        tvOther2 = findViewById(R.id.tvOther2);
        tvPresentValue2 = findViewById(R.id.tvPresentValue2);
        tvAbsentValue2 = findViewById(R.id.tvAbsentValue2);
        tvOtherValue2 = findViewById(R.id.tvOtherValue2);
        tvTotalClassesValue2 = findViewById(R.id.tvTotalClassesValue2);

        rl1 = findViewById(R.id.rl1);
        rl2 = findViewById(R.id.rl2);

        pbPresent1 = findViewById(R.id.pbPresent1);
        pbAbsent1 = findViewById(R.id.pbAbsent1);
        pbPresent2 = findViewById(R.id.pbPresent2);
        pbAbsent2 = findViewById(R.id.pbAbsent2);
        pbOther2 = findViewById(R.id.pbOther2);

        tvPresentPercentage1 = findViewById(R.id.tvPresentPercentage1);
        tvAbsentPercentage1 = findViewById(R.id.tvAbsentPercentage1);
        tvPresentPercentage2 = findViewById(R.id.tvPresentPercentage2);
        tvAbsentPercentage2 = findViewById(R.id.tvAbsentPercentage2);
        tvOtherPercentage2 = findViewById(R.id.tvOtherPercentage2);

        tvPresent.setText(translationManager.PRESENT_KEY);
        tvPresent1.setText(translationManager.PRESENT_KEY+" :");
        tvPresent2.setText(translationManager.PRESENT_KEY+" :");

        tvAbsent.setText(translationManager.ABSENT_KEY);
        tvAbsent1.setText(translationManager.ABSENT_KEY+" :");
        tvAbsent2.setText(translationManager.ABSENT_KEY+" :");

        tvOther.setText(translationManager.OTHER_KEY);
        tvOther2.setText(translationManager.OTHER_KEY+" :");

        tvTotalClasses1.setText(translationManager.TOTAL_CLASSES_KEY+" :");
        tvTotalClasses2.setText(translationManager.TOTAL_CLASSES_KEY+" :");

        tvOverall.setText(translationManager.OVERALL_KEY);
        tvThisMonth.setText(translationManager.THISMONTH_KEY);

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rl1.setVisibility(View.GONE);
                rl2.setVisibility(View.VISIBLE);
            }
        });

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl2.setVisibility(View.GONE);
                rl1.setVisibility(View.VISIBLE);
            }
        });
    }

    protected void setNextMonth() {

        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }

        monthAttendanceMethod(cal_month);
    }

    protected void setPreviousMonth() {

        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }

        monthAttendanceMethod(cal_month);
    }

    public void refreshCalendar() {
        tvMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
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

    @Override
    public void onTaskComplete(HashMap<String, String> result) {

        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);
        JSONArray arr;

        switch (callFor) {
            case KEYS.SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY_MONTHWISE:
                hideProgressDialog();
                attendanceList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("rows")) {
                        arr = responseObject.getJSONArray("rows");

                        Integer total = arr.length();
                        Integer present = 0;
                        Integer absent = 0;
                        Integer others = total;

                        for (int i = 0; i < arr.length(); ++i) {
                            JSONObject object = arr.getJSONObject(i);
                            attendanceList.add(object);
                            String attendanceStatus = object.getString("attendanceStatus");
                            if (attendanceStatus.equalsIgnoreCase("Present")) {
                                ++present;
                                --others;
                            } else if (attendanceStatus.equalsIgnoreCase("Absent")) {
                                ++absent;
                                --others;
                            }
                        }

                        JSONObject object = new JSONObject();
                        object.put("total", total);
                        object.put("present", present);
                        object.put("absent", absent);
                        object.put("others", others);

                        JSONObject attendance = object;
                        total = attendance.getInt("total");
                        present = attendance.getInt("present");
                        absent = attendance.getInt("absent");
                        others = attendance.getInt("others");

                        tvTotalClassesValue2.setText(total.toString());
                        tvPresentValue2.setText(present.toString());
                        tvAbsentValue2.setText(absent.toString());
                        tvOtherValue2.setText(others.toString());

                        showThisMonthPercentage(total, present, absent, others);

                        setAdapter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION_MONTHWISE:
                hideProgressDialog();
                attendanceList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("rows")) {
                        arr = responseObject.getJSONArray("rows");

                        Integer total = arr.length();
                        Integer present = 0;
                        Integer absent = 0;
                        Integer others = total;

                        for (int i = 0; i < arr.length(); ++i) {
                            JSONObject object = arr.getJSONObject(i);
                            attendanceList.add(object);
                            String attendanceStatus = object.getString("attendanceStatus");
                            if (attendanceStatus.equalsIgnoreCase("Present")) {
                                ++present;
                                --others;
                            } else if (attendanceStatus.equalsIgnoreCase("Absent")) {
                                ++absent;
                                --others;
                            }
                        }

                        JSONObject object = new JSONObject();
                        object.put("total", total);
                        object.put("present", present);
                        object.put("absent", absent);
                        object.put("others", others);

                        JSONObject attendance = object;
                        total = attendance.getInt("total");
                        present = attendance.getInt("present");
                        absent = attendance.getInt("absent");
                        others = attendance.getInt("others");

                        tvTotalClassesValue2.setText(total.toString());
                        tvPresentValue2.setText(present.toString());
                        tvAbsentValue2.setText(absent.toString());
                        tvOtherValue2.setText(others.toString());

                        showThisMonthPercentage(total, present, absent, others);

                        setAdapter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL_MONTHWISE:
                try {
                    hideProgressDialog();
                    attendanceList = new ArrayList<>();

                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("rows")) {
                        arr = responseObject.getJSONArray("rows");
                        Integer total = arr.length();
                        Integer present = 0;
                        Integer absent = 0;
                        Integer others = total;

                        for (int i = 0; i < arr.length(); ++i) {
                            JSONObject object = arr.getJSONObject(i);
                            attendanceList.add(object);
                            String attendanceStatus = object.getString("attendanceStatus");
                            if (attendanceStatus.equalsIgnoreCase("Present")) {
                                ++present;
                                --others;
                            } else if (attendanceStatus.equalsIgnoreCase("Absent")) {
                                ++absent;
                                --others;
                            }
                        }

                        JSONObject object = new JSONObject();
                        object.put("total", total);
                        object.put("present", present);
                        object.put("absent", absent);
                        object.put("others", others);

                        JSONObject attendance = object;
                        total = attendance.getInt("total");
                        present = attendance.getInt("present");
                        absent = attendance.getInt("absent");
                        others = attendance.getInt("others");

                        tvTotalClassesValue2.setText(total.toString());
                        tvPresentValue2.setText(present.toString());
                        tvAbsentValue2.setText(absent.toString());
                        tvOtherValue2.setText(others.toString());

                        showThisMonthPercentage(total, present, absent, others);

                        setAdapter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_COMPLETE_DAY:
                hideProgressDialog();
                attendanceList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    arr = responseObject.getJSONArray("rows");

                    if (arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            attendanceList.add(obj);
                        }
                    }
                    setAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlertDialog(this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
                }
                break;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_MULTIPLE_SESSION:
                hideProgressDialog();
                attendanceList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    arr = responseObject.getJSONArray("rows");

                    if (arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            attendanceList.add(obj);
                        }
                    }
                    setAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlertDialog(this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
                }
                break;

            case KEYS.SWITCH_ATTENDANCE_DETAILS_COURSE_LEVEL:
                hideProgressDialog();
                attendanceList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    arr = responseObject.getJSONArray("rows");

                    if (arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            attendanceList.add(obj);
                        }
                    }
                    setAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlertDialog(this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
                }
                break;

            case KEYS.SWITCH_STUDENT_CALENDAR_DURATION:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    Long startLong = responseObject.optLong("startDate");
                    Long endLong = responseObject.optLong("endDate");

                    //Here set startMonth and endMonth
                    startDate = ProjectUtils.getMonth1(startLong);
                    endDate = ProjectUtils.getMonth1(endLong);

                    if(!fStartDate.equalsIgnoreCase("") && !fEndDate.equalsIgnoreCase("")){
                        startDate = fStartDate;
                        endDate = fEndDate;
                    }else if(!fStartDate.equalsIgnoreCase("")){
                        startDate = fStartDate;
                    }else if(!fEndDate.equalsIgnoreCase("")){
                        endDate = fEndDate;
                    }

                    populateContents();

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

    private void showThisMonthPercentage(Integer total, Integer present, Integer absent, Integer others) {

        tvPresentPercentage2.setText("0.0%");
        tvAbsentPercentage2.setText("0.0%");
        tvOtherPercentage2.setText("0.0%");

        float presentPercentage = (Float.parseFloat(String.valueOf(present)) * 100) / Float.parseFloat(String.valueOf(total));
        float absentPercentage = (Float.parseFloat(String.valueOf(absent)) * 100) / Float.parseFloat(String.valueOf(total));
        float otherPercentage = (Float.parseFloat(String.valueOf(others)) * 100) / Float.parseFloat(String.valueOf(total));

        String presentPer = String.format("%.1f", presentPercentage);
        String absentPer = String.format("%.1f", absentPercentage);
        String otherPer = String.format("%.1f", otherPercentage);

        if(!presentPer.equalsIgnoreCase("NaN")){
            tvPresentPercentage2.setText(presentPer + "%");
        }

        if(!absentPer.equalsIgnoreCase("NaN")){
            tvAbsentPercentage2.setText(absentPer + "%");
        }

        if(!otherPer.equalsIgnoreCase("NaN")){
            tvOtherPercentage2.setText(otherPer + "%");
        }

        pbPresent2.setProgress((int) presentPercentage);
        pbAbsent2.setProgress((int) absentPercentage);
        pbOther2.setProgress((int) otherPercentage);
    }
}