package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultReportType_Dto implements Serializable {

    private String treeNode,maxMarksOrGrade,effetiveMarksGrade,obtainedMarksGrade,moderationPoints;
    private double weightage;
    private ArrayList<ResultReportSubType_Dto> subTypeList;

    public ResultReportType_Dto(String treeNode, String maxMarksOrGrade, String effetiveMarksGrade, String obtainedMarksGrade, String moderationPoints, double weightage, ArrayList<ResultReportSubType_Dto> subTypeList) {
        this.treeNode = treeNode;
        this.maxMarksOrGrade = maxMarksOrGrade;
        this.effetiveMarksGrade = effetiveMarksGrade;
        this.obtainedMarksGrade = obtainedMarksGrade;
        this.moderationPoints = moderationPoints;
        this.weightage = weightage;
        this.subTypeList = subTypeList;
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

    public ArrayList<ResultReportSubType_Dto> getSubTypeList() {
        return subTypeList;
    }
}
