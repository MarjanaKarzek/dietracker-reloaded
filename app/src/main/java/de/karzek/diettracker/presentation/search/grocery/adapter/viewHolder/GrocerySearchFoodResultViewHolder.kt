package de.karzek.diettracker.presentation.search.grocery.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.model.GroceryDisplayModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_grocery_search_food.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class GrocerySearchFoodResultViewHolder(override val containerView: View,
                                        private val onItemClickedListener: OnFoodSearchResultItemClickedListener
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(foodSearchResultItem: GroceryDisplayModel) {
        grocery_name.text = foodSearchResultItem.name
        itemView.tag = foodSearchResultItem.id

        grocery_search_food_item.setOnClickListener { onItemClickedListener.onItemClicked(itemView.tag as Int) }
    }

    interface OnFoodSearchResultItemClickedListener {
        fun onItemClicked(id: Int)
    }

}
