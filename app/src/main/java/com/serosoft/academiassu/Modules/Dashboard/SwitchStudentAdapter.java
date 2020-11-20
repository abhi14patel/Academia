package com.serosoft.academiassu.Modules.Dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Widgets.CircularImage;
import com.serosoft.academiassu.Networking.KEYS;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by opj1 on 26/08/2016.
 */
public class SwitchStudentAdapter extends ArrayAdapter<JSONObject> {
    Context context;
    List<JSONObject> switchStudentList;

    public SwitchStudentAdapter(Context context, List<JSONObject> arrayList, String callBy) {
        super(context, 0, arrayList);
        this.context = context;
        this.switchStudentList = arrayList;
    }

    @Override
    public int getCount() {
        return switchStudentList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return switchStudentList.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.switch_student_item, parent, false);
        }

        TextView studentName = convertView.findViewById(R.id.studentName);
        CircularImage studentImage = convertView.findViewById(R.id.studentImage);
        ImageView greenTick = convertView.findViewById(R.id.greenTick);

        JSONObject jsonObject = getItem(position);

        try {
            String name = jsonObject.optString("studentCodeAndPrintName");
            studentName.setText(name);
            String studentPhotoURL = jsonObject.optString("photoUrl");
            studentPhotoURL = BaseURL.BASE_URL + KEYS.ULTIMATE_GALLERY_IMAGE_METHOD + "/" + studentPhotoURL;
            Picasso.with(context).load(studentPhotoURL).placeholder(R.drawable.user_icon1).into(studentImage);
            Boolean isLoggedInStudent = jsonObject.optBoolean("isLoggedInStudent");
            if (isLoggedInStudent) {
                greenTick.setVisibility(View.VISIBLE);
            } else {
                greenTick.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

}
