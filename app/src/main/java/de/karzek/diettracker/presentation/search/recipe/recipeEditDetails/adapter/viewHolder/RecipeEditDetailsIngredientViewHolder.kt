package de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.model.IngredientDisplayModel
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.itemWrapper.RecipeEditDetailsViewItemWrapper
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_ingredient.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeEditDetailsIngredientViewHolder(override val containerView: View,
                                            private val onDeleteIngredientClickListener: OnDeleteIngredientClickListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeEditDetailsViewItemWrapper, id: Int) {
        ingredient_summary.text = formatSummary(item.ingredientDisplayModel, item.portions)
        itemView.tag = id
        action_delete.setOnClickListener {
            onDeleteIngredientClickListener.onDeleteIngredientClicked(itemView.tag as Int)
        }
    }

    private fun formatSummary(ingredient: IngredientDisplayModel, portions: Float): String {
        val amount = StringUtils.formatFloat(ingredient.amount * portions)
        return amount + ingredient.unit.name + " " + ingredient.grocery.name
    }

    interface OnDeleteIngredientClickListener {
        fun onDeleteIngredientClicked(id: Int)
    }

}
