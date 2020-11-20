package com.serosoft.academiassu.Modules.Circular;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by opj1 on 23/08/2017.
 */
public class DocumentDetailAdapter extends BaseAdapter {

    List<JSONObject> documentList;
    Context context;

    public DocumentDetailAdapter(Context context, List<JSONObject> documentList) {
        this.documentList = documentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return documentList.size();
    }

    @Override
    public Object getItem(int i) {
        return documentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if(null==convertView){
            convertView= LayoutInflater.from(context).inflate(R.layout.circular_doc_grid_item,viewGroup,false);
        }
        JSONObject circular = documentList.get(i);
        try {
            AppCompatImageView ivAttachment = convertView.findViewById(R.id.ivAttachment);
            TextView tvAttachment = convertView.findViewById(R.id.tvAttachment);

            if(null != circular){
                String pathName = ProjectUtils.getCorrectedString(circular.optString("documentName"));
                if(!pathName.equalsIgnoreCase(""))
                {
                    int index = pathName.lastIndexOf("/");
                    String docFileName = pathName.substring(index + 1);
                    tvAttachment.setText(docFileName);

                    ivAttachment.setImageResource(ProjectUtils.showDocIcon(docFileName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
