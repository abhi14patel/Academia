package com.serosoft.academiassu.Modules.Attendance.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Widgets.CircularTextView;
import com.serosoft.academiassu.Networking.KEYS;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MultipleCalendarAdapter extends BaseAdapter {
    private Context context;

    private Calendar month;
    private GregorianCalendar pmonth;

    private String curentDateString;
    private DateFormat df;
    public String attendanceType;

    private static List<String> day_string;
    private List<JSONObject>  attendanceList;

    public MultipleCalendarAdapter(Context context, GregorianCalendar monthCalendar, List<JSONObject> attendanceList, String at) {
        this.attendanceList=attendanceList;
        this.attendanceType = at;
        MultipleCalendarAdapter.day_string = new ArrayList<>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        GregorianCalendar selectedDate = (GregorianCalendar) monthCalendar.clone();
        this.context = context;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);

        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    public int getCount() {
        return day_string.size();
    }

    public Object getItem(int position) {
        return day_string.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (attendanceType.equals(KEYS.COURSE_LEVEL)) {
            View v = convertView;
            CircularTextView dayView;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.cal_item2, null);
            }

            dayView = v.findViewById(R.id.date);
            String[] separatedTime = day_string.get(position).split("-");
            String gridvalue = separatedTime[2].replaceFirst("^0*", "");
            dayView.setTextColor(Color.BLACK);

            if (isItToday(day_string.get(position)) && isTheDateInThisMonth(day_string.get(position)) ) {
                dayView.setTextColor(context.getResources().getColor(R.color.colorForCurrentDate));
            } else {
                dayView.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            dayView.setText(gridvalue);

            String date = day_string.get(position);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parse;
            try {
                //Fading past and next dates:
                parse = sdf.parse(date);
                Calendar c = Calendar.getInstance();
                c.setTime(parse);
                int m1 = c.get(Calendar.MONTH);
                int m2 = month.get(Calendar.MONTH);
                if (m1 != m2) {
                    dayView.setTextColor(context.getResources().getColor(R.color.colorSlateGray));
                }

                //Fading dates past the current date:
//                if (date.compareTo(curentDateString) > 0) {
//                    System.out.println("Date is after Date2");
//                    dayView.setTextColor(context.getResources().getColor(R.color.colorSlateGray));
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date.length() == 1) {
            }
            String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
            if (monthStr.length() == 1) {
            }
            setEventView(v, position,dayView);
            return v;

        } else if (attendanceType.equals(KEYS.MULTIPLE_SESSION)) {
            View v = convertView;
            CircularTextView dayView;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.cal_item2, null);
            }

            dayView = v.findViewById(R.id.date);
            String[] separatedTime = day_string.get(position).split("-");
            String gridvalue = separatedTime[2].replaceFirst("^0*", "");
            dayView.setTextColor(Color.BLACK);

            if (isItToday(day_string.get(position)) && isTheDateInThisMonth(day_string.get(position)) ) {
                dayView.setTextColor(context.getResources().getColor(R.color.colorForCurrentDate));
            } else {
                dayView.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            dayView.setText(gridvalue);

            String date = day_string.get(position);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parse;
            try {
                //Fading past and next dates:
                parse = sdf.parse(date);
                Calendar c = Calendar.getInstance();
                c.setTime(parse);
                int m1 = c.get(Calendar.MONTH);
                int m2 = month.get(Calendar.MONTH);
                if (m1 != m2) {
                    dayView.setTextColor(context.getResources().getColor(R.color.colorSlateGray));
                }

                //Fading dates past the current date:
//                if (date.compareTo(curentDateString) > 0) {
//                    System.out.println("Date is after Date2");
//                    dayView.setTextColor(context.getResources().getColor(R.color.colorSlateGray));
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date.length() == 1) {
            }
            String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
            if (monthStr.length() == 1) {
            }
            setEventView(v, position,dayView);
            return v;

        } else {
            //COMPLETE_DAY:

            View v = convertView;
            CircularTextView dayView;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.cal_item, null);
            }

            dayView = v.findViewById(R.id.date);
            String[] separatedTime = day_string.get(position).split("-");
            String gridvalue = separatedTime[2].replaceFirst("^0*", "");
            dayView.setTextColor(Color.BLACK);

            if (isItToday(day_string.get(position)) && isTheDateInThisMonth(day_string.get(position)) ) {
                v.setBackgroundResource(R.drawable.rounded_corner_attendance);
                GradientDrawable drawable = (GradientDrawable) v.getBackground();
                drawable.setColor(context.getResources().getColor(R.color.colorForCurrentDate));
                dayView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            } else {
                dayView.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            dayView.setText(gridvalue);

            String date = day_string.get(position);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parse;
            try {
                //Fading past and next dates:
                parse = sdf.parse(date);
                Calendar c = Calendar.getInstance();
                c.setTime(parse);
                int m1 = c.get(Calendar.MONTH);
                int m2 = month.get(Calendar.MONTH);
                if (m1 != m2) {
                    dayView.setTextColor(context.getResources().getColor(R.color.colorSlateGray));
                }

                //Fading dates past the current date:
//                if (date.compareTo(curentDateString) > 0) {
//                    System.out.println("Date is after Date2");
//                    dayView.setTextColor(context.getResources().getColor(R.color.colorSlateGray));
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date.length() == 1) {
            }
            String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
            if (monthStr.length() == 1) {
            }
            setEventView(v, position,dayView);
            return v;
        }
    }

    public void refreshDays() {
        day_string.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        int firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        int maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        int mnthlength = maxWeeknumber * 7;
        int maxP = getMaxP();
        int calMaxP = maxP - (firstDay - 1);
        GregorianCalendar pmonthmaxset = (GregorianCalendar) pmonth.clone();
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        for (int n = 0; n < mnthlength; n++) {
            String itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);
        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        return maxP;
    }

    public void setEventView(View v, final int pos, CircularTextView txt){

        int len=attendanceList.size();
        for (int i = 0; i < len; i++) {

            try {
                if (attendanceType.equals(KEYS.COMPLETE_DAY) ) {
                    JSONObject attendance = attendanceList.get(i);
//                    Long dateOfAttendance = attendance.getLong("dateOfAttendance");
                    Long dateOfAttendance = attendance.getLong("date_long");
                    Date dd = new Date(dateOfAttendance);
                    String date = BaseActivity.stringFromDate("yyyy-MM-dd", dd);
//                    Integer attendanceMarkingTypeId = attendance.getInt("attendanceMarkingTypeId");
                    String attendanceStatus = attendance.getString("attendanceStatus");
                    int len1 = day_string.size();
                    if (len1 > pos) {
                        if (day_string.get(pos).equals(date) && (isTheDateInThisMonth(day_string.get(pos)))) {
                            v.setBackgroundColor(Color.parseColor("#ffffff"));
                            if (attendanceStatus.equalsIgnoreCase("Present")) {
                                v.setBackgroundResource(R.drawable.rounded_corner_attendance);
                                GradientDrawable drawable = (GradientDrawable) v.getBackground();
                                drawable.setColor(context.getResources().getColor(R.color.colorForPresentDate));
                                txt.setTextColor(Color.WHITE);
                            }
                            else if (attendanceStatus.equalsIgnoreCase("Absent")) {
                                v.setBackgroundResource(R.drawable.rounded_corner_attendance);
                                GradientDrawable drawable = (GradientDrawable) v.getBackground();
                                drawable.setColor(context.getResources().getColor(R.color.colorRed));
                                txt.setTextColor(Color.WHITE);
                            } else  {
                                v.setBackgroundResource(R.drawable.rounded_corner_attendance);
                                GradientDrawable drawable = (GradientDrawable) v.getBackground();
                                drawable.setColor(context.getResources().getColor(R.color.colorYellow));
                                txt.setTextColor(Color.WHITE);
                            }
                        }
                    }

                } else if ( attendanceType.equals(KEYS.MULTIPLE_SESSION)) {

                    JSONObject attendance = attendanceList.get(i);
                    String dateOfAttendance = attendance.getString("date");
                    String date = dateOfAttendance;
                    String attendanceStatus = attendance.getString("attendanceStatus");
                    String courseName = attendance.getString("courseName");

                    View dotForPresent = v.findViewById(R.id.ViewForPresentDot);
                    View dotForAbsent = v.findViewById(R.id.ViewForAbsentDot);
                    View dotForOther = v.findViewById(R.id.ViewForOtherDot);

                    int len1 = day_string.size();
                    if (len1 > pos) {
                        if (day_string.get(pos).equals(date) && (isTheDateInThisMonth(day_string.get(pos)))) {
                            v.setBackgroundColor(Color.parseColor("#ffffff"));
                            if (attendanceStatus.equalsIgnoreCase("Present")) {
                                dotForPresent.setVisibility(View.VISIBLE);
                            }
                            else if (attendanceStatus.equalsIgnoreCase("Absent")) {
                                dotForAbsent.setVisibility(View.VISIBLE);
                            } else  {
                                dotForOther.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    final int len2 = len1;
                    final int pos2 = pos;
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int presentForThisDay = 0;
                            int absentForThisDay = 0;
                            int otherForThisDay = 0;

                            for ( int i = 0 ; i < attendanceList.size() ; ++i ) {
                                JSONObject attendance = attendanceList.get(i);
                                String dateOfAttendance = attendance.optString("date");
                                String date = dateOfAttendance;
                                String attendanceStatus = attendance.optString("attendanceStatus");

                                if (len2 > pos2) {
                                    if (day_string.get(pos2).equals(date) && (isTheDateInThisMonth(day_string.get(pos2)))) {
                                        v.setBackgroundColor(Color.parseColor("#ffffff"));
                                        if (attendanceStatus.equalsIgnoreCase("Present")) {
                                            ++presentForThisDay;
                                        }
                                        else if (attendanceStatus.equalsIgnoreCase("Absent")) {
                                            ++absentForThisDay;
                                        } else  {
                                            ++otherForThisDay;
                                        }
                                    }
                                }
                            }

                            List<JSONObject>  attendanceListForThisDate = new ArrayList<>();
                            String tappedDate = BaseActivity.getAcademiaDate("yyyy-MM-dd", day_string.get(pos2), context);

                            for (int i = 0 ; i < attendanceList.size() ; ++i) {
                                JSONObject object = attendanceList.get(i);
//                                Integer dateSelectedInt = object.optInt("date_long");
                                String dateSelected = object.optString("date");
                                if (dateSelected.equals(day_string.get(pos2))) {
                                    attendanceListForThisDate.add(object);
                                }
                            }

                            BaseActivity baseActivity = (BaseActivity) context;
                            baseActivity.showMultipleAttendanceDialog(context, attendanceListForThisDate, tappedDate, courseName, attendanceType);
                        }
                    });

                } else {
                    //COURSE_LEVEL:

                    JSONObject attendance = attendanceList.get(i);
                    String dateOfAttendance = attendance.getString("date");
                    String date = dateOfAttendance;
                    String attendanceStatus = attendance.getString("attendanceStatus");
                    String courseName = attendance.getString("courseName");

                    View dotForPresent = v.findViewById(R.id.ViewForPresentDot);
                    View dotForAbsent = v.findViewById(R.id.ViewForAbsentDot);
                    View dotForOther = v.findViewById(R.id.ViewForOtherDot);

                    int len1 = day_string.size();
                    if (len1 > pos) {
                        if (day_string.get(pos).equals(date) && (isTheDateInThisMonth(day_string.get(pos)))) {
                            v.setBackgroundColor(Color.parseColor("#ffffff"));
                            if (attendanceStatus.equalsIgnoreCase("Present")) {
                                dotForPresent.setVisibility(View.VISIBLE);
                            }
                            else if (attendanceStatus.equalsIgnoreCase("Absent")) {
                                dotForAbsent.setVisibility(View.VISIBLE);
                            } else  {
                                dotForOther.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    final int len2 = len1;
                    final int pos2 = pos;
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int presentForThisDay = 0;
                            int absentForThisDay = 0;
                            int otherForThisDay = 0;

                            for ( int i = 0 ; i < attendanceList.size() ; ++i ) {
                                JSONObject attendance = attendanceList.get(i);
                                String dateOfAttendance = attendance.optString("date");
                                String date = dateOfAttendance;
                                String attendanceStatus = attendance.optString("attendanceStatus");

                                if (len2 > pos2) {
                                    if (day_string.get(pos2).equals(date) && (isTheDateInThisMonth(day_string.get(pos2)))) {
                                        v.setBackgroundColor(Color.parseColor("#ffffff"));
                                        if (attendanceStatus.equalsIgnoreCase("Present")) {
                                            ++presentForThisDay;
                                        }
                                        else if (attendanceStatus.equalsIgnoreCase("Absent")) {
                                            ++absentForThisDay;
                                        } else  {
                                            ++otherForThisDay;
                                        }
                                    }
                                }
                            }
                            String tappedDate = BaseActivity.getAcademiaDate("yyyy-MM-dd", day_string.get(pos2), context);
                            List<JSONObject>  attendanceListForThisDate = new ArrayList<>();

                            for (int i = 0 ; i < attendanceList.size() ; ++i) {
                                JSONObject object = attendanceList.get(i);
                                String dateSelected = object.optString("date");
                                if (dateSelected.equals(day_string.get(pos2))) {
                                    attendanceListForThisDate.add(object);
                                }
                            }

                            BaseActivity baseActivity = (BaseActivity) context;
                            baseActivity.showMultipleAttendanceDialog(context, attendanceListForThisDate, tappedDate, courseName, attendanceType);
                        }
                    });
                }
            }
            catch (JSONException e) { e.printStackTrace(); }
        }
    }

    public boolean isTheDateInThisMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(parse);
            int m1 = c.get(Calendar.MONTH);
            int m2 = month.get(Calendar.MONTH);
            if (m1 != m2) {
                return false;
            } else {
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public View setSelected(View view,int pos) {
        view.setEnabled(false);
        return view;
    }

    public boolean isItToday(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todaysDate = df.format(c);

        if (date.equals(todaysDate)) {
            return true;
        } else {
            return false;
        }
    }

}