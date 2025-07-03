package com.example.ratemyplate.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ratemyplate.Model.Meal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MealStorage {
    private static final String PREFS_NAME = "RateMyPlatePrefs";
    private static final String PREFS_KEY = "Meals_list";

    public static void saveMeal(Context context, Meal meal) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // Load existing meals
        String existingJson = prefs.getString(PREFS_KEY, null);
        Type type = new TypeToken<List<Meal>>() {}.getType();
        List<Meal> mealList = existingJson != null ? gson.fromJson(existingJson, type) : new ArrayList<>();

        // Add new meal
        mealList.add(meal);

        // Save updated list
        String updatedJson = gson.toJson(mealList);
        prefs.edit().putString(PREFS_KEY, updatedJson).apply();
    }

    public static List<Meal> loadMeals(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String json = prefs.getString(PREFS_KEY, null);
        Type type = new TypeToken<List<Meal>>() {}.getType();

        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }
}
