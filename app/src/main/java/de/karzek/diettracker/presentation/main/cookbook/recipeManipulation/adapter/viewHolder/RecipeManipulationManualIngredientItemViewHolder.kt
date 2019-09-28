package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.itemWrapper.RecipeManipulationViewItemWrapper
import de.karzek.diettracker.presentation.model.ManualIngredientDisplayModel
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
class RecipeManipulationManualIngredientItemViewHolder(override val containerView: View,
                                                       private val onDeleteIngredientClickListener: OnDeleteIngredientClickListener,
                                                       private val onManualIngredientClickedListener: OnManualIngredientClickedListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeManipulationViewItemWrapper, id: Int) {
        ingredient_summary.text = formatSummary(item.ingredientDisplayModel as ManualIngredientDisplayModel)
        itemView.tag = id
        setupClickListeners()
    }

    private fun setupClickListeners() {
        action_delete.setOnClickListener { onDeleteIngredientClickListener.onDeleteManualIngredientClicked(itemView.tag as Int) }
        item_layout.setOnClickListener { onManualIngredientClickedListener.onManualIngredientClicked(itemView.tag as Int) }
    }

    private fun formatSummary(ingredient: ManualIngredientDisplayModel): String {
        val amount = StringUtils.formatFloat(ingredient.amount)
        return amount + ingredient.unit.name + " " + ingredient.groceryQuery
    }

    interface OnDeleteIngredientClickListener {
        fun onDeleteManualIngredientClicked(id: Int)
    }

    interface OnManualIngredientClickedListener {
        fun onManualIngredientClicked(id: Int)
    }

}
