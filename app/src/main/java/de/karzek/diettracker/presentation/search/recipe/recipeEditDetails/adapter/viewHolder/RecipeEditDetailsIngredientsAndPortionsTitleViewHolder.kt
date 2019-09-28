package de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.itemWrapper.RecipeEditDetailsViewItemWrapper
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_edit_details_ingredients_title_and_portions.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeEditDetailsIngredientsAndPortionsTitleViewHolder(override val containerView: View,
                                                             private val onPortionChangedListener: OnPortionChangedListener,
                                                             private val onExpandNutritionDetailsViewClickListener: OnExpandNutritionDetailsViewClickListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private var expanded = false

    private fun setupPortionsChangedListener() {
        value_portions.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onPortionChangedListener.onPortionChanged(java.lang.Float.valueOf(value_portions.text.toString()))
            }
            true
        }
    }

    fun bind(item: RecipeEditDetailsViewItemWrapper) {
        value_portions.setText(StringUtils.formatFloat(item.portions))
        setupPortionsChangedListener()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        remove_portion.setOnClickListener {
            val newValue = java.lang.Float.valueOf(value_portions.text.toString()) - 1
            value_portions.setText(StringUtils.formatFloat(newValue))
            onPortionChangedListener.onPortionChanged(newValue)
        }
        add_portion.setOnClickListener {
            val newValue = java.lang.Float.valueOf(value_portions.text.toString()) + 1
            value_portions.setText(StringUtils.formatFloat(newValue))
            onPortionChangedListener.onPortionChanged(newValue)
        }
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

    interface OnPortionChangedListener {
        fun onPortionChanged(portion: Float)
    }

}
