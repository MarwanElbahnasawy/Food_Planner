package com.example.foodplanner.Controller.Fragments.MainFragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.Model.MealsItem;
import com.example.foodplanner.Network.RetrofitClient;
import com.example.foodplanner.R;
import com.example.foodplanner.View.MealByCategoriesAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealByCategoryFragment extends Fragment {
    MealByCategoriesAdapter mealByCategoriesAdapter;
    RecyclerView recyclerView;
    TextInputEditText searchTextInput;
    List<MealsItem> mealsItemList = new ArrayList<>();
    private static final String TAG = "MealByCategory";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_by_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_MealByCategory);
        searchTextInput = view.findViewById(R.id.textInput_Meal_search_category);

        String category = MealByCategoryFragmentArgs.fromBundle(getArguments()).getCategory();
        RetrofitClient.getInstance().getMyApi()
                .getCategoryMeal(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        e -> {
                            mealsItemList = e.getMeals();
                            mealByCategoriesAdapter = new MealByCategoriesAdapter(mealsItemList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(mealByCategoriesAdapter);

                        },
                        error -> {
                            Log.i(TAG, "onViewCreated: " + error.getMessage());
                        }
                );
        searchTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mealByCategoriesAdapter = new MealByCategoriesAdapter(mealsItemList.stream().filter(
                        mealsItem -> mealsItem.getStrMeal().startsWith(s.toString())).collect(Collectors.toList()));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(mealByCategoriesAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}