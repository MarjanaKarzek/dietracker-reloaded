package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.viewHolder

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.itemWrapper.RecipeManipulationViewItemWrapper
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.viewholder_recipe_man_preparation_step.*

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
class RecipeManipulationPreparationStepItemViewHolder(override val containerView: View,
                                                      private val onDeletePreparationStepClickedListener: OnDeletePreparationStepClickedListener,
                                                      private val onEditPreparationStepFinishedListener: OnEditPreparationStepFinishedListener) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private fun setupDescriptionChangedListener() {
        preparation_step_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onEditPreparationStepFinishedListener.onEditPreparationStepFinished(itemView.tag as Int, preparation_step_text.text.toString())
            }
            true
        }
        preparation_step_text.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> onEditPreparationStepFinishedListener.onEditPreparationStepFinished(itemView.tag as Int, preparation_step_text.text.toString()) }
    }

    fun bind(item: RecipeManipulationViewItemWrapper, innerListId: Int) {
        preparation_step_number.text = "${item.preparationStepDisplayModel.stepNo}"
        preparation_step_text.setText(item.preparationStepDisplayModel.description)

        itemView.tag = innerListId

        setupDescriptionChangedListener()
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        action_delete.setOnClickListener { onDeletePreparationStepClickedListener.onDeletePreparationStepClicked(itemView.tag as Int) }
    }

    interface OnDeletePreparationStepClickedListener {
        fun onDeletePreparationStepClicked(id: Int)
    }

    interface OnEditPreparationStepFinishedListener {
        fun onEditPreparationStepFinished(id: Int, description: String)
    }

}
