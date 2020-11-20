package com.serosoft.academiassu.Modules.Login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.serosoft.academiassu.FacultyMainActivity;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Dashboard.UpdateActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.Utils.RSAEncryption;
import com.serosoft.academiassu.Utils.Version;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LoginActivity extends BaseActivity implements View.OnClickListener, AsyncTaskCompleteListener, GoogleApiClient.OnConnectionFailedListener
{
    EditText userNameEditText, passwordEditText;
    private Button appLoginButton;
    private Button forgotPasswordButton;
    private String userName="";
    private String password="";
    private SharedPrefrenceManager sharedPrefrenceManager;
    private String userNameAF="";
    private String passwordAF="";
    private Context mContext;
    String appVersion = "";

    private CardView btnGoogle;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_SIGN_IN = 1;

    private TextView tvInstanceVersion,tvAppVersion,tvInstanceURL;

    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;

        Initialize();

        appLoginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);

        fetchFirebaseDeviceToken();

        populateContent();

        googleSigninConfiguration();
    }

    private void populateContent() {

        if (ConnectionDetector.isConnectingToInternet(this))
        {
            if(Flags.CHECK_VERSION){
                new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_LATEST_VERSION_NEW2).execute();
            }

            populateVersions();

            new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_GOOGLE_SIGNIN_SETUP).execute();
        }
        else
        {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }


    private void googleSigninConfiguration() {

        //Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(KEYS.GOOGLE_AUTH_TOKEN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void populateVersions() {

        showProgressDialog(this);

        //Here do api calling for login
        String url = BaseURL.BASE_URL + KEYS.BUILD_INFO_METHOD;

        AndroidNetworking.get(url)
                .setOkHttpClient(ProjectUtils.getUnsafeOkHttpClient())
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        // do anything with response
                        //ProjectUtils.showLog(TAG, "Response"+response);
                        hideProgressDialog();

                        //Here parse Instance version

                        /************** Read XML *************/
                        BufferedReader br = new BufferedReader(new StringReader(response));
                        InputSource is = new InputSource(br);

                        /************  Parse XML **************/

                        try {
                            DocumentBuilderFactory D=DocumentBuilderFactory.newInstance();
                            DocumentBuilder d=D.newDocumentBuilder();
                            Document mydocument =d.parse(is);

                            // to get the elements from document 'mydocument'
                            Element E=mydocument.getDocumentElement();
                            E.normalize();
                            NodeList MyListOfElemnts = mydocument.getElementsByTagName("buildInfo");

                            for (int i=0;i<MyListOfElemnts.getLength();i++)
                            {
                                Node N = MyListOfElemnts.item(i);
                                Element myElement = (Element) N;

                                NodeList N1 = myElement.getElementsByTagName("buildName").item(i).getChildNodes();
                                Node node1 = N1.item(i);
                                String name = node1.getNodeValue();

                                NodeList N2 = myElement.getElementsByTagName("buildVersion").item(i).getChildNodes();
                                Node node2 = N2.item(i);
                                String version = node2.getNodeValue();

                                tvInstanceVersion.setText(name+" "+version+" | ");
                            }

                        }catch (Exception ex){
                            ex.printStackTrace();
                            ProjectUtils.showLog(TAG,""+ex.getMessage());
                        }

                        appVersion();
                    }

                    @Override
                    public void onError(ANError anError) {

                        hideProgressDialog();
                    }
                });
    }

    private void appVersion() {
        //Here get app version
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = pInfo.versionName;
            ProjectUtils.showLog(TAG,appVersion);

            tvAppVersion.setText("V"+appVersion+" | ");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //Here get instanceUrl
        String url = BaseURL.BASE_URL;
        if(url.contains("://")){

            String[] separated = url.split("://");
            String str1 =  separated[0];
            String str2 = separated[1];

            if(str2.contains(".academiaerp.com")){

                String[] separated1 = str2.split(".academiaerp.com");
                String st1 = separated1[0];
                String st2 = separated1[1];

                tvInstanceURL.setText(st1);
            }else {
                String sttr2 = ProjectUtils.removeLastChar(str2);
                tvInstanceURL.setText(sttr2);
            }
        }
    }

    public void fetchFirebaseDeviceToken()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task)
                    {
                        if (!task.isSuccessful())
                        {
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.e("FCM Token: ", token);
                        sharedPrefrenceManager.setFirebaseTokenInSP(token);
                    }
                });
    }

    private void Initialize()
    {
        userNameEditText = findViewById(R.id.userEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        appLoginButton = findViewById(R.id.userLoginButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);

        btnGoogle = findViewById(R.id.btnGoogle);
        tvInstanceVersion = findViewById(R.id.tvInstanceVersion);
        tvAppVersion = findViewById(R.id.tvAppVersion);
        tvInstanceURL = findViewById(R.id.tvInstanceURL);

        sharedPrefrenceManager=new SharedPrefrenceManager(LoginActivity.this);
        userNameAF = sharedPrefrenceManager.getUserNameFromKey();
        passwordAF = sharedPrefrenceManager.getPasswordFromKey();
        if((null!=userNameAF)&&(!userNameAF.equals("")))
            userNameEditText.setText(userNameAF);
        if((null!=passwordAF)&&(!passwordAF.equals("")))
            passwordEditText.setText(passwordAF);
        if (sharedPrefrenceManager.getUserLoginStatusFromKey())
        {
            goToDashboard();
        }
    }

    @Override
    public void onClick(View v)
    {
        try {
            switch (v.getId())
            {
                case R.id.userLoginButton:
                {
                    if (userNameEditText.getText().toString().length() > 0 && passwordEditText.getText().toString().length() > 0)
                    {
                        if (ConnectionDetector.isConnectingToInternet(LoginActivity.this))
                        {
                            ProjectUtils.preventTwoClick(appLoginButton);
                            ProjectUtils.hideKeyboard(LoginActivity.this);

                            if(Flags.CHECK_VERSION){
                                new OptimizedServerCallAsyncTask(this, this, KEYS.SWITCH_LATEST_VERSION_NEW).execute();
                            }else{
                                showProgressDialog(this);

                                userName = userNameEditText.getText().toString();
                                password = passwordEditText.getText().toString();

                                new OptimizedServerCallAsyncTask(LoginActivity.this, LoginActivity.this, KEYS.SWITCH_USER_LOGIN).
                                        execute(userName, password, KEYS.GRANT_TYPE_VALUE, KEYS.CLIENT_ID_VALUE, KEYS.CLIENT_SECRET_VALUE);
                            }
                        }
                        else
                        {
                            showAlertDialog2(LoginActivity.this, getString(R.string.network_error), getString(R.string.please_check_your_network_connection));
                        }
                    } else if(userNameEditText.getText().toString().length() <=0){
                        userNameEditText.setError("Please Enter User Name");
                    } else if(passwordEditText.getText().toString().length() <=0){
                        passwordEditText.setError("Please Enter Password");
                    }
                }
                break;

                case R.id.forgotPasswordButton:
                {
                    showForgotPasswordDialog();
                }
                break;

                case R.id.btnGoogle:{
                    //Here Sign in with Google code
                    loginGoogleUser();
                }break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showForgotPasswordDialog() {

        Dialog alertDialog = new Dialog(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.forgot_password_dialog, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(convertView);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        AppCompatImageView ivClose = convertView.findViewById(R.id.ivClose);

        EditText etUsername = convertView.findViewById(R.id.etUsername);
        Button btnSend = convertView.findViewById(R.id.btnSend);

        titleTextView.setText(getString(R.string.forgot_password).toUpperCase());
        ivClose.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectUtils.hideKeyboard(LoginActivity.this);
                String task = etUsername.getText().toString().trim();

                if (task.length() > 0)
                {
                    alertDialog.dismiss();
                    new OptimizedServerCallAsyncTask(LoginActivity.this,
                            LoginActivity.this, KEYS.SWITCH_FORGOT_PASSWORD).execute(task);
                    LoginActivity.this.showProgressDialog(LoginActivity.this);
                } else {
                    ProjectUtils.showLong(mContext,"Please enter the username");
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        alertDialog.getWindow().setAttributes(lp);
    }

    private void goToDashboard()
    {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result)
    {
        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);
        String returnResult = result.get(KEYS.RETURN_RESULT);

        switch (callFor)
        {
            case KEYS.SWITCH_GOOGLE_SIGNIN_SETUP:
            {
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    if(responseObject.has("socialId")){
                        btnGoogle.setVisibility(View.VISIBLE);
                    }else{
                        btnGoogle.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    btnGoogle.setVisibility(View.GONE);
                }

            }break;

            case KEYS.SWITCH_LATEST_VERSION_NEW:
            {
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("latestVersion"))
                    {
                        String latestVersionString = responseObject.optString("latestVersion");
                        boolean isForceUpdate = responseObject.optBoolean("isForceUpdate");
                        PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                        String currentVersionString = pInfo.versionName;

                        Version currentVersion = new Version(currentVersionString);
                        Version latestVersion = new Version(latestVersionString);

                        int compareStatus = currentVersion.compareTo(latestVersion);
                        if (compareStatus == -1 && isForceUpdate)
                        {
                            //Show Update PopUp
                            showUpdatePopUp(responseObject);
                        }else{
                            showProgressDialog(this);

                            userName = userNameEditText.getText().toString();
                            password = passwordEditText.getText().toString();

                            new OptimizedServerCallAsyncTask(LoginActivity.this, LoginActivity.this, KEYS.SWITCH_USER_LOGIN).
                                    execute(userName, password, KEYS.GRANT_TYPE_VALUE, KEYS.CLIENT_ID_VALUE, KEYS.CLIENT_SECRET_VALUE);
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    showAlertDialog2(LoginActivity.this, getString(R.string.oops), getString(R.string.versioning_api_data_not_set));
                }
            }
            break;

            case KEYS.SWITCH_LATEST_VERSION_NEW2:
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
                    showAlertDialog2(LoginActivity.this, getString(R.string.oops), getString(R.string.versioning_api_data_not_set));
                }
            }
            break;

            case KEYS.SWITCH_USER_LOGIN:
            {
                if (returnResult == KEYS.TRUE)
                {
                    hideProgressDialog();

                    String s1 = tvInstanceVersion.getText().toString();
                    String s2 = tvAppVersion.getText().toString();
                    String s3 = tvInstanceURL.getText().toString();

                    sharedPrefrenceManager.setVersionInSP(s1+s2+s3);

                    userName = userNameEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    sharedPrefrenceManager.setUserNameInSP(userName);
                    sharedPrefrenceManager.setPasswordInSP(password);

                    if(sharedPrefrenceManager.getIsFacultyStatusFromKey()){
                        Intent intent = new Intent(mContext, FacultyMainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        goToDashboard();
                    }
                }
                else
                {
                    userName = userNameEditText.getText().toString();
                    String password1 = passwordEditText.getText().toString().trim();
                    password = RSAEncryption.encryptData(password1);

                    new OptimizedServerCallAsyncTask(LoginActivity.this, LoginActivity.this, KEYS.SWITCH_USER_LOGIN_INCREPTION).
                            execute(userName, password, KEYS.GRANT_TYPE_VALUE, KEYS.CLIENT_ID_VALUE, KEYS.CLIENT_SECRET_VALUE);
                }
            }
            break;

            case KEYS.SWITCH_USER_LOGIN_INCREPTION:
            {
                hideProgressDialog();
                if (returnResult == KEYS.TRUE)
                {
                    String s1 = tvInstanceVersion.getText().toString();
                    String s2 = tvAppVersion.getText().toString();
                    String s3 = tvInstanceURL.getText().toString();

                    sharedPrefrenceManager.setVersionInSP(s1+s2+s3);

                    userName = userNameEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    sharedPrefrenceManager.setUserNameInSP(userName);
                    sharedPrefrenceManager.setPasswordInSP(password);

                    if(sharedPrefrenceManager.getIsFacultyStatusFromKey()){
                        Intent intent = new Intent(mContext, FacultyMainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        goToDashboard();
                    }
                }
                else
                {
                    showAlertDialog2(LoginActivity.this, getString(R.string.info), getString(R.string.wrong_credentials));
                }
            }
            break;

            case KEYS.SWITCH_GOOGLE_SIGNIN:
            {
                hideProgressDialog();
                if (returnResult == KEYS.TRUE)
                {
                    if(sharedPrefrenceManager.getIsFacultyStatusFromKey()){
                        Intent intent = new Intent(mContext, FacultyMainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        goToDashboard();
                    }
                }
                else
                {
                    showAlertDialog2(LoginActivity.this, getString(R.string.info), getString(R.string.wrong_credentials));
                }

            }break;

            case KEYS.SWITCH_FORGOT_PASSWORD:
            {
                hideProgressDialog();

                try {
                    JSONObject returnObject = new JSONObject(responseResult);
                    String message = returnObject.optString("message");
                    if (message.equals("true"))
                    {
                        message = "";
                        showAlertDialog2(LoginActivity.this, "Login details sent!", "Please check your registered email address and mobile no. for login details.");
                    }
                    else if (message.equals("false"))
                    {
                        message = "";
                        showAlertDialog2(LoginActivity.this, getString(R.string.oops), getString(R.string.user_not_found));
                    }
                    else if (message.equals("error"))
                    {
                        message = "";
                        showAlertDialog2(LoginActivity.this, getString(R.string.oops), getString(R.string.unexpected_error));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        finish();
    }

    private void showUpdatePopUp(JSONObject jsonObject)
    {
        Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
        intent.putExtra("json", jsonObject.toString());
        startActivity(intent);
    }

    private void loginGoogleUser()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GOOGLE_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result)
    {
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();

            String email = account.getEmail();
            String auth = account.getServerAuthCode();

            sharedPrefrenceManager.setUserEmailInSP(email);

            //After You got your data add this to clear the priviously selected mail
            mGoogleApiClient.clearDefaultAccountAndReconnect();

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(LoginActivity.this, LoginActivity.this, KEYS.SWITCH_GOOGLE_SIGNIN).
                    execute(email, "", KEYS.GRANT_TYPE_VALUE, KEYS.CLIENT_ID_VALUE, KEYS.CLIENT_SECRET_VALUE,KEYS.PORTAL_CODE_VALUE,auth);
        }
        else
        {
            Status status = result.getStatus();
            ProjectUtils.showLong(mContext,"Login Failed");
        }
    }
}
