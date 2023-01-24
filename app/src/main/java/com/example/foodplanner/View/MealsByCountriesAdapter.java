package com.example.foodplanner.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Controller.Fragments.MainFragments.MealByCategoryFragmentDirections;
import com.example.foodplanner.Controller.Fragments.MainFragments.MealByCountryFragmentDirections;
import com.example.foodplanner.Model.MealsItem;
import com.example.foodplanner.Model.Root;
import com.example.foodplanner.Network.RetrofitClient;
import com.example.foodplanner.R;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsByCountriesAdapter  extends RecyclerView.Adapter<MealsByCountriesAdapter.MyViewHolder> {
    List<MealsItem> meals;
    ViewGroup viewGroup;

    public MealsByCountriesAdapter(List<MealsItem> meals) {
        this.meals=meals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewGroup=parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_meal_items, parent, false);
        return new MealsByCountriesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(holder.itemView).load(meals.get(position).getStrMealThumb()).into(holder.mealImage);
        holder.mealName.setText(meals.get(position).getStrMeal());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitClient.getInstance().getMyApi().getMealById(Integer.parseInt(meals.get(position).getIdMeal()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Root>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                // loading ui
                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Root root) {
                                Navigation.findNavController(viewGroup).navigate(MealByCountryFragmentDirections.actionMealByCountryFragmentToMealDeatailsFragment(root.getMeals().get(0)));
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }
        });

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
                TextView mealName;
                ImageView mealImage;
        public MyViewHolder(@NonNull View itemView) {
        super(itemView);
            mealName=itemView.findViewById(R.id.country_meal);
            mealImage=itemView.findViewById(R.id.countryMeal_image);

        }
    }
}
