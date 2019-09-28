package de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.itemWrapper.RecipeDetailsViewItemWrapper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_details_ingredients_title_and_portions.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeDetailsIngredientsAndPortionsTitleViewHolder(override val containerView: View,
                                                         private val onExpandNutritionDetailsViewClickListener: OnExpandNutritionDetailsViewClickListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private var expanded = false

    fun bind(item: RecipeDetailsViewItemWrapper) {
        recipe_details_ingredients_title.text = item.title

        expandable_nutrition_details_action.setOnClickListener {
            expanded = !expanded
            if (expanded)
                expandable_nutrition_details_action.setImageDrawable(containerView.context.getDrawable(R.drawable.ic_expand_less_primary_text))
            else
                expandable_nutrition_details_action.setImageDrawable(containerView.context.getDrawable(R.drawable.ic_expand_more_primary_text))
            onExpandNutritionDetailsViewClickListener.onExpandNutritionDetailsViewClicked()
        }
    }

    interface OnExpandNutritionDetailsViewClickListener {
        fun onExpandNutritionDetailsViewClicked()
    }

}
