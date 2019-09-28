package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.viewHolder

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.itemWrapper.RecipeManipulationViewItemWrapper
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_man_ingredients_title_and_portions.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeManipulationIngredientsTitleAndPortionsViewHolder(override val containerView: View,
                                                              private val onPortionChangedListener: OnPortionChangedListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private fun setupPortionsChangedListener() {
        value_portions.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onPortionChangedListener.onPortionChanges(java.lang.Float.valueOf(value_portions.text.toString()))
            }
            true
        }
        value_portions.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> onPortionChangedListener.onPortionChanges(java.lang.Float.valueOf(value_portions.text.toString())) }
    }

    fun bind(item: RecipeManipulationViewItemWrapper) {
        value_portions.setText(StringUtils.formatFloat(item.portions!!))

        setupPortionsChangedListener()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        remove_portion.setOnClickListener {
            val newValue = java.lang.Float.valueOf(value_portions.text.toString()) - 1
            value_portions.setText(StringUtils.formatFloat(newValue))
            onPortionChangedListener.onPortionChanges(newValue)
        }
        add_portion.setOnClickListener {
            val newValue = java.lang.Float.valueOf(value_portions.text.toString()) + 1
            value_portions.setText(StringUtils.formatFloat(newValue))
            onPortionChangedListener.onPortionChanges(newValue)
        }
    }

    interface OnPortionChangedListener {
        fun onPortionChanges(portion: Float)
    }

}
