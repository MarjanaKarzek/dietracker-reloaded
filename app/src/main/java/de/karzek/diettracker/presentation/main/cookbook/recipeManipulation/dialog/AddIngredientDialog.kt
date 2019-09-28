package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialogFragment
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.util.Constants
import de.karzek.diettracker.presentation.util.StringUtils
import de.karzek.diettracker.presentation.util.ValidationUtil
import kotlinx.android.synthetic.main.dialog_add_ingredient.*
import java.util.*

/**
 * Created by MarjanaKarzek on 07.06.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 07.06.2018
 */
class AddIngredientDialog : AppCompatDialogFragment() {

    private var dialogView: View? = null
    private var units: ArrayList<String>? = null
    private var manualIngredientId: Int = 0

    private var addListener: OnAddIngredientClickedInDialogListener? = null
    private var saveListener: OnSaveIngredientClickedInDialogListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(R.layout.dialog_add_ingredient, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            units = bundle.getStringArrayList(EXTRA_UNITS)
            manualIngredientId = bundle.getInt(EXTRA_MANUAL_INGREDIENT_ID)
            initializeSpinner()
        }

        dialog_action_dismiss.setOnClickListener {
            dismiss()
            addListener = null
            saveListener = null
        }

        if (manualIngredientId == Constants.INVALID_ENTITY_ID)
            dialog_action_add_ingredient.setOnClickListener {
                if (inputFieldsValid()) {
                    val selectedUnitId = spinner_unit.selectedItemPosition
                    dismiss()
                    if (ingredient_amount.text.toString() == "")
                        addListener!!.onAddManualIngredientClicked(java.lang.Float.valueOf(ingredient_amount.hint.toString()), selectedUnitId, ingredient_product_name.text.toString())
                    else
                        addListener!!.onAddManualIngredientClicked(java.lang.Float.valueOf(ingredient_amount.text.toString()), selectedUnitId, ingredient_product_name.text.toString())
                    addListener = null
                }
            }
        else {
            dialog_action_add_ingredient.setText(R.string.save_button)
            dialog_action_add_ingredient.setOnClickListener {
                if (inputFieldsValid()) {
                    val selectedUnitId = spinner_unit.selectedItemPosition
                    dismiss()
                    if (ingredient_amount.text.toString() == "")
                        saveListener!!.onSaveManualIngredientClicked(manualIngredientId, java.lang.Float.valueOf(ingredient_amount.hint.toString()), selectedUnitId, ingredient_product_name.text.toString())
                    else
                        saveListener!!.onSaveManualIngredientClicked(manualIngredientId, java.lang.Float.valueOf(ingredient_amount.text.toString()), selectedUnitId, ingredient_product_name.text.toString())
                    saveListener = null
                }
            }

            ingredient_product_name.setText(bundle!!.getString(EXTRA_GROCERY_QUERY))
            ingredient_amount.setText(StringUtils.formatFloat(bundle.getFloat(EXTRA_AMOUNT)))
            spinner_unit.setSelection(bundle.getInt(EXTRA_UNIT_ID))
        }

        return dialogView
    }

    private fun inputFieldsValid(): Boolean {
        return if (ingredient_product_name.text.toString() == "") {
            showInvalidFieldsError()
            false
        } else {
            if (!StringUtils.isNullOrEmpty(ingredient_amount.text.toString())) {
                ValidationUtil.isValid(LOWER_BOUND_AMOUNT, UPPER_BOUND_AMOUNT, java.lang.Float.valueOf(ingredient_amount.text.toString()), ingredient_amount, context)
            } else {
                true
            }
        }
    }

    private fun showInvalidFieldsError() {
        ingredient_product_name.error = getString(R.string.error_message_missing_product_name)
    }

    private fun initializeSpinner() {
        val unitAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, units!!)
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_unit.adapter = unitAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            addListener = activity as OnAddIngredientClickedInDialogListener?
            saveListener = activity as OnSaveIngredientClickedInDialogListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("fragment must implement OnAddIngredientClickedInDialogListener and OnSaveIngredientClickedInDialogListener")
        }

    }

    interface OnAddIngredientClickedInDialogListener {
        fun onAddManualIngredientClicked(amount: Float, selectedUnitId: Int, groceryQuery: String)
    }

    interface OnSaveIngredientClickedInDialogListener {
        fun onSaveManualIngredientClicked(id: Int, amount: Float, selectedUnitId: Int, groceryQuery: String)
    }

    companion object {

        private const val LOWER_BOUND_AMOUNT = 0.0f
        private const val UPPER_BOUND_AMOUNT = 2000.0f

        private const val EXTRA_MANUAL_INGREDIENT_ID = "EXTRA_MANUAL_INGREDIENT_ID"
        private const val EXTRA_UNITS = "EXTRA_UNITS"
        const val EXTRA_GROCERY_QUERY = "EXTRA_GROCERY_QUERY"
        const val EXTRA_AMOUNT = "EXTRA_AMOUNT"
        const val EXTRA_UNIT_ID = "EXTRA_UNIT_ID"
    }
}
