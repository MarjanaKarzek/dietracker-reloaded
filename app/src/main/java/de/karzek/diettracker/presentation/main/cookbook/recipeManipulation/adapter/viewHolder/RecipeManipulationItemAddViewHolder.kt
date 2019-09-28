package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.itemWrapper.RecipeManipulationViewItemWrapper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_man_ingredient_add.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeManipulationItemAddViewHolder(override val containerView: View,
                                          private val onAddManualIngredientClickListener: OnAddManualIngredientClickListener,
                                          private val onStartGrocerySearchClickListener: OnStartGrocerySearchClickListener,
                                          private val onStartBarcodeScanClickListener: OnStartBarcodeScanClickListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeManipulationViewItemWrapper) {
        setupClickListeners()
    }

    private fun setupClickListeners() {
        add_ingredient.setOnClickListener { onAddManualIngredientClickListener.onAddManualIngredientClicked() }
        action_search.setOnClickListener { onStartGrocerySearchClickListener.onStartGrocerySearchClicked() }
        action_barcode_search.setOnClickListener { onStartBarcodeScanClickListener.onStartBarcodeScanClicked() }
    }

    interface OnAddManualIngredientClickListener {
        fun onAddManualIngredientClicked()
    }

    interface OnStartGrocerySearchClickListener {
        fun onStartGrocerySearchClicked()
    }

    interface OnStartBarcodeScanClickListener {
        fun onStartBarcodeScanClicked()
    }

}
