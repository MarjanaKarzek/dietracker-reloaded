package de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.itemWrapper.RecipeDetailsViewItemWrapper
import de.karzek.diettracker.presentation.util.Constants
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.snippet_calory_and_macro_detail.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeDetailsCaloryAndMacroDetailsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeDetailsViewItemWrapper) {
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
    }

}
