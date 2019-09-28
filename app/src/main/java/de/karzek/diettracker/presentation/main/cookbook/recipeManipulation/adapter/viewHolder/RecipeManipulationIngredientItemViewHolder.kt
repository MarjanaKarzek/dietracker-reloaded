package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.itemWrapper.RecipeManipulationViewItemWrapper
import de.karzek.diettracker.presentation.model.IngredientDisplayModel
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
class RecipeManipulationIngredientItemViewHolder(override val containerView: View,
                                                 private val onDeleteIngredientClickListener: OnDeleteIngredientClickListener,
                                                 private val onIngredientClickListener: OnIngredientClickListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeManipulationViewItemWrapper, id: Int) {
        ingredient_summary.text = formatSummary(item.ingredientDisplayModel)
        itemView.tag = id

        setupClickListeners()
    }

    private fun setupClickListeners() {
        action_delete.setOnClickListener { onDeleteIngredientClickListener.onDeleteIngredientClicked(itemView.tag as Int) }
        item_layout.setOnClickListener { onIngredientClickListener.onIngredientClicked(itemView.tag as Int) }
    }

    private fun formatSummary(ingredient: IngredientDisplayModel): String {
        val amount = StringUtils.formatFloat(ingredient.amount)
        return amount + ingredient.unit.name + " " + ingredient.grocery.name
    }

    interface OnDeleteIngredientClickListener {
        fun onDeleteIngredientClicked(id: Int)
    }

    interface OnIngredientClickListener {
        fun onIngredientClicked(id: Int)
    }

}
