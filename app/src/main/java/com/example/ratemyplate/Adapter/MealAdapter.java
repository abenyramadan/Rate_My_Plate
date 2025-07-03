package com.example.ratemyplate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ratemyplate.Activities.MealDetail;
import com.example.ratemyplate.Model.Meal;
import com.example.ratemyplate.R;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private final Context context;
    private final List<Meal> meals;

    public MealAdapter(Context context, List<Meal> meals) {
        this.context = context;
        this.meals = meals;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.imageView.setImageURI(Uri.parse(meal.imageUri));
        holder.ratingView.setText("Rating: " + meal.healthRating + "/10");
        holder.caloriesView.setText("Calories: " + meal.calories);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MealDetail.class);
            intent.putExtra("meal", meal); // Make sure Meal implements Serializable or Parcelable
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView ratingView, caloriesView;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mealImage);
            ratingView = itemView.findViewById(R.id.mealRating);
            caloriesView = itemView.findViewById(R.id.mealCalories);
        }
    }
}
