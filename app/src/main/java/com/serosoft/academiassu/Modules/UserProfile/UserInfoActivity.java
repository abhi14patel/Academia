package com.serosoft.academiassu.Modules.UserProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.cipolat.superstateview.SuperStateView;
import com.google.android.material.tabs.TabLayout;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Helpers.FilePath;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Networking.ServiceCalls;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Utils.ImageRotation;
import com.serosoft.academiassu.Widgets.BottomDialog;
import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Widgets.CustomViewPager;
import com.serosoft.academiassu.Helpers.ViewPagerAdapter;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Widgets.ProfileCircularImage;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.Utils.RSAEncryption;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abhishek on January 15 2020.
 */

public class UserInfoActivity extends BaseActivity {

    private Context mContext;
    public Toolbar toolbar;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    private SuperStateView superStateView;
    private BottomDialog bottomDialog;
    private AppCompatImageView ivTakePicture;

    private String userName="";
    private String password="";

    private ProfileCircularImage ivProfile;
    private TextView tvStudentName,tvEmailId,tvStudentId;

    private ProfileInformationFragment profileInformationFragment = null;
    private PastHistoryFragment pastHistoryFragment = null;

    private ServiceCalls serverCalls;
    private JSONObject responseObject;

    ArrayList<Integer> list = new ArrayList<>();
    int PERSONAL_INFORMATION_VIEW = 0;

    private static final int CAPTURE_IMAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private Uri picUri;
    private String encodedImageData = "",imageName = "";

