package com.example.foodplanner.Controller.Fragments.MainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Model.MealsItem;
import com.example.foodplanner.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MealDetailsFragment extends Fragment {
    TextView mealName, area, instructions;
    ImageView mealImage;
    YouTubePlayerView videoView;
    String[] split;
    Boolean youtubeURLisExists = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MealsItem mealsItem = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealDetailsArgs();

        mealName = view.findViewById(R.id.tv_mealName);
        area = view.findViewById(R.id.tv_meal_area);
        instructions = view.findViewById(R.id.tv_Meal_instructions);
        mealImage = view.findViewById(R.id.mealImage);
        videoView = view.findViewById(R.id.video);
        getLifecycle().addObserver((LifecycleObserver) videoView);


        if(!mealsItem.getStrYoutube().equals("")){

            split = mealsItem.getStrYoutube().split("=");
            youtubeURLisExists = true;
            videoView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    if(youtubeURLisExists){
                        String videoId = split[1];
                        youTubePlayer.loadVideo(videoId, 0);
                    }
                }
            });
        }
        else{
            videoView.setVisibility(View.GONE);
        }

        mealName.setText(mealsItem.getStrMeal());
        area.setText(mealsItem.getStrArea());
        Glide.with(view.getContext()).load(mealsItem.getStrMealThumb()).into(mealImage);
        instructions.setText(mealsItem.getStrInstructions());


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    public MealDetailsFragment() {

    }
}