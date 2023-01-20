package com.example.foodplanner.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.Model.MealsItem;
import com.example.foodplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMealsAdapter extends RecyclerView.Adapter<FavoriteMealsAdapter.ViewHolder> {

    private Context context;
    List<MealsItem> mealsFavorite = new ArrayList<>();
    private static final String TAG = "FavoriteMealsAdapter";

    public FavoriteMealsAdapter(List<MealsItem> mealsFavorite) {
        this.mealsFavorite = mealsFavorite;
    }

    @NonNull
    @Override
    public FavoriteMealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();                      ////////////
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.row_fav , parent , false);          //\\\\\\\\\\
        ViewHolder viewHolder = new ViewHolder(itemView);
        Log.i(TAG, "onCreateViewHolder: ");
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMealsAdapter.ViewHolder holder, int position) {
        MealsItem mealsItem = mealsFavorite.get(position);
        holder.fav_tv_mealName.setText(mealsItem.getStrMeal());
        holder.fav_tv_mealArea.setText(mealsItem.getStrArea());

        Glide.with(context).load(mealsItem.getStrMealThumb()).into(holder.fav_img_mealImg);

        holder.btn_removeFromFavorites.setOnClickListener(new View.OnClickListener() {        //\\\\\\\
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("users").document(mealsItem.documentID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "DocumentSnapshot successfully deleted!");
                                //(FavoriteMealsAdapter.this).notifyDataSetChanged();
                                mealsFavorite.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "Error deleting document", e);
                            }
                        });                                            //\\\\\\\
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealsFavorite.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fav_tv_mealName;
        public TextView fav_tv_mealArea;
        public ImageView fav_img_mealImg;
        public Button btn_removeFromFavorites;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fav_tv_mealName = itemView.findViewById(R.id.fav_tv_mealName);
            fav_tv_mealArea = itemView.findViewById(R.id.fav_tv_mealArea);
            fav_img_mealImg = itemView.findViewById(R.id.fav_img_mealImg);
            btn_removeFromFavorites = itemView.findViewById(R.id.btn_removeFav);
        }
    }
}
