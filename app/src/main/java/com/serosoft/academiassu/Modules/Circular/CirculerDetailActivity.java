package com.serosoft.academiassu.Modules.Circular;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.Widgets.ExpandedListView;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.PDFActivity2;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CirculerDetailActivity extends BaseActivity implements AsyncTaskCompleteListener {

    private Context mContext;
    private TextView titleTV;
    private TextView circularDate,attachmentTV,descriptionTV;
    private LinearLayout docll;
    private ExpandedListView attachmentsListView;
    private JSONObject circular;
    private TextView circularDetail;
    private List<JSONObject> documentList;
    private Toolbar toolbar;

    private String idForDocument = "";
    private String fileName = "";

    public static final int PERMISSION_CODE = 100;

    String permission[] = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circular_detail);

        mContext = CirculerDetailActivity.this;

        Initialize();

        populateDetails();

    }

    private void populateDetails() {

        descriptionTV.setText(translationManager.CIRCULAR_DETAILS_KEY);

        String circularString = getIntent().getStringExtra("circularObject");
        try {
            circular = new JSONObject(circularString);
            setView(circular);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setView(JSONObject circular) {

        try {
            titleTV.setText(circular.optString("subject"));
            Long dateInLong = circular.optLong("date_long");
            String academiaDate = BaseActivity.getAcademiaDate(dateInLong, this);
            String academiaTime = BaseActivity.getAcademiaTime(dateInLong, this);
            circularDate.setText(academiaDate + " " + academiaTime);

            String stringForMessageContent = circular.optString("msgContent");

            String prettyPrintedBodyFragment = Jsoup.clean(stringForMessageContent, "", Whitelist.none().addTags("br", "p"), new Document.OutputSettings().prettyPrint(true));
            String cleanString = prettyPrintedBodyFragment.replaceAll("(?i)<br[^>]*>", "br2n");
            cleanString = Jsoup.parse(cleanString).wholeText();
            cleanString = cleanString.replaceAll("br2n", "\n");
            circularDetail.setText(cleanString);

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Spanned htmlDescription = Html.fromHtml(stringForMessageContent, Html.FROM_HTML_MODE_LEGACY);
                String descriptionWithOutExtraSpace = new String(htmlDescription.toString()).trim();
                circularDetail.setText(htmlDescription.subSequence(0, descriptionWithOutExtraSpace.length()));
                circularDetail.setMovementMethod(new ScrollingMovementMethod());
            } else {
                Spanned htmlDescription = Html.fromHtml(stringForMessageContent);
                String descriptionWithOutExtraSpace = new String(htmlDescription.toString()).trim();
                circularDetail.setText(htmlDescription.subSequence(0, descriptionWithOutExtraSpace.length()));
                circularDetail.setMovementMethod(new ScrollingMovementMethod());
            }*/

            JSONArray documentArray = circular.optJSONArray("msgDocuments");

            if (documentArray != null) {
                documentList = new ArrayList<JSONObject>();
                for (int i = 0; i < documentArray.length(); ++i) {
                    JSONObject obj = documentArray.getJSONObject(i);
                    documentList.add(obj);
                }

                if (documentList.size() == 0) {
                    docll.setVisibility(View.GONE);
                } else {
                    docll.setVisibility(View.VISIBLE);
                }

                if (null != documentList && documentList.size() > 0) {

                    if(documentList.size() == 1){
                        attachmentTV.setText(translationManager.ATTACHMENT_KEY);
                    }else{
                        attachmentTV.setText(translationManager.ATTACHMENTS_KEY);
                    }

                    attachmentsListView.setAdapter(new DocumentDetailAdapter(CirculerDetailActivity.this, documentList));
                    attachmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            JSONObject jsonObject = documentList.get(i);
                            idForDocument = String.valueOf(jsonObject.optInt("id"));
                            fileName = String.valueOf(jsonObject.optString("documentName"));

                            if (ProjectUtils.hasPermissionInManifest(CirculerDetailActivity.this,PERMISSION_CODE,permission))
                            {
                                callAPIWithPermission();
                            }
                        }
                    });
                } else {
                    docll.setVisibility(View.GONE);
                }
            } else {
                docll.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null != circular){
        }
    }
    private void callAPIWithPermission() {

        showProgressDialog(mContext);
        new OptimizedServerCallAsyncTask(CirculerDetailActivity.this, CirculerDetailActivity.this,
                KEYS.SWITCH_NOTIFICATION_DOCUMENT_DOWNLOAD).execute(idForDocument, fileName , Consts.CIRCULARS);
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

                            if (ProjectUtils.hasPermissionInManifest(CirculerDetailActivity.this,PERMISSION_CODE,permission))
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
    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);
        titleTV = findViewById(R.id.titleTV);
        circularDate = findViewById(R.id.circularDate);
        circularDetail = findViewById(R.id.circularDetail);
        docll = findViewById(R.id.docll);
        attachmentsListView = findViewById(R.id.attachmentsListView);
        attachmentTV = findViewById(R.id.attachmentTV);
        descriptionTV = findViewById(R.id.descriptionTV);

        toolbar.setTitle(translationManager.CIRCULAR_DETAILS_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorNotification, toolbar, this); }
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
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result)
    {
        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);

        switch (callFor)
        {
            case KEYS.SWITCH_NOTIFICATION_DOCUMENT_DOWNLOAD:
                hideProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(responseResult);
                Boolean downloaded = jsonObject.optBoolean("downloaded");
                String path = jsonObject.optString("path");
                String extension = FilenameUtils.getExtension(fileName).toUpperCase();
                String message = jsonObject.optString("message");

                if (extension.equalsIgnoreCase("PDF"))
                {
                    if (downloaded)
                    {
                        Intent intent=new Intent(CirculerDetailActivity.this, PDFActivity2.class);
                        intent.putExtra("filePath",path);
                        intent.putExtra("folderName",Consts.CIRCULARS);
                        intent.putExtra("screenName",Consts.CIRCULARS);
                        intent.putExtra("idForDocument",idForDocument);
                        intent.putExtra("headerColor",R.color.colorNotification);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(CirculerDetailActivity.this, message, Toast.LENGTH_LONG).show();
                    if (downloaded)
                    {
                        openFile(new File(path), this);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
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
}
