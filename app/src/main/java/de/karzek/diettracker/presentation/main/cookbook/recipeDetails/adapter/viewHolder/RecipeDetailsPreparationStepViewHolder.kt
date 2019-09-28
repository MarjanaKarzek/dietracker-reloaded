package de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.itemWrapper.RecipeDetailsViewItemWrapper
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_details_preparation_step.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeDetailsPreparationStepViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeDetailsViewItemWrapper) {
        preparation_step_number.text = StringUtils.formatInt(item.preparationStepDisplayModel.stepNo)
        preparation_step_text.text = item.preparationStepDisplayModel.description
    }

}
