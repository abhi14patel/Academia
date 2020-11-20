package com.serosoft.academiassu.Modules.MyCourses.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Helpers.Permissions;
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener;
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask;
import com.serosoft.academiassu.Manager.SharedPrefrenceManager;
import com.serosoft.academiassu.Manager.TranslationManager;

import com.serosoft.academiassu.Modules.Assignments.Assignment.AssignmentCourseListActivity;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.AssignmentClub_Dto;
import com.serosoft.academiassu.Modules.Assignments.Assignment.Models.Assignment_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDiaryClub_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDiary_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.Models.SessionDocuemnt_Dto;
import com.serosoft.academiassu.Modules.Assignments.SessionDiary.SessionDiaryCourseListActivity;
import com.serosoft.academiassu.Modules.Attendance.AttendanceMainActivity;
import com.serosoft.academiassu.Modules.Attendance.Models.AttendanceType_Dto;
import com.serosoft.academiassu.Modules.Attendance.MultipleAttendanceActivity;
import com.serosoft.academiassu.Modules.Attendance.MultipleAttendanceMainActivity;
import com.serosoft.academiassu.Modules.MyCourses.Models.MyCourse_Dto;
import com.serosoft.academiassu.Modules.TimeTable.TimeTableDayWiseListActivityK;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.BaseActivity;
import com.serosoft.academiassu.Utils.ConnectionDetector;
import com.serosoft.academiassu.Networking.KEYS;
import com.serosoft.academiassu.Utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Abhishek on March 11 2020.
 */

public class MyCoursesListAdapter extends RecyclerView.Adapter<MyCoursesListAdapter.MyViewHolder> implements AsyncTaskCompleteListener {

    Context context;
    BaseActivity baseActivity;
    ArrayList<MyCourse_Dto> myCoursesList;
    ArrayList<Integer> list = new ArrayList<>();

    int course_id = 0;
    String course_name = "";
    int otherTillDate = 0;

    String facultyName = "", courseName = "";
    int totalRecords = 0, presentRecords = 0, absentRecords = 0, courseVariantId = 0;

    AssignmentClub_Dto assignmentClub_dto;
    SessionDiaryClub_Dto sessionDiaryClub_dto;
    private boolean isCourseName = false;
    ArrayList<Assignment_Dto> assignmentList;
    HashMap<String,ArrayList<Assignment_Dto>> assignmentTempMap = new HashMap<>();

    ArrayList<SessionDiary_Dto> sessionDiaryList;
    ArrayList<SessionDocuemnt_Dto> sessionDocumentList;
    HashMap<String,ArrayList<SessionDiary_Dto>> sessionDiaryTempMap = new HashMap<>();

    TranslationManager translationManager;
    SharedPrefrenceManager sharedPrefrenceManager;
    PopupWindow popupWindow;
    View viewPopup;

    private final String TAG = MyCoursesListAdapter.class.getSimpleName();

    public MyCoursesListAdapter(Context context, ArrayList<MyCourse_Dto> myCoursesList) {
        this.context = context;
        this.myCoursesList = myCoursesList;
        translationManager = new TranslationManager(context);
        sharedPrefrenceManager = new SharedPrefrenceManager(context);
        baseActivity = new BaseActivity();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycourses_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MyCourse_Dto myCourse_dto = myCoursesList.get(position);

        String courseName = ProjectUtils.getCorrectedString(myCourse_dto.getCourseName());
        if(!courseName.equalsIgnoreCase(""))
        {
            holder.tvCourseName.setText(courseName);
        }

        String courseCode = ProjectUtils.getCorrectedString(myCourse_dto.getCourseCode());
        if(!courseCode.equalsIgnoreCase(""))
        {
            holder.tvCourseCode.setText(courseCode);
        }

        String faculty = ProjectUtils.getCorrectedString(myCourse_dto.getFaculty());
        if(!faculty.equalsIgnoreCase(""))
        {
            holder.tvFacultyName.setText(faculty);
        }else{
            holder.tvFacultyName.setText("-");
        }

        permissionSetup(holder);
    }

