package com.serosoft.academiassu.Modules.TimeTable;

public class TimeTableDayWiseData {
    private static final TimeTableDayWiseData ourInstance = new TimeTableDayWiseData();
    public String timeTableDayWiseString;

    public static TimeTableDayWiseData getInstance() {
        return ourInstance;
    }

    private TimeTableDayWiseData() {
    }
}
