package com.serosoft.academiassu.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.serosoft.academiassu.BuildConfig;
import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Helpers.StatusClass;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.R;

import org.threeten.bp.YearMonth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class ProjectUtils
{
    public static final String TAG = "ProjectUtility";
    private static Toast toast;
    private static AlertDialog dialog;
    private static ProgressDialog mProgressDialog;
    private static final String VERSION_UNAVAILABLE = "N/A";

    public static int getScreenWidth()
    {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight()
    {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, float dpValue)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue)
    {
        return (int) (spValue * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public static int px2sp(Context context, float pxValue)
    {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    //For Changing Status Bar Color if Device is above 5.0(Lollipop)
    public static void changeStatusBarColor(Activity activity)
    {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimary));
        }
    }

    public static void changeStatusBarColorNew(Activity activity, int color)
    {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(activity.getResources().getColor(color));
        }
    }

    public static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    //For Long Period Toast Message
    public static void showLong(Context context, String message)
    {
        if (message == null)
        {
            return;
        }
        if (toast == null && context != null)
        {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        if (toast != null)
        {
            toast.setText(message);
            toast.show();
        }
    }

    public static void showDialog(Context context, String title, String msg,
                                  DialogInterface.OnClickListener OK, boolean isCancelable)
    {
        if (title == null)
            title = context.getResources().getString(R.string.app_name);

        if (OK == null)
            OK = new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                    hideDialog();
                }
            };

        if (dialog == null)
        {
            Builder builder = new Builder(context,R.style.MaterialThemeDialog);
            builder.setTitle(title);
            builder.setMessage(msg);
            builder.setPositiveButton("OK", OK);
            dialog = builder.create();

            dialog.setCancelable(isCancelable);
        }

        try
        {
            dialog.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void showDialog(Context context, String title, String msg,
                                  DialogInterface.OnClickListener OK,
                                  DialogInterface.OnClickListener cancel, boolean isCancelable)
    {
        AlertDialog dialog = null;

        if (title == null)
            title = context.getResources().getString(R.string.app_name);

        if (OK == null)
            OK = new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                    hideDialog();
                }
            };

        if (cancel == null)
            cancel = new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                    hideDialog();
                }
            };

        if (dialog == null)
        {
            Builder builder = new Builder(context,R.style.MaterialThemeDialog);
            builder.setTitle(title);
            builder.setMessage(msg);
            builder.setPositiveButton("OK", OK);
            builder.setNegativeButton("Cancel", cancel);
            dialog = builder.create();
            dialog.setCancelable(isCancelable);
        }

        try
        {
            dialog.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void showDialog2(Context context, String title, String msg,
                                  DialogInterface.OnClickListener OK,
                                  DialogInterface.OnClickListener cancel, boolean isCancelable)
    {
        AlertDialog dialog = null;

        if (title == null)
            title = context.getResources().getString(R.string.app_name);

        if (OK == null)
            OK = new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                    hideDialog();
                }
            };

        if (cancel == null)
            cancel = new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                    hideDialog();
                }
            };

        if (dialog == null)
        {
            Builder builder = new Builder(context,R.style.MaterialThemeDialog);
            builder.setTitle(title);
            builder.setMessage(msg);
            builder.setPositiveButton("Yes", OK);
            builder.setNegativeButton("No", cancel);
            dialog = builder.create();
            dialog.setCancelable(isCancelable);
        }

        try
        {
            dialog.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Here Show ProgressDialog
    public static ProgressDialog showProgressDialog(Context context, String title, String message, boolean isCancelable)
    {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        mProgressDialog.setCancelable(isCancelable);
        return mProgressDialog;
    }

    // Static method to pause the progress dialog.
    public static void pauseProgressDialog()
    {
        try {
            if (mProgressDialog != null)
            {
                mProgressDialog.cancel();
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Static method to cancel the Dialog.
     */
    public static void cancelDialog()
    {
        try
        {
            if (dialog != null)
            {
                dialog.cancel();
                dialog.dismiss();
                dialog = null;
            }
        } catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Static method to hide the dialog if visible
     */
    public static void hideDialog()
    {
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
            dialog.cancel();
            dialog = null;
        }
    }

    public static boolean isEmailValid(String email)
    {
        String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches())
        {
            return true;
        } else if (email.equals(""))
        {
            return false;
        }
        return false;
    }

    public static boolean isPhoneNumberValid(int minimum, int maximum, String number)
    {
        if (number.length() < minimum || number.length() > maximum)
        {
            return false;
        }

        return true;
    }

    public static boolean isPasswordValid(String number)
    {
        String regexStr = " (?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8,20})$";

        if (number.length() <= 5)
        {
            return false;
        }

        return true;
    }

    public static boolean isNameValid(EditText text)
    {
        CharSequence inputStr = text.getText().toString();
        Pattern pattern = Pattern.compile(new String ("^[a-zA-Z\\s]*$"));
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNricValid(String number)
    {
        if (number.length() < 6 || number.length() > 12)
        {
            return false;
        }

        return true;
    }

    public static boolean isEditTextFilled(EditText text)
    {
        if (text.getText() != null && text.getText().toString().trim().length() > 0)
        {
            return true;
        } else
        {
            return false;
        }
    }

    public static boolean isTextViewFilled(TextView text)
    {
        if (text.getText() != null && text.getText().toString().trim().length() > 0)
        {
            return true;
        } else
        {
            return false;
        }
    }

    //Aadhar Cards Format
    public static class aadharCardFormatWatcher implements TextWatcher
    {
        private static final char space = '-';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {}

        @Override
        public void afterTextChanged(Editable s)
        {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 5) == 0)
            {
                final char c = s.charAt(s.length() - 1);
                if (space == c)
                {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0)
            {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3)
                {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }
    }

    //Pan card format
    public static boolean panCardFormat(EditText text)
    {
        String value = text.getText().toString().trim();

        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        Matcher matcher = pattern.matcher(value);

        // Check if pattern matches
        if (matcher.matches())
        {
            Log.e("Matching","Yes");

            return true;
        }
        else
        {
            Log.e("Matching","No");

            return false;
        }
    }

    public static long currentTimeInMillis()
    {
        Time time = new Time();
        time.setToNow();
        return time.toMillis(false);
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context)
    {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean hasFroyo()
    {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasICS()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasKitKat()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static String getVersionName(Context context)
    {
        // Get app version
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        String versionName;
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            versionName = VERSION_UNAVAILABLE;
        }
        return versionName;
    }

    public static int getVersionCode(Context ctx)
    {
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static void showLog(String tag,String text)
    {
        Log.e(tag,text);
    }

    //To check collection empty or not
    public static boolean isEmpty(Collection obj)
    {
        return obj == null || obj.isEmpty();
    }

    public static String maskString(String strText, int start, int end, char maskChar)
            throws Exception
    {
        if(strText == null || strText.equals(""))
            return "";

        if(start < 0)
            start = 0;

        if( end > strText.length() )
            end = strText.length();

        if(start > end)
            throw new Exception("End index cannot be greater than start index");

        int maskLength = end - start;

        if(maskLength == 0)
            return strText;

        StringBuilder sbMaskString = new StringBuilder(maskLength);

        for(int i = 0; i < maskLength; i++)
        {
            sbMaskString.append(maskChar);
        }

        return strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength);
    }

    public static YearMonth getMonth(Long strDate)
    {
        Date date = new Date(strDate);

        YearMonth month = null;
        try {
            //  date = df.parse(strDate);
            Format formatter = new SimpleDateFormat("yyyy-MM");
            month = YearMonth.parse(formatter.format(date));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return month;
    }

    public static String getMonth1(Long strDate)
    {
        Date date = new Date(strDate);

        String month = null;
        try {
            //  date = df.parse(strDate);
            Format formatter = new SimpleDateFormat("yyyy-MM");
            month = formatter.format(date);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return month;
    }

    //Here get is current date between start date and end date
    public static boolean CheckDates(String d1, String d2, String d3){

        SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM");

        boolean month = false;

        try {
            Date sd1 = dfDate.parse(d1);
            Date ed1 = dfDate.parse(d2);
            Date cd1 = dfDate.parse(d3);

            month = ((cd1.after(sd1)) || cd1.equals(sd1)) && (cd1.before(ed1) || cd1.equals(ed1));

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return month;
    }

    //For hide keyboard
    public static void hideKeyboard(Activity activity)
    {
        View view = activity.findViewById(android.R.id.content);
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboard2(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getCorrectedString(String str)
    {
        if (str != null && !str.equalsIgnoreCase("null") && !str.equalsIgnoreCase(""))
        {
            return str;
        }
        else
        {
            return "";
        }
    }

    public static String getCorrectedString2(String str)
    {
        if (str != null && !str.equalsIgnoreCase("null") && !str.equalsIgnoreCase("") && !str.equalsIgnoreCase(" - "))
        {
            return str;
        }
        else
        {
            return "";
        }
    }

    public static boolean isWhatsAppInstalledOrNot(Context context,String uri)
    {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    //Share app
    public static void shareApp(Context mContext,String content)
    {
        String appPackageName = BuildConfig.APPLICATION_ID;
        String appName = mContext.getString(R.string.app_name);
        String appPath = "";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if(Flags.IS_APP_LIVE){
            appPath  = "https://play.google.com/store/apps/details?id=" +appPackageName;
        }else{
            appPath = BaseURL.BASE_URL;
        }
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content+"\n\n"+appPath);
        mContext.startActivity(Intent.createChooser(shareIntent, "Share with"));
    }

    public static void setViewEnabled(View view, boolean enabled)
    {
        view.setClickable(enabled);
    }

    public static String capitalize(String line)
    {
        if (line.length() > 1)
            return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
        else if (line.length() == 1)
            return line.toUpperCase();
        else
            return line;
    }

    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            public void run() {
                view.setEnabled(true);
            }
        }, 500);
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void openPdf(Context context,String pdf)
    {
        Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
        pdfViewIntent.setDataAndType(Uri.parse(pdf),"application/pdf");
        pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(pdfViewIntent, "Open File");
        try
        {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static  boolean isAppForground(Context mContext)
    {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty())
        {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName()))
            {
                return false;
            }
        }
        return true;
    }

    public static void hideSpace(Editable editable, EditText etConPassword)
    {
        String result = editable.toString().replaceAll(" ", "");
        if (!editable.toString().equals(result))
        {
            etConPassword.setText(result);
            etConPassword.setSelection(result.length());
        }
    }

    //apply font to tab layout
    public static void setCustomFont(Context context, TabLayout tabs)
    {
        ViewGroup vg = (ViewGroup) tabs.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++)
        {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++)
            {
                View tabViewChild = vgTab.getChildAt(i);

                if (tabViewChild instanceof TextView)
                {
                    //Put your font in assests folder
                    //assign name of the font here (Must be case sensitive)
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.ttf"));
                }
            }
        }
    }

    //Here enable/disable child views
    public static void setViewAndChildrenEnabled(View view, boolean enabled)
    {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup)
        {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++)
            {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    public static String convertTimestampToDate(long timestamp,Context mContext)
    {
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat;

        String result="";

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String academiaFormat = sharedPrefrenceManager.getDateFormatlFromKey();

        switch (academiaFormat)
        {
            case "DMY_DESH" :
                simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                break;
            case "DMY_SLASH" :
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                break;
            case "MDY_DESH" :
                simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                break;
            case "MDY_SLASH" :
                simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                break;
            case "DMY_DESH_MONTH" :
                simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                break;
            default:
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                break;
        }

        result = simpleDateFormat.format(date);
        return result;
    }

    public static String convertTimestampToDate2(long timestamp,Context mContext)
    {
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String result = simpleDateFormat.format(date);
        return result;
    }

    public static String convertTimestampToTime(long timestamp,Context mContext)
    {
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat;

        String result="";

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String academiaFormat = sharedPrefrenceManager.getTimeFormatlFromKey();

        switch (academiaFormat)
        {
            case "HM24" :
                simpleDateFormat = new SimpleDateFormat("HH:mm");
                break;
            case "HM12" :
                simpleDateFormat = new SimpleDateFormat("hh:mm a");
                break;
            default:
                simpleDateFormat = new SimpleDateFormat("hh:mm a");
                break;
        }

        result = simpleDateFormat.format(date);

        return result;
    }

    public static String getTimestampToDate(Context mContext)
    {
        String result="";

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String academiaFormat = sharedPrefrenceManager.getDateFormatlFromKey();

        switch (academiaFormat)
        {
            case "DMY_DESH" :
                result = "dd-MM-yyyy";
                break;
            case "DMY_SLASH" :
                result = "dd/MM/yyyy";
                break;
            case "MDY_DESH" :
                result = "MM-dd-yyyy";
                break;
            case "MDY_SLASH" :
                result = "MM/dd/yyyy";
                break;
            case "DMY_DESH_MONTH" :
                result = "dd-MMM-yyyy";
                break;
            default:
                result = "dd/MM/yyyy";
                break;
        }
        return result;
    }

    public static String getTimestampToTime(Context mContext)
    {
        String result="";

        SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(mContext);
        String academiaFormat = sharedPrefrenceManager.getTimeFormatlFromKey();

        switch (academiaFormat)
        {
            case "HM24" :
                result = "HH:mm";
                break;
            case "HM12" :
                result = "hh:mm a";
                break;
            default:
                result = "hh:mm a";
                break;
        }

        return result;
    }

    public static Bitmap setBitmapImage(int maxWidth, int maxHeight, Bitmap bitmap)
    {
        try {
            if (maxHeight > 0 && maxWidth > 0) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                int finalWidth = maxWidth;
                int finalHeight;
                float ratioOfWidth = (float) width / (float) maxWidth;

                if (ratioOfWidth > 0) {
                    finalHeight = (int) ((float) height / ratioOfWidth);
                } else if (ratioOfWidth < 0) {
                    finalHeight = (int) ((float) height / ratioOfWidth);
                } else {
                    finalHeight = height;
                }
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true);
                return newBitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int showDocIcon(String document) {
        String extension = "";
        int i = document.lastIndexOf('.');
        if (i > 0) {
            extension = document.substring(i + 1).toUpperCase();
        }
        switch (extension) {
            case "CSV":
                return R.drawable.xls;
            case "DOC":
                return R.drawable.doc;
            case "DOCX":
                return R.drawable.doc;
            case "GIF":
                return R.drawable.doc;
            case "HTM":
                return R.drawable.html;
            case "HTML":
                return R.drawable.html;
            case "JPG":
                return R.drawable.jpg;
            case "JPEG":
                return R.drawable.jpeg;
            case "PDF":
                return R.drawable.pdf;
            case "PNG":
                return R.drawable.png;
            case "PPT":
                return R.drawable.ppt;
            case "PPTX":
                return R.drawable.ppt;
            case "TXT":
                return R.drawable.txt;
            case "XLS":
                return R.drawable.xls;
            case "XLSX":
                return R.drawable.xls;
            case "XLXV":
                return R.drawable.xls;
            case "ZIP":
                return R.drawable.zip;
            case "RAR":
                return R.drawable.rar;
            default:
                return R.drawable.default_doc;
        }
    }

    public static boolean isSpecialCharacter(Character value){

        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(value.toString());

        if (!matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNumericDigit(Character value){

        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(value.toString());

        if (!matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCapitalChar(Character value){

        if (Character.isUpperCase(value)) {
            return true;
        } else {
            return false;
        }
    }

    public static long getFileFolderSize(File dir) {
        long size = 0;
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    size += file.length();
                } else
                    size += getFileFolderSize(file);
            }
        } else if (dir.isFile()) {
            size += dir.length();
        }
        return size;
    }

    public static String formateDateFromString(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }

    public static boolean hasPermissionInManifest(Activity activity, int requestCode, String[] permissionName)
    {
        for (String s:permissionName)
        {
            if (ContextCompat.checkSelfPermission(activity, s) != PackageManager.PERMISSION_GRANTED)
            {
                // Some permissions are not granted, ask the user.
                ActivityCompat.requestPermissions(activity, permissionName, requestCode);
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermissionInManifest2(Fragment fragment, int requestCode, String[] permissionName)
    {
        for (String s:permissionName)
        {
            if (ContextCompat.checkSelfPermission(fragment.getActivity(), s) != PackageManager.PERMISSION_GRANTED)
            {
                // Some permissions are not granted, ask the user.
                fragment.requestPermissions(permissionName, requestCode);
                return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> reverseArray(ArrayList<Integer> aList){
        ArrayList<Integer> tempList = new ArrayList<>();
        for(int i = aList.size() - 1 ; i>= 0 ; i--){

            tempList.add(aList.get(i));
        }
        return tempList;
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDateFormat(String inputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }

    public static String getDateFormat2(String inputDate){

        Date parsed = null;
        String outputDate = "";

        if(!inputDate.equalsIgnoreCase("")){
            SimpleDateFormat df_input = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            SimpleDateFormat df_output = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());

            try {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);

            } catch (ParseException e) {
                Log.e(TAG, "ParseException - dateFormat");
            }
        }
        return outputDate;
    }

    public static String getDateFormat3(String inputDate){

        Date parsed = null;
        String outputDate = "";

        if(!inputDate.equalsIgnoreCase("")){
            SimpleDateFormat df_input = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            SimpleDateFormat df_output = new SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault());

            try {
                parsed = df_input.parse(inputDate);
                outputDate = df_output.format(parsed);

            } catch (ParseException e) {
                Log.e(TAG, "ParseException - dateFormat");
            }
        }
        return outputDate;
    }

    public static long getLongDateFormat(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);

        Date date = calendar.getTime();
        return date.getTime();
    }

    public static File getLocalBitmapUri(Bitmap bmp, Context context) {
        File bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public static void openBrowser(Context mContext,String link){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        mContext.startActivity(browserIntent);
    }

    public static int getCountOfDays(String dateBeforeString, String dateCurrentString){

        int days = 0;
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());

        try {
            Date dateBefore = myFormat.parse(dateBeforeString);
            Date dateAfter = myFormat.parse(dateCurrentString);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            float daysBetween = (difference / (1000*60*60*24));

            days = (int) daysBetween;
            return days;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    public static void getRequestStatus(Context mContext,TextView textView,int requestStatusId) {

        switch (requestStatusId){
            case StatusClass.REQUEST_STATUS_SUBMITTED:
                textView.setText("Not Yet Executed");
                textView.setTextColor(mContext.getResources().getColor(R.color.colorRed3));
                break;
            case StatusClass.REQUEST_STATUS_ASSIGNED:
                textView.setText("Not Yet Executed");
                textView.setTextColor(mContext.getResources().getColor(R.color.colorRed3));
                break;
            case StatusClass.REQUEST_STATUS_ESCLATED:
                textView.setText("Escalated");
                textView.setTextColor(mContext.getResources().getColor(R.color.colorRed3));
                break;
            case StatusClass.REQUEST_STATUS_APPROVED:
                textView.setText("Not Yet Executed");
                textView.setTextColor(mContext.getResources().getColor(R.color.colorRed3));
                break;
            case StatusClass.REQUEST_STATUS_REJECTED:
                textView.setText("This request has been rejected");
                textView.setTextColor(mContext.getResources().getColor(R.color.colorRed3));
                break;
            case StatusClass.REQUEST_STATUS_CLOSED:
                textView.setText("Executed Successfully");
                textView.setTextColor(mContext.getResources().getColor(R.color.colorGreen2));
                break;
            case StatusClass.REQUEST_STATUS_WITHDRAWN:
                textView.setText("This request has been withdrawn");
                textView.setTextColor(mContext.getResources().getColor(R.color.colorRed3));
                break;
            default:
                textView.setText("Not Yet Executed");
                textView.setTextColor(mContext.getResources().getColor(R.color.colorRed3));
                break;
        }
    }

    public static String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static void disableSpinner(Spinner spinner){

        spinner.setEnabled(false);
        spinner.setBackgroundResource(R.drawable.spinner_bg_normal);
    }

    public static void deleteTempImageFolder()
    {
        File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + Consts.ACADEMIA + "/temp");

        if (myDir.exists() && myDir.isDirectory())
        {
            String[] children = myDir.list();
            for (int i = 0; i < children.length; i++)
            {
                File fileDirChild =  new File(myDir, children[i]);
                String[] subchildren = fileDirChild.list();
                if (fileDirChild.exists() && fileDirChild.isDirectory())
                {
                    for (int j = 0 ; j<subchildren.length;j++)
                    {
                        new File(fileDirChild, subchildren[j]).delete();
                    }
                }
                else
                {
                    fileDirChild.delete();
                }
            }
        }
        else
        {
            myDir.delete();
        }
    }

    static public boolean deleteDirectory(File path)
    {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }
}