package com.example.disha.ViewDetails.data;

public class ReviewData {
    String username, date, description, ratings;

    public ReviewData() {
    }

    public ReviewData(String username, String date, String description, String ratings) {
        this.username = username;
        this.date = date;
        this.description = description;
        this.ratings = ratings;
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
