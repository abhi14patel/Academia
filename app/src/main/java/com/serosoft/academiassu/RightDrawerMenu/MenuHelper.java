package com.serosoft.academiassu.RightDrawerMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.core.view.MenuItemCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Networking.KEYS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuHelper implements AsyncTaskCompleteListener
{
    private static PopupWindow popupWindow;
    private static List<JSONObject> msgList;
    private static SharedPrefrenceManager prefrenceManager;
    private static MenuHelper menuHelper;
    private TextView textViewForNotificationCount;

    private MenuHelper()
    {
        msgList = new ArrayList<JSONObject>();
    }

    public static MenuHelper getMenuHelper()
    {
        if (menuHelper == null)
        {
            menuHelper = new MenuHelper();
        }
        return menuHelper;
    }

    public boolean showNotification(Menu menu, Context context)
    {
        final MenuItem item = menu.findItem(R.id.notificationRL);
        MenuItemCompat.setActionView(item, R.layout.notification_layout);
        final Menu m = menu;
        View notifCount = MenuItemCompat.getActionView(item);
        ImageView notificationIconImage = (ImageView) notifCount.findViewById(R.id.notificationIconImage);
        notificationIconImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                m.performIdentifierAction(item.getItemId(), 0);
            }
        });
        prefrenceManager = new SharedPrefrenceManager(context);
        textViewForNotificationCount = notifCount.findViewById(R.id.actionbar_notifcation_textview);
        textViewForNotificationCount.setVisibility(View.VISIBLE);
        int tvCount = prefrenceManager.getNotificationCountsFromKey();
        if (tvCount > 0 && tvCount < 10)
        {
            textViewForNotificationCount.setText("  " + String.valueOf(tvCount) + "  ");
        } else if (tvCount >= 10)
        {
            textViewForNotificationCount.setText("  9+  ");
        } else if(tvCount <=0)
        {
            textViewForNotificationCount.setVisibility(View.GONE);
        }
        return true;
    }

    public void populateNotificationList(JSONArray arr)
    {
        try {
            msgList = new ArrayList<JSONObject>();
            for (int i = 0 ; i < arr.length() ; ++i)
            {
                JSONObject obj = arr.getJSONObject(i);
                msgList.add(obj);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    public boolean handleOnItemSelected(MenuItem item, final Context context,String notificationKey,String notificationDetailsKey)
    {
        switch (item.getItemId())
        {
            case R.id.notificationRL:
                View notifCount = MenuItemCompat.getActionView(item);
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                popupWindow = new PopupWindow(notifCount, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT /*layoutWidth, layoutHeight*/, true);
                popupWindow.setFocusable(true);
                popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setBackgroundDrawable(
                        new ColorDrawable(context.getResources().getColor(R.color.popupBg2)));
                popupWindow.setOutsideTouchable(false);
                popupWindow.setTouchable(true);
                popupWindow.setAnimationStyle(R.style.AnimationPopup);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rootView = inflater.inflate(R.layout.notification_right_drawer, null);

                ListView listView = rootView.findViewById(R.id.notificationListView);
                TextView noNotif = rootView.findViewById(R.id.noNotif);
                TextView tvTitle = rootView.findViewById(R.id.tvTitle);
                listView.setVisibility(View.VISIBLE);
                noNotif.setVisibility(View.GONE);

                tvTitle.setText(notificationKey);

                listView.setOnTouchListener(new View.OnTouchListener()
                {
                    public float downX, downY, upX, upY;
                    final int MIN_DISTANCE = 200;

                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        switch (event.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                            {
                                downX = event.getX();
                                downY = event.getY();
                            }
                            case MotionEvent.ACTION_UP:
                            {
                                upX = event.getX();
                                upY = event.getY();

                                float deltaX = downX - upX;
                                float deltaY = downY - upY;

                                // swipe horizontal?
                                if (Math.abs(deltaX) > MIN_DISTANCE)
                                {
                                    // left or right
                                    if (deltaX < 0)
                                    {
                                        popupWindow.dismiss();
                                    }
                                }
                                // no swipe horizontally and no swipe vertically
                            }// case MotionEvent.ACTION_UP:
                        }
                        return false;
                    }
                });
                Button outsideButton = (Button) rootView.findViewById(R.id.outsideButton);
                outsideButton.setOnTouchListener(new View.OnTouchListener()
                {
                    public float downX, downY, upX, upY;
                    final int MIN_DISTANCE = 50;

                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        switch (event.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                            {
                                downX = event.getX();
                                downY = event.getY();
                            }
                            case MotionEvent.ACTION_UP:
                            {
                                upX = event.getX();
                                upY = event.getY();

                                float deltaX = downX - upX;
                                float deltaY = downY - upY;

                                if (Math.abs(deltaX) > MIN_DISTANCE)
                                {
                                    // left or right
                                    if (deltaX < 0)
                                    {
                                        popupWindow.dismiss();
                                    }
                                }
                            }
                        }
                        return false;
                    }
                });
                outsideButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        popupWindow.dismiss();
                    }
                });


                if ((msgList != null) && (msgList.size() > 0))
                {
                    listView.setAdapter(new MenuAdapter(context, msgList, MenuHelper.class.getSimpleName()));
                }
                else
                {
                    listView.setVisibility(View.GONE);
                    noNotif.setText("No Notifications to show!");
                    noNotif.setVisibility(View.VISIBLE);
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        popupWindow.dismiss();
                        Intent intent = new Intent(context, NotificationDetailActivity.class);

                        JSONObject jsonObject = msgList.get(position);
                        intent.putExtra("circularObject", jsonObject.toString());
                        intent.putExtra("title", notificationDetailsKey);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        String messageId = String.valueOf(jsonObject.optInt("messageId"));
                        new OptimizedServerCallAsyncTask(context, MenuHelper.this, KEYS.SWITCH_MARK_NOTIFICATION_AS_READ).execute(messageId);
                        context.startActivity(intent);
                    }
                });
                popupWindow.update();
                popupWindow.setContentView(rootView);
                popupWindow.showAtLocation(notifCount, Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
                return true;
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

        }
    }
}