package com.serosoft.academiassu.Utils;

import org.json.JSONObject;

import java.util.Comparator;

public class JSONComparator implements Comparator<JSONObject> {
    public int compare(JSONObject a, JSONObject b)
    {
        try {
//            String stringForDateA = a.optString("start");
//            String stringForDateB = b.optString("start");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            Date dateA = sdf.parse(stringForDateA);
//            Date dateB = sdf.parse(stringForDateB);
//            if ( dateA.before(dateB) ) {
//                return -1;
//            } else {
//                return 1;
//            }
            Long longForDateA = a.optLong("startDate_long");
            Long longForDateB = b.optLong("startDate_long");
            if ( longForDateA < longForDateB ) {
                return -1;
            } else {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}