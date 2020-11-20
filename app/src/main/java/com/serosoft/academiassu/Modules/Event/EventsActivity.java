package com.serosoft.academiassu.Modules.Event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.serosoft.academiassu.Widgets.CustomViewPager;
import com.serosoft.academiassu.Helpers.ViewPagerAdapter;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Event.Fragments.EventCurrentFragment;
import com.serosoft.academiassu.Modules.Event.Fragments.EventPastFragment;
import com.serosoft.academiassu.Modules.Event.Fragments.EventUpcomingFragment;
import com.serosoft.academiassu.Modules.Event.Models.Events_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek on November 23 2019.
 */

public class EventsActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    public Toolbar toolbar;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;

    ArrayList<Events_Dto> currentEventList;
    ArrayList<Events_Dto> upcomingEventList;
    ArrayList<Events_Dto> pastEventList;

    private EventUpcomingFragment eventUpcomingFragment = null;
    private EventCurrentFragment eventCurrentFragment = null;
    private EventPastFragment eventPastFragment = null;

    private final String TAG = EventsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = EventsActivity.this;

        Initialize();

        populateContents();

        Bundle bundle = new Bundle();
        bundle.putInt("StudentId", sharedPrefrenceManager.getUserIDFromKey());
        firebaseAnalytics.logEvent(getString(R.string.events), bundle);
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_STUDENT_EVENTS).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.EVENT_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorEvents, toolbar, this); }

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setPagingEnabled(false);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(currentEventList!=null && currentEventList.size()>0)
        {
            viewPagerAdapter.addFragment(eventCurrentFragment,"OnGoing");
        }
        viewPagerAdapter.addFragment(eventUpcomingFragment,"Upcoming");
        viewPagerAdapter.addFragment(eventPastFragment,"Past");
        viewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

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
                Intent intent = new Intent(mContext,EventsActivity.class);
                startActivity(intent);
                finish();

            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    public JSONArray correctEventArray(JSONArray jsonArray) {
        JSONArray correctedArray = new JSONArray();
        for(int i = 0 ; i<jsonArray.length() ; i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            Boolean isCompleteDay = jsonObject.optBoolean("isCompleteDay");
            long startDate = jsonObject.optLong("start_long");
            if (isCompleteDay) {
                long afterAdding1439Mins = startDate + 86340000;
                try {
                    jsonObject.put("end_long", afterAdding1439Mins);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            correctedArray.put(jsonObject);
        }

        return correctedArray;
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result) {
        String callForSwitch = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);

        switch (callForSwitch) {

            case KEYS.SWITCH_STUDENT_EVENTS:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    JSONArray jsonArray = responseObject.optJSONArray("whatever");
                    jsonArray = correctEventArray(jsonArray);

                    currentEventList = new ArrayList<>();
                    upcomingEventList = new ArrayList<>();
                    pastEventList = new ArrayList<>();

                    for(int i = 0 ; i<jsonArray.length() ; i++) {

                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        int id = jsonObject.optInt("id");
                        int eventId = jsonObject.optInt("eventId");
                        long start_long = jsonObject.optLong("start_long");
                        Boolean isCompleteDay = jsonObject.optBoolean("isCompleteDay");
                        long end_long = jsonObject.optLong("end_long");
                        String start = ProjectUtils.convertTimestampToDate(start_long, mContext);
                        String end = ProjectUtils.convertTimestampToDate(end_long, mContext);
                        String title = jsonObject.optString("title");
                        String notes = jsonObject.optString("notes");
                        String eventBanner = jsonObject.optString("eventBanner");
                        String eventVenue = jsonObject.optString("eventVenue");
                        String conductedBy = jsonObject.optString("conductedBy");
                        String eventName = jsonObject.optString("eventName");
                        Boolean isRecurring = jsonObject.optBoolean("isRecurring");

                        int status = getEventStatus(start_long,end_long);
                        ProjectUtils.showLog(TAG,"Status = "+status);

                        if(status == 1){

                            pastEventList.add(new Events_Dto(id,eventId,1,start_long,end_long, start,end, title, notes, eventBanner, eventVenue, conductedBy, eventName, isCompleteDay, isRecurring));
                        }
                        else if(status == 2){

                            upcomingEventList.add(new Events_Dto(id,eventId,2,start_long, end_long, start, end, title, notes, eventBanner, eventVenue, conductedBy, eventName, isCompleteDay, isRecurring));
                        }
                        else if(status == 0){

                            currentEventList.add(new Events_Dto(id,eventId,0,start_long,end_long,start,end,title, notes, eventBanner, eventVenue, conductedBy, eventName, isCompleteDay, isRecurring));
                        }
                    }

                        eventCurrentFragment = new EventCurrentFragment();
                        Bundle args1 = new Bundle();
                        args1.putSerializable("Current", currentEventList);
                        eventCurrentFragment.setArguments(args1);

                        eventUpcomingFragment = new EventUpcomingFragment();
                        Bundle args2 = new Bundle();
                        args2.putSerializable("Upcoming", upcomingEventList);
                        eventUpcomingFragment.setArguments(args2);

                        eventPastFragment = new EventPastFragment();
                        Bundle args3 = new Bundle();
                        args3.putSerializable("Past", pastEventList);
                        eventPastFragment.setArguments(args3);

                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);

                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgressDialog();
                    ProjectUtils.showLog(TAG,""+e.getMessage());

                    eventUpcomingFragment = new EventUpcomingFragment();
                    eventCurrentFragment = new EventCurrentFragment();
                    eventPastFragment = new EventPastFragment();

                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
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

    //Here get event status according to start and end date
    private int getEventStatus(long start_long, long end_long) {

        long currentDate = System.currentTimeMillis();

        //here for past event
        if(currentDate > end_long) {
            return 1;
        }
        //here for upcoming event
        else if(currentDate < start_long) {
            return 2;
        }
        //here for current event
        else {
            return 0;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
