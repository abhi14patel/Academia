package com.serosoft.academiassu.Modules.Assignments.Assignment.Models;

import java.io.Serializable;

public class AssignmentDocument_Dto implements Serializable {

    int documentId;
    String documentName,path;

    public AssignmentDocument_Dto(int documentId, String documentName, String path) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.path = path;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
