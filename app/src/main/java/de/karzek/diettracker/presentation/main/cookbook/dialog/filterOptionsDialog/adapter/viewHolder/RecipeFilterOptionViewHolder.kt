package de.karzek.diettracker.presentation.main.cookbook.dialog.filterOptionsDialog.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_dialog_checkbox.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeFilterOptionViewHolder(override val containerView: View,
                                   private val onItemCheckedChangeListener: OnItemCheckedChangeListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(value: String, status: Boolean) {
        itemView.tag = value
        checkbox.text = value
        checkbox.isChecked = status
        checkbox.setOnClickListener {
            onItemCheckedChangeListener.onItemCheckChanged(itemView.tag as String, checkbox.isChecked)
        }
    }

    interface OnItemCheckedChangeListener {
        fun onItemCheckChanged(option: String, checked: Boolean)
    }

}
