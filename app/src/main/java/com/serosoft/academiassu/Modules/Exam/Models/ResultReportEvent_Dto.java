package com.serosoft.academiassu.Modules.Exam.Models;

import java.io.Serializable;

public class ResultReportEvent_Dto implements Serializable {

    private String treeNode,maxMarksOrGrade,effetiveMarksGrade,obtainedMarksGrade,moderationPoints;
    private double weightage;

    public ResultReportEvent_Dto(String treeNode, String maxMarksOrGrade, String effetiveMarksGrade, String obtainedMarksGrade, String moderationPoints, double weightage) {
        this.treeNode = treeNode;
        this.maxMarksOrGrade = maxMarksOrGrade;
        this.effetiveMarksGrade = effetiveMarksGrade;
        this.obtainedMarksGrade = obtainedMarksGrade;
        this.moderationPoints = moderationPoints;
        this.weightage = weightage;
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
}
