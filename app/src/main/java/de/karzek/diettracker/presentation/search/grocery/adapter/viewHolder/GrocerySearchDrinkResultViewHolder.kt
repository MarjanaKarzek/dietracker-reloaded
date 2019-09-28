package de.karzek.diettracker.presentation.search.grocery.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.model.GroceryDisplayModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_grocery_search_drink.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class GrocerySearchDrinkResultViewHolder(override val containerView: View,
                                         private val onItemClickedListener: OnGrocerySearchDrinkResultItemClickedListener,
                                         private val onAddBottleClickedListener: OnGrocerySearchDrinkResultAddBottleClickedListener,
                                         private val onAddGlassClickedListener: OnGrocerySearchDrinkResultAddGlassClickedListener
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(foodSearchResultItem: GroceryDisplayModel) {
        grocery_name.text = foodSearchResultItem.name
        itemView.tag = foodSearchResultItem.id

        grocery_search_drink_item.setOnClickListener { onItemClickedListener.onItemClicked(itemView.tag as Int) }
        action_add_bottle.setOnClickListener { onAddBottleClickedListener.onAddBottleClicked(itemView.tag as Int) }
        action_add_glass.setOnClickListener { onAddGlassClickedListener.onAddGlassClicked(itemView.tag as Int) }
    }

    interface OnGrocerySearchDrinkResultItemClickedListener {
        fun onItemClicked(id: Int)
    }

    interface OnGrocerySearchDrinkResultAddBottleClickedListener {
        fun onAddBottleClicked(id: Int)
    }

    interface OnGrocerySearchDrinkResultAddGlassClickedListener {
        fun onAddGlassClicked(id: Int)
    }

}