    @Override
    public int getItemCount() {

        return (null != myCoursesList ? myCoursesList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView tvCourseName,tvCourseCode1,tvCourseCode,tvFacultyName1,tvFacultyName;
        private LinearLayout llTimeTable,llAttendance,llHomeworkAssignment,llCourseSession;
        private TextView tvTimetable,tvAttendance,tvHomeworkAssignment,tvCourseSession;
        private AppCompatImageView ivHeaderIndicator;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseCode1 = itemView.findViewById(R.id.tvCourseCode1);
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvFacultyName1 = itemView.findViewById(R.id.tvFacultyName1);
            tvFacultyName = itemView.findViewById(R.id.tvFacultyName);
            ivHeaderIndicator = itemView.findViewById(R.id.ivHeaderIndicator);

            popupWindow = popupDisplay();
            viewPopup = popupWindow.getContentView();

            llTimeTable = viewPopup.findViewById(R.id.llTimeTable);
            llAttendance = viewPopup.findViewById(R.id.llAttendance);
            llHomeworkAssignment = viewPopup.findViewById(R.id.llHomeworkAssignment);
            llCourseSession = viewPopup.findViewById(R.id.llCourseSession);

            llTimeTable.setVisibility(View.GONE);
            llAttendance.setVisibility(View.GONE);
            llHomeworkAssignment.setVisibility(View.GONE);
            llCourseSession.setVisibility(View.GONE);

            tvTimetable = viewPopup.findViewById(R.id.tvTimetable);
            tvAttendance = viewPopup.findViewById(R.id.tvAttendance);
            tvHomeworkAssignment = viewPopup.findViewById(R.id.tvHomeworkAssignment);
            tvCourseSession = viewPopup.findViewById(R.id.tvCourseSession);

            tvFacultyName1.setText(translationManager.FACULTY_KEY+": ");
            tvCourseCode1.setText(translationManager.COURSE_CODE_KEY+": ");
            tvTimetable.setText(translationManager.TIMETABLE_KEY1);
            tvAttendance.setText(translationManager.ATTENDANCE_KEY1);
            tvHomeworkAssignment.setText(translationManager.HOMEWORKASSIGNMENT_KEY);
            tvCourseSession.setText(translationManager.COURSE_SESSIONDIARY_KEY);

            llTimeTable.setOnClickListener(this::onClick);
            llAttendance.setOnClickListener(this::onClick);
            llHomeworkAssignment.setOnClickListener(this::onClick);
            llCourseSession.setOnClickListener(this::onClick);
            ivHeaderIndicator.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {

            int id = v.getId();

            switch (id)
            {
                case R.id.llTimeTable:{
                    Intent intent=new Intent(context, TimeTableDayWiseListActivityK.class);
                    context.startActivity(intent);
                }break;

                case R.id.llAttendance:{
                    if(sharedPrefrenceManager.getIsCourseLevelStatusFromKey()){

                        populateAttendanceContent();
                    }else{
                        if (sharedPrefrenceManager.getAttendanceTypeCountFromKey() == 1)
                        {
                            Intent intent = new Intent(context, AttendanceMainActivity.class);
                            context.startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(context, MultipleAttendanceMainActivity.class);
                            context.startActivity(intent);
                        }
                    }
                }break;

                case R.id.llHomeworkAssignment:{
                    populateHomeworkAssignmentContent();
                }break;

                case R.id.llCourseSession:{
                    populateCourseSessionContent();
                }break;

                case R.id.ivHeaderIndicator:{

                    MyCourse_Dto myCourse_dto = myCoursesList.get(getAdapterPosition());
                    course_id = myCourse_dto.getCourseId();
                    course_name = myCourse_dto.getCourseName();
                    isCourseName = false;

                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    } else {
                        //Show the PopupWindow anchored to the button we
                        //pressed. It will be displayed below the button
                        //if there's room, otherwise above.

                        if (android.os.Build.VERSION.SDK_INT >=24) {
                            int[] a = new int[2]; //getLocationInWindow required array of size 2
                            v.getLocationInWindow(a);
                            popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(),Gravity.TOP | Gravity.RIGHT,0,a[1]+v.getHeight());
                        } else{
                            popupWindow.showAsDropDown(v);
                        }

                        popupWindow.update();
                    }

                }break;
            }
        }
    }

    private PopupWindow popupDisplay()
    {
        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.mycourses_items_menu, null);

        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setWindowLayoutMode(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(100);
        popupWindow.setWidth(100);

        //set content and background
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_mycourses_menu));

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        return popupWindow;
    }

    private void populateAttendanceContent() {

        if (ConnectionDetector.isConnectingToInternet(context))
        {
            String programId = String.valueOf(sharedPrefrenceManager.getProgramIDFromKey());
            String batchId = String.valueOf(sharedPrefrenceManager.getBatchIDFromKey());
            String periodId = String.valueOf(sharedPrefrenceManager.getPeriodIDFromKey());

            baseActivity.showProgressDialog(context);

            new OptimizedServerCallAsyncTask(context, this,
                    KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL).execute(programId,batchId,periodId,"","","0","100","","");
        }
        else
        {
            Toast.makeText(context, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }

    }

    private void populateHomeworkAssignmentContent() {

        if (ConnectionDetector.isConnectingToInternet(context)) {

            baseActivity.showProgressDialog(context);
            new OptimizedServerCallAsyncTask(context,
                    this, KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS).execute();
        } else {
            Toast.makeText(context, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void populateCourseSessionContent() {

        if (ConnectionDetector.isConnectingToInternet(context))
        {
            baseActivity.showProgressDialog(context);
            new OptimizedServerCallAsyncTask(context,
                    this, KEYS.SWITCH_COURSE_COVERAGE_LIST_DETAILS).execute();
        } else {
            Toast.makeText(context, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, String> result) {

        String callFor = result.get(KEYS.CALL_FOR);
        String responseResult = result.get(KEYS.RETURN_RESPONSE);

        switch (callFor){

            case KEYS.SWITCH_ATTENDANCE_SUMMARY_COURSE_LEVEL:

                baseActivity.hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult.toString());

                    JSONArray arr = responseObject.optJSONArray("rows");

                    if (arr != null && arr.length() > 0)
                    {
                        ArrayList<AttendanceType_Dto> attendanceTypeList = new ArrayList<>();

                        for (int i = 0; i < arr.length(); i++)
                        {
                            JSONObject obj = arr.getJSONObject(i);

                            String programName = obj.optString("programName");
                            String sectionCode = obj.optString("sectionCode");
                            String facultyName = obj.optString("facultyName");
                            String courseName = obj.optString("courseName");
                            int sectionId = obj.optInt("sectionId");
                            int totalRecords = obj.optInt("totalRecords");
                            int presentRecords = obj.optInt("presentRecords");
                            int absentRecords = obj.optInt("absentRecords");
                            int courseVariantId = obj.optInt("courseVariantId");
                            int courseId = obj.optInt("courseId");

                            attendanceTypeList.add(new AttendanceType_Dto(programName,sectionCode,facultyName,courseName,sectionId,totalRecords,presentRecords,absentRecords,courseVariantId,courseId));
                        }

                        for(int i =0; i<attendanceTypeList.size(); i++) {

                            AttendanceType_Dto attendanceType_dto = attendanceTypeList.get(i);

                            if(attendanceType_dto.getCourseId() == course_id){

                                Intent intent = new Intent(context, MultipleAttendanceActivity.class);

                                courseName = ProjectUtils.getCorrectedString(attendanceType_dto.getCourseName());
                                totalRecords = attendanceType_dto.getTotalRecords();
                                presentRecords = attendanceType_dto.getPresentRecords();
                                absentRecords = attendanceType_dto.getAbsentRecords();
                                courseVariantId = attendanceType_dto.getCourseVariantId();

                                otherTillDate = totalRecords - presentRecords - absentRecords;

                                facultyName = ProjectUtils.getCorrectedString(facultyName);
                                otherTillDate = totalRecords - presentRecords - absentRecords;
                                String periodId = String.valueOf(sharedPrefrenceManager.getPeriodIDFromKey());

                                intent.putExtra("title", courseName);
                                intent.putExtra("details", facultyName);
                                intent.putExtra("totalTillDate", totalRecords);
                                intent.putExtra("presentTillDate", presentRecords);
                                intent.putExtra("absentTillDate", absentRecords);
                                intent.putExtra("otherTillDate", otherTillDate);
                                intent.putExtra("attendanceType", KEYS.COURSE_LEVEL);
                                intent.putExtra("courseId", course_id);
                                intent.putExtra("sectionId", "");
                                intent.putExtra("periodId", periodId);
                                context.startActivity(intent);
                                break;
                            } else{
                                if (i >= attendanceTypeList.size()-1)
                                ProjectUtils.showLong(context,"No "+translationManager.ATTENDANCE_KEY+" marked for this course!");
                            }
                        }
                    }else{
                        ProjectUtils.showLong(context,"No "+translationManager.ATTENDANCE_KEY+" marked for this course!");
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,"Attendance = "+ex.getMessage());
                }break;

            case KEYS.SWITCH_HOMEWORK_ASSIGNMENT_LIST_DETAILS:
                baseActivity.hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);

                    JSONArray data = responseObject.optJSONArray("rows");

                    if(data != null && data.length() > 0) {

                        assignmentList = new ArrayList<>();

                        for(int i = 0 ; i< data.length() ; i++) {

                            JSONObject jsonObject = data.optJSONObject(i);

                            int id = jsonObject.optInt("ID");
                            int doc_id = jsonObject.optInt("DOC_ID");
                            int serial_no = jsonObject.optInt("SERIAL_NO");
                            long submission_last_date_long = jsonObject.optLong("SUBMISSION_LAST_DATE_LONG");
                            long publish_date_long = jsonObject.optLong("PUBLISH_DATE_LONG");
                            String course_name = jsonObject.optString("COURSE_NAME");
                            String course_code = jsonObject.optString("COURSE_CODE");
                            String assignment_name = jsonObject.optString("ASSIGNMENT_NAME");
                            String assignment_type = jsonObject.optString("ASSIGNMENT_TYPE");
                            String assign_section_type = jsonObject.optString("ASSIGN_SECTION_TYPE");
                            String hwonline_submissions = jsonObject.optString("HWONLINESUBMISSIONS");
                            String course_variant = jsonObject.optString("COURSE_VARIANT");
                            String submission_last_date = jsonObject.optString("SUBMISSION_LAST_DATE");
                            String publish_date = jsonObject.optString("PUBLISH_DATE");
                            String hw_extended_date = jsonObject.optString("HW_EXTENDED_DATE");
                            String faculty_name = jsonObject.optString("FACULTY_NAME");

                            Assignment_Dto assignment_dto = new Assignment_Dto(id,doc_id,serial_no,submission_last_date_long,publish_date_long,course_name,course_code,assignment_name,assignment_type,assign_section_type,hwonline_submissions,course_variant,submission_last_date,publish_date,hw_extended_date,faculty_name);
                            assignmentList.add(assignment_dto);
                        }

                        //Here sort data for club same course name data
                        Collections.sort(assignmentList, Assignment_Dto.sortByName);
                        generateMergedData1(assignmentList);
                    }else{
                        ProjectUtils.showLong(context,"No "+translationManager.HOMEWORKASSIGNMENT_KEY+" given for this course!");
                    }

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                    ProjectUtils.showLog(TAG,"HW Assignment = "+ex.getMessage());
                }
                break;

            case KEYS.SWITCH_COURSE_COVERAGE_LIST_DETAILS:
                baseActivity.hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(responseResult);
                    JSONArray data = responseObject.optJSONArray("rows");

                    if(data != null && data.length() > 0)
                    {
                        sessionDiaryList = new ArrayList<>();
                        for(int i = 0 ; i< data.length() ; i++)
                        {
                            JSONObject jsonObject = data.optJSONObject(i);

                            int id = jsonObject.optInt("id");
                            long date_long = jsonObject.optLong("date_long");
                            long fromSlot_long = jsonObject.optLong("fromSlot_long");
                            long toSlot_long = jsonObject.optLong("toSlot_long");
                            String date = jsonObject.optString("date");
                            String sessionNo = jsonObject.optString("sessionNo");
                            String topic = jsonObject.optString("topic");
                            String course = jsonObject.optString("course");
                            String facultyName = jsonObject.optString("facultyName");
                            String courseVariant = jsonObject.optString("courseVariant");

                            JSONArray docuementArray = jsonObject.optJSONArray("documents");

                            sessionDocumentList = new ArrayList<>();
                            for(int j = 0 ; j<docuementArray.length() ; j++)
                            {
                                JSONObject jsonObject1 = docuementArray.optJSONObject(j);

                                int id1 = jsonObject1.optInt("id");
                                String value = jsonObject1.optString("value");
                                String secondValue = jsonObject1.optString("secondValue");

                                sessionDocumentList.add(new SessionDocuemnt_Dto(id1,value,secondValue));
                            }

                            SessionDiary_Dto sessionDiary_dto = new SessionDiary_Dto(id,date_long,fromSlot_long,toSlot_long,date,sessionNo,topic,course,facultyName,courseVariant,sessionDocumentList);
                            sessionDiaryList.add(sessionDiary_dto);
                        }

                        Collections.sort(sessionDiaryList, SessionDiary_Dto.sortByName);
                        generateMergedData2(sessionDiaryList);

                    }else{
                        ProjectUtils.showLong(context,"No "+translationManager.COURSE_SESSIONDIARY_KEY+" available for this course!");
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                    ProjectUtils.showLog(TAG,""+e.getMessage());
                }break;
        }
    }

    private void generateMergedData1(ArrayList<Assignment_Dto> list)
    {
        for(int i=0; i<list.size() ; i++)
        {
            ArrayList<Assignment_Dto> tempList;

            if(i==0) {

                tempList = new ArrayList<>();
                tempList.add(list.get(i));
                assignmentTempMap.put(list.get(i).getCourseName(),tempList);
            }
            else if(list.get(i-1).getCourseName().equalsIgnoreCase(list.get(i).getCourseName())) {

                if(assignmentTempMap.containsKey(list.get(i).getCourseName()))
                {
                    tempList = assignmentTempMap.get(list.get(i).getCourseName());
                    tempList.add(list.get(i));
                    assignmentTempMap.put(list.get(i).getCourseName(),tempList);
                }
                else
                {
                    tempList = new ArrayList<>();
                    tempList.add(list.get(i));
                    assignmentTempMap.put(list.get(i).getCourseName(),tempList);
                }
            }
            else {
                tempList = new ArrayList<>();
                tempList.add(list.get(i));
                assignmentTempMap.put(list.get(i).getCourseName(),tempList);
            }
        }

        //Set of entries
        Set<Map.Entry<String, ArrayList<Assignment_Dto>>> demoEntrySet = assignmentTempMap.entrySet();
        ArrayList<Map.Entry<String, ArrayList<Assignment_Dto>>> list2 = new ArrayList<>(demoEntrySet);

        for(int i = 0 ; i < list2.size() ; i++){

            String courseName = list2.get(i).getKey();
            ArrayList<Assignment_Dto> mergedDataList = assignmentTempMap.get(courseName);

            if(mergedDataList != null && mergedDataList.size() > 0){

                for(Assignment_Dto assignment_dto : mergedDataList){

                    String course_Name = assignment_dto.getCourseName();
                    if(course_Name.equalsIgnoreCase(course_name)){
                        isCourseName = true;
                        assignmentClub_dto = new AssignmentClub_Dto(courseName,mergedDataList);
                        break;
                    }
                }
            }
        }

        if(isCourseName){
            Intent intent = new Intent(context, AssignmentCourseListActivity.class);
            intent.putExtra(Consts.ASSIGNMENT_LIST,assignmentClub_dto);
            context.startActivity(intent);
        }else{
            ProjectUtils.showLong(context,"No "+translationManager.HOMEWORKASSIGNMENT_KEY+" given for this course!");
        }
    }

    private void generateMergedData2(ArrayList<SessionDiary_Dto> list)
    {
        for(int i=0; i<list.size() ; i++)
        {
            ArrayList<SessionDiary_Dto> tempList;

            if(i==0)
            {
                tempList = new ArrayList<>();
                tempList.add(list.get(i));
                sessionDiaryTempMap.put(list.get(i).getCourse(),tempList);
            }
            else if(list.get(i-1).getCourse().equalsIgnoreCase(list.get(i).getCourse()))
            {
                if(sessionDiaryTempMap.containsKey(list.get(i).getCourse()))
                {
                    tempList = sessionDiaryTempMap.get(list.get(i).getCourse());
                    tempList.add(list.get(i));
                    sessionDiaryTempMap.put(list.get(i).getCourse(),tempList);
                }
                else
                {
                    tempList = new ArrayList<>();
                    tempList.add(list.get(i));
                    sessionDiaryTempMap.put(list.get(i).getCourse(),tempList);
                }
            }
            else {
                tempList = new ArrayList<>();
                tempList.add(list.get(i));
                sessionDiaryTempMap.put(list.get(i).getCourse(),tempList);
            }
        }

        //Set of entries
        Set<Map.Entry<String, ArrayList<SessionDiary_Dto>>> demoEntrySet = sessionDiaryTempMap.entrySet();
        ArrayList<Map.Entry<String, ArrayList<SessionDiary_Dto>>> list2 = new ArrayList<>(demoEntrySet);

        for(int i = 0 ; i < list2.size() ; i++){

            String courseName = list2.get(i).getKey();
            ArrayList<SessionDiary_Dto> mergedDataList = sessionDiaryTempMap.get(courseName);

            if(mergedDataList != null && mergedDataList.size() > 0){

                for(SessionDiary_Dto sessionDiary_dto : mergedDataList){

                    String course_Name = sessionDiary_dto.getCourse();
                    if(course_Name.equalsIgnoreCase(course_name)){
                        isCourseName = true;
                        sessionDiaryClub_dto = new SessionDiaryClub_Dto(courseName,mergedDataList);
                    }
                }
            }
        }

        if(isCourseName){
            Intent intent = new Intent(context, SessionDiaryCourseListActivity.class);
            intent.putExtra(Consts.SESSION_DIARY_LIST,sessionDiaryClub_dto);
            context.startActivity(intent);
        }else{
            ProjectUtils.showLong(context,"No "+translationManager.COURSE_SESSIONDIARY_KEY+" available for this course!");
        }
    }

    private void permissionSetup(MyViewHolder holder) {

        //Here set module permission without api response
        list = sharedPrefrenceManager.getModulePermissionList(Permissions.MODULE_PERMISSION);

        int moduleCount = 0;

        for (int i = 0 ; i < list.size() ; i++){

            switch (list.get(i)){
                case Permissions.PARENT_PERMISSION_TIMETABLE_VIEW:
                case Permissions.STUDENT_PERMISSION_TIMETABLE_VIEW:
                    holder.llTimeTable.setVisibility(View.VISIBLE);
                    moduleCount++;
                    break;
                case Permissions.PARENT_PERMISSION_ATTENDANCE_VIEW:
                case Permissions.STUDENT_PERMISSION_ATTENDANCE_VIEW:
                    holder.llAttendance.setVisibility(View.VISIBLE);
                    moduleCount++;
                    break;
                case Permissions.PARENT_PERMISSION_ASSIGNMENT_HOMEWORK_VIEW:
                case Permissions.STUDENT_PERMISSION_ASSIGNMENT_HOMEWORK_VIEW:
                    holder.llHomeworkAssignment.setVisibility(View.VISIBLE);
                    moduleCount++;
                    break;
                case Permissions.PARENT_PERMISSION_ASSIGNMENT_SESSION_DIARY_VIEW:
                case Permissions.STUDENT_PERMISSION_ASSIGNMENT_SESSION_DIARY_VIEW:
                    holder.llCourseSession.setVisibility(View.VISIBLE);
                    moduleCount++;
                    break;
            }
        }

        if (moduleCount == 0)
        {
            holder.ivHeaderIndicator.setVisibility(View.GONE);
        }
    }
}
