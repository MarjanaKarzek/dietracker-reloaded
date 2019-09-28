package de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.karzek.diettracker.R;
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.itemWrapper.RecipeEditDetailsViewItemWrapper;
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder.RecipeEditDetailsAddViewHolder;
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder.RecipeEditDetailsCaloryAndMacroDetailsViewHolder;
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder.RecipeEditDetailsCaloryDetailsViewHolder;
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder.RecipeEditDetailsDateSelectorViewHolder;
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder.RecipeEditDetailsIngredientViewHolder;
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder.RecipeEditDetailsIngredientsAndPortionsTitleViewHolder;
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder.RecipeEditDetailsMealSelectorViewHolder;
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder.RecipeEditDetailsPhotoViewHolder;

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
public class RecipeEditDetailsViewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<RecipeEditDetailsViewItemWrapper> list;

    private int currentIngredientInnerListId = 0;

    private RecipeEditDetailsIngredientsAndPortionsTitleViewHolder.OnPortionChangedListener onPortionChangedListener;
    private RecipeEditDetailsIngredientsAndPortionsTitleViewHolder.OnExpandNutritionDetailsViewClickListener onExpandNutritionDetailsViewClickListener;
    private RecipeEditDetailsIngredientViewHolder.OnDeleteIngredientClickListener onDeleteIngredientClickListener;
    private RecipeEditDetailsMealSelectorViewHolder.OnMealItemSelectedListener onMealItemSelectedListener;
    private RecipeEditDetailsDateSelectorViewHolder.OnDateClickListener onDateClickListener;
    private RecipeEditDetailsAddViewHolder.OnSaveRecipeClickedListener onSaveRecipeClickedListener;

    public RecipeEditDetailsViewListAdapter(RecipeEditDetailsIngredientsAndPortionsTitleViewHolder.OnPortionChangedListener onPortionChangedListener,
                                            RecipeEditDetailsIngredientsAndPortionsTitleViewHolder.OnExpandNutritionDetailsViewClickListener onExpandNutritionDetailsViewClickListener,
                                            RecipeEditDetailsIngredientViewHolder.OnDeleteIngredientClickListener onDeleteIngredientClickListener,
                                            RecipeEditDetailsMealSelectorViewHolder.OnMealItemSelectedListener onMealItemSelectedListener,
                                            RecipeEditDetailsDateSelectorViewHolder.OnDateClickListener onDateClickListener,
                                            RecipeEditDetailsAddViewHolder.OnSaveRecipeClickedListener onSaveRecipeClickedListener){
        list = new ArrayList<>();

        this.onPortionChangedListener = onPortionChangedListener;
        this.onExpandNutritionDetailsViewClickListener = onExpandNutritionDetailsViewClickListener;
        this.onDeleteIngredientClickListener = onDeleteIngredientClickListener;
        this.onMealItemSelectedListener = onMealItemSelectedListener;
        this.onDateClickListener = onDateClickListener;
        this.onSaveRecipeClickedListener = onSaveRecipeClickedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case RecipeEditDetailsViewItemWrapper.ItemType.PHOTO_VIEW:
                return new RecipeEditDetailsPhotoViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_details_photo, parent, false
                ));
            case RecipeEditDetailsViewItemWrapper.ItemType.INGREDIENTS_TITLE_VIEW:
                return new RecipeEditDetailsIngredientsAndPortionsTitleViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_edit_details_ingredients_title_and_portions, parent, false
                ), onPortionChangedListener, onExpandNutritionDetailsViewClickListener);
            case RecipeEditDetailsViewItemWrapper.ItemType.CALORY_DETAILS_VIEW:
                return new RecipeEditDetailsCaloryDetailsViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_calory_details, parent, false
                ));
            case RecipeEditDetailsViewItemWrapper.ItemType.CALORIES_AND_MACROS_DETAILS_VIEW:
                return new RecipeEditDetailsCaloryAndMacroDetailsViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_calory_and_macro_details, parent, false
                ));
            case RecipeEditDetailsViewItemWrapper.ItemType.INGREDIENT_VIEW:
                return new RecipeEditDetailsIngredientViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_ingredient, parent, false
                ), onDeleteIngredientClickListener);
            case RecipeEditDetailsViewItemWrapper.ItemType.MEAL_SELECTOR_VIEW:
                return new RecipeEditDetailsMealSelectorViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_edit_details_meal_selector, parent, false
                ), onMealItemSelectedListener);
            case RecipeEditDetailsViewItemWrapper.ItemType.DATE_SELECTOR_VIEW:
                return new RecipeEditDetailsDateSelectorViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_edit_details_date_selector, parent, false
                ), onDateClickListener);
            case RecipeEditDetailsViewItemWrapper.ItemType.ADD_VIEW:
                return new RecipeEditDetailsAddViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.viewholder_recipe_save, parent, false
                ), onSaveRecipeClickedListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeEditDetailsPhotoViewHolder) {
            ((RecipeEditDetailsPhotoViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeEditDetailsIngredientsAndPortionsTitleViewHolder) {
            ((RecipeEditDetailsIngredientsAndPortionsTitleViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeEditDetailsCaloryDetailsViewHolder) {
            ((RecipeEditDetailsCaloryDetailsViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeEditDetailsCaloryAndMacroDetailsViewHolder) {
            ((RecipeEditDetailsCaloryAndMacroDetailsViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeEditDetailsIngredientViewHolder) {
            ((RecipeEditDetailsIngredientViewHolder) holder).bind(list.get(position), currentIngredientInnerListId);
            currentIngredientInnerListId++;
        } else if (holder instanceof RecipeEditDetailsMealSelectorViewHolder) {
            ((RecipeEditDetailsMealSelectorViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeEditDetailsDateSelectorViewHolder) {
            ((RecipeEditDetailsDateSelectorViewHolder) holder).bind(list.get(position));
        } else if (holder instanceof RecipeEditDetailsAddViewHolder) {
            ((RecipeEditDetailsAddViewHolder) holder).bind(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<RecipeEditDetailsViewItemWrapper> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override public int getItemViewType(int position) {
        return list.get(position).getType();
    }

}
