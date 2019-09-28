package de.karzek.diettracker.presentation.main.diary.meal.adapter.diaryEntryList.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.data.cache.model.GroceryEntity
import de.karzek.diettracker.presentation.model.DiaryEntryDisplayModel
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_diary_grocery.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class DiaryEntryViewHolder(override val containerView: View,
                           private val onItemClickedListener: OnDiaryEntryItemClickedListener,
                           private val onDeleteDiaryEntryItemListener: OnDeleteDiaryEntryItemListener,
                           private val onMoveDiaryEntryItemListener: OnMoveDiaryEntryItemListener,
                           private val onEditDiaryEntryItemListener: OnEditDiaryEntryItemListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(diaryEntry: DiaryEntryDisplayModel) {
        grocery_summary.text = formatGrocerySummary(diaryEntry)
        itemView.tag = diaryEntry.id

        if (diaryEntry.grocery.type == GroceryEntity.GroceryEntityType.TYPE_DRINK)
            swipe_option_move.visibility = View.GONE

        setupClickListeners()
    }

    private fun formatGrocerySummary(diaryEntry: DiaryEntryDisplayModel): String {
        val amount = StringUtils.formatFloat(diaryEntry.amount)
        return "" + amount + diaryEntry.unit.name + " " + diaryEntry.grocery.name
    }

    private fun setupClickListeners() {
        grocery_summary.setOnClickListener { onItemClickedListener.onDiaryEntryClicked(itemView.tag as Int) }
        swipe_option_delete.setOnClickListener {
            swipe_layout.close(false)
            onDeleteDiaryEntryItemListener.onDiaryEntryDeleteClicked(itemView.tag as Int)
        }
        swipe_option_move.setOnClickListener {
            swipe_layout.close()
            onMoveDiaryEntryItemListener.onDiaryEntryMoveClicked(itemView.tag as Int)
        }
        swipe_option_edit.setOnClickListener { onEditDiaryEntryItemListener.onDiaryEntryEditClicked(itemView.tag as Int) }
    }

    interface OnDiaryEntryItemClickedListener {
        fun onDiaryEntryClicked(id: Int)
    }

    interface OnDeleteDiaryEntryItemListener {
        fun onDiaryEntryDeleteClicked(id: Int)
    }

    interface OnMoveDiaryEntryItemListener {
        fun onDiaryEntryMoveClicked(id: Int)
    }

    interface OnEditDiaryEntryItemListener {
        fun onDiaryEntryEditClicked(id: Int)
    }

}
