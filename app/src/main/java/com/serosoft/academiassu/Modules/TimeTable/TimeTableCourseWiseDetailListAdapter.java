package com.serosoft.academiassu.Modules.TimeTable;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimeTableCourseWiseDetailListAdapter extends ArrayAdapter<JSONObject> {
    Context context;
    List<JSONObject> jsonList;

    public TimeTableCourseWiseDetailListAdapter(Context context, List<JSONObject> jsonList) {
        super(context, 0, jsonList);
        this.context = context;
        this.jsonList = jsonList;
    }

    @Override
    public int getCount() {
        return jsonList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return jsonList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.time_table_course_wise_detail_list_item2, parent, false);
        }
        TextView textViewForDuration = convertView.findViewById(R.id.textViewForDuration);
        TextView textViewForDate = convertView.findViewById(R.id.textViewForDate);
        TextView textViewForDay = convertView.findViewById(R.id.textViewForDay);
        TextView textViewForLocation = convertView.findViewById(R.id.textViewForLocation);
        TextView textViewForMonthAndYear = convertView.findViewById(R.id.textViewForMonthAndYear);
        TextView textViewForFaculty = convertView.findViewById(R.id.textViewForFaculty);

        View viewForDate = convertView.findViewById(R.id.viewForDate);

        viewForDate.setBackgroundResource(R.drawable.rounded_corner);
        GradientDrawable drawable = (GradientDrawable) viewForDate.getBackground();
        drawable.setColor(context.getResources().getColor(R.color.colorBlue));
        textViewForDate.setTextColor(context.getResources().getColor(R.color.colorWhite));

        JSONObject object = jsonList.get(position);
        try {
            String location = object.optString("classroom");
            if (location.length() > 0 && location != "null") {
                textViewForLocation.setText(location);
            } else {
                textViewForLocation.setText("Location not specified!");
            }

            textViewForFaculty.setText(object.optString("conductedBy"));
            String dateString = object.getString("start");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String startTime = object.getString("start");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String endTime = object.getString("end");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            try {
                Date date = sdf.parse(dateString);
                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String day          = (String) DateFormat.format("dd",   date); // 20
                String monthString  = (String) DateFormat.format("MMM",  date); // Jun
                String year         = (String) DateFormat.format("yyyy", date); // 2013
                textViewForDay.setText(dayOfTheWeek.toUpperCase());
                textViewForDate.setText(day);
                textViewForMonthAndYear.setText(monthString.toUpperCase() + " - " + year);

                Long dateInLong1 = object.optLong("startTime_long");
                Long dateInLong2 = object.optLong("endTime_long");
                String d1 = BaseActivity.getAcademiaTime(dateInLong1, context);
                String d2 = BaseActivity.getAcademiaTime(dateInLong2, context);
                textViewForDuration.setText(d1 + " - " + d2);

//                Date time1 = sdf2.parse(startTime);
//                SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm a");
//                String formattedDate = targetFormat.format(time1);
//
//                Date time2 = sdf3.parse(endTime);
//                String formattedDate2 = targetFormat.format(time2);
//
//                textViewForDuration.setText(formattedDate + " - " + formattedDate2);

            } catch (ParseException ex) {
                ex.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
