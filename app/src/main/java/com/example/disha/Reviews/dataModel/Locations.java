package com.example.disha.Reviews.dataModel;

public class Locations {
    String locationName, description;

    public Locations(String locationName, String description) {
        this.locationName = locationName;
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
