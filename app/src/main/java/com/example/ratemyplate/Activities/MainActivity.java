package com.example.ratemyplate.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratemyplate.Adapter.MealAdapter;
import com.example.ratemyplate.Model.Meal;
import com.example.ratemyplate.R;
import com.example.ratemyplate.Storage.MealStorage;

import java.io.File;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 102;

    private ImageView mealImageView;
    private EditText descriptionInput;
    private Spinner mealTypeSpinner;
    private Button uploadBtn, cameraBtn, submitBtn;
    private TextView characterCounter;
    private RecyclerView mealRecyclerView;

    private Uri selectedImageUri, cameraImageUri;
    private ProgressDialog progressDialog;

    private final String[] suggestions = {
            "Try adding more greens", "Too much fried food", "Looking yummy",
            "Cut down on the calories", "Smaller portions please!!!!!",
            "Add a fruit salad", "More proteins", "Perfectly balanced"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // User interface bind

        mealImageView = findViewById(R.id.imageView);
        descriptionInput = findViewById(R.id.descriptionInput);
        mealTypeSpinner = findViewById(R.id.mealTypeSpinner);
        uploadBtn = findViewById(R.id.uploadBtn);
        cameraBtn = findViewById(R.id.cameraBtn);
        submitBtn = findViewById(R.id.submitMealBtn);
        characterCounter = findViewById(R.id.characterCounter);
        mealRecyclerView = findViewById(R.id.mealRecyclerView);

        // Camera access permissions
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ android.Manifest.permission.CAMERA }, 100);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{ android.Manifest.permission.READ_MEDIA_IMAGES }, 101);
            }
        }

        // Spinner, a drop down menu
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(adapter);

        uploadBtn.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pick, REQUEST_IMAGE_PICK);
        });

        // Capture images from the camera
        cameraBtn.setOnClickListener(v -> {
            try {
                File photoFile = new File(getExternalFilesDir(null), "meal_photo.jpg");

                if (!photoFile.exists()) {
                    photoFile.createNewFile();
                }

                cameraImageUri = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".provider",
                        photoFile
                );

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                // This snippet ensures there's a camera app available
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } else {
                    Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Camera failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //Description input
        descriptionInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                characterCounter.setText(s.length() + "/150");
                checkSubmitState();
            }
        });

        submitBtn.setOnClickListener(v -> submitMeal());
        submitBtn.setEnabled(false);

        loadMealsToRecycler();
    }

    private void checkSubmitState() {
        String desc = descriptionInput.getText().toString().trim();
        submitBtn.setEnabled(!desc.isEmpty() && selectedImageUri != null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                selectedImageUri = data.getData();
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                selectedImageUri = cameraImageUri;
            }

            if (selectedImageUri != null) {
                mealImageView.setImageURI(selectedImageUri);
                mealImageView.setVisibility(View.VISIBLE);
                checkSubmitState();
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
            String suggestion = suggestions[Math.min(rating - 1, suggestions.length - 1)];

            Meal meal = new Meal(
                    selectedImageUri.toString(),
                    descriptionInput.getText().toString().trim(),
                    rating,
                    calories,
                    suggestion,
                    false
            );
            meal.setMealType(mealTypeSpinner.getSelectedItem().toString());

            MealStorage.saveMeal(this, meal);
            Toast.makeText(this, "Meal submitted!", Toast.LENGTH_SHORT).show();

            resetForm();
            loadMealsToRecycler();
        }, 2000);
    }

    private void resetForm() {
        mealImageView.setImageURI(null);
        mealImageView.setVisibility(View.GONE);
        descriptionInput.setText("");
        selectedImageUri = null;
        submitBtn.setEnabled(false);
    }

    private void loadMealsToRecycler() {
        List<Meal> mealList = MealStorage.loadMeals(this);
        MealAdapter adapter = new MealAdapter(this, mealList);
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealRecyclerView.setAdapter(adapter);
        mealRecyclerView.setHasFixedSize(true);
        mealRecyclerView.setNestedScrollingEnabled(false);
    }
}