    public static final int PERMISSION_CODE = 100;
    String permission[] = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private final String TAG = UserInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_info);
        ProjectUtils.showLog(TAG,"onCreate");

        mContext = UserInfoActivity.this;
        serverCalls = new ServiceCalls();

        permissionSetup();

        Initialize();

        setValues();

        populateContents();
    }

    private void setValues() {

        tvStudentName.setText(sharedPrefrenceManager.getUserFirstNameFromKey() + " " + sharedPrefrenceManager.getUserLastNameFromKey());
        tvStudentId.setText("Std Id : "+sharedPrefrenceManager.getUserCodeFromKey());

        String emailid = ProjectUtils.getCorrectedString(sharedPrefrenceManager.getUserEmailFromKey());
        if(!emailid.equalsIgnoreCase("")){
            tvEmailId.setVisibility(View.VISIBLE);
            tvEmailId.setText(sharedPrefrenceManager.getUserEmailFromKey());
        }else{
            tvEmailId.setVisibility(View.GONE);
        }

        String stringForImage = sharedPrefrenceManager.getUserImageFromKey();

        if (stringForImage.length() > 0) {

            byte[] decodedString = Base64.decode(stringForImage, Base64.DEFAULT);

            Glide.with(mContext)
                    .asBitmap()
                    .load(decodedString)
                    .placeholder(R.drawable.user_icon1)
                    .override(600, 200)
                    .fitCenter()
                    .into(ivProfile);
            ivProfile.setBorderWidth(0);
        }
    }

    private void Initialize() {

        toolbar = findViewById(R.id.group_toolbar);

        toolbar.setTitle(translationManager.MYPROFILE_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorPrimary, toolbar, this); }

        ivProfile = findViewById(R.id.ivProfile);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvEmailId = findViewById(R.id.tvEmailId);
        tvStudentId = findViewById(R.id.tvStudentId);
        superStateView = findViewById(R.id.superStateView);
        ivTakePicture = findViewById(R.id.ivTakePicture);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setPagingEnabled(false);

        profileInformationFragment = new ProfileInformationFragment();
        pastHistoryFragment = new PastHistoryFragment();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        ivTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ProjectUtils.hasPermissionInManifest(UserInfoActivity.this,PERMISSION_CODE,permission))
                {
                    bottomSheetMenu();
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager)
    {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(Flags.DISEABLE_PERMISSION_FOR_TAIPEI){

            viewPagerAdapter.addFragment(profileInformationFragment,translationManager.PROFILE_INFORMATION_KEY);
            superStateView.setVisibility(View.GONE);

        }else{

            if(PERSONAL_INFORMATION_VIEW == 1){
                viewPagerAdapter.addFragment(profileInformationFragment,translationManager.PROFILE_INFORMATION_KEY);
                superStateView.setVisibility(View.GONE);
            }else{
                superStateView.setVisibility(View.VISIBLE);
            }
        }

        //viewPagerAdapter.addFragment(pastHistoryFragment,translationManager.PAST_HISTORY_KEY);
        viewPager.setAdapter(viewPagerAdapter);
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

            case R.id.refresh:{
                populateContents();
                getNotifications();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result) {
        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);
        String returnResult = result.get(KEYS.RETURN_RESULT);

        switch (callFor) {

            case KEYS.SWITCH_USER_LOGIN:

                if (returnResult == KEYS.TRUE) {
                    hideProgressDialog();
                    setValues();
                } else{

                    userName = sharedPrefrenceManager.getUserNameFromKey();
                    String password1 = sharedPrefrenceManager.getPasswordFromKey();
                    password = RSAEncryption.encryptData(password1);

                    if (((null != userName) && (!userName.equals(""))) && ((null != password) && (!password.equals("")))) {
                        new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_USER_LOGIN_INCREPTION).
                                execute(userName, password, KEYS.GRANT_TYPE_VALUE, KEYS.CLIENT_ID_VALUE, KEYS.CLIENT_SECRET_VALUE);
                    }
                }
                break;

            case KEYS.SWITCH_USER_LOGIN_INCREPTION:
                hideProgressDialog();
                if (returnResult == KEYS.TRUE) {
                    setValues();
                } else{
                    showAlertDialog(this, getString(R.string.info), getString(R.string.unexpected_error));
                }
                break;

            case KEYS.SWITCH_SWITCH_STUDENT:
                hideProgressDialog();
                if (returnResult == KEYS.TRUE) {
                    setValues();
                    getNotifications();
                } else{
                    showAlertDialog(this, getString(R.string.info), getString(R.string.unexpected_error));
                }
                break;

            case KEYS.SWITCH_REMOVE_PROFILE:
                hideProgressDialog();

                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    String success = responseObject.optString("response");

                    if(success.equalsIgnoreCase("Success")){
                        ProjectUtils.showLong(mContext,"Profile photo removed successfully!");
                        populateContents();
                    }else{
                        ProjectUtils.showLong(mContext,"There is no profile photo to remove!");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();

                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                    ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                }
                break;

            case KEYS.SWITCH_STUDENT_PROFILE_PICTURE:

                hideProgressDialog();

                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("value")) {
                        sharedPrefrenceManager.setUserImageInSP(responseObject.getString("value"));
                    }
                    ProjectUtils.showLong(mContext,"Profile photo uploaded successfully!");
                }catch (Exception ex){
                    ex.printStackTrace();

                    ProjectUtils.showLog(TAG,""+ex.getMessage());
                    ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
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

    public void populateContents() {
        if (sharedPrefrenceManager.getIsParentStatusFromKey()) {
            if (ConnectionDetector.isConnectingToInternet(mContext)) {

                showProgressDialog(UserInfoActivity.this);
                new OptimizedServerCallAsyncTask(UserInfoActivity.this, UserInfoActivity.this,
                        KEYS.SWITCH_SWITCH_STUDENT).execute();
            }
            else{
                showAlertDialog(this, "Network Error", "Please Check Your Network Connection");
            }

        } else {
            userName = sharedPrefrenceManager.getUserNameFromKey();
            password = sharedPrefrenceManager.getPasswordFromKey();

            if (((null != userName) && (!userName.equals(""))) && ((null != password) && (!password.equals("")))) {
                if (ConnectionDetector.isConnectingToInternet(mContext)) {
                    showProgressDialog(this);
                    new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_USER_LOGIN).
                            execute(userName, password, KEYS.GRANT_TYPE_VALUE, KEYS.CLIENT_ID_VALUE, KEYS.CLIENT_SECRET_VALUE);
                }else{
                    showAlertDialog(this, "Network Error", "Please Check Your Network Connection");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void permissionSetup() {

        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        PERSONAL_INFORMATION_VIEW = 0;

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i)) {
                case Permissions.PARENT_PERMISSION_PROFILE_PERSONAL_INFORMATION_VIEW:
                case Permissions.STUDENT_PERMISSION_PROFILE_PERSONAL_INFORMATION_VIEW:
                    PERSONAL_INFORMATION_VIEW = 1;
                    break;
            }
        }
    }

    private void bottomSheetMenu()
    {
        bottomDialog = new BottomDialog.Builder(this)
                .setTitle("Profile Photo")
                .setTextColor(mContext.getResources().getColor(R.color.colorBlack))
                .setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite))
                .onGallery(new BottomDialog.ButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull BottomDialog dialog)
                    {
                        try
                        {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE_REQUEST);

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(mContext, getString(R.string.please_give_permission), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }

                    }
                })
                .onCamera(new BottomDialog.ButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull BottomDialog dialog)
                    {
                        try {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            //Here get folder and image file name
                            File file = getOutputMediaFile();

                            if (!file.exists())
                            {
                                try
                                {
                                    file.createNewFile();
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            //Here check android version camera
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            {
                                picUri = FileProvider.getUriForFile(UserInfoActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);
                            } else
                            {
                                picUri = Uri.fromFile(file);
                            }

                            sharedPrefrenceManager.setStringValue(Consts.IMAGE_URI_CAMERA, picUri.toString());
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                            startActivityForResult(intent, CAPTURE_IMAGE);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                })
                .onRemove(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog dialog)
                    {
                        if (ConnectionDetector.isConnectingToInternet(mContext))
                        {
                            showProgressDialog(mContext);
                            new OptimizedServerCallAsyncTask(mContext, UserInfoActivity.this, KEYS.SWITCH_REMOVE_PROFILE).execute();
                        }
                        else
                        {
                            ProjectUtils.showLong(mContext, KEYS.NET_ERROR_MESSAGE);
                        }
                    }
                })
                .build();
        bottomDialog.show();
    }

    private File getOutputMediaFile()
    {
        //Here create a folder where bitmap image saved
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + Consts.ACADEMIA +"/temp");

        // Create the storage directory if it does not exist
        if (!dir.exists())
        {
            dir.mkdirs();
        }

        File mediaFile = new File(dir.getPath(), System.currentTimeMillis() + ".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        imageName = "file:" + mediaFile.getAbsolutePath();

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case PICK_IMAGE_REQUEST:
            {
                if(resultCode == RESULT_OK && data != null && data.getData() != null)
                {
                    picUri = data.getData();

                    String path = FilePath.getPath(mContext, picUri);
                    String fileName = path.substring(path.lastIndexOf('/') + 1);

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);

                        encodedImageData = BitMapToString(bitmap);

                        byte[] decodedString = Base64.decode(encodedImageData, Base64.DEFAULT);

                        Glide.with(mContext)
                                .asBitmap()
                                .load(decodedString)
                                .placeholder(R.drawable.user_icon1)
                                .override(600, 200)
                                .fitCenter()
                                .into(ivProfile);
                        ivProfile.setBorderWidth(0);

                        //Here API Calling
                        uploadProfilePicture(fileName,encodedImageData);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ProjectUtils.showLong(mContext,getString(R.string.cant_get_image));
                }
            }
            break;

            case CAPTURE_IMAGE:
            {
                if(resultCode == RESULT_OK)
                {
                    if (data != null)
                    {
                        picUri = data.getData();
                    }
                    else if (picUri == null)
                    {
                        picUri = Uri.parse(sharedPrefrenceManager.getStringValue(Consts.IMAGE_URI_CAMERA));
                    }
                    else
                    {
                        picUri = Uri.parse(imageName);
                    }

                    String path = FilePath.getPath(mContext, picUri);
                    String fileName = path.substring(path.lastIndexOf('/') + 1);

                    try {
                        Bitmap bitmap = ImageRotation.handleSamplingAndRotationBitmap(mContext,picUri);

                        encodedImageData = BitMapToString(bitmap);

                        byte[] decodedString = Base64.decode(encodedImageData, Base64.DEFAULT);

                        Glide.with(mContext)
                                .asBitmap()
                                .load(decodedString)
                                .placeholder(R.drawable.user_icon1)
                                .override(600, 200)
                                .fitCenter()
                                .into(ivProfile);
                        ivProfile.setBorderWidth(0);

                        //Here API Calling
                        uploadProfilePicture(fileName,encodedImageData);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }else
                {
                    ProjectUtils.showLong(mContext,getString(R.string.cant_take_image));
                }
            }
            break;
        }
    }

    private void uploadProfilePicture(String fileName, String encodedImageData) {

        showProgressDialog(mContext);

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String accessToken = sharedPrefrenceManager.getAccessTokenFromKey();
        String tokenType = sharedPrefrenceManager.getTokenTypeFromKey();

        String str = tokenType + " " + accessToken;

        String personId = String.valueOf(sharedPrefrenceManager.getPersonIDFromKey());

        //Here do api calling for login
        String url = BaseURL.BASE_URL + KEYS.UPLOAD_PROFILE_METHOD+"?access_token="+accessToken;

        AndroidNetworking.upload(url)
                .setOkHttpClient(ProjectUtils.getUnsafeOkHttpClient())
                .addHeaders("Authorization",str)
                .addHeaders("Content-Type","application/json")
                .addMultipartParameter("personId",personId)
                .addMultipartParameter("fileName",fileName)
                .addMultipartParameter("file", "data:image/png;base64,"+encodedImageData)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        // do anything with response
                        ProjectUtils.showLog("Upload Temp Response: ", response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            boolean success = jsonObject.optBoolean("success");

                            if(success){
                                String path = jsonObject.optString("path");
                                updateProfilePicture(path);
                            }else{
                                hideProgressDialog();
                                ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                            }

                        }catch (Exception ex){
                            ex.printStackTrace();
                            hideProgressDialog();
                            ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                        }
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        hideProgressDialog();
                        ProjectUtils.showLog(TAG,""+error.getMessage());
                        ProjectUtils.showLong(mContext,getString(R.string.something_went_wrong));
                    }
                });
    }

    private void updateProfilePicture(String path) {

        if (ConnectionDetector.isConnectingToInternet(mContext))
        {
            new OptimizedServerCallAsyncTask(mContext, UserInfoActivity.this, KEYS.SWITCH_STUDENT_PROFILE_PICTURE).execute(path);
        }
        else
        {
            ProjectUtils.showLong(mContext, KEYS.NET_ERROR_MESSAGE);
        }
    }

    public String BitMapToString(Bitmap userImage1) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 80, baos);
        byte[] b = baos.toByteArray();
        String imgBitmap = Base64.encodeToString(b, Base64.DEFAULT);
        return imgBitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasAllPermissions = false;

        switch (requestCode)
        {
            case PERMISSION_CODE:

                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED))
                {
                    hasAllPermissions = true;
                }
                else
                {
                    hasAllPermissions = false;

                    ProjectUtils.showDialog(mContext, getString(R.string.app_name), getString(R.string.allow_all_permissions), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            if (ProjectUtils.hasPermissionInManifest(UserInfoActivity.this,PERMISSION_CODE,permission))
                            {
                                bottomSheetMenu();
                            }
                        }
                    }, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialogInterface.dismiss();
                        }
                    }, false);
                }

                if(hasAllPermissions)bottomSheetMenu();
                break;
        }
    }
}
