package com.serosoft.academiassu.RightDrawerMenu;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by opj1 on 26/08/2016.
 */
public class MenuAdapter extends ArrayAdapter<JSONObject> {
    Context context;
    List<JSONObject> notificationList;

    public MenuAdapter(Context context, List<JSONObject> arrayList, String callBy) {
        super(context, 0, arrayList);
        this.context = context;
            this.notificationList = arrayList;
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return notificationList.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.right_nav_item, parent, false);
        }

        TextView notifTextView = convertView.findViewById(R.id.drawerNotifText);
        TextView timeTextView = convertView.findViewById(R.id.drawerNotifTimeText);
        JSONObject circular = getItem(position);
        try {
            notifTextView.setText(circular.optString("subject"));

            Boolean read = circular.optBoolean("whetherRead");
            if (read == true) {
                notifTextView.setTypeface(Typeface.DEFAULT);
            } else {
                notifTextView.setTypeface(notifTextView.getTypeface(), Typeface.BOLD);
            }
            Long dateInLong = circular.optLong("date_long");
            String academiaDate = BaseActivity.getAcademiaDate(dateInLong, context);
            String academiaTime = BaseActivity.getAcademiaTime(dateInLong, context);
            timeTextView.setText(academiaDate + " " + academiaTime);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }

}
