package com.example.foodplanner.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.Controller.Fragments.MainFragments.CategoryFragmentDirections;
import com.example.foodplanner.Controller.Fragments.MainFragments.SearchBYCountryFragmentDirections;
import com.example.foodplanner.Model.AreaModel;
import com.example.foodplanner.Model.CategoryModel;
import com.example.foodplanner.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder>{
    ViewGroup CountryView;
    List<CategoryModel> categories;

    public CategoryAdapter(List<CategoryModel>  categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CountryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountryView =parent;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new CountryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.MyViewHolder holder, int position) {
        CategoryModel categoryModel=categories.get(position);
        holder.country.setText(categoryModel.getStrCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(CountryView).navigate(CategoryFragmentDirections.actionCategoryFragmentToMealByCategoryFragment(categories.get(position).getStrCategory()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView country;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            country=itemView.findViewById(R.id.item_country);
        }
    }
}
