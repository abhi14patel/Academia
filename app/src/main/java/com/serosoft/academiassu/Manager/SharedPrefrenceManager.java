package com.serosoft.academiassu.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.serosoft.academiassu.Modules.Attendance.Models.AttendanceCourse_Dto;
import com.serosoft.academiassu.Modules.Exam.Models.ExamResult_Dto;
import com.serosoft.academiassu.Modules.Login.TranslationKeys;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ammarali on 23/06/16.
 */
public class SharedPrefrenceManager
{
    private static final String ACADEMIA_SP = "academia-sp";
    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;
    Context context;

    public SharedPrefrenceManager(Context context)
    {
        this.context = context;
        sp = context.getSharedPreferences(ACADEMIA_SP, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setaccessTokenInSP(String value)
    {
        editor.putString("accessToken", value);
        editor.commit();
    }

    public String getAccessTokenFromKey()
    {
        return sp.getString("accessToken", "");
    }

    public void setTokenTypeInSP(String value)
    {
        editor.putString("tokenType", value);
        editor.commit();
    }

    public String getTokenTypeFromKey()
    {
        return sp.getString("tokenType", "");
    }

    public void setRefreshTokenInSP(String value)
    {
        editor.putString("refreshToken", value);
        editor.commit();
    }

    public String getRefreshTokenFromKey()
    {
        return sp.getString("refreshToken", "");
    }

    public void setTokenScopeInSP(String value)
    {
        editor.putString("tokenScope", value);
        editor.commit();
    }

    public String getTokenScopeFromKey()
    {
        return sp.getString("tokenScope", "");
    }

    public void setUserLoginStatusInSP(Boolean value)
    {
        editor.putBoolean("LoggedIn", value);
        editor.commit();
    }

    public boolean getUserLoginStatusFromKey()
    {
        return sp.getBoolean("LoggedIn", false);
    }

    public void setUserGLoginStatusInSP(Boolean value)
    {
        editor.putBoolean("GoogleLogIn", value);
        editor.commit();
    }

    public boolean getUserGLoginStatusFromKey()
    {
        return sp.getBoolean("GoogleLogIn", false);
    }


    public void setIsParentStatusInSP(Boolean value)
    {
        editor.putBoolean("IsParent", value);
        editor.commit();
    }

    public boolean getIsParentStatusFromKey()
    {
        return sp.getBoolean("IsParent", false);
    }

    public void setIsFacultyStatusInSP(Boolean value)
    {
        editor.putBoolean("IsFaculty", value);
        editor.commit();
    }

    public boolean getIsFacultyStatusFromKey()
    {
        return sp.getBoolean("IsFaculty", false);
    }

    public void setPersonIDInSP(Integer value)
    {
        editor.putInt("PersonID", value);
        editor.commit();
    }

    public int getPersonIDFromKey()
    {
        return sp.getInt("PersonID", -1);
    }

    public void setUserIDInSP(Integer value)
    {
        editor.putInt("userID", value);
        editor.commit();
    }

    public int getUserIDFromKey()
    {
        return sp.getInt("userID", -1);
    }

    public void setParentPersonIDInSP(Integer value)
    {
        editor.putInt("ParentPersonID", value);
        editor.commit();
    }

    public int getParentPersonIDFromKey()
    {
        return sp.getInt("ParentPersonID", -1);
    }


    public void setParentUserIDInSP(Integer value)
    {
        editor.putInt("ParentUserID", value);
        editor.commit();
    }

    public int getParentUserIDFromKey()
    {
        return sp.getInt("ParentUserID", -1);
    }

    public void setParentNameInSP(String value)
    {
        editor.putString("ParentName", value);
        editor.commit();
    }

    public String getParentNameFromKey()
    {
        return sp.getString("ParentName", "");
    }

    public void setUserCodeInSP(String value)
    {
        editor.putString("UserCode", value);
        editor.commit();
    }

    public String getUserCodeFromKey()
    {
        return sp.getString("UserCode", "");
    }

    public void setCurrencySymbolInSP(String value)
    {
        editor.putString("CurrencySymbol", value);
        editor.commit();
    }

    public String getCurrencySymbolFromKey()
    {
        return sp.getString("CurrencySymbol", "");
    }

    public void setDateFormatInSP(String value)
    {
        editor.putString("DateFormat", value);
        editor.commit();
    }

    public String getDateFormatlFromKey()
    {
        return sp.getString("DateFormat", "");
    }

    public void setTimeFormatInSP(String value)
    {
        editor.putString("TimeFormat", value);
        editor.commit();
    }

    public String getTimeFormatlFromKey()
    {
        return sp.getString("TimeFormat", "");
    }

    public void setUserImageURLInSP(String value)
    {
        editor.putString("UserImageURL", value);
        editor.commit();
    }

    public String getUserImageURLFromKey()
    {
        return sp.getString("UserImageURL", "");
    }

    public void setUserImageInSP(String value)
    {
        editor.putString("UserImage", value);
        editor.commit();
    }

    public String getUserImageFromKey()
    {
        return sp.getString("UserImage", "");
    }

    public void setUserNameInSP(String value)
    {
        editor.putString("UserName", value);
        editor.commit();
    }

    public String getUserNameFromKey()
    {
        return sp.getString("UserName", "");
    }

    public void setUserEmailInSP(String value)
    {
        editor.putString("UserEmail", value);
        editor.commit();
    }

    public String getUserEmailFromKey()
    {
        return sp.getString("UserEmail", "");
    }

    public void setUserFirstNameInSP(String value)
    {
        editor.putString("FirstName", value);
        editor.commit();
    }

    public String getUserFirstNameFromKey()
    {
        return sp.getString("FirstName", "");
    }

    public void setUserLastNameInSP(String value)
    {
        editor.putString("LastName", value);
        editor.commit();
    }

    public String getUserLastNameFromKey()
    {
        return sp.getString("LastName", "");
    }

    public void setPasswordInSP(String value)
    {
        editor.putString("Password", value);
        editor.commit();
    }

    public String getPasswordFromKey()
    {
        return sp.getString("Password", "");
    }

    public void setBatchIDInSP(Integer value)
    {
        editor.putInt("batchID", value);
        editor.commit();
    }

    public int getBatchIDFromKey()
    {
        return sp.getInt("batchID", -1);
    }

    public void setAdmissionIDInSP(Integer value)
    {
        editor.putInt("admissionID", value);
        editor.commit();
    }

    public String getAdmissionCodeInSP()
    {
        return sp.getString("admissionCode", "");
    }

    public void setAdmissionCodeInSP(String value)
    {
        editor.putString("admissionCode", value);
        editor.commit();
    }

    public int getAlertCountsFromKey()
    {
        return sp.getInt("alertCounts", -1);
    }

    public void setAlertCountsInSP(Integer value)
    {
        editor.putInt("alertCounts", value);
        editor.commit();
    }

    public int getNotificationCountsFromKey()
    {
        return sp.getInt("notificationCounts", -1);
    }

    public void setNotificationCountsInSP(Integer value)
    {
        editor.putInt("notificationCounts", value);
        editor.commit();
    }

    public int getAdmissionIDFromKey()
    {
        return sp.getInt("admissionID", -1);
    }

    public void setAcademyLocationIDInSP(Integer value)
    {
        editor.putInt("academyLocationID", value);
        editor.commit();
    }

    public int getAcademyLocationIDFromKey()
    {
        return sp.getInt("academyLocationID", -1);
    }

    public void setAcademyLocationNameInSP(String value)
    {
        editor.putString("academyLocationName", value);
        editor.commit();
    }

    public String getAcademyLocationNameInSP()
    {
        return sp.getString("academyLocationName", "");
    }

    public void setPortalIDInSP(Integer value)
    {
        editor.putInt("portalID", value);
        editor.commit();
    }

    public int getPortalIDFromKey()
    {
        return sp.getInt("portalID", -1);
    }

    public void setProgramIDInSP(Integer value)
    {
        editor.putInt("programID", value);
        editor.commit();
    }

    public int getProgramIDFromKey()
    {
        return sp.getInt("programID", -1);
    }

    public void setPeriodIDInSP(Integer value)
    {
        editor.putInt("periodID", value);
        editor.commit();
    }

    public int getPeriodIDFromKey()
    {
        return sp.getInt("periodID", -1);
    }

    public void setSectionIDInSP(Integer value)
    {
        editor.putInt("sectionID", value);
        editor.commit();
    }

    public int getSectionIDFromKey()
    {
        return sp.getInt("sectionID", -1);
    }

    public String getNotificationsFromKey()
    {
        return sp.getString("Notifications", "");
    }

    public void setNotificationsInSP(String value)
    {
        editor.putString("Notifications", value);
        editor.commit();
    }

    public String getAttendanceTypeFromKey()
    {
        return sp.getString("attendanceType", "");
    }

    public void setAttendanceTypeInSP(String value)
    {
        editor.putString("attendanceType", value);
        editor.commit();
    }

    public void setIsCompleteDayStatusInSP(Boolean value)
    {
        editor.putBoolean("IsCompleteDay", value);
        editor.commit();
    }

    public boolean getIsCompleteDayStatusFromKey()
    {
        return sp.getBoolean("IsCompleteDay", false);
    }

    public void setIsCourseLevelStatusInSP(Boolean value)
    {
        editor.putBoolean("IsCourseLevel", value);
        editor.commit();
    }

    public boolean getIsCourseLevelStatusFromKey()
    {
        return sp.getBoolean("IsCourseLevel", false);
    }

    public void setIsMultipleSessionStatusInSP(Boolean value)
    {
        editor.putBoolean("IsMultipleSession", value);
        editor.commit();
    }

    public boolean getIsMultipleSessionStatusFromKey()
    {
        return sp.getBoolean("IsMultipleSession", false);
    }

    public void setAttendanceTypeCountInSP(Integer value)
    {
        editor.putInt("AttendanceTypeCount", value);
        editor.commit();
    }

    public int getAttendanceTypeCountFromKey()
    {
        return sp.getInt("AttendanceTypeCount", -1);
    }

    public String getFirebaseTokenFromKey()
    {
        return sp.getString("FirebaseToken", "");
    }

    public void setFirebaseTokenInSP(String value)
    {
        editor.putString("FirebaseToken", value);
        editor.commit();
    }

    public Long getFirebaseIDFromKey()
    {
        return sp.getLong("FirebaseID", -1);
    }

    public void setFirebaseIDInSP(Long value)
    {
        editor.putLong("FirebaseID", value);
        editor.commit();
    }

    public String getContact1FromKey()
    {
        return sp.getString("Contact1", "");
    }

    public void setContact1InSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("Contact1", value);
        editor.commit();
    }

    public String getContact2FromKey()
    {
        return sp.getString("Contact2", "");
    }

    public void setContact2InSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("Contact2", value);
        editor.commit();
    }

