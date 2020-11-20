package com.serosoft.academiassu.Modules.Dashboard;

import android.content.Context;

import com.serosoft.academiassu.Modules.Dashboard.Models.ModuleData;
import com.serosoft.academiassu.R;

import java.util.ArrayList;
import java.util.Collections;

public class DashboardSort {

    public static Integer containsMyCourses(ArrayList<ModuleData> array) {

        for (int i = 0 ; i < array.size() ; ++i) {
            ModuleData md = array.get(i);
            if (md.getId() == 9558 || md.getId() == 9559) {
                return i;
            }
        }
        return -1;
    }

    public static ArrayList<ModuleData> trySettingMyCoursesOnTop(ArrayList<ModuleData> array) {
        ArrayList<ModuleData> returnableArray = array;

        Integer myCoursesIndex = containsMyCourses(array);
        if (myCoursesIndex != -1) {
            ModuleData myCourses = array.get(myCoursesIndex);
            returnableArray.remove(myCourses);
            returnableArray.add(0,myCourses);
            if (consecutiveColorFoundAt(returnableArray) == -1) {
                return returnableArray;
            } else {
                return array;
            }
        }
        return array;
    }

    public static Integer consecutiveColorFoundAt(ArrayList<ModuleData> array) {

        for (int i = 0 ; i < (array.size() - 1) ; ++i) {
            ModuleData data1 = array.get(i);
            ModuleData data2 = array.get(i + 1);
            int color1 = data1.getColor();
            int color2 = data2.getColor();
            if (color1 == color2) {
                return i + 1;
            }
        }
        return -1;
    }

    public static ArrayList<ModuleData> swapD(ArrayList<ModuleData> array, int index) {
        ArrayList<ModuleData> returnableArray = array;
        if (index != (array.size() - 1)) {
            Collections.swap(returnableArray, index, index + 1);
        } else {
            Collections.swap(returnableArray, index, 0);
        }
        return returnableArray;
    }

    public static ArrayList<ModuleData> order(ArrayList<ModuleData> array) {
        Integer consecutiveColorIndex = consecutiveColorFoundAt(array);
        if (consecutiveColorIndex == -1) {
            return trySettingMyCoursesOnTop(array);
        } else {
            return trySettingMyCoursesOnTop(order(swapD(array, consecutiveColorIndex)));
        }
    }

    public static ArrayList<ModuleData> sortColors(Context context, ArrayList<ModuleData> array) {

        //initializing variables:
        int maxModules = 9;
        ArrayList<ModuleData> returnableArray = array;
        int numberOfReds = 0;

        ArrayList<Integer> arrayOfColors = new ArrayList<>();
        for (int i = 0 ; i < array.size() ; ++i) {
            arrayOfColors.add(array.get(i).getColor());
            if (array.get(i).getColor() == context.getResources().getColor(R.color.colorMyCourse)) {
                ++numberOfReds;
            } else if (array.get(i).getColor() == context.getResources().getColor(R.color.colorAssignment)) {
                ++numberOfReds;
            } else if (array.get(i).getColor() == context.getResources().getColor(R.color.colorDrive)) {
                ++numberOfReds;
            }
        }

        if (returnableArray.size() < 3 || returnableArray.size() == maxModules) {
            return returnableArray;
        }

        if ((returnableArray.size() == 3 || returnableArray.size() == 4) && numberOfReds == 3) {
            return returnableArray;
        }

        returnableArray = order(returnableArray);

        return returnableArray;
    }

    public static ArrayList<ModuleData> sortColors(Context context, ArrayList<ModuleData> fullArray, ArrayList<ModuleData> finalArray) {
        ArrayList<ModuleData> returnableArray = new ArrayList<>();

        for (int i = 0; i < fullArray.size() ; ++ i) {
            ModuleData data1 = fullArray.get(i);
            if (finalArray.contains(data1)) {
                returnableArray.add(data1);
            }
        }

        return returnableArray;
    }

    }
