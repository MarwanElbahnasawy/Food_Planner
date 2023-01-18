package com.example.foodplanner.View;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Controller.Fragments.MainFragments.HomeDirections;
import com.example.foodplanner.Model.MealsItem;
import com.example.foodplanner.Model.RootSingleMeal;
import com.example.foodplanner.Network.RetrofitClient;
import com.example.foodplanner.R;
import com.example.foodplanner.databinding.FragmentMealDetailsBinding;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SliderAdapter extends  RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    Context context;
    ViewGroup frag;

    public SliderAdapter(List<SliderItem> sliderItemList, ViewPager2 viewPager2, Context context) {
        this.context = context;
        this.sliderItemList = sliderItemList;
        this.viewPager2 = viewPager2;


    }

    private List<SliderItem> sliderItemList;
    private ViewPager2 viewPager2;

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        frag=parent;
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false)

                );

    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        //holder.imageView.setImageResource(sliderItemList.get(position).getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Observable<RootSingleMeal> observable = RetrofitClient.getInstance().getMyApi().getRootSingleMeal(holder.getTv_mealName().getText().toString());

                Log.i("aaaaaaaaaa", "onClick: should be name" + holder.getTv_mealName().getText());

                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    List<MealsItem> singleMeal = response.getMeals();
                                    Log.i("aaaaaaaaaa", "onClick: " + singleMeal.get(0).getStrInstructions());
                                } ,
                                error -> {
                                    Log.i("aaaaaaaaaa", "onClick: " + "errorrr" + error.toString());
                                    error.printStackTrace();}
                        );

               Navigation.findNavController(frag).navigate(HomeDirections.actionNavHomeToMealDeatailsFragment());

            }
        });
        SliderItem sliderItem = sliderItemList.get(position);
        Glide.with(context).load(sliderItem.getImageURL()).into(holder.imageView);
        holder.tv_mealName.setText(sliderItem.getMealName());

        // to make scrolling infinite part 2/2
        if(position == sliderItemList.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItemList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;
        private TextView tv_mealName;

        SliderViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            tv_mealName = itemView.findViewById(R.id.tv_mealName);
        }

        public TextView getTv_mealName() {
            return tv_mealName;
        }
    }

    // to make scrolling infinite part 1/2
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItemList.addAll(sliderItemList);
            notifyDataSetChanged();
        }
    };
}
