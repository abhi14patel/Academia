package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultReportSubType_Dto implements Serializable {

    private String treeNode,maxMarksOrGrade,effetiveMarksGrade,obtainedMarksGrade,moderationPoints;
    private double weightage;
    private ArrayList<ResultReportMethod_Dto> methodList;

    public ResultReportSubType_Dto(String treeNode, String maxMarksOrGrade, String effetiveMarksGrade, String obtainedMarksGrade, String moderationPoints, double weightage, ArrayList<ResultReportMethod_Dto> methodList) {
        this.treeNode = treeNode;
        this.maxMarksOrGrade = maxMarksOrGrade;
        this.effetiveMarksGrade = effetiveMarksGrade;
        this.obtainedMarksGrade = obtainedMarksGrade;
        this.moderationPoints = moderationPoints;
        this.weightage = weightage;
        this.methodList = methodList;
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

    public ArrayList<ResultReportMethod_Dto> getMethodList() {
        return methodList;
    }
}
