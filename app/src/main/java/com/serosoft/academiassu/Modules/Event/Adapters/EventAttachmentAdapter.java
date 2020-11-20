package com.serosoft.academiassu.Modules.Event.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.serosoft.academiassu.R;

import org.json.JSONObject;

import java.util.List;

public class EventAttachmentAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private List<JSONObject> attachmentList;

    public EventAttachmentAdapter(Context context, List<JSONObject> attachmentList) {
        super(context, 0, attachmentList);
        this.context = context;
        this.attachmentList = attachmentList;
    }

    @Override
    public int getCount() {
        return attachmentList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return attachmentList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_assignment_file, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.textViewForAssignmentName);

        JSONObject jsonObject = attachmentList.get(position);

        if (jsonObject.has("documentName")) {
            String documentName = jsonObject.optString("documentName");
            textView.setText(documentName);
        }
        return convertView;
    }
}
