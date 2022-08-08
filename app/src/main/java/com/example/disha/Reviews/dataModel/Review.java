package com.example.disha.Reviews.dataModel;

public class Review {
    String placeName, username, date, description, ratings, ramp, rampDescription, handrail,
    handrailDescription, braille, brailleDescription, lifts, liftsDescription, wheelchair, wheelchairDescription,
    toilet, ntoilet, toiletDescription;

    public Review() {}

    public Review(String placeName, String username, String date, String description, String ratings, String ramp, String rampDescription, String handrail, String handrailDescription, String braille, String brailleDescription, String lifts, String liftsDescription, String wheelchair, String wheelchairDescription, String toilet, String ntoilet, String toiletDescription) {
        this.placeName = placeName;
        this.username = username;
        this.date = date;
        this.description = description;
        this.ratings = ratings;
        this.ramp = ramp;
        this.rampDescription = rampDescription;
        this.handrail = handrail;
        this.handrailDescription = handrailDescription;
        this.braille = braille;
        this.brailleDescription = brailleDescription;
        this.lifts = lifts;
        this.liftsDescription = liftsDescription;
        this.wheelchair = wheelchair;
        this.wheelchairDescription = wheelchairDescription;
        this.toilet = toilet;
        this.ntoilet = ntoilet;
        this.toiletDescription = toiletDescription;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getRamp() {
        return ramp;
    }

    public void setRamp(String ramp) {
        this.ramp = ramp;
    }

    public String getRampDescription() {
        return rampDescription;
    }

    public void setRampDescription(String rampDescription) {
        this.rampDescription = rampDescription;
    }

    public String getHandrail() {
        return handrail;
    }

    public void setHandrail(String handrail) {
        this.handrail = handrail;
    }

    public String getHandrailDescription() {
        return handrailDescription;
    }

    public void setHandrailDescription(String handrailDescription) {
        this.handrailDescription = handrailDescription;
    }

    public String getBraille() {
        return braille;
    }

    public void setBraille(String braille) {
        this.braille = braille;
    }

    public String getBrailleDescription() {
        return brailleDescription;
    }

    public void setBrailleDescription(String brailleDescription) {
        this.brailleDescription = brailleDescription;
    }

    public String getLifts() {
        return lifts;
    }

    public void setLifts(String lifts) {
        this.lifts = lifts;
    }

    public String getLiftsDescription() {
        return liftsDescription;
    }

    public void setLiftsDescription(String liftsDescription) {
        this.liftsDescription = liftsDescription;
    }

    public String getWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(String wheelchair) {
        this.wheelchair = wheelchair;
    }

    public String getWheelchairDescription() {
        return wheelchairDescription;
    }

    public void setWheelchairDescription(String wheelchairDescription) {
        this.wheelchairDescription = wheelchairDescription;
    }

    public String getToilet() {
        return toilet;
    }

    public void setToilet(String toilet) {
        this.toilet = toilet;
    }

    public String getNtoilet() {
        return ntoilet;
    }

    public void setNtoilet(String ntoilet) {
        this.ntoilet = ntoilet;
    }

    public String getToiletDescription() {
        return toiletDescription;
    }

    public void setToiletDescription(String toiletDescription) {
        this.toiletDescription = toiletDescription;
    }
}
