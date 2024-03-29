package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog.editMeals.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import de.karzek.diettracker.R;
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog.editMeals.adapter.viewHolder.MealViewHolder;
import de.karzek.diettracker.presentation.model.MealDisplayModel;

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
public class EditMealsListAdapter extends RecyclerView.Adapter<MealViewHolder> {

    private MealViewHolder.OnItemCheckedChangeListener onItemCheckedChangeListener;

    private ArrayList<MealDisplayModel> list;
    private HashMap<Integer, Boolean> status;

    public EditMealsListAdapter(MealViewHolder.OnItemCheckedChangeListener onItemCheckedChangeListener) {
        list = new ArrayList<>();
        this.onItemCheckedChangeListener = onItemCheckedChangeListener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MealViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_dialog_checkbox, parent, false
        ), onItemCheckedChangeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        holder.bind(list.get(position), status.get(list.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setMeals(ArrayList<MealDisplayModel> meals, HashMap<Integer, Boolean> mealStatus) {
        this.list = meals;
        this.status = mealStatus;
        notifyDataSetChanged();
    }
}
