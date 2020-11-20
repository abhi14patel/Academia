package com.serosoft.academiassu.Modules.Event.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Modules.Event.EventDetailsActivity;
import com.serosoft.academiassu.Modules.Event.Models.Events_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseURL;
import com.serosoft.academiassu.Widgets.CircularImage;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>{

    Context context;
    ArrayList<Events_Dto> eventList;

    public EventAdapter(Context context, ArrayList<Events_Dto> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Events_Dto list = eventList.get(position);

        String image = ProjectUtils.getCorrectedString(list.getEventBanner());

        if(!image.equalsIgnoreCase("")) {

            if (image.contains(".")) {
                Integer dot = image.lastIndexOf(".");
                String s1 = image.substring(0, dot) + "_icon";
                String s2 = ".jpg";
                image = BaseURL.BASE_URL + KEYS.ULTIMATE_GALLERY_IMAGE_METHOD + "/" + s1 + s2;
                Picasso.with(context).load(image).placeholder(R.drawable.event_img_small).into(holder.ivEvent);
            }else {
                Picasso.with(context).load(R.drawable.event_img_small).into(holder.ivEvent);
            }
        } else {
            Picasso.with(context).load(R.drawable.event_img_small).into(holder.ivEvent);
        }

        String eventName = ProjectUtils.getCorrectedString(list.getEventName());
        if(!eventName.equalsIgnoreCase("")){

            holder.tvEventName.setText(eventName);
        }

        String eventVanue = ProjectUtils.getCorrectedString(list.getEventVenue());
        if(!eventVanue.equalsIgnoreCase("")){

            holder.tvVenue.setVisibility(View.VISIBLE);
            holder.tvVenue.setText(eventVanue);
        }else{
            holder.tvVenue.setVisibility(View.GONE);
        }

        long startDate = list.getStart_long();
        String date = ProjectUtils.convertTimestampToDate(startDate,context);
        String time = ProjectUtils.convertTimestampToTime(startDate,context);

        if(!date.equalsIgnoreCase(""))
        {
            holder.tvStartDate.setText(date+" "+time);
        }

        long endDate = list.getEnd_long();
        String date1 = ProjectUtils.convertTimestampToDate(endDate,context);
        String time1 = ProjectUtils.convertTimestampToTime(endDate,context);

        Boolean isCompleteDay = list.getIsCompleteDay();
        if (isCompleteDay) {
            long afterAdding1439Mins = startDate + 86340000;
            time1 = ProjectUtils.convertTimestampToTime(afterAdding1439Mins,context);
        }

        if(!date1.equalsIgnoreCase(""))
        {
            holder.tvEndDate.setText(date1+" "+time1);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String admin = list.getConductedBy();
                String name = list.getEventName();
                String venue = list.getEventVenue();
                String description = list.getNotes();
                String startTime = list.getStart();
                String endTime = list.getEnd();
                String eventBanner = list.getEventBanner();
                int eventID = list.getEvent_id();

                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("conductedBy", admin);
                intent.putExtra("eventName", name);
                intent.putExtra("eventVenue", venue);
                intent.putExtra("notes", description);
                intent.putExtra("start", startTime);
                intent.putExtra("end", endTime);
                intent.putExtra("eventBanner", eventBanner);
                intent.putExtra("eventID", eventID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != eventList ? eventList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvEventName,tvStartDate,tvEndDate,tvVenue;
        private LinearLayout linearLayout;
        private CircularImage ivEvent;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvVenue = itemView.findViewById(R.id.tvVenue);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            ivEvent = itemView.findViewById(R.id.ivEvent);
        }
    }
}
