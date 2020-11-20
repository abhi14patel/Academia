package com.serosoft.academiassu.Modules.Dashboard;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Utils.Flags;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.serosoft.academiassu.Utils.RSAEncryption;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Abhishek on November 11 2019.
 */

public class ResetPassword extends BaseActivity implements AsyncTaskCompleteListener {

    Context mContext;
    private RelativeLayout relativeLayout;
    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmNewPassword;
    private Button btnReset;
    private String password;
    private String oldPassword = "",newPassword = "";
    public Toolbar toolbar;
    private TextView tvMessage;

    String message = "";

    int minLength = 0, maxLength = 0, minSpecialCharacter = 0, minDigits = 0, minCapitalLetter = 0;
    String allowableSpecialCharacters = "";
    boolean whetherReusePassword = false,whetherReuseUsername = false;

    private boolean APPLY_VALIDATION = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        toolbar = findViewById(R.id.group_toolbar);
        mContext = ResetPassword.this;

        Initialize();

        sharedPrefrenceManager = new SharedPrefrenceManager(this);
        password = sharedPrefrenceManager.getPasswordFromKey();

        populateContents();

        etOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ProjectUtils.hideSpace(editable, etOldPassword);
            }
        });

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ProjectUtils.hideSpace(editable, etNewPassword);
            }
        });

        etConfirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ProjectUtils.hideSpace(editable, etConfirmNewPassword);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectUtils.hideKeyboard(ResetPassword.this);
                String oldPassword = etOldPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmNewPassword.getText().toString().trim();

                if (!validateOldPassword()) {

                    return;
                }
                else if (!validateNewPassword()) {

                    return;
                }
                else if (!validateConfirmPassword()) {

                    return;
                }
                else if(!confirmPassword.equals(newPassword)) {

                    Toast.makeText(mContext,getString(R.string.confirm_password_does_not_match),Toast.LENGTH_LONG).show();
                    etConfirmNewPassword.requestFocus();
                }
                else if(!oldPassword.equalsIgnoreCase(password)){

                    Toast.makeText(mContext,getString(R.string.current_old_password_not_match),Toast.LENGTH_LONG).show();
                    etOldPassword.requestFocus();
                }
                else {

                    String errorMsg = tvMessage.getText().toString();

                    if(APPLY_VALIDATION) {

                        int validation = checkValidation(newPassword);

                        if(validation == 1) {

                            showAlertDialog(ResetPassword.this, getString(R.string.oops), errorMsg);
                            etNewPassword.requestFocus();
                        }
                        else if(validation == 2) {

                            showAlertDialog(ResetPassword.this, getString(R.string.oops), errorMsg);
                            etNewPassword.requestFocus();
                        }
                        else if(validation == 3) {

                            showAlertDialog(ResetPassword.this, getString(R.string.oops), errorMsg);
                            etNewPassword.requestFocus();
                        }
                        else if(validation == 4) {

                            showAlertDialog(ResetPassword.this, getString(R.string.oops), errorMsg);
                            etNewPassword.requestFocus();
                        }
                        else if(validation == 5) {

                            showAlertDialog(ResetPassword.this, getString(R.string.oops), errorMsg);
                            etNewPassword.requestFocus();
                        }
                        else if(validation == 6) {

                            showAlertDialog(ResetPassword.this, getString(R.string.oops), errorMsg);
                            etNewPassword.requestFocus();
                        }
                        else if(!whetherReusePassword && (oldPassword.equalsIgnoreCase(newPassword))) {

                            showAlertDialog(ResetPassword.this, getString(R.string.oops), getString(R.string.your_password_has_already_been_used));
                        }
                        else {

                            etNewPassword.clearFocus();

                            if (ConnectionDetector.isConnectingToInternet(ResetPassword.this))
                            {
                                serviceCallToReset(oldPassword, confirmPassword);
                            } else {
                                showAlertDialog(ResetPassword.this, KEYS.NET_ERROR_HEAD, KEYS.NET_ERROR_MESSAGE);
                            }
                        }
                    }else{

                        if(!whetherReusePassword && (oldPassword.equalsIgnoreCase(newPassword))) {

                            showAlertDialog(ResetPassword.this, getString(R.string.oops), errorMsg);
                        }else{
                            if (ConnectionDetector.isConnectingToInternet(ResetPassword.this))
                            {
                                serviceCallToReset(oldPassword, confirmPassword);
                            } else {
                                showAlertDialog(ResetPassword.this, KEYS.NET_ERROR_HEAD, KEYS.NET_ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
    }

    private int checkValidation(String newPassword) {

        //Here for length check
        int length = newPassword.length();

        //Here for special char count
        int splCharCount = getSpecialCharCount(newPassword);

        //Here for numeric char count
        int numCount = getNumericDigitCount(newPassword);

        //Here for capital char count
        int capsCount = getCapitalCharCount(newPassword);

        if(minLength > length) {

            return 1;
        }
        else if(maxLength > 0 && maxLength < length) {

            return 2;
        }
        else if(minSpecialCharacter > splCharCount || (minLength > length && maxLength < length)) {

            return 3;
        }
        else if(minDigits > numCount || (minLength > length && maxLength < length)) {

            return 4;
        }
        else if(minCapitalLetter > capsCount || (minLength > length && maxLength < length)) {

            return 5;
        }
        else if(!newPassword.contains(allowableSpecialCharacters)) {

            return 6;
        }

        return 0;
    }

    private int getSpecialCharCount(String newPassword) {

        int count = 0;

        char[] specialChar = newPassword.toCharArray();

        for(char output : specialChar){

            boolean isSpecialChar = ProjectUtils.isSpecialCharacter(output);

            if(isSpecialChar) {

                count++;
            }
        }

        return count;
    }

    private int getNumericDigitCount(String newPassword) {

        int count = 0;

        char[] specialChar = newPassword.toCharArray();

        for(char output : specialChar){

            boolean isNumericDigit = ProjectUtils.isNumericDigit(output);

            if(isNumericDigit) {

                count++;
            }
        }

        return count;
    }

    private int getCapitalCharCount(String newPassword) {

        int count = 0;

        char[] specialChar = newPassword.toCharArray();

        for(char output : specialChar){

            boolean isSpecialChar = ProjectUtils.isCapitalChar(output);

            if(isSpecialChar) {

                count++;
            }
        }

        return count;
    }

    private void serviceCallToReset(String oldPass, String newPass) {

        ProjectUtils.preventTwoClick(btnReset);

        oldPassword = oldPass;
        newPassword = newPass;

        showProgressDialog(this);
        new OptimizedServerCallAsyncTask(mContext, ResetPassword.this, KEYS.SWITCH_RESET_PASSWORD)
                .execute(oldPassword, newPassword);
    }

    private void populateContents() {

        if (ConnectionDetector.isConnectingToInternet(this)) {

            showProgressDialog(this);
            new OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_PASSWORD_POLICY).execute();
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void Initialize() {

        tvMessage = findViewById(R.id.tvMessage);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnReset = findViewById(R.id.btnReset);
        relativeLayout = findViewById(R.id.relativeLayout);

        toolbar.setTitle(translationManager.RESET_PASSWORD_KEY.toUpperCase());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Flags.COLOR_FLAG) { this.setScreenThemeColor(R.color.colorPrimary, toolbar, this); }

        etOldPassword.setHint(translationManager.OLD_PASSWORD_KEY);
        etNewPassword.setHint(translationManager.NEW_PASSWORD_KEY);
        etConfirmNewPassword.setHint(translationManager.CONFIRM_PASSWORD_KEY);
        btnReset.setText(translationManager.RESET_PASSWORD_KEY);
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
        if (callFor == null) {return;}

        switch (callFor) {
            case KEYS.SWITCH_RESET_PASSWORD:
                try {
                    hideProgressDialog();

                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("whatever")) {
                        String str = responseObject.optString("whatever");
                        if (str.contains("true")) {
                            Toast.makeText(mContext, "Password Reset Successful", Toast.LENGTH_SHORT).show();
                            password = etConfirmNewPassword.getText().toString();
                            sharedPrefrenceManager.setPasswordInSP(password);
                            finish();
                        } else {
                            showAlertDialog(this, getString(R.string.oops), getString(R.string.unexpected_error));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    oldPassword = RSAEncryption.encryptData(etOldPassword.getText().toString().trim());
                    newPassword = RSAEncryption.encryptData(etNewPassword.getText().toString().trim());

                    new OptimizedServerCallAsyncTask(mContext, ResetPassword.this, KEYS.SWITCH_RESET_PASSWORD_ENCRYPT)
                            .execute(oldPassword, newPassword);
                }
                break;

            case KEYS.SWITCH_RESET_PASSWORD_ENCRYPT:
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    if (responseObject.has("whatever")) {
                        String str = responseObject.optString("whatever");
                        if (str.contains("true")) {
                            Toast.makeText(mContext, "Password Reset Successful", Toast.LENGTH_SHORT).show();
                            password = etConfirmNewPassword.getText().toString();
                            sharedPrefrenceManager.setPasswordInSP(password);
                            finish();
                        } else {
                            showAlertDialog(this, getString(R.string.oops), getString(R.string.unexpected_error));
                        }
                    } else {
                        showAlertDialog(this, getString(R.string.oops), getString(R.string.unexpected_error));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProjectUtils.showLog("ResetPassword",e.getMessage());
                }
                break;

            case KEYS.SWITCH_NOTIFICATIONS:
                populateNotificationsList(responseResult);
                break;

            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
                showNotificationCount(responseResult);
                break;

            case KEYS.SWITCH_PASSWORD_POLICY:
                hideProgressDialog();

                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    int id = responseObject.optInt("id");
                    int version = responseObject.optInt("version");
                    minLength = responseObject.optInt("minLength");
                    maxLength = responseObject.optInt("maxLength");
                    minSpecialCharacter = responseObject.optInt("minSpecialCharacter");
                    minDigits = responseObject.optInt("minDigits");
                    int maxPasswordAge = responseObject.optInt("maxPasswordAge");
                    int unsuccessfulAttempts = responseObject.optInt("unsuccessfulAttempts");
                    minCapitalLetter = responseObject.optInt("minCapitalLetter");
                    whetherReusePassword = responseObject.optBoolean("whetherReusePassword");
                    whetherReuseUsername = responseObject.optBoolean("whetherReuseUsername");
                    boolean whetherPasswordExpires = responseObject.optBoolean("whetherPasswordExpires");
                    boolean whetherTemporaryLock = responseObject.optBoolean("whetherTemporaryLock");
                    boolean whetherUnlockOnPasswordReset = responseObject.optBoolean("whetherUnlockOnPasswordReset");
                    allowableSpecialCharacters = ProjectUtils.getCorrectedString(responseObject.optString("allowableSpecialCharacters"));

                    if(minLength>0) {

                        message = "Your password must be at least "+minLength+" characters long.";
                    }
                    if(maxLength>0){

                        message = "Your password must be at least "+maxLength+" characters long.";
                    }
                    if(minLength>0 && maxLength>0){

                        message = "Your password must be ("+minLength+" - "+maxLength+") characters long.";
                    }
                    if(minSpecialCharacter>0 && minDigits==0 && minCapitalLetter==0){

                        if(!allowableSpecialCharacters.equalsIgnoreCase("")){

                            message = message+"It must contain at least "+minSpecialCharacter+" special character(s) (only "+allowableSpecialCharacters+" allowed).";

                        }else{

                            message = message+"It must contain at least "+minSpecialCharacter+" special character(s).";
                        }
                    }
                    if(minDigits>0 && minSpecialCharacter==0 && minCapitalLetter==0){

                        message = message+"It must contain at least "+minDigits+" number(s).";
                    }
                    if(minCapitalLetter>0 && minSpecialCharacter==0 && minDigits==0){

                        message = message+"It must contain at least "+minCapitalLetter+" letter(s).";
                    }
                    if(minSpecialCharacter>0 && minDigits>0 && minCapitalLetter==0){

                        if(!allowableSpecialCharacters.equalsIgnoreCase("")){

                            message = message+"It must contain at least "+minSpecialCharacter+" special character(s) (only "+allowableSpecialCharacters+" allowed) & "+minDigits+" number(s).";

                        }else{

                            message = message+"It must contain at least "+minSpecialCharacter+" special character(s) & "+minDigits+" number(s).";
                        }
                    }
                    if(minSpecialCharacter>0 && minCapitalLetter>0 && minDigits==0){

                        if(!allowableSpecialCharacters.equalsIgnoreCase("")){

                            message = message+"It must contain at least "+minSpecialCharacter+" special character(s) (only "+allowableSpecialCharacters+" allowed) & "+minCapitalLetter+" capital letter(s).";

                        }else{

                            message = message+"It must contain at least "+minSpecialCharacter+" special character(s) & "+minCapitalLetter+" capital letter(s).";
                        }
                    }
                    if(minDigits>0 && minCapitalLetter>0 && minSpecialCharacter==0){

                        message = message+"It must contain at least "+minDigits+" number & "+minCapitalLetter+" capital letter(s).";
                    }
                    if(minSpecialCharacter>0 && minDigits>0 && minCapitalLetter>0){

                        if(!allowableSpecialCharacters.equalsIgnoreCase("")){

                            message = message+"It must contain at least "+minSpecialCharacter+" special character(s) (only "+allowableSpecialCharacters+" allowed), "+minDigits+" number(s) & "+minCapitalLetter+" capital letter(s).";
                        }else{

                            message = message+"It must contain at least "+minSpecialCharacter+" special character(s), "+minDigits+" number(s) & "+minCapitalLetter+" capital letter(s).";
                        }
                    }

                    if(minSpecialCharacter==0 && !allowableSpecialCharacters.equalsIgnoreCase("")){

                        message = message+" You may include ("+allowableSpecialCharacters+").";
                    }

                    if((minLength>0) || (maxLength>0) || (minSpecialCharacter>0) || (minDigits>0) || (minSpecialCharacter>0) || !allowableSpecialCharacters.equalsIgnoreCase("")|| (whetherReusePassword==true)|| (whetherReuseUsername==true) ){
                        APPLY_VALIDATION = true;

                        if(minLength>0) {

                            tvMessage.setText(message);
                        }

                        if(maxLength>0) {

                            tvMessage.setText(message);
                        }

                        if(minLength>0 && maxLength>0){

                            tvMessage.setText(message);
                        }

                        if(minSpecialCharacter>0 && minDigits==0 && minCapitalLetter==0){

                            tvMessage.setText(message);
                        }

                        if(minDigits>0 && minSpecialCharacter==0 && minCapitalLetter==0){

                            tvMessage.setText(message);
                        }

                        if(minCapitalLetter>0 && minSpecialCharacter==0 && minDigits==0){

                            tvMessage.setText(message);
                        }

                        if(minSpecialCharacter>0 && minDigits>0 && minCapitalLetter==0){

                            tvMessage.setText(message);
                        }

                        if(minSpecialCharacter>0 && minCapitalLetter>0 && minDigits==0){

                            tvMessage.setText(message);
                        }

                        if(minDigits>0 && minCapitalLetter>0 && minSpecialCharacter==0){

                            tvMessage.setText(message);
                        }

                        if(minSpecialCharacter>0 && minDigits>0 && minCapitalLetter>0){

                            tvMessage.setText(message);
                        }

                        if(minSpecialCharacter==0 && !allowableSpecialCharacters.equalsIgnoreCase("")){

                            tvMessage.setText(message);
                        }

                        if(!whetherReusePassword && !whetherReuseUsername) {

                            tvMessage.setText(message+" "+getString(R.string.your_password_must_different_from_old_username_password));
                        }else if(!whetherReuseUsername){

                            tvMessage.setText(message+" "+getString(R.string.your_password_must_different_from_username));
                        }else if(!whetherReusePassword){

                            tvMessage.setText(message+" "+getString(R.string.your_password_must_different_from_password));
                        }
                    }
                    else
                    {
                        APPLY_VALIDATION = false;
                        tvMessage.setText(getString(R.string.your_password_must_different_from_old_username_password));
                    }
                }catch (Exception ex) {

                    ex.printStackTrace();

                    APPLY_VALIDATION = false;

                    showAlertDialog(this, getString(R.string.oops), getString(R.string.unexpected_error));
                }

                break;
        }
    }

    //Validation for old password
    public boolean validateOldPassword() {

        if (!ProjectUtils.isEditTextFilled(etOldPassword)) {

            SpannableString s = new SpannableString("Please enter old password");
            s.setSpan(null, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            etOldPassword.setError(s);
            etOldPassword.requestFocus();
            return false;
        }
        else {
            etOldPassword.clearFocus();
            return true;
        }
    }

    //Validation for new password
    public boolean validateNewPassword() {

        if (!ProjectUtils.isEditTextFilled(etNewPassword)) {

            SpannableString s = new SpannableString("Please enter new password");
            s.setSpan(null, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            etNewPassword.setError(s);
            etNewPassword.requestFocus();
            return false;
        }
        else {
            etNewPassword.clearFocus();
            return true;
        }
    }

    //Validation for confirm password
    public boolean validateConfirmPassword() {

        if (!ProjectUtils.isEditTextFilled(etConfirmNewPassword)) {

            SpannableString s = new SpannableString("Please enter confirm password");
            s.setSpan(null, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            etConfirmNewPassword.setError(s);
            etConfirmNewPassword.requestFocus();
            return false;
        }
        else {
            etConfirmNewPassword.clearFocus();
            return true;
        }
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

            case R.id.refresh:{
                populateContents();
                getNotifications();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

}
