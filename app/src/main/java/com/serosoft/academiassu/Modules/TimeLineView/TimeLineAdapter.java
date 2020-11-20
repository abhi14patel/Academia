package com.serosoft.academiassu.Modules.TimeLineView;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.serosoft.academiassu.R;

import com.github.vipulasri.timelineview.TimelineView;
import com.serosoft.academiassu.Modules.TimeLineView.model.OrderStatus;
import com.serosoft.academiassu.Modules.TimeLineView.model.Orientation;
import com.serosoft.academiassu.Modules.TimeLineView.model.TimeLineModel;
import com.serosoft.academiassu.Modules.TimeLineView.utils.VectorDrawableUtils;
import com.serosoft.academiassu.Utils.BaseActivity;

import java.util.List;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<TimeLineModel> mFeedList;
    private List<TimeLineModel> mFeedList2;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(List<TimeLineModel> feedList, List<TimeLineModel> feedList2, Orientation orientation, boolean withLinePadding) {
        mFeedList = feedList;
        mFeedList2 = feedList2;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

        if(mOrientation == Orientation.HORIZONTAL) {
            view = mLayoutInflater.inflate(mWithLinePadding ? R.layout.item_timeline_horizontal_line_padding : R.layout.item_timeline_horizontal, parent, false);
        } else {
            view = mLayoutInflater.inflate(mWithLinePadding ? R.layout.item_timeline_line_padding : R.layout.item_timeline, parent, false);
        }

        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        TimeLineModel timeLineModel = mFeedList.get(position);
        TimeLineModel timeLineMode2 = mFeedList2.get(position);

        if(timeLineModel.getStatus() == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.getStatus() == OrderStatus.ACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorBlue));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorBlue));
        }

        if(!timeLineModel.getDate().isEmpty()) {
            holder.mDate.setVisibility(View.VISIBLE);
//            String d1 = DateTimeUtils.parseDateTime(timeLineModel.getDate(), "yyyy-MM-dd'T'HH:mm:ss", "hh:mm a");
//            String d2 = DateTimeUtils.parseDateTime(timeLineMode2.getDate(), "yyyy-MM-dd'T'HH:mm:ss", "hh:mm a");

            String d1 = BaseActivity.getAcademiaTime(timeLineModel.getDateInLong(), mContext);
            String d2 = BaseActivity.getAcademiaTime(timeLineMode2.getDateInLong(), mContext);

            holder.mDate.setText(d1 + " to " + d2);
        }
        else {
            holder.mDate.setVisibility(View.VISIBLE);
            holder.mDate.setText("10:00 to 11:00");
        }

        holder.mMessage.setText(timeLineModel.getMessage());

        holder.mFaculty.setText(timeLineModel.getFaculty());

        holder.mRoom.setText(timeLineModel.getRoom());

        holder.mCardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorLightGray));
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

}
