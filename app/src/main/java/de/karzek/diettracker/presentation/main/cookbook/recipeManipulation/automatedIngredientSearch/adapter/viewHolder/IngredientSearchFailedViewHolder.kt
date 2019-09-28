package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.automatedIngredientSearch.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.automatedIngredientSearch.AutomatedIngredientSearchContract
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.automatedIngredientSearch.adapter.itemWrapper.IngredientSearchItemWrapper
import de.karzek.diettracker.presentation.model.IngredientDisplayModel
import de.karzek.diettracker.presentation.model.ManualIngredientDisplayModel
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_ingredient_search_failed.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class IngredientSearchFailedViewHolder(override val containerView: View,
                                       private val onStartGrocerySearchClickListener: OnStartGrocerySearchClickListener,
                                       private val onStartBarcodeScanClickListener: OnStartBarcodeScanClickListener,
                                       private val onDeleteIngredientClickListener: OnDeleteIngredientClickListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: IngredientSearchItemWrapper, index: Int) {
        ingredient_summary.text = formatSummary(item.ingredientDisplayModel)
        if (item.failReason == AutomatedIngredientSearchContract.FailReasons.FAIL_REASON_WRONG_UNIT)
            fail_reason.text = containerView.context.getString(R.string.fail_reason_description_wrong_unit)
        else
            fail_reason.text = containerView.context.getString(R.string.fail_reason_description_grocery_not_found)

        itemView.tag = index
        setupClickListeners()
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

    private fun setupClickListeners() {
        action_search.setOnClickListener { onStartGrocerySearchClickListener.onStartGrocerySearchClicked(itemView.tag as Int) }
        action_barcode_search.setOnClickListener { onStartBarcodeScanClickListener.onStartBarcodeScanClicked(itemView.tag as Int) }
        action_delete.setOnClickListener { onDeleteIngredientClickListener.onDeleteIngredientClicked(itemView.tag as Int) }
    }

    interface OnStartGrocerySearchClickListener {
        fun onStartGrocerySearchClicked(index: Int)
    }

    interface OnStartBarcodeScanClickListener {
        fun onStartBarcodeScanClicked(index: Int)
    }

    interface OnDeleteIngredientClickListener {
        fun onDeleteIngredientClicked(index: Int)
    }

}
