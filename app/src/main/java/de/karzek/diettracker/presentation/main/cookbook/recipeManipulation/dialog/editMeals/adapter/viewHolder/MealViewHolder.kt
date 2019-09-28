package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog.editMeals.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.model.MealDisplayModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_dialog_checkbox.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class MealViewHolder(override val containerView: View,
                     private val onItemCheckedChangeListener: OnItemCheckedChangeListener
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(displayModel: MealDisplayModel, status: Boolean) {
        itemView.tag = displayModel.id

        checkbox.text = displayModel.name
        checkbox.isChecked = status

        checkbox.setOnClickListener { onItemCheckedChangeListener.onItemCheckChanged(itemView.tag as Int, checkbox.isChecked) }
    }

    interface OnItemCheckedChangeListener {
        fun onItemCheckChanged(id: Int, checked: Boolean)
    }

}
