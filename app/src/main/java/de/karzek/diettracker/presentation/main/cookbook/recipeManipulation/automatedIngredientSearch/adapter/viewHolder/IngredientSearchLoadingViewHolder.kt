package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.automatedIngredientSearch.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.automatedIngredientSearch.adapter.itemWrapper.IngredientSearchItemWrapper
import de.karzek.diettracker.presentation.model.IngredientDisplayModel
import de.karzek.diettracker.presentation.model.ManualIngredientDisplayModel
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_ingredient_search_loading.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class IngredientSearchLoadingViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: IngredientSearchItemWrapper) {
        ingredient_summary.text = formatSummary(item.ingredientDisplayModel)
        itemView.tag = item.ingredientDisplayModel.id
    }

    private fun formatSummary(ingredient: IngredientDisplayModel): String {
        return if (ingredient is ManualIngredientDisplayModel) {
            val amount = StringUtils.formatFloat(ingredient.amount)
            amount + ingredient.unit.name + " " + ingredient.groceryQuery
        } else {
            val amount = StringUtils.formatFloat(ingredient.amount)
            amount + ingredient.unit.name + " " + ingredient.grocery.name
        }
    }

}

