package com.serosoft.academiassu.Modules.Attendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.serosoft.academiassu.Modules.Attendance.Models.AttendanceCourse_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on April 15 2020.
 */

public class AttendanceCourseAdapter extends ArrayAdapter<AttendanceCourse_Dto> {

    private Context context;
    int resource;
    private ArrayList<AttendanceCourse_Dto> courseList, tempItems, suggestions;

    public AttendanceCourseAdapter(Context context, int resource, ArrayList<AttendanceCourse_Dto> courseList) {
        super(context, resource, courseList);
        this.context = context;
        this.resource = resource;
        this.courseList = courseList;
        tempItems = new ArrayList<AttendanceCourse_Dto>(courseList);
        suggestions = new ArrayList<AttendanceCourse_Dto>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.document_type_item, parent, false);
        }
        AttendanceCourse_Dto list = courseList.get(position);

        TextView tvItem = view.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getCourseCodeName());

        if(!value.equalsIgnoreCase("")){
            tvItem.setText(value);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    @Override
    public int getCount() {

        return (null != courseList ? courseList.size() : 0);
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((AttendanceCourse_Dto) resultValue).getCourseCodeName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (AttendanceCourse_Dto people : tempItems) {
                    if (people.getCourseCodeName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<AttendanceCourse_Dto> filterList = (ArrayList<AttendanceCourse_Dto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (AttendanceCourse_Dto people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
