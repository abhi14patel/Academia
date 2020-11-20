package com.serosoft.academiassu.Utils;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.serosoft.academiassu.BuildConfig;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Manager.TranslationManager;
import com.serosoft.academiassu.Modules.Attendance.Adapters.MultipleAttendanceDialogAdapter;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity;
import com.serosoft.academiassu.Modules.Login.LoginActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.RightDrawerMenu.MenuHelper;
import com.serosoft.academiassu.SqliteDB.DbHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class BaseActivity extends AppCompatActivity implements AsyncTaskCompleteListener,View.OnClickListener /*implements View.OnTouchListener, ValueAnimator.AnimatorUpdateListener */
{
    private static Menu menu;
    public ACProgressFlower acProgressFlowerDialog;
    public SharedPrefrenceManager sharedPrefrenceManager;
    Uri fileUri;
    public TranslationManager translationManager;
    public FirebaseAnalytics firebaseAnalytics;
    public DbHelper dbHelper;
    public GoogleApiClient mGoogleApiClient;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        sharedPrefrenceManager = new SharedPrefrenceManager(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        dbHelper = new DbHelper(this);

        getNotifications();

        translationManager = new TranslationManager(this);
    }

    public void getNotifications()
    {
        if (ConnectionDetector.isConnectingToInternet(BaseActivity.this))
        {
            new OptimizedServerCallAsyncTask(BaseActivity.this, BaseActivity.this, KEYS.SWITCH_NOTIFICATIONS).execute();
            new OptimizedServerCallAsyncTask(BaseActivity.this, BaseActivity.this, KEYS.SWITCH_NOTIFICATIONS_COUNT).execute();
        }
    }

    @Override
    protected void onStart() {

        if(sharedPrefrenceManager.getUserGLoginStatusFromKey()){

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                    .requestServerAuthCode(KEYS.GOOGLE_AUTH_TOKEN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    public void showNotificationCount(String responseString)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(responseString);

            JSONArray array = jsonObject
                    .getJSONArray("whatever");
            JSONObject objectForAlerts = array.getJSONObject(0);
            JSONObject objectForNotifications = array.getJSONObject(1);

            Integer unreadAlertCount = objectForAlerts.getInt("unreadCount");
            Integer unreadNotificationsCount = objectForNotifications.getInt("unreadCount");

            sharedPrefrenceManager.setAlertCountsInSP(unreadAlertCount);
            sharedPrefrenceManager.setNotificationCountsInSP(unreadNotificationsCount);

            MenuHelper.getMenuHelper().showNotification(menu, this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void populateNotificationsList(String responseString)
    {
        try {
            JSONObject jsonObject = new JSONObject(responseString);

                if (jsonObject.has("error_message"))
                {
                    String error_message = jsonObject.optString("error_message");
                    this.handleExpiredSession(error_message);
                }

            JSONArray jsonArray = jsonObject.optJSONArray("rows");
            sharedPrefrenceManager.setNotificationsInSP(jsonArray.toString());
            MenuHelper.getMenuHelper().populateNotificationList(jsonArray);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void handleExpiredSession(String message)
    {
        sharedPrefrenceManager.setUserLoginStatusInSP(false);
        sharedPrefrenceManager.setUserGLoginStatusInSP(false);
        sharedPrefrenceManager.setaccessTokenInSP("");
        sharedPrefrenceManager.clearPreferences(Permissions.MODULE_PERMISSION);
        sharedPrefrenceManager.clearPreferences(Consts.TRANSLATION_KEYS);
        dbHelper.deleteCurrencyDB();

        hideProgressDialog();

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void showAlertDialog(Context context, String title, String message)
    {
        Dialog dialog = new Dialog(context);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvMsg = window.findViewById(R.id.errorMessageTextView);
        TextView tvTitle = window.findViewById(R.id.titleTextView);
        TextView tvOk = window.findViewById(R.id.okTextView);
        tvMsg.setText(message);
        tvTitle.setText(title.toUpperCase());

        tvOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        String token = sharedPrefrenceManager.getAccessTokenFromKey();

        if(!token.equalsIgnoreCase(""))
        {
            dialog.show();
        }
    }

    public void showAlertDialog2(Context context, String title, String message)
    {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        TextView tvMsg = dialog.findViewById(R.id.errorMessageTextView);
        TextView tvTitle = dialog.findViewById(R.id.titleTextView);
        TextView tvOk = dialog.findViewById(R.id.okTextView);

        tvMsg.setText(message);
        tvTitle.setText(title.toUpperCase());

        tvOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showMultipleAttendanceDialog(Context context, List<JSONObject> attendanceList, String title, String courseName, String at)
    {
        try
        {
            Dialog alertDialog = new Dialog(context);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.multiple_attendance_dialog, null);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(convertView);
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            ListView lv = (ListView) convertView.findViewById(R.id.listViewForSwitchStudents);
            AppCompatImageView ivClose = (AppCompatImageView) convertView.findViewById(R.id.ivClose);
            TextView tvTitle = convertView.findViewById(R.id.titleTextView);
            TextView emptyStateTextView = convertView.findViewById(R.id.emptyStateTextView);
            if (attendanceList.size() == 0)
            {
                emptyStateTextView.setVisibility(View.VISIBLE);
            }
            else
            {
                emptyStateTextView.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
            }
            lv.setAdapter(new MultipleAttendanceDialogAdapter(context, attendanceList, "", at));
            tvTitle.setText(title);

            ivClose.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    alertDialog.dismiss();
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
        }
    }

    public static String getAcademiaDate(String currentFormat,String dateString, Context context)
    {
        String result="";
        String requiredFormat = "";

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
        String academiaFormat = sharedPrefrenceManager.getDateFormatlFromKey();

        switch (academiaFormat)
        {
            case "DMY_DESH" :
                requiredFormat = "dd-MM-yyyy";
                break;
            case "DMY_SLASH" :
                requiredFormat = "dd/MM/yyyy";
                break;
            case "MDY_DESH" :
                requiredFormat = "MM-dd-yyyy";
                break;
            case "MDY_SLASH" :
                requiredFormat = "MM/dd/yyyy";
                break;
            case "DMY_DESH_MONTH" :
                requiredFormat = "dd-MMM-yyyy";
                break;
            default:
                requiredFormat = "dd/MM/yyyy";
                break;
        }

        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        Date date=null;
        try
        {
            date = formatterOld.parse(dateString);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        if (date != null)
        {
            result = formatterNew.format(date);
        }

        if (result == "")
        {
            result = dateString;
        }
        return result;
    }

    public static String getAcademiaTime(String currentFormat,String dateString, Context context)
    {
        String result="";
        String requiredFormat = "";

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
        String academiaFormat = sharedPrefrenceManager.getTimeFormatlFromKey();

        switch (academiaFormat)
        {
            case "HM24" :
                requiredFormat = "HH:mm";
                break;
            case "HM12" :
                requiredFormat = "hh:mm a";
                break;
            default:
                requiredFormat = "hh:mm a";
                break;
        }

        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        Date date=null;
        try
        {
            date = formatterOld.parse(dateString);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        if (date != null)
        {
            result = formatterNew.format(date);
        }

        if (result == "")
        {
            result = dateString;
        }
        return result;
    }

    public static String getAcademiaDate(Long dateInLong, Context context)
    {
        String requiredFormat = "";

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
        String academiaFormat = sharedPrefrenceManager.getDateFormatlFromKey();

        switch (academiaFormat)
        {
            case "DMY_DESH" :
                requiredFormat = "dd-MM-yyyy";
                break;
            case "DMY_SLASH" :
                requiredFormat = "dd/MM/yyyy";
                break;
            case "MDY_DESH" :
                requiredFormat = "MM-dd-yyyy";
                break;
            case "MDY_SLASH" :
                requiredFormat = "MM/dd/yyyy";
                break;
            case "DMY_DESH_MONTH" :
                requiredFormat = "dd-MMM-yyyy";
                break;
            default:
                requiredFormat = "dd/MM/yyyy";
                break;
        }

        Date date = new Date(dateInLong);
        SimpleDateFormat df = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        return df.format(date);
    }

    public static String getAcademiaTime(Long dateInLong, Context context)
    {
        String requiredFormat = "";

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
        String academiaFormat = sharedPrefrenceManager.getTimeFormatlFromKey();

        switch (academiaFormat)
        {
            case "HM24" :
                requiredFormat = "HH:mm";
                break;
            case "HM12" :
                requiredFormat = "hh:mm a";
                break;
            default:
                requiredFormat = "hh:mm a";
                break;
        }

        Date date = new Date(dateInLong);
        SimpleDateFormat df = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        return df.format(date);
    }

    public void setScreenThemeColor(int color, androidx.appcompat.widget.Toolbar toolbar, Context context)
    {
        View view1 = findViewById(R.id.user_info_layout);
        View view2 = findViewById(R.id.stupidSubInfoLL);
        View view3 = findViewById(R.id.sub_info_layout);
        View view4 = findViewById(R.id.drawer_layout);
        View view5 = findViewById(R.id.stupidUserInfoLL);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor(this, color));
        }
        toolbar.setBackgroundColor(ContextCompat.getColor(this, color));
        if (view1 != null)
        {
            view1.setBackgroundColor(ContextCompat.getColor(this, color));
        }

        if (view2 != null)
        {
            view2.setBackgroundColor(ContextCompat.getColor(this, color));
        }

        if (view3 != null)
        {
            view3.setBackgroundColor(ContextCompat.getColor(this, color));
        }

        if (view4 != null)
        {
            view4.setBackgroundColor(ContextCompat.getColor(this, color));
        }

        if (view5 != null)
        {
            view5.setBackgroundColor(ContextCompat.getColor(this, color));
        }
    }

    public void openFile(File url, Context context)
    {
        try
        {
            //Here check android version file
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                fileUri = FileProvider.getUriForFile(BaseActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", url);
            }
            else
            {
                fileUri = Uri.fromFile(url);
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (url.toString().contains(".doc") || url.toString().contains(".docx"))
            {
                // Word document
                intent.setDataAndType(fileUri, "application/msword");
            } else if (url.toString().contains(".pdf"))
            {
                // PDF file
                intent.setDataAndType(fileUri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx"))
            {
                // Powerpoint file
                intent.setDataAndType(fileUri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx"))
            {
                // Excel file
                intent.setDataAndType(fileUri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar"))
            {
                // WAV audio file
                intent.setDataAndType(fileUri, "application/x-wav");
            } else if (url.toString().contains(".rtf"))
            {
                // RTF file
                intent.setDataAndType(fileUri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3"))
            {
                // WAV audio file
                intent.setDataAndType(fileUri, "audio/x-wav");
            } else if (url.toString().contains(".gif"))
            {
                // GIF file
                intent.setDataAndType(fileUri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png"))
            {
                // JPG file
                intent.setDataAndType(fileUri, "image/jpeg");
            } else if (url.toString().contains(".txt"))
            {
                // Text file
                intent.setDataAndType(fileUri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") ||
                    url.toString().contains(".mp4") || url.toString().contains(".avi"))
            {
                // Video files
                intent.setDataAndType(fileUri, "video/*");
            }
            else
            {
                intent.setDataAndType(fileUri, "*/*");
            }

            context.startActivity(Intent.createChooser(intent,"Select"));
        }
        catch (ActivityNotFoundException e)
        {
            e.printStackTrace();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
                }
            },500);
        }
    }

    public static String stringFromDate(String formatString, Date dateString)
    {
        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());
        format.setLenient(false);
        String date = null;

        format = new SimpleDateFormat(formatString);
        try
        {
            date = format.format(dateString);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return date;
    }

    public static String doublePrecision(double doubleValue)
    {
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(doubleValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu;

        menu.findItem(R.id.refresh).setTitle(translationManager.REFREASH_KEY);
        menu.findItem(R.id.dashboardMenu).setTitle(translationManager.DASHBOARD_KEY);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.notificationRL:
            {
                MenuHelper.getMenuHelper().handleOnItemSelected(item, BaseActivity.this,translationManager.NOTIFICATION_KEY,translationManager.NOTIFICATION_DETAILS_KEY);
            }break;

            case R.id.dashboardMenu:
            {
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }break;
        }
        return true;
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result)
    {
        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);
        switch (callFor)
        {
            case KEYS.SWITCH_NOTIFICATIONS:
            {}
            break;

            case KEYS.SWITCH_NOTIFICATIONS_COUNT:
            try
            {
                JSONObject responseObject = new JSONObject(responseResult);
                JSONArray array = responseObject.getJSONArray("whatever");
                JSONObject objectForAlerts = array.getJSONObject(0);
                JSONObject objectForNotifications = array.getJSONObject(1);


                Integer unreadAlertCount = objectForAlerts.getInt("unreadCount");
                Integer unreadNotificationsCount = objectForNotifications.getInt("unreadCount");

                SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(this);
                sharedPrefrenceManager.setAlertCountsInSP(unreadAlertCount);
                sharedPrefrenceManager.setNotificationCountsInSP(unreadNotificationsCount);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v)
    {}

    public static class Utils
    {
        public static SortedMap<Currency, Locale> currencyLocaleMap;

        static {
            currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>()
            {
                public int compare(Currency c1, Currency c2)
                {
                    return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
                }
            });
            for (Locale locale : Locale.getAvailableLocales())
            {
                try
                {
                    Currency currency = Currency.getInstance(locale);
                    currencyLocaleMap.put(currency, locale);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        public static String getCurrencySymbol(String currencyCode)
        {
            if (currencyCode.equals("RTGS"))
            {
                return "RTGS ";
            } else if (currencyCode.equals("R"))
            {
                return "Rands ";
            }
            Currency currency = Currency.getInstance(currencyCode);
            return currency.getSymbol(currencyLocaleMap.get(currency));
        }
    }

    public void showProgressDialog(Context context)
    {
        acProgressFlowerDialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .petalThickness(3)
                .fadeColor(Color.DKGRAY).build();
        acProgressFlowerDialog.setCancelable(false);
        acProgressFlowerDialog.setCanceledOnTouchOutside(false);
        acProgressFlowerDialog.show();
    }

    public void hideProgressDialog()
    {
        try {
            if(acProgressFlowerDialog != null && acProgressFlowerDialog.isShowing()){
                acProgressFlowerDialog.dismiss();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            ProjectUtils.showLog("BaseActivity",ex.getMessage());
        }
    }
}
