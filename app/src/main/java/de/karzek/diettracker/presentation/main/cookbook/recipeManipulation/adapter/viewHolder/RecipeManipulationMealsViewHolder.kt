package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.itemWrapper.RecipeManipulationViewItemWrapper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_man_meal_list.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeManipulationMealsViewHolder(override val containerView: View,
                                        private val onEditMealsClickedListener: OnEditMealsClickedListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeManipulationViewItemWrapper) {
        var meals = ""

        for (i in 0 until item.meals.size) {
            meals += item.meals[i].name
            if (i < item.meals.size - 1)
                meals += ", "
        }

        if (item.meals.size > 0)
            text_meals.text = meals

        setupClickListeners()
    }

    private fun setupClickListeners() {
        text_meals.setOnClickListener { onEditMealsClickedListener.onEditMealsClicked() }
    }

    interface OnEditMealsClickedListener {
        fun onEditMealsClicked()
    }

}
