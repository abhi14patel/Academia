package com.serosoft.academiassu.Modules.TimeTable;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.TimeLineView.TimeLineAdapter;
import com.serosoft.academiassu.Modules.TimeLineView.model.OrderStatus;
import com.serosoft.academiassu.Modules.TimeLineView.model.Orientation;
import com.serosoft.academiassu.Modules.TimeLineView.model.TimeLineModel;
import com.serosoft.academiassu.Modules.TimeLineView.utils.DateTimeUtils;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TimeTableDayWiseDetailListActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Toolbar toolbar;
    private List<JSONObject> timeTableViewType2018List;
    private TextView textViewForHeader;
    private String dateOfTimeTable;
    private String timeTableString;

    //TimeLine Related:
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private List<TimeLineModel> mDataList2 = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_day_wise_detail_list_2018);
        dateOfTimeTable = getIntent().getStringExtra("startDate");

        Initialize();

        populateListView();

        //TimeLine Related:
        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = false;
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewForTimeLine);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
        setDataListItems();
        mTimeLineAdapter = new TimeLineAdapter(mDataList, mDataList2, mOrientation, mWithLinePadding);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void populateListView() {
        try {
            timeTableString =  getIntent().getStringExtra("timeTableString");
//            timeTableString = TimeTableDayWiseData.getInstance().timeTableDayWiseString;

            JSONArray arr = new JSONArray(timeTableString);

            timeTableViewType2018List = new ArrayList<JSONObject>();

            for (int i = 0 ; i < arr.length() ; ++i ) {

                JSONObject obj = arr.getJSONObject(i);

                Long dateInLong = obj.optLong("orgDate_long");
//                String academiaDate = BaseActivity.getAcademiaDate(dateInLong, this);
                String dateString = BaseActivity.stringFromDate("yyyy-M-d", new Date(dateInLong));

//                String startDate = obj.getString("start");
//                startDate = BaseActivity.getAcademiaDate("yyyy-MM-dd'T'HH:mm:ss", startDate, this);
                if (dateString.equals(dateOfTimeTable)) {
                    timeTableViewType2018List.add(obj);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.DAY_WISE_KEY);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorTimetable, toolbar, this); }

        textViewForHeader = findViewById(R.id.sub_info_tv_second);
        String d1 = DateTimeUtils.parseDateTime(dateOfTimeTable, "yyyy-MM-dd", "dd-MMM-yyyy");
        textViewForHeader.setText(d1);
    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
    }
    private void setDataListItems(){

        for (int i = 0 ; i < timeTableViewType2018List.size() ; ++i) {
            JSONObject jsonObject = timeTableViewType2018List.get(i);
            String msg = jsonObject.optString("nameToBePrinted");
            String dateString1 = jsonObject.optString("start");
            String dateString2 = jsonObject.optString("end");
            Long dateInLong1 = jsonObject.optLong("startTime_long");
            Long dateInLong2 = jsonObject.optLong("endTime_long");
            String colrString = jsonObject.optString("color");
            String facultyString = jsonObject.optString("conductedBy");
            String roomString = jsonObject.optString("classroom");
            mDataList.add(new TimeLineModel(msg, dateString1, dateInLong1, OrderStatus.COMPLETED, colrString, facultyString, roomString));
            mDataList2.add(new TimeLineModel(msg, dateString2, dateInLong2, OrderStatus.COMPLETED, colrString, facultyString, roomString));
        }
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
                getNotifications();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.refresh).setVisible(false);
        return true;
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

        switch (callFor) {
            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;
            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;
        }
    }
}