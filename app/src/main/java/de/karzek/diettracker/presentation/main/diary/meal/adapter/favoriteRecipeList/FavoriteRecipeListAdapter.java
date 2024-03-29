package de.karzek.diettracker.presentation.main.diary.meal.adapter.favoriteRecipeList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.karzek.diettracker.R;
import de.karzek.diettracker.presentation.main.diary.meal.adapter.favoriteRecipeList.viewHolder.FavoriteRecipeViewHolder;
import de.karzek.diettracker.presentation.model.RecipeDisplayModel;

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
public class FavoriteRecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FavoriteRecipeViewHolder.OnFavoriteRecipeItemClickedListener onItemClickListener;

    private ArrayList<RecipeDisplayModel> list;

    public FavoriteRecipeListAdapter(FavoriteRecipeViewHolder.OnFavoriteRecipeItemClickedListener onItemClickListener) {
        list = new ArrayList<>();

        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteRecipeViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_diary_favorite_recipe, parent, false
        ), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FavoriteRecipeViewHolder) holder).bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<RecipeDisplayModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
