package de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder

import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.itemWrapper.RecipeEditDetailsViewItemWrapper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_edit_details_meal_selector.*
import java.util.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeEditDetailsMealSelectorViewHolder(override val containerView: View,
                                              private val onMealItemSelectedListener: OnMealItemSelectedListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeEditDetailsViewItemWrapper) {
        val mealNames = ArrayList<String>()
        for (meal in item.meals)
            mealNames.add(meal.name)

        val mealAdapter = ArrayAdapter(containerView.context, android.R.layout.simple_spinner_item, mealNames)
        mealAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_meal.adapter = mealAdapter
        spinner_meal.setSelection(item.selectedMeal)
        spinner_meal.setOnClickListener { onMealItemSelectedListener.onMealItemSelected(spinner_meal.selectedItemPosition) }
    }

    interface OnMealItemSelectedListener {
        fun onMealItemSelected(id: Int)
    }

}
