package de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.itemWrapper.RecipeEditDetailsViewItemWrapper
import de.karzek.diettracker.presentation.util.Constants
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.snippet_calory_detail.*
import kotlinx.android.synthetic.main.viewholder_recipe_calory_details.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeEditDetailsCaloryDetailsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeEditDetailsViewItemWrapper) {
        circle_progress_bar_calories_max_value.text = "${item.maxValues[Constants.CALORIES]!!}"
        circle_progress_bar_calories.progress = 100.0f / item.maxValues[Constants.CALORIES]!! * item.values[Constants.CALORIES]!!
        circle_progress_bar_calories_value.text = "${item.values[Constants.CALORIES]!!.toFloat().toInt()}"

        portions_hint.visibility = View.GONE
    }

}
