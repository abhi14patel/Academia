package com.serosoft.academiassu.Modules.AcademiaDrive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.serosoft.academiassu.Widgets.CustomViewPager;
import com.serosoft.academiassu.Helpers.ViewPagerAdapter;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.AcademiaDrive.Fragments.DriveDocumentFragment;
import com.serosoft.academiassu.Modules.AcademiaDrive.Fragments.DriveMediaFragment;
import com.serosoft.academiassu.Modules.AcademiaDrive.Models.DriveFile_Dto;
import com.serosoft.academiassu.Modules.AcademiaDrive.Models.DriveFolder_Dto;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
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
 * Created by Abhishek on April 07 2020.
 */

public class DriveTabActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    public Toolbar toolbar;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;

    DriveFolder_Dto driveFolder_dto;

    String title = "";
    String id = "";

    private DriveMediaFragment driveMediaFragment = null;
    private DriveDocumentFragment driveDocumentFragment = null;

    ArrayList<DriveFile_Dto> documentList;
    ArrayList<DriveFile_Dto> mediaList;

    private final String TAG = DriveTabActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive_tab_activity);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = DriveTabActivity.this;

        Intent intent = getIntent();
        driveFolder_dto = (DriveFolder_Dto) intent.getSerializableExtra("folder");
        title = driveFolder_dto.getAlbumName();
        id = String.valueOf(driveFolder_dto.getId());

        Initialize();

        populateContents();
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {
            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_ALBUM_DETAILS).execute(id);
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(title.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorDrive, toolbar, this); }

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setPagingEnabled(false);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(driveDocumentFragment,translationManager.DOCUMENTS_KEY);
        viewPagerAdapter.addFragment(driveMediaFragment,translationManager.MEDIA_FILES_KEY);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result) {
        String callForSwitch = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);
        JSONArray arr;

        switch (callForSwitch) {

            case KEYS.SWITCH_ALBUM_DETAILS:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    JSONArray jsonArray = responseObject.optJSONArray("whatever");

                    documentList = new ArrayList<>();
                    mediaList = new ArrayList<>();

                    if (jsonArray != null && jsonArray.length() > 0) {

                        for(int i = 0 ; i< jsonArray.length() ; i++) {

                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                            String imageName = jsonObject1.optString("imageName");
                            String path = jsonObject1.optString("path");
                            String fileType = jsonObject1.optString("fileType");
                            int id = jsonObject1.optInt("id");
                            int galleryId = jsonObject1.optInt("galleryId");

                            if(fileType.equalsIgnoreCase("png") || fileType.equalsIgnoreCase("jpg") || fileType.equalsIgnoreCase("jpeg") || fileType.equalsIgnoreCase("gif")){
                                mediaList.add(new DriveFile_Dto(imageName,path,fileType,id,galleryId));
                            }else{
                                documentList.add(new DriveFile_Dto(imageName,path,fileType,id,galleryId));
                            }
                        }

                        driveDocumentFragment = new DriveDocumentFragment();
                        Bundle args2 = new Bundle();
                        args2.putSerializable("DocumentList", documentList);
                        driveDocumentFragment.setArguments(args2);

                        driveMediaFragment = new DriveMediaFragment();
                        Bundle args3 = new Bundle();
                        args3.putSerializable("MediaList", mediaList);
                        driveMediaFragment.setArguments(args3);

                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);
                    }else{
                        driveMediaFragment = new DriveMediaFragment();
                        driveDocumentFragment = new DriveDocumentFragment();

                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    hideProgressDialog();

                    driveMediaFragment = new DriveMediaFragment();
                    driveDocumentFragment = new DriveDocumentFragment();

                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                }
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
                populateContents();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
