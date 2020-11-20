package com.serosoft.academiassu.Modules.Assignments.Assignment.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.Assignment_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on March 19 2020.
 */

public class AssignmentNameAdapter extends ArrayAdapter<Assignment_Dto> {

    private Context context;
    int resource;
    private ArrayList<Assignment_Dto> assignmentList, tempItems, suggestions;

    public AssignmentNameAdapter(Context context, int resource, ArrayList<Assignment_Dto> assignmentList) {
        super(context, resource, assignmentList);
        this.context = context;
        this.resource = resource;
        this.assignmentList = assignmentList;
        tempItems = new ArrayList<Assignment_Dto>(assignmentList);
        suggestions = new ArrayList<Assignment_Dto>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.document_type_item, parent, false);
        }
        Assignment_Dto list = assignmentList.get(position);

        TextView tvItem = view.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(list.getAssignmentName());

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

        return (null != assignmentList ? assignmentList.size() : 0);
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Assignment_Dto) resultValue).getAssignmentName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Assignment_Dto people : tempItems) {
                    if (people.getAssignmentName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<Assignment_Dto> filterList = (ArrayList<Assignment_Dto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Assignment_Dto people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
