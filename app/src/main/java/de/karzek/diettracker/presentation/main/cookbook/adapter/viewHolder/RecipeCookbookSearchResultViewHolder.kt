package de.karzek.diettracker.presentation.main.cookbook.adapter.viewHolder

import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.model.RecipeDisplayModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cookbook_recipe.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeCookbookSearchResultViewHolder(override val containerView: View,
                                           private val onRecipeItemClickedListener: OnRecipeItemClickedListener,
                                           private val onRecipeAddPortionClickedListener: OnRecipeAddPortionClickedListener,
                                           private val onRecipeEditClickedListener: OnRecipeEditClickedListener,
                                           private val onRecipeDeleteClickedListener: OnRecipeDeleteClickedListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(recipe: RecipeDisplayModel) {
        if (recipe.photo != null)
            recipe_image.setImageBitmap(BitmapFactory.decodeByteArray(recipe.photo, 0, recipe.photo.size))
        recipe_title.text = recipe.title
        itemView.tag = recipe.id

        setupClickListeners()
    }

    private fun setupClickListeners() {
        item_layout.setOnClickListener {
            swipe_layout.close()
            onRecipeItemClickedListener.onItemClicked(itemView.tag as Int)
        }
        swipe_option_portion.setOnClickListener { onRecipeAddPortionClickedListener.onAddPortionClicked(itemView.tag as Int) }
        swipe_option_edit.setOnClickListener {
            swipe_layout.close()
            onRecipeEditClickedListener.onEditRecipeClicked(itemView.tag as Int)
        }
        swipe_option_delete.setOnClickListener {
            swipe_layout.close()
            onRecipeDeleteClickedListener.onDeleteRecipeClicked(itemView.tag as Int)
        }
    }

    interface OnRecipeItemClickedListener {
        fun onItemClicked(id: Int)
    }

    interface OnRecipeAddPortionClickedListener {
        fun onAddPortionClicked(id: Int)
    }

    interface OnRecipeEditClickedListener {
        fun onEditRecipeClicked(id: Int)
    }

    interface OnRecipeDeleteClickedListener {
        fun onDeleteRecipeClicked(id: Int)
    }

}
