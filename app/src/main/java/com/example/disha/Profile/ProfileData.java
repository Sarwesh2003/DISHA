package com.example.disha.Profile;

public class ProfileData {
    String UserName, UserEmail, DisabilityDescription, DisabilityType, aadharNo, EmergencyContact;

    public ProfileData(String userName, String userEmail, String disabilityDescription, String disabilityType, String aadharNo, String emergencyContact) {
        UserName = userName;
        UserEmail = userEmail;
        DisabilityDescription = disabilityDescription;
        DisabilityType = disabilityType;
        this.aadharNo = aadharNo;
        EmergencyContact = emergencyContact;
    }

    public ProfileData() {

    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getDisabilityDescription() {
        return DisabilityDescription;
    }

    public void setDisabilityDescription(String disabilityDescription) {
        DisabilityDescription = disabilityDescription;
    }

    public String getDisabilityType() {
        return DisabilityType;
    }

    public void setDisabilityType(String disabilityType) {
        DisabilityType = disabilityType;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getEmergencyContact() {
        return EmergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        EmergencyContact = emergencyContact;
    }
}
