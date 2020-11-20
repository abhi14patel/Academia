package com.serosoft.academiassu.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Modules.Assignments.Assignment.AssignmentDetailTempActivity;
import com.serosoft.academiassu.Modules.Login.LoginActivity;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.serosoft.academiassu.Utils.ProjectUtils;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    public static final String ANDROID_CHANNEL_ID = "com.serosoft.academia.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";

    int assignmentId = 0;
    String tag = "";
    Intent intent;
    JSONObject jsonObject = null;

    SharedPrefrenceManager sharedPrefrenceManager;

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

        //Check if the message contain data
        if(remoteMessage.getData().size()>0)
        {
            ProjectUtils.showLog(TAG,"Message Data Payload="+remoteMessage.getData());

            Map<String, String> data = remoteMessage.getData();

            String message = data.get("body");
            String title = data.get("title");

            try {
                jsonObject = new JSONObject(data.get("info"));
            }catch (Exception ex) {
                ex.printStackTrace();
            }

            sendNotification(title,message,jsonObject);
        }

        if(remoteMessage.getNotification() != null)
        {
            ProjectUtils.showLog(TAG,"Notification Body="+remoteMessage.getNotification().getBody());


            Map<String, String> data = remoteMessage.getData();

            String message = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();

            try {
                jsonObject = new JSONObject(data.get("info"));
            }catch (Exception ex) {
                ex.printStackTrace();
            }

            sendNotification(title,message,jsonObject);
        }
    }

    private void sendNotification(String title,String messageBody,JSONObject jsonObject)
    {
        sharedPrefrenceManager = new SharedPrefrenceManager(this);

        try{
            assignmentId = jsonObject.getInt("dataId");
            tag = jsonObject.getString("tag");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //Here check user logged in or not
        if (sharedPrefrenceManager.getUserLoginStatusFromKey()) {

            //Here check tag and redirect to required screen
            if(tag.equalsIgnoreCase(Consts.TAG_HOMEWORK_ASSIGNMENTS)) {

                intent = new Intent(this, AssignmentDetailTempActivity.class);
                intent.putExtra(Consts.ASSIGNMENT_ID,assignmentId);

            }else{
                intent = new Intent(this, SplashActivity.class);
            }
        }
        else {
            intent = new Intent(this, LoginActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,ANDROID_CHANNEL_ID);

        notificationBuilder.setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))               //Here add this for multiline notification message
                .setSmallIcon(R.drawable.ic_academia_notification)
                .setColor(getResources().getColor(R.color.colorRed))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] {200, 200,200,200})
                .setLights(Color.RED, 2000, 1500)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(uri)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
        createChannels(messageBody);
    }

    //Here create channel id for oreo and above device
    public void createChannels(String messageBody)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            // create android channel
            NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            androidChannel.setDescription(messageBody);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.RED);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            androidChannel.setVibrationPattern(new long[] {200, 200,200,200});

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(androidChannel);
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token)
    {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token)
    {
        // TODO: Implement this method to send token to your app server.
    }
}