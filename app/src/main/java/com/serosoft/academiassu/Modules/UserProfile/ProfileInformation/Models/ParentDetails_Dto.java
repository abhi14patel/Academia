package com.serosoft.academiassu.Modules.UserProfile.ProfileInformation.Models;

import java.io.Serializable;

public class ParentDetails_Dto implements Serializable {

    String parentName,relationship,parentUserCode,relatedPersonEmailId,relatedPersonMobile,photoUrl;
    boolean relatedPersonHasAccessToStudent,isEmergencyContact;

    public ParentDetails_Dto(String parentName, String relationship, String parentUserCode, String relatedPersonEmailId, String relatedPersonMobile, String photoUrl, boolean relatedPersonHasAccessToStudent, boolean isEmergencyContact) {
        this.parentName = parentName;
        this.relationship = relationship;
        this.parentUserCode = parentUserCode;
        this.relatedPersonEmailId = relatedPersonEmailId;
        this.relatedPersonMobile = relatedPersonMobile;
        this.photoUrl = photoUrl;
        this.relatedPersonHasAccessToStudent = relatedPersonHasAccessToStudent;
        this.isEmergencyContact = isEmergencyContact;
    }

    public String getParentName() {
        return parentName;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getParentUserCode() {
        return parentUserCode;
    }

    public String getRelatedPersonEmailId() {
        return relatedPersonEmailId;
    }

    public String getRelatedPersonMobile() {
        return relatedPersonMobile;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public boolean isRelatedPersonHasAccessToStudent() {
        return relatedPersonHasAccessToStudent;
    }

    public boolean isEmergencyContact() {
        return isEmergencyContact;
    }
}
