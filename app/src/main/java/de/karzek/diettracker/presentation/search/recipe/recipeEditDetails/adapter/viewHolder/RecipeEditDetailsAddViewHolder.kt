package de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.itemWrapper.RecipeEditDetailsViewItemWrapper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_save.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeEditDetailsAddViewHolder(override val containerView: View,
                                     private val onSaveRecipeClickedListener: OnSaveRecipeClickedListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeEditDetailsViewItemWrapper) {
        add_recipe.setText(R.string.button_add)
        add_recipe.setOnClickListener { onSaveRecipeClickedListener.onSaveRecipeClicked() }
    }

    interface OnSaveRecipeClickedListener {
        fun onSaveRecipeClicked()
    }

}