    public String getAddressFromKey()
    {
        return sp.getString("Address", "");
    }

    public void setAddressInSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("Address", value);
        editor.commit();
    }

    public String getDateOfBirthFromKey()
    {
        return sp.getString("DateOfBirth", "");
    }

    public void setDateOfBirthInSP(String value) {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("DateOfBirth", value);
        editor.commit();
    }

    public String getDateOfJoiningFromKey()
    {
        return sp.getString("DateOfJoining", "");
    }

    public void setDateOfJoiningInSP(String value)

    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("DateOfJoining", value);
        editor.commit();
    }

    public String getDateOfRegistrationFromKey()
    {
        return sp.getString("DateOfRegistration", "");
    }

    public void setDateOfRegistrationInSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("DateOfRegistration", value);
        editor.commit();
    }

    public Long getDateOfRegistrationLongFromKey()
    {
        return sp.getLong("DateOfRegistrationLong", 0);
    }

    public void setDateOfRegistrationLongInSP(Long value)
    {
        editor.putLong("DateOfRegistrationLong", value);
        editor.commit();
    }

    public Long getDateOfBirthLongFromKey()
    {
        return sp.getLong("DateOfBirthLong", 0);
    }

    public void setDateOfBirthLongInSP(Long value)
    {
        editor.putLong("DateOfBirthLong", value);
        editor.commit();
    }

    public String getStudentCategoryFromKey()
    {
        return sp.getString("StudentCategory", "");
    }

    public void setStudentCategoryInSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("StudentCategory", value);
        editor.commit();
    }

    public String getStudentGenderFromKey()
    {
        return sp.getString("StudentGender", "");
    }

    public void setStudentGenderInSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("StudentGender", value);
        editor.commit();
    }

    public String getStudentFathersNameFromKey()
    {
        return sp.getString("StudentFathersName", " - ");
    }

    public void setStudentFathersNameInSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("StudentFathersName", value);
        editor.commit();
    }

    public String getStudentMothersNameFromKey()
    {
        return sp.getString("StudentMothersName", " - ");
    }

    public void setStudentMothersNameInSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("StudentMothersName", value);
        editor.commit();
    }

    public String getStudentReligionFromKey()
    {
        return sp.getString("StudentReligion", "");
    }

    public void setStudentReligionInSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("StudentReligion", value);
        editor.commit();
    }

    public String getStudentCasteFromKey()
    {
        return sp.getString("StudentCaste", "");
    }

    public void setStudentCasteInSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("StudentCaste", value);
        editor.commit();
    }

    public String getStudentBloodGroupFromKey()
    {
        return sp.getString("StudentBloodGroup", "");
    }

    public void setStudentBloodGroupInSP(String value)
    {
        if (value.equals("null") || value.length() == 0)
            value = " - ";
        editor.putString("StudentBloodGroup", value);
        editor.commit();
    }

    public String getAcademyTypeFromKey()
    {
        return sp.getString("academyType", "defualt");
    }

    public void setAcademyTypeInSP(String value)
    {
        editor.putString("academyType", value);
        editor.commit();
    }

    public String getProgramIdsFromKey()
    {
        return sp.getString("programIds", "defualt");
    }

    public void setProgramIdsInSP(String value)
    {
        editor.putString("programIds", value);
        editor.commit();
    }

    public int getActivityInfoByKey(String key)
    {
        return sp.getInt(key, 0);
    }

    public void setModulePermissionList(String Tag, ArrayList<Integer> lst)
    {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        editor.putString(Tag, json);
        editor.commit();
    }

    public ArrayList<Integer> getModulePermissionList(String Tag)
    {
        String obj = sp.getString(Tag, "defValue");
        if (obj.equals("defValue"))
        {
            return new ArrayList<Integer>();
        }
        else
        {
            Type type = new TypeToken<ArrayList<Integer>>()
            {}.getType();
            Gson gson = new Gson();
            ArrayList<Integer> List = gson.fromJson(obj, type);
            return List;
        }
    }

    public void clearPreferences(String key)
    {
        editor.remove(key);
        editor.commit();
    }

    public void setTranslationKeyList(String Tag, ArrayList<TranslationKeys> lst)
    {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        editor.putString(Tag, json);
        editor.commit();
    }

    public ArrayList<TranslationKeys> getTranslationKeyList(String Tag)
    {
        String obj = sp.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<TranslationKeys>();
        } else {
            Type type = new TypeToken<ArrayList<TranslationKeys>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<TranslationKeys> List = gson.fromJson(obj, type);
            return List;
        }
    }

    public void setCurrentVersionSP(String value)
    {
        editor.putString("CurrentVersion", value);
        editor.commit();
    }

    public String getCurrentVersionFromKey()
    {
        return sp.getString("CurrentVersion", "");
    }

    public void setExamResult_Dto(String Tag, ExamResult_Dto lst)
    {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        editor.putString(Tag, json);
        editor.commit();
    }

    public ExamResult_Dto getExamResult_Dto(String Tag)
    {
        String obj = sp.getString(Tag, "defValue");
        if (obj.equals("defValue"))
        {
            return new ExamResult_Dto();
        } else
        {
            Type type = new TypeToken<ExamResult_Dto>(){}.getType();
            Gson gson = new Gson();
            ExamResult_Dto List = gson.fromJson(obj, type);
            return List;
        }
    }

    public void setAttendanceCourse_Dto(String Tag, AttendanceCourse_Dto lst)
    {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        editor.putString(Tag, json);
        editor.commit();
    }

    public AttendanceCourse_Dto getAttendanceCourse_Dto(String Tag)
    {
        String obj = sp.getString(Tag, "defValue");
        if (obj.equals("defValue"))
        {
            return new AttendanceCourse_Dto();
        } else
        {
            Type type = new TypeToken<AttendanceCourse_Dto>(){}.getType();
            Gson gson = new Gson();
            AttendanceCourse_Dto List = gson.fromJson(obj, type);
            return List;
        }
    }

    public String getVersionFromKey()
    {
        return sp.getString("versions", "");
    }

    public void setVersionInSP(String value)
    {
        editor.putString("versions", value);
        editor.commit();
    }

    public void setRequestIdList(String Tag, ArrayList<String> lst)
    {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        editor.putString(Tag, json);
        editor.commit();
    }

    public ArrayList<String> getRequestIdList(String Tag)
    {
        String obj = sp.getString(Tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<String>();
        } else {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<String> List = gson.fromJson(obj, type);
            return List;
        }
    }

    public void setIntValue(String Tag, int value)
    {
        editor.putInt(Tag, value);
        editor.apply();
    }

    public int getIntValue(String Tag)
    {
        return sp.getInt(Tag, 0);
    }

    public void setStringValue(String Tag, String token)
    {
        editor.putString(Tag, token);
        editor.commit();
    }

    public String getStringValue(String Tag)
    {
        return sp.getString(Tag, "");
    }
}