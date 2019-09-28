package de.karzek.diettracker.presentation.main.diary.meal.adapter.favoriteRecipeList.viewHolder

import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.model.RecipeDisplayModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_diary_favorite_recipe.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class FavoriteRecipeViewHolder(override val containerView: View,
                               private val onItemClickedListener: OnFavoriteRecipeItemClickedListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(entry: RecipeDisplayModel) {
        if (entry.photo != null)
            favorite_recipe_image.setImageBitmap(BitmapFactory.decodeByteArray(entry.photo, 0, entry.photo.size))
        else
            favorite_recipe_image.setImageDrawable(containerView.context.getDrawable(R.drawable.ic_chef_accent_dark))
        favorite_recipe_title.text = entry.title

        itemView.tag = entry.id
        favorite_recipe_image.setOnClickListener { onItemClickedListener.onFavoriteRecipeItemClicked(itemView.tag as Int) }
    }

    interface OnFavoriteRecipeItemClickedListener {
        fun onFavoriteRecipeItemClicked(id: Int)
    }
}
