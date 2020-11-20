package com.serosoft.academiassu.Modules.TimeLineView.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineModel implements Parcelable {

    private String mMessage;
    private String mDate;
    private Long mDateInLong;
    private String mColor;
    private String mFaculty;
    private String mRoom;
    private OrderStatus mStatus;

    public TimeLineModel(String mMessage, String mDate, OrderStatus mStatus, String mColor, String mFaculty, String mRoom) {
        this.mMessage = mMessage;
        this.mDate = mDate;
        this.mStatus = mStatus;
        this.mColor = mColor;
        this.mFaculty = mFaculty;
        this.mRoom = mRoom;
    }

    public TimeLineModel(String mMessage, String mDate, Long mDateInLong, OrderStatus mStatus, String mColor, String mFaculty, String mRoom) {
        this.mMessage = mMessage;
        this.mDate = mDate;
        this.mDateInLong = mDateInLong;
        this.mStatus = mStatus;
        this.mColor = mColor;
        this.mFaculty = mFaculty;
        this.mRoom = mRoom;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getFaculty() {
        return mFaculty;
    }

    public String getRoom() {
        return mRoom;
    }

    public String getColor() {
        return mColor;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public Long getDateInLong() {
        return mDateInLong;
    }

    public void setDateInLong(Long dateInLong) {
        this.mDateInLong = dateInLong;
    }

    public OrderStatus getStatus() {
        return mStatus;
    }

    public void setStatus(OrderStatus mStatus) {
        this.mStatus = mStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mMessage);
        dest.writeString(this.mDate);
        dest.writeInt(this.mStatus == null ? -1 : this.mStatus.ordinal());
    }

    protected TimeLineModel(Parcel in) {
        this.mMessage = in.readString();
        this.mDate = in.readString();
        int tmpMStatus = in.readInt();
        this.mStatus = tmpMStatus == -1 ? null : OrderStatus.values()[tmpMStatus];
    }

    public static final Parcelable.Creator<TimeLineModel> CREATOR = new Parcelable.Creator<TimeLineModel>() {
        @Override
        public TimeLineModel createFromParcel(Parcel source) {
            return new TimeLineModel(source);
        }

        @Override
        public TimeLineModel[] newArray(int size) {
            return new TimeLineModel[size];
        }
    };
}
