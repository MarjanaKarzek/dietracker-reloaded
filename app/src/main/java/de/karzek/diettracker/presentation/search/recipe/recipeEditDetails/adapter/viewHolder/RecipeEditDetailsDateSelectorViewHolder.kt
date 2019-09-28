package de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.itemWrapper.RecipeEditDetailsViewItemWrapper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_edit_details_date_selector.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeEditDetailsDateSelectorViewHolder(override val containerView: View,
                                              private val onDateClickListener: OnDateClickListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val databaseDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY)
    private val simpleDateFormat = SimpleDateFormat("d. MMM yyyy", Locale.GERMANY)

    fun bind(item: RecipeEditDetailsViewItemWrapper) {
        try {
            date_label.text = simpleDateFormat.format(databaseDateFormat.parse(item.date)!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        date_label.setOnClickListener { onDateClickListener.onDateClicked() }
    }

    interface OnDateClickListener {
        fun onDateClicked()
    }

}
