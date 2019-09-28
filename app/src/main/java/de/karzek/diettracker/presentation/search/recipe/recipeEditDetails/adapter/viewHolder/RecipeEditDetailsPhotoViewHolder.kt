package de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.itemWrapper.RecipeEditDetailsViewItemWrapper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_details_photo.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeEditDetailsPhotoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RecipeEditDetailsViewItemWrapper) {
        recipe_image.setImageBitmap(item.image)
    }

}
