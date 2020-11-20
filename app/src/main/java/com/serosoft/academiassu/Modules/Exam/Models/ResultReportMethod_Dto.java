package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultReportMethod_Dto implements Serializable {

    private String treeNode,maxMarksOrGrade,effetiveMarksGrade,obtainedMarksGrade,moderationPoints;
    private double weightage;
    private ArrayList<ResultReportEvent_Dto> eventList;

    public ResultReportMethod_Dto(String treeNode, String maxMarksOrGrade, String effetiveMarksGrade, String obtainedMarksGrade, String moderationPoints, double weightage, ArrayList<ResultReportEvent_Dto> eventList) {
        this.treeNode = treeNode;
        this.maxMarksOrGrade = maxMarksOrGrade;
        this.effetiveMarksGrade = effetiveMarksGrade;
        this.obtainedMarksGrade = obtainedMarksGrade;
        this.moderationPoints = moderationPoints;
        this.weightage = weightage;
        this.eventList = eventList;
    }

    public String getTreeNode() {
        return treeNode;
    }

    public String getMaxMarksOrGrade() {
        return maxMarksOrGrade;
    }

    public String getEffetiveMarksGrade() {
        return effetiveMarksGrade;
    }

    public String getObtainedMarksGrade() {
        return obtainedMarksGrade;
    }

    public String getModerationPoints() {
        return moderationPoints;
    }

    public double getWeightage() {
        return weightage;
    }

    public ArrayList<ResultReportEvent_Dto> getEventList() {
        return eventList;
    }
}
