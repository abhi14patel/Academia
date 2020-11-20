package com.serosoft.academiassu.Manager;

import android.content.Context;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Modules.Exam.Models.ExamResult_Dto;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Networking.ServiceCalls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ammarali on 22/05/16.
 */
public class SwitchStudentManager extends BaseClass {
    Context context;
    private SharedPrefrenceManager sharedPrefrenceManager;
    private ServiceCalls serverCalls;
    private HashMap<String, String> hashMap;
    private JSONObject responseObject;


    public SwitchStudentManager(Context context) {
        super(context);
        this.context = context;
        sharedPrefrenceManager = new SharedPrefrenceManager(context);
        serverCalls = new ServiceCalls();
    }

    public boolean getAssociatedStudentsFromServer() {

        Integer parentPersonID = sharedPrefrenceManager.getParentPersonIDFromKey();

        hashMap = new HashMap<>();
        hashMap.put("personId", parentPersonID.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ASSOCIATED_STUDENTS);

        if (responseObject != null) {
            return saveAssociatedStudentsInfo(responseObject);
        }
        return false;
    }

    private boolean saveAssociatedStudentsInfo(JSONObject responseObject) {

        try {
            JSONArray array = responseObject
                    .getJSONArray("whatever");
            JSONObject userObject = array.getJSONObject(0);

            if (userObject.has("id")) {
                sharedPrefrenceManager.setUserIDInSP(userObject.optInt("id"));
            }
            if (userObject.has("code")) {
                sharedPrefrenceManager.setUserCodeInSP(userObject.optString("code"));
            }
            if (userObject.has("emailId")) {
                sharedPrefrenceManager.setUserEmailInSP(userObject.optString("emailId"));
            }
            if (userObject.has("firstName")) {
                sharedPrefrenceManager.setUserFirstNameInSP(userObject.optString("firstName"));
            }
            if (userObject.has("lastName")) {
                String lastName = userObject.optString("lastName");
                if ((lastName.length() > 0 && !lastName.equals("null"))) {
                    sharedPrefrenceManager.setUserLastNameInSP(userObject.optString("lastName"));
                } else {
                    sharedPrefrenceManager.setUserLastNameInSP("");
                }
            }

            JSONObject personObject = userObject.optJSONObject("person");
            Integer personID = personObject.optInt("id");
            sharedPrefrenceManager.setPersonIDInSP(personID);

            return getStudentAcademicDetailsFromServer();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean getStudentAcademicDetailsFromServer() {

        Integer uid = sharedPrefrenceManager.getUserIDFromKey();

        hashMap = new HashMap<>();
        hashMap.put("studentId", uid.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_ACADEMIC_DETAILS);

        if (responseObject != null) {
            return saveAcademicDetailsInfo(responseObject);
        }
        return false;
    }

    private boolean saveAcademicDetailsInfo(JSONObject responseObject) {

        try {
            JSONArray array = responseObject
                    .getJSONArray("whatever");
            JSONObject obj = array.getJSONObject(0);
            JSONArray array2 = obj
                    .getJSONArray("admissionDetails");
            JSONObject obj2 = array2.getJSONObject(0);
            Integer admissionId = obj2.getInt("id");
            String admissionCode = obj2.getString("code");
            Long admissionDate = obj2.getLong("admissionDate");

            Date date=new Date(admissionDate);
            SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
            String dateText1 = df1.format(date);

            JSONObject obj3 = obj2.optJSONObject("academy");
            String academyName = obj3.optString("value");
            sharedPrefrenceManager.setAcademyLocationNameInSP(academyName);

            JSONObject obj4 = obj.getJSONObject("programBatchDetails");
            Integer programId = obj4.optInt("programId");
            String programName = obj4.optString("programName");
            Integer periodId = obj4.optInt("periodId");
            String periodName = obj4.optString("periodName");
            String periodPrintName = obj4.optString("periodPrintName");
            Integer sectionId = obj4.optInt("sectionId");
            Integer batchId = obj4.optInt("batchId");
            String batchName = obj4.optString("batchName");
            String batchPrintName = obj4.optString("batchPrintName");

            //Save the info in Shared Preferences like this:
            sharedPrefrenceManager.setBatchIDInSP(batchId);
            sharedPrefrenceManager.setAdmissionIDInSP(admissionId);
            sharedPrefrenceManager.setAdmissionCodeInSP(admissionCode);
            sharedPrefrenceManager.setProgramIDInSP(programId);
            sharedPrefrenceManager.setPeriodIDInSP(periodId);
            sharedPrefrenceManager.setSectionIDInSP(sectionId);

            sharedPrefrenceManager.setDateOfJoiningInSP(dateText1);
            sharedPrefrenceManager.setDateOfRegistrationInSP(dateText1);

            //Here story current program batch period id and name in sp
            ExamResult_Dto examResult_dto = new ExamResult_Dto();
            examResult_dto.setProgramName(programName);
            examResult_dto.setProgramId(String.valueOf(programId));
            examResult_dto.setBatch(batchName);
            examResult_dto.setBatchPrintName(batchPrintName);
            examResult_dto.setBatchId(String.valueOf(batchId));
            examResult_dto.setPeriod(periodName);
            examResult_dto.setPeriodPrintName(periodPrintName);
            examResult_dto.setPeriodId(String.valueOf(periodId));
            sharedPrefrenceManager.setExamResult_Dto(Consts.EXAM_RESULT,examResult_dto);

            //Here add current program id
            ArrayList<String> programIdList = new ArrayList<>();
            programIdList.add(String.valueOf(programId));

            //Here get past program id
            JSONArray jsonArray = obj.optJSONArray("pastProgramBatchDetails");
            for(int i = 0 ; i<jsonArray.length() ; i++){
                JSONObject obj5 = jsonArray.optJSONObject(i);

                String proId = String.valueOf(obj5.getInt("programId"));

                if (!programIdList.contains(proId)) {

                    programIdList.add(proId);
                }
            }

            // Comman Seprated format program ids
            String programIds = programIdList.toString().replace("[", "").replace("]", "").replace(", ", ",");
            sharedPrefrenceManager.setProgramIdsInSP(programIds);

            return getStudentPersonalDetailsFromServer();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean getStudentPersonalDetailsFromServer() {

        Integer uid = sharedPrefrenceManager.getUserIDFromKey();

        hashMap = new HashMap<>();
        hashMap.put("studentId", uid.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_PERSONAL_DETAILS);

        if (responseObject != null) {
            return savePersonalDetailsInfo(responseObject);
        }
        return false;
    }

    private boolean savePersonalDetailsInfo(JSONObject responseObject) {

        try {
            JSONObject userObject = responseObject
                    .getJSONObject("person");
            if (userObject.has("mobileNumber") && userObject.optString("mobileNumber") != "null")  {
                String contact = userObject.getString("mobileNumber");
                if (userObject.has("mobileCountryCode") && userObject.optString("mobileCountryCode") != "null") {
                    contact =  userObject.getString("mobileCountryCode")+ "-" + contact;
                }
                sharedPrefrenceManager.setContact1InSP(contact);
            }else{
                sharedPrefrenceManager.setContact1InSP("");
            }

            if (userObject.has("phoneNo") && userObject.optString("phoneNo") != "null") {

                String contact = userObject.getString("phoneNo");
                if (userObject.has("phoneAreaCode") && userObject.optString("phoneAreaCode") != "null") {
                    contact =  userObject.getString("phoneAreaCode")+ "-" + contact;
                    if (userObject.has("phoneCountryCode") && userObject.optString("phoneCountryCode") != "null") {
                        contact =  userObject.getString("phoneCountryCode")+ "-" + contact;
                    }
                }
                sharedPrefrenceManager.setContact2InSP(contact);
            }else{
                sharedPrefrenceManager.setContact2InSP("");
            }

            if (userObject.has("emailId")) {
                sharedPrefrenceManager.setUserEmailInSP(userObject.getString("emailId"));
            }
            if (userObject.has("category")) {
                sharedPrefrenceManager.setStudentCategoryInSP(userObject.getString("category"));
            }
            if (userObject.has("castCategory")) {
                if (!userObject.isNull("castCategory")) {
                    JSONObject jsonObject = userObject.optJSONObject("castCategory");
                    String caste = jsonObject.optString("value");
                    sharedPrefrenceManager.setStudentCasteInSP(caste);
                } else {
                    sharedPrefrenceManager.setStudentCasteInSP("");
                }
            }else {
                sharedPrefrenceManager.setStudentCasteInSP("");
            }

            if (userObject.has("genderCSM")) {
                if(!userObject.isNull("genderCSM")){
                    JSONObject jsonObject = userObject.optJSONObject("genderCSM");
                    String value = jsonObject.optString("value");
                    sharedPrefrenceManager.setStudentGenderInSP(value);
                }
            }else if (userObject.has("gender")) {
                sharedPrefrenceManager.setStudentGenderInSP(userObject.getString("gender"));
            }

            if (userObject.has("bloodGroup")) {
                sharedPrefrenceManager.setStudentBloodGroupInSP(userObject.optString("bloodGroup"));
            }
            if (userObject.has("religion")) {
                if (!userObject.isNull("religion")) {
                    JSONObject jsonObject = userObject.optJSONObject("religion");
                    String religion = jsonObject.optString("value");
                    sharedPrefrenceManager.setStudentReligionInSP(religion);
                } else {
                    sharedPrefrenceManager.setStudentReligionInSP("");
                }
            }
            if (userObject.has("dateOfBirth")) {
                sharedPrefrenceManager.setDateOfBirthInSP(userObject.optString("dateOfBirth"));
            }

            return getStudentPersonalDetails2FromServer();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean getStudentPersonalDetails2FromServer() {

        hashMap = new HashMap<>();
        hashMap.put("page", String.valueOf(1) );
        hashMap.put("start", String.valueOf(0) );
        hashMap.put("limit", String.valueOf(-1) );

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_PERSONAL_DETAILS2);

        if (responseObject != null) {
            return savePersonalDetails2Info(responseObject);
        }
        return false;
    }

    private boolean savePersonalDetails2Info(JSONObject responseObject) {

        try {
            JSONArray jsonArray = responseObject.optJSONArray("rows");
            JSONObject userObject = jsonArray.getJSONObject(0);

            if (userObject.has("MOTHERS_FULL_NAME")) {
                sharedPrefrenceManager.setStudentMothersNameInSP(userObject.optString("MOTHERS_FULL_NAME"));
            }
            if (userObject.has("FATHERS_FULL_NAME")) {
                sharedPrefrenceManager.setStudentFathersNameInSP(userObject.optString("FATHERS_FULL_NAME"));
            }
            if (userObject.has("ADDRESS")) {
                sharedPrefrenceManager.setAddressInSP(userObject.optString("ADDRESS"));
            }
            if (userObject.has("EMAIL_ID")) {
                sharedPrefrenceManager.setUserEmailInSP(userObject.getString("EMAIL_ID"));
            }
            /*if (userObject.has("GENDER")) {
                sharedPrefrenceManager.setStudentGenderInSP(userObject.getString("GENDER"));
            }*/
            if (userObject.has("PERSON_IMAGE")) {
                String stringForURL = userObject.optString("PERSON_IMAGE");
                sharedPrefrenceManager.setUserImageURLInSP(stringForURL);
                if (stringForURL.length() > 0) {
                    return getUserImageFromServer((userObject.optString("PERSON_IMAGE")));
                } else {
                    return getDateTimeFormatFromServer();
                }
            } else {
                return getDateTimeFormatFromServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean getUserImageFromServer(String imagePath) {

        hashMap = new HashMap<>();
        hashMap.put("imagePath", imagePath);
        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_STUDENT_PROFILE_PICTURE);

        if (responseObject != null) {
            return saveUserImageInfo(responseObject);
        }
        return false;
    }

    private boolean saveUserImageInfo(JSONObject responseObject) {

        try {
            SharedPrefrenceManager sharedPrefrenceManager = new SharedPrefrenceManager(context);
            if (responseObject.has("value")) {
                sharedPrefrenceManager.setUserImageInSP(responseObject.getString("value"));
            }
            return getDateTimeFormatFromServer();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean getDateTimeFormatFromServer() {

        hashMap = new HashMap<>();
        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_DATE_TIME_FORMAT);

        if (responseObject != null) {
            return saveDateTimeFormatInfo(responseObject);
        }
        return false;
    }

    private boolean saveDateTimeFormatInfo(JSONObject responseObject) {

        try {
            JSONArray jsonArray = responseObject.optJSONArray("organizationSettings");
            JSONObject object1 = jsonArray.getJSONObject(0);
            JSONObject object2 = jsonArray.getJSONObject(1);
            String format1 = object1.getString("value");
            String format2 = object2.getString("value");

            JSONObject trashObject1 = object1.optJSONObject("setting");
            String code1 = trashObject1.getString("fieldCode");

            if (code1.equals("DF")) {
                sharedPrefrenceManager.setDateFormatInSP(format1);
                sharedPrefrenceManager.setTimeFormatInSP(format2);
            } else {
                sharedPrefrenceManager.setDateFormatInSP(format2);
                sharedPrefrenceManager.setTimeFormatInSP(format1);
            }

            JSONObject jsonObject = responseObject.optJSONObject("currency");
            String currencyName = jsonObject.optString("code");
            String currencySymbol = BaseActivity.Utils.getCurrencySymbol(currencyName);
            sharedPrefrenceManager.setCurrencySymbolInSP(currencySymbol);

            return getAttendanceTypeFromServer();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getAttendanceTypeFromServer() {

        hashMap = new HashMap<>();
        Integer academyLocationId = sharedPrefrenceManager.getAcademyLocationIDFromKey();
        hashMap.put("academyLocationId", academyLocationId.toString());

        responseObject = serverCalls.sendDataToServer(context, hashMap, KEYS.SWITCH_ATTENDANCE_TYPE);

        if (responseObject != null) {
            return saveAttendanceTypeInfo(responseObject);
        }
        return false;
    }

    public boolean saveAttendanceTypeInfo(JSONObject responseObject) {
        try {
            String attendanceType = "";
            if (responseObject.has("studentAttendanceType")) {
                attendanceType = responseObject.getString("studentAttendanceType");
                sharedPrefrenceManager.setAttendanceTypeInSP(attendanceType);

            } else if (responseObject.has("whatever")) {
                JSONArray array = responseObject.getJSONArray("whatever");
                JSONObject object = (JSONObject) array.get(0);

                if (object.has("studentAttendanceType")) {
                    attendanceType = object.getString("studentAttendanceType");
                    sharedPrefrenceManager.setAttendanceTypeInSP(attendanceType);
                    sharedPrefrenceManager.setUserLoginStatusInSP(true);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}