package de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.itemWrapper.RecipeEditDetailsViewItemWrapper
import de.karzek.diettracker.presentation.util.Constants
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.snippet_calory_and_macro_detail.*
import kotlinx.android.synthetic.main.viewholder_recipe_calory_and_macro_details.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeEditDetailsCaloryAndMacroDetailsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeEditDetailsViewItemWrapper) {
        circle_progress_bar_calories_max_value.text = "${item.maxValues[Constants.CALORIES]!!}"
        circle_progress_bar_calories.progress = 100.0f / item.maxValues[Constants.CALORIES]!! * item.values[Constants.CALORIES]!!
        circle_progress_bar_calories_value.text = "${item.values[Constants.CALORIES]!!.toFloat().toInt()}"

        circle_progress_bar_protein_max_value.text = "${item.maxValues[Constants.PROTEINS]!!}"
        circle_progress_bar_protein.progress = 100.0f / item.maxValues[Constants.PROTEINS]!! * item.values[Constants.PROTEINS]!!
        circle_progress_bar_protein_value.text = "${item.values[Constants.PROTEINS]!!.toFloat().toInt()}"

        circle_progress_bar_carbs_max_value.text = "${item.maxValues[Constants.CARBS]!!}"
        circle_progress_bar_carbs.progress = 100.0f / item.maxValues[Constants.CARBS]!! * item.values[Constants.CARBS]!!
        circle_progress_bar_carbs_value.text = "${item.values[Constants.CARBS]!!.toFloat().toInt()}"

        circle_progress_bar_fats_max_value.text = "${item.maxValues[Constants.FATS]!!}"
        circle_progress_bar_fats.progress = 100.0f / item.maxValues[Constants.FATS]!! * item.values[Constants.FATS]!!
        circle_progress_bar_fats_value.text = "${item.values[Constants.FATS]!!.toFloat().toInt()}"

        portions_hint.visibility = View.GONE
    }

}
