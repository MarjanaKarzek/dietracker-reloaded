package de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.itemWrapper.RecipeDetailsViewItemWrapper
import de.karzek.diettracker.presentation.model.IngredientDisplayModel
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_details_ingredient.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeDetailsIngredientViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {


    fun bind(item: RecipeDetailsViewItemWrapper) {
        ingredient_summary.text = formatSummary(item.ingredientDisplayModel)
    }

    private fun formatSummary(ingredient: IngredientDisplayModel): String {
        val amount = StringUtils.formatFloat(ingredient.amount)
        return amount + ingredient.unit.name + " " + ingredient.grocery.name
    }

}
