package de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.itemWrapper.RecipeDetailsViewItemWrapper
import de.karzek.diettracker.presentation.util.Constants
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.snippet_calory_detail.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeDetailsCaloryDetailsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeDetailsViewItemWrapper) {
        circle_progress_bar_calories_max_value.text = "${item.maxValues[Constants.CALORIES]!!}"
        circle_progress_bar_calories.progress = 100.0f / item.maxValues[Constants.CALORIES]!! * item.values[Constants.CALORIES]!!
        circle_progress_bar_calories_value.text = "${item.values[Constants.CALORIES]!!.toFloat().toInt()}"
    }

}
