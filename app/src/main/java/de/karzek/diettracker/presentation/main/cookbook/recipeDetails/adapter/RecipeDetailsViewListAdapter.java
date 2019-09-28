package de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.karzek.diettracker.R;
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.itemWrapper.RecipeDetailsViewItemWrapper;
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder.RecipeDetailsCaloryAndMacroDetailsViewHolder;
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder.RecipeDetailsCaloryDetailsViewHolder;
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder.RecipeDetailsIngredientViewHolder;
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder.RecipeDetailsIngredientsAndPortionsTitleViewHolder;
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder.RecipeDetailsMealsViewHolder;
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder.RecipeDetailsPhotoViewHolder;
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder.RecipeDetailsPreparationStepViewHolder;
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder.RecipeDetailsTitleViewHolder;

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
public class RecipeDetailsViewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<RecipeDetailsViewItemWrapper> list;

    private RecipeDetailsIngredientsAndPortionsTitleViewHolder.OnExpandNutritionDetailsViewClickListener onExpandNutritionDetailsViewClickListener;

    public RecipeDetailsViewListAdapter(RecipeDetailsIngredientsAndPortionsTitleViewHolder.OnExpandNutritionDetailsViewClickListener onExpandNutritionDetailsViewClickListener){
        list = new ArrayList<>();

        this.onExpandNutritionDetailsViewClickListener = onExpandNutritionDetailsViewClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case RecipeDetailsViewItemWrapper.ItemType.PHOTO_VIEW:
                return new RecipeDetailsPhotoViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_details_photo, parent, false
                ));
            case RecipeDetailsViewItemWrapper.ItemType.INGREDIENTS_TITLE_VIEW:
                return new RecipeDetailsIngredientsAndPortionsTitleViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_details_ingredients_title_and_portions, parent, false
                ), onExpandNutritionDetailsViewClickListener);
            case RecipeDetailsViewItemWrapper.ItemType.CALORY_DETAILS_VIEW:
                return new RecipeDetailsCaloryDetailsViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_calory_details, parent, false
                ));
            case RecipeDetailsViewItemWrapper.ItemType.CALORIES_AND_MACROS_DETAILS_VIEW:
                return new RecipeDetailsCaloryAndMacroDetailsViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_calory_and_macro_details, parent, false
                ));
            case RecipeDetailsViewItemWrapper.ItemType.INGREDIENT_VIEW:
                return new RecipeDetailsIngredientViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_details_ingredient, parent, false
                ));
            case RecipeDetailsViewItemWrapper.ItemType.TITLE_VIEW:
                return new RecipeDetailsTitleViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_title, parent, false
                ));
            case RecipeDetailsViewItemWrapper.ItemType.PREPARATION_STEP_VIEW:
                return new RecipeDetailsPreparationStepViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_details_preparation_step, parent, false
                ));
            case RecipeDetailsViewItemWrapper.ItemType.MEALS_VIEW:
                return new RecipeDetailsMealsViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_details_meal_list, parent, false
                ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeDetailsPhotoViewHolder) {
            ((RecipeDetailsPhotoViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeDetailsIngredientsAndPortionsTitleViewHolder) {
            ((RecipeDetailsIngredientsAndPortionsTitleViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeDetailsCaloryDetailsViewHolder) {
            ((RecipeDetailsCaloryDetailsViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeDetailsCaloryAndMacroDetailsViewHolder) {
            ((RecipeDetailsCaloryAndMacroDetailsViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeDetailsIngredientViewHolder) {
            ((RecipeDetailsIngredientViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeDetailsTitleViewHolder) {
            ((RecipeDetailsTitleViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeDetailsPreparationStepViewHolder) {
            ((RecipeDetailsPreparationStepViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeDetailsMealsViewHolder) {
            ((RecipeDetailsMealsViewHolder) holder).bind(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<RecipeDetailsViewItemWrapper> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override public int getItemViewType(int position) {
        return list.get(position).getType();
    }

}
