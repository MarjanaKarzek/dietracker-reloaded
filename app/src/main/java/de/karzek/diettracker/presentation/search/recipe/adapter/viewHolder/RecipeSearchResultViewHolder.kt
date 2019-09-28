package de.karzek.diettracker.presentation.search.recipe.adapter.viewHolder

import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.model.RecipeDisplayModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_recipe_search_result.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeSearchResultViewHolder(override val containerView: View,
                                   private val onRecipeItemClickedListener: OnRecipeItemClickedListener,
                                   private val onRecipeAddPortionClickedListener: OnRecipeAddPortionClickedListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(recipe: RecipeDisplayModel) {
        if (recipe.photo != null)
            recipe_image.setImageBitmap(BitmapFactory.decodeByteArray(recipe.photo, 0, recipe.photo.size))
        recipe_title.text = recipe.title
        itemView.tag = recipe.id

        item_layout.setOnClickListener { onRecipeItemClickedListener.onItemClicked(itemView.tag as Int) }
        recipe_add_portion.setOnClickListener { onRecipeAddPortionClickedListener.onAddPortionClicked(itemView.tag as Int) }
    }

    interface OnRecipeItemClickedListener {
        fun onItemClicked(id: Int)
    }

    interface OnRecipeAddPortionClickedListener {
        fun onAddPortionClicked(id: Int)
    }

}
