package com.serosoft.academiassu.Modules.Event;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Event.Adapters.EventAttachmentAdapter;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.PDFActivity2;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventDetailsActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;
    private TextView tvHeading1,tvHeading2,tvHeading3;

    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvEventVenue;
    private TextView tvAdmin;
    private TextView tvEventDescription;
    private ImageView ivEvent;

    private String eventBanner;
    private int eventID;
    private List<JSONObject> attachmentList;
    public EventAttachmentAdapter eventAttachmentAdapter;
    private ListView listViewForAttachments;

    private RelativeLayout eventAttachmentsRL;
    private TextView tvAttachments1;
    private TextView tvEventDescription1,tvEventVenue1,tvStartDate1,tvEndDate1,tvAdmin1;

    private String idForDocument = "";
    private String fileName = "";

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_activity);
        mContext = EventDetailsActivity.this;

        Initialize();

        setEventDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.refresh).setVisible(false);
        return true;
    }

    private void setEventDetails() {

        String admin = getIntent().getStringExtra("conductedBy");
        String name = getIntent().getStringExtra("eventName");
        String venue = getIntent().getStringExtra("eventVenue");
        String description = getIntent().getStringExtra("notes");
        String startTime = getIntent().getStringExtra("start");
        String endTime = getIntent().getStringExtra("end");
        eventBanner = getIntent().getStringExtra("eventBanner");
        eventID = getIntent().getIntExtra("eventID", -1);

        if (venue.equals("null") || venue == null) {
            venue = "Venue not specified!";
        }

        tvStartDate.setText(startTime);
        tvEndDate.setText(endTime);
        tvEventVenue.setText(venue);
        tvAdmin.setText(admin);
        tvEventDescription.setText(description);
        tvHeading2.setText(name);
        tvHeading3.setText(admin);

        if(!eventBanner.equalsIgnoreCase("")){

            if (eventBanner.contains(".")) {
                Integer dot = eventBanner.lastIndexOf(".");
                String s1 = eventBanner.substring(0, dot) + "_banner";
                String s2 = ".jpg";
                eventBanner = BaseURL.BASE_URL + KEYS.ULTIMATE_GALLERY_IMAGE_METHOD + "/" + s1 + s2;
                Picasso.with(this).load(eventBanner).placeholder(R.drawable.event_img_big).into(ivEvent);
            }else{
                Picasso.with(this).load(R.drawable.event_img_big).into(ivEvent);
            }
        }else {
            Picasso.with(this).load(R.drawable.event_img_big).into(ivEvent);
        }

        populateContents();
    }

    private void populateContents() {
        if (ConnectionDetector.isConnectingToInternet(this)) {
            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_EVENT_ATTACHMENTS).execute(String.valueOf(eventID));
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateAttachments() {
        if (attachmentList != null && attachmentList.size() > 0) {
            eventAttachmentsRL.setVisibility(View.VISIBLE);
            setAttachmentAdapter(attachmentList);
        } else {
            eventAttachmentsRL.setVisibility(View.GONE);
        }
    }

    private void setAttachmentAdapter(final List<JSONObject> attachmentList) {

        eventAttachmentAdapter = new EventAttachmentAdapter(this, attachmentList);

        listViewForAttachments.setAdapter(eventAttachmentAdapter);
        listViewForAttachments.setClickable(true);
        listViewForAttachments.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                JSONObject jsonObject = attachmentList.get(position);
                idForDocument = String.valueOf(jsonObject.optInt("id"));
                fileName = "";

                if (jsonObject.has("value")) {
                    String path = jsonObject.optString("secondValue");
                    int index = path.lastIndexOf("/");
                    fileName = path.substring(index + 1);
                } else if (jsonObject.has("documentName")) {
                    String path = jsonObject.optString("path");
                    int index = path.lastIndexOf("/");
                    fileName = path.substring(index + 1);
                    idForDocument = String.valueOf(jsonObject.optInt("documentId"));
                }

                if (ProjectUtils.hasPermissionInManifest(EventDetailsActivity.this,PERMISSION_CODE,permission))
                {
                    callAPIWithPermission();
                }
            }
        });
    }

    private void callAPIWithPermission() {

        showProgressDialog(mContext);
        new OptimizedServerCallAsyncTask(EventDetailsActivity.this, EventDetailsActivity.this,
                KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD).execute(idForDocument, fileName , Consts.EVENTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        boolean hasAllPermissions = false;

        switch (requestCode)
        {
            case PERMISSION_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    hasAllPermissions = true;
                }
                else
                {
                    hasAllPermissions = false;

                    ProjectUtils.showDialog(mContext, getString(R.string.app_name), getString(R.string.allow_all_permissions), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if (ProjectUtils.hasPermissionInManifest(EventDetailsActivity.this,PERMISSION_CODE,permission))
                            {
                                callAPIWithPermission();
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },false);
                }

                if(hasAllPermissions) callAPIWithPermission();

                break;
        }
    }

    public void Initialize()
    {
        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.EVENT_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorEvents, toolbar, this); }

        tvHeading1 = findViewById(R.id.tvHeading1);
        tvHeading2 = findViewById(R.id.tvHeading2);
        tvHeading3 = findViewById(R.id.tvHeading3);

        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvEventVenue = findViewById(R.id.tvEventVenue);
        tvAdmin = findViewById(R.id.tvAdmin);
        tvEventDescription = findViewById(R.id.tvEventDescription);
        ivEvent = findViewById(R.id.ivEvent);

        tvHeading1.setVisibility(View.GONE);

        listViewForAttachments = findViewById(R.id.attachmentsListView);
        eventAttachmentsRL = findViewById(R.id.eventAttachmentsRL);

        tvAttachments1 = findViewById(R.id.tvAttachments1);
        tvEventDescription1 = findViewById(R.id.tvEventDescription1);
        tvEventVenue1 = findViewById(R.id.tvEventVenue1);
        tvStartDate1 = findViewById(R.id.tvStartDate1);
        tvEndDate1 = findViewById(R.id.tvEndDate1);
        tvAdmin1 = findViewById(R.id.tvAdmin1);

        tvEventVenue1.setText(translationManager.EVENT_VENUE_KEY);
        tvEventDescription1.setText(translationManager.EVENT_DESCRIPTION_KEY);
        tvAdmin1.setText(translationManager.EVENT_ADMIN_KEY);
        tvStartDate1.setText(translationManager.EVENT_START_DATE_KEY);
        tvEndDate1.setText(translationManager.EVENT_END_DATE_KEY);
        tvAttachments1.setText(translationManager.ATTACHMENTS_KEY);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result)
    {
        String callForSwitch = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);

        switch (callForSwitch)
        {
            case KEYS.SWITCH_EVENT_PICTURE:
                hideProgressDialog();
                try
                {
                    JSONObject responseObject = new JSONObject(responseResult);
                    String stringForImage = responseObject.getString("value");
                    byte[] decodedString = Base64.decode(stringForImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);

                    ivEvent.setImageBitmap(decodedByte);

                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgressDialog();
                    showAlertDialog(this, "OOPS!", "Parsing Error at " + this.getLocalClassName());
                }
                break;

            case KEYS.SWITCH_EVENT_ATTACHMENTS:
                hideProgressDialog();
                attachmentList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    JSONArray array;

                    if (responseObject.has("whatever")) {
                        array = responseObject.getJSONArray("whatever");
                        if (array.length() > 0) {
                            for (int i = 0 ; i < array.length() ; ++i ) {
                                JSONObject obj = array.getJSONObject(i);
                                attachmentList.add(obj);
                            }
                        }
                    }
                    populateAttachments();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case KEYS.SWITCH_HOMEWORK_DOCUMENT_DOWNLOAD:
                hideProgressDialog();
                try
                {
                    JSONObject jsonObject = new JSONObject(responseResult);
                    Boolean downloaded = jsonObject.optBoolean("downloaded");
                    String path = jsonObject.optString("path");
                    String extension = FilenameUtils.getExtension(fileName).toUpperCase();
                    String message = jsonObject.optString("message");

                    if (extension.equalsIgnoreCase("PDF"))
                    {
                        if (downloaded)
                        {
                            Intent intent=new Intent(mContext, PDFActivity2.class);
                            intent.putExtra("filePath",path);
                            intent.putExtra("folderName",Consts.EVENTS);
                            intent.putExtra("screenName",Consts.EVENTS);
                            intent.putExtra("idForDocument",idForDocument);
                            intent.putExtra("headerColor",R.color.colorEvents);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(this,message,Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                        if (downloaded)
                        {
                            openFile(new File(path), this);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }break;
            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;
            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;
        }
    }
}
