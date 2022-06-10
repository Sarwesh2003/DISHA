package com.example.disha.Reviews.dataModel;

public class Review {
    String placeName, username, date, description, ratings;

    public Review() {}

    public Review(String placeName, String username, String date, String description, String ratings) {
        this.placeName = placeName;
        this.username = username;
        this.date = date;
        this.description = description;
        this.ratings = ratings;
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
}
