package com.serosoft.academiassu.Modules.MyRequest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on May 14 2020.
 */

public class RequestIdAdapter extends ArrayAdapter<String> {

    private Context context;
    int resource;
    private ArrayList<String> requestIdList, tempItems, suggestions;

    public RequestIdAdapter(Context context, int resource, ArrayList<String> requestIdList) {
        super(context, resource, requestIdList);
        this.context = context;
        this.resource = resource;
        this.requestIdList = requestIdList;
        tempItems = new ArrayList<String>(requestIdList);
        suggestions = new ArrayList<String>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.document_type_item, parent, false);
        }

        TextView tvItem = view.findViewById(R.id.tvItem);

        String value = ProjectUtils.getCorrectedString(requestIdList.get(position));

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

        return (null != requestIdList ? requestIdList.size() : 0);
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((String) resultValue);
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (String people : tempItems) {
                    if (people.toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<String> filterList = (ArrayList<String>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (String people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
