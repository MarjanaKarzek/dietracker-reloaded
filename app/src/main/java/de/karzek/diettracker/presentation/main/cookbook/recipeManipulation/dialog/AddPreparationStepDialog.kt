package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import de.karzek.diettracker.R
import kotlinx.android.synthetic.main.dialog_add_preparation_step.*

/**
 * Created by MarjanaKarzek on 07.06.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 07.06.2018
 */
class AddPreparationStepDialog : AppCompatDialogFragment() {

    private var listener: OnAddPreparationStepClickedInDialogListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_add_preparation_step, container, false)

        dialog_action_dismiss.setOnClickListener {
            dismiss()
            listener = null
        }

        dialog_action_add.setOnClickListener {
            if (inputFieldsValid()) {
                dismiss()
                listener!!.onAddPreparationStepClicked(preparation_step_description.text.toString())
                listener = null
            } else {
                showInvalidFieldsError()
            }
        }

        return view
    }

    private fun inputFieldsValid(): Boolean {
        return preparation_step_description.text.toString() != ""
    }

    private fun showInvalidFieldsError() {
        preparation_step_description.error = getString(R.string.error_message_missing_preparation_step_description)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            listener = activity as OnAddPreparationStepClickedInDialogListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("fragment must implement OnAddPreparationStepClickedInDialogListener")
        }

    }

    interface OnAddPreparationStepClickedInDialogListener {
        fun onAddPreparationStepClicked(description: String)
    }
}
