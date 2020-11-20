package com.serosoft.academiassu.Modules.TimeLineView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.serosoft.academiassu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_timeline_date)
    TextView mDate;
    @BindView(R.id.text_timeline_title)
    TextView mMessage;
    @BindView(R.id.text_timeline_detail_1)
    TextView mFaculty;
    @BindView(R.id.text_timeline_detail_2)
    TextView mRoom;
    @BindView(R.id.card_view)
    CardView mCardView;
    @BindView(R.id.time_marker)
    TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        mTimelineView.initLine(viewType);
    }
}
