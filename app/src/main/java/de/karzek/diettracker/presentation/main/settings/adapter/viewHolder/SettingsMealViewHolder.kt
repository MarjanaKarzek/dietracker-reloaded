package de.karzek.diettracker.presentation.main.settings.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.model.MealDisplayModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_settings_meal.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class SettingsMealViewHolder(override val containerView: View,
                             private val onEditMealClickedListener: OnEditMealClickedListener,
                             private val onDeleteMealClickedListener: OnDeleteMealClickedListener,
                             private val lastItem: Boolean) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(meal: MealDisplayModel) {
        meal_name.text = meal.name

        itemView.tag = meal.id
        if (lastItem) {
            action_delete_meal.visibility = View.GONE
        }

        action_meal_time.setOnClickListener { onEditMealClickedListener.onEditMealItemClicked(itemView.tag as Int) }
        action_delete_meal.setOnClickListener { onDeleteMealClickedListener.onMealItemDelete(itemView.tag as Int) }
    }

    interface OnEditMealClickedListener {
        fun onEditMealItemClicked(id: Int)
    }

    interface OnDeleteMealClickedListener {
        fun onMealItemDelete(id: Int)
    }

}
