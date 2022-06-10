package com.example.disha.AddPlace.data;

public class PlaceData {
    String placeName, Placedescription, phoneNo, placeType, infraType, lat, lang, address, ramp, handrail, toilet, ntoilet,braille, lifts, wheelchair, facilityDescription;

    public PlaceData() {
    }

    public PlaceData(String placeName, String description, String phoneNo, String placeType, String infraType, String lat, String lang, String address, String ramp, String handrail, String toilet, String ntoilet, String braille, String lifts, String wheelchair, String desc) {
        this.placeName = placeName;
        this.Placedescription = description;
        this.phoneNo = phoneNo;
        this.placeType = placeType;
        this.infraType = infraType;
        this.lat = lat;
        this.lang = lang;
        this.address = address;
        this.ramp = ramp;
        this.handrail = handrail;
        this.toilet = toilet;
        this.ntoilet = ntoilet;
        this.braille = braille;
        this.lifts = lifts;
        this.wheelchair = wheelchair;
        this.facilityDescription = desc;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlacedescription() {
        return Placedescription;
    }

    public void setPlacedescription(String placedescription) {
        this.Placedescription = placedescription;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getInfraType() {
        return infraType;
    }

    public void setInfraType(String infraType) {
        this.infraType = infraType;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRamp() {
        return ramp;
    }

    public void setRamp(String ramp) {
        this.ramp = ramp;
    }

    public String getHandrail() {
        return handrail;
    }

    public void setHandrail(String handrail) {
        this.handrail = handrail;
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

    public String getBraille() {
        return braille;
    }

    public void setBraille(String braille) {
        this.braille = braille;
    }

    public String getLifts() {
        return lifts;
    }

    public void setLifts(String lifts) {
        this.lifts = lifts;
    }

    public String getWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(String wheelchair) {
        this.wheelchair = wheelchair;
    }

    public String getDesc() {
        return facilityDescription;
    }

    public void setDesc(String desc) {
        this.facilityDescription = desc;
    }
}
