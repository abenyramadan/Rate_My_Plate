package com.example.ratemyplate.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

import com.example.ratemyplate.Model.Meal;
import com.example.ratemyplate.R;
import com.example.ratemyplate.Storage.MealStorage;

public class MealSubmission extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 101;
    private ImageView mealImageView;
    private EditText description;
    private Spinner mealTypeSpinner;
    Button uploadBtn, submit;
    private Uri selectedImageUri = null;
    private ProgressDialog progressDialog;

    private final String[] suggestions = {
            "Try adding more greens",
            "Too much fried food",
            "Looking yummy",
            "Cut down on the calories",
            "Smaller portions please!!!!!",
            "Add a fruit salad",
            "More proteins",
            "Perfectly balanced"

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_submission);

        mealImageView = findViewById(R.id.imageView);
        description = findViewById(R.id.descriptionInput);
        mealTypeSpinner = findViewById(R.id.mealTypeSpinner);
        uploadBtn = findViewById(R.id.uploadBtn);
        submit = findViewById(R.id.submitMealBtn);

        // Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(adapter);

        // Upload image
        uploadBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });
        // Submit button
        submit.setOnClickListener(v -> submitMeal());

        // Description Button
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submit.setEnabled(s.length() > 0 && selectedImageUri != null);
            }
        });

        submit.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            mealImageView.setImageURI(selectedImageUri);
            if (!description.getText().toString().trim().isEmpty()) {
                submit.setEnabled(true);
            }
        }
    }

    private void submitMeal() {
        progressDialog = ProgressDialog.show(this, "Analyzing Meal", "Please wait...", true);

        new Handler().postDelayed(() -> {
            progressDialog.dismiss();

            Random rand = new Random();
            int rating = rand.nextInt(10) + 1;
            int calories = rand.nextInt(1000) + 200;
            String suggestion = suggestions[rating - 1];

            String imageUriStr = selectedImageUri.toString();
            String desc = description.getText().toString().trim();
            String type = mealTypeSpinner.getSelectedItem().toString();

            Meal meal = new Meal(imageUriStr, desc, rating, calories, suggestion, false);
            meal.setMealType(type);

            MealStorage.saveMeal(this, meal);

            Toast.makeText(this, "Meal submitted!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MealSubmission.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish(); // finish the current screen
        }, 2000);
    }

}