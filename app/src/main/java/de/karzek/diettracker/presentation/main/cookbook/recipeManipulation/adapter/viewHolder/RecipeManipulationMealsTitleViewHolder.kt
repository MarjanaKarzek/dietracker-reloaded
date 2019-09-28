package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.itemWrapper.RecipeManipulationViewItemWrapper
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeManipulationMealsTitleViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeManipulationViewItemWrapper) {}

}
