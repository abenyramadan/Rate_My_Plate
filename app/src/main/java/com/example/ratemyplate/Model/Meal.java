package com.example.ratemyplate.Model;

import java.io.Serializable;

public class Meal implements Serializable {
    public String imageUri;
    public String description;
    public int healthRating;
    public int calories;
    public String suggestion;
    public boolean favorite;
    private String mealType;

    public Meal(String imageUri, String description, int healthRating, int calories, String suggestion, boolean favorite) {
        this.imageUri = imageUri;
        this.description = description;
        this.healthRating = healthRating;
        this.calories = calories;
        this.suggestion = suggestion;
        this.favorite = favorite;
    }
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getMealType() {
        return mealType;
    }
}



