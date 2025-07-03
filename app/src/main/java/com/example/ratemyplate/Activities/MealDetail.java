package com.example.ratemyplate.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratemyplate.Model.Meal;
import com.example.ratemyplate.R;

public class MealDetail extends AppCompatActivity {

    private ImageView detailImage;
    private TextView detailDescription, detailRating, detailCalories, detailSuggestion, detailType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);  // make sure this filename matches your XML

        // Bind views
        detailImage = findViewById(R.id.detailImage);
        detailDescription = findViewById(R.id.detailDescription);
        detailRating = findViewById(R.id.detailRating);
        detailCalories = findViewById(R.id.detailCalories);
        detailSuggestion = findViewById(R.id.detailSuggestion);
        detailType = findViewById(R.id.detailType);

        // Receive the Meal object (make sure it's Serializable or Parcelable)
        Meal meal = (Meal) getIntent().getSerializableExtra("meal");

        if (meal != null) {
            detailImage.setImageURI(Uri.parse(meal.imageUri));
            detailDescription.setText(meal.description);
            detailRating.setText("Rating: " + meal.healthRating + "/10");
            detailCalories.setText("Calories: " + meal.calories);
            detailSuggestion.setText("Suggestion: " + meal.suggestion);
            detailType.setText(meal.getMealType());
        }
    }
}
