package de.karzek.diettracker.presentation.main.diary.meal.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialogFragment
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.model.MealDisplayModel
import kotlinx.android.synthetic.main.dialog_meal_selector.*
import java.util.*

/**
 * Created by MarjanaKarzek on 07.06.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 07.06.2018
 */
class MealSelectorDialog : AppCompatDialogFragment() {

    private var dialogView: View? = null
    private var currentId: Int = 0
    private var meals: ArrayList<MealDisplayModel>? = null
    private val mealNames = ArrayList<String>()

    private var listener: MealSelectedInDialogListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(R.layout.dialog_meal_selector, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            currentId = bundle.getInt("id")
            meals = bundle.getParcelableArrayList("meals")

            for (meal in meals!!)
                mealNames.add(meal.name)

            if (bundle.getString("instructions") != null) {
                instructions.text = bundle.getString("instructions")
                dialog_action_move.text = getString(R.string.dialog_action_add)
            }
            initializeSpinner()
        }

        dialog_action_dismiss.setOnClickListener {
            dismiss()
            listener = null
        }

        dialog_action_move.setOnClickListener {
            dismiss()
            listener!!.mealSelectedInDialog(currentId, meals!![spinner_meal.selectedItemPosition])
            listener = null
        }

        return dialogView
    }

    private fun initializeSpinner() {
        val mealAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, mealNames)
        mealAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_meal.adapter = mealAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            listener = parentFragment as MealSelectedInDialogListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("fragment must implement MealSelectedInDialogListener")
        }

    }

    interface MealSelectedInDialogListener {
        fun mealSelectedInDialog(id: Int, meal: MealDisplayModel)
    }
}
