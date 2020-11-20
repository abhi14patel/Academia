package com.serosoft.academiassu.Modules.Circular;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import org.json.JSONObject;
import java.util.List;

/**
 * Created by ajitmaya on 14/06/16.
 */
public class CircularListAdapter extends ArrayAdapter<JSONObject>{

    Context context;
    List<JSONObject> circularArrayList=null;

    public CircularListAdapter(Context context, List<JSONObject> circularArrayList) {
        super(context, 0, circularArrayList);
        this.context=context;
        this.circularArrayList=circularArrayList;
    }


    @Override
    public int getCount() {
        return circularArrayList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return circularArrayList.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.circular_list_item, parent, false);
        }
        TextView circularTextView = convertView.findViewById(R.id.event_name);
        TextView circularStartDate = convertView.findViewById(R.id.event_start_date_Lv);
        TextView circularEndDate = convertView.findViewById(R.id.event_end_date_Lv);
        circularEndDate.setVisibility(View.GONE);
        TextView circularStartTime = convertView.findViewById(R.id.event_start_time_Lv);
        TextView circularEndTime = convertView.findViewById(R.id.event_end_time_Lv);
        circularEndTime.setVisibility(View.GONE);
        TextView circularVenueTextView = convertView.findViewById(R.id.event_venue);
        ImageView circularImageView = convertView.findViewById(R.id.event_icon);

        circularVenueTextView.setVisibility(View.GONE);
        JSONObject circular = getItem(position);
        try {
            circularTextView.setText(circular.optString("subject"));

            circularTextView.setTypeface(Typeface.DEFAULT);
            circularTextView.setTextSize(16);
            Long dateInLong = circular.optLong("date_long");
            String academiaDate = BaseActivity.getAcademiaDate(dateInLong, context);
            String academiaTime = BaseActivity.getAcademiaTime(dateInLong, context);
            circularStartDate.setText(academiaDate + " " + academiaTime);
            circularStartTime.setVisibility(View.GONE);
//            circularStartDate.setText(BaseActivity.getAcademiaDate("yyyy-MM-dd hh:mm a", circular.optString("date"), context));
//            circularStartTime.setText(BaseActivity.getAcademiaTime("yyyy-MM-dd hh:mm a", circular.optString("date"), context));
            circularStartTime.setVisibility(View.GONE);
            circularEndDate.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        circularImageView.setImageResource(R.mipmap.circulars_black_2018);

        return convertView;
    }

    public void upDateNotificationDBList(List<JSONObject> allNotificationDBs) {
        this.circularArrayList=allNotificationDBs;
        CircularListAdapter.this.notifyDataSetChanged();
    }
}
