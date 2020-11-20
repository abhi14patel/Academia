package com.serosoft.academiassu.Modules.TimeTable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.serosoft.academiassu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TimeTableCourseWiseListAdapter extends ArrayAdapter<JSONObject> {
    Context context;
    List<JSONObject> jsonList;

    public TimeTableCourseWiseListAdapter(Context context, List<JSONObject> jsonList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.time_table_view_type_list_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.item_name);

        JSONObject object = jsonList.get(position);
        try {
            String courseName = object.getString("courseName");
            if (courseName == null || courseName.equals("null")) {
                courseName = object.getString("courseCode");
            }
            textView.setText(courseName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
