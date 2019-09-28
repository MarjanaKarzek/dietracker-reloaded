package de.karzek.diettracker.presentation.main.settings.dialog.manipulateMeal

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.util.Constants
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.synthetic.main.dialog_add_meal.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by MarjanaKarzek on 10.07.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 10.07.2018
 */
class ManipulateMealDialog : AppCompatDialogFragment() {

    private var currentId = -1
    private var originalMealTitle: String? = null
    private var originalStartTime: String? = null
    private var originalEndTime: String? = null

    private val startTimeCalendar = Calendar.getInstance()
    private val endTimeCalendar = Calendar.getInstance()

    private val databaseTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.GERMANY)

    private var addListener: AddMealFromDialogListener? = null
    private var saveListener: SaveMealFromDialogListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_add_meal, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            currentId = bundle.getInt("mealId")
            originalMealTitle = bundle.getString("mealTitle")
            originalStartTime = bundle.getString("startTime")
            originalEndTime = bundle.getString("endTime")

            setupFieldsWithPreselection()
        } else {
            setupDefaultTime()
        }

        setupTimeChangeListener()

        dialog_action_dismiss.setOnClickListener {
            dismiss()
            addListener = null
            saveListener = null
        }

        dialog_action_save.setOnClickListener {
            if (inputFieldsValid()) {
                dismiss()
                if (currentId == Constants.INVALID_ENTITY_ID)
                    addListener!!.addMealInDialogClicked(meal_title.text.toString(), databaseTimeFormat.format(startTimeCalendar.time), databaseTimeFormat.format(endTimeCalendar.time))
                else
                    saveListener!!.saveMealInDialogClicked(currentId, meal_title.text.toString(), databaseTimeFormat.format(startTimeCalendar.time), databaseTimeFormat.format(endTimeCalendar.time))
                addListener = null
                saveListener = null
            } else {
                showInvalidFieldsError()
            }
        }

        return view
    }

    private fun setupFieldsWithPreselection() {
        meal_title.setText(originalMealTitle)

        try {
            startTimeCalendar.time = databaseTimeFormat.parse(originalStartTime!!)!!
            endTimeCalendar.time = databaseTimeFormat.parse(originalEndTime!!)!!
        } catch (e: ParseException) {
            e.printStackTrace()
            setupDefaultTime()
        }

        dialog_start_hour.setText(StringUtils.formatInt(startTimeCalendar.get(Calendar.HOUR_OF_DAY)))
        dialog_start_minute.setText(StringUtils.formatInt(startTimeCalendar.get(Calendar.MINUTE)))

        dialog_end_hour.setText(StringUtils.formatInt(endTimeCalendar.get(Calendar.HOUR_OF_DAY)))
        dialog_end_minute.setText(StringUtils.formatInt(endTimeCalendar.get(Calendar.MINUTE)))
    }

    private fun setupTimeChangeListener() {
        dialog_start_hour.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(dialog_start_hour)
                startTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dialog_start_hour.text.toString()))
            }
            true
        }
        dialog_start_hour.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> startTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dialog_start_hour.text.toString())) }

        dialog_start_minute.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(dialog_start_minute)
                startTimeCalendar.set(Calendar.MINUTE, Integer.valueOf(dialog_start_minute.text.toString()))
            }
            true
        }

        dialog_start_minute.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> startTimeCalendar.set(Calendar.MINUTE, Integer.valueOf(dialog_start_minute.text.toString())) }

        dialog_end_hour.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(dialog_end_hour)
                endTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dialog_end_hour.text.toString()))
            }
            true
        }
        dialog_end_hour.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> endTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dialog_end_hour.text.toString())) }

        dialog_end_minute.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(dialog_end_minute)
                endTimeCalendar.set(Calendar.MINUTE, Integer.valueOf(dialog_end_minute.text.toString()))
            }
            true
        }
        dialog_end_minute.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> endTimeCalendar.set(Calendar.MINUTE, Integer.valueOf(dialog_end_minute.text.toString())) }
    }

    private fun clearFocusOfView(view: EditText) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun setupDefaultTime() {
        startTimeCalendar.set(Calendar.HOUR_OF_DAY, 8)
        startTimeCalendar.set(Calendar.MINUTE, 0)
        startTimeCalendar.set(Calendar.SECOND, 0)

        endTimeCalendar.set(Calendar.HOUR_OF_DAY, 10)
        endTimeCalendar.set(Calendar.MINUTE, 0)
        endTimeCalendar.set(Calendar.SECOND, 0)

        dialog_start_hour.setText(StringUtils.formatInt(startTimeCalendar.get(Calendar.HOUR_OF_DAY)))
        dialog_start_minute.setText(StringUtils.formatInt(startTimeCalendar.get(Calendar.MINUTE)))

        dialog_end_hour.setText(StringUtils.formatInt(endTimeCalendar.get(Calendar.HOUR_OF_DAY)))
        dialog_end_minute.setText(StringUtils.formatInt(endTimeCalendar.get(Calendar.MINUTE)))
    }

    private fun inputFieldsValid(): Boolean {
        return meal_title.text.toString() != "" && !startTimeCalendar.after(endTimeCalendar)
    }

    private fun showInvalidFieldsError() {
        if (meal_title.text.toString() == "")
            meal_title.error = getString(R.string.error_message_missing_meal_title)
        if (startTimeCalendar.after(endTimeCalendar))
            Toast.makeText(context, R.string.error_message_invalid_start_end_time, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            addListener = parentFragment as AddMealFromDialogListener?
            saveListener = parentFragment as SaveMealFromDialogListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("fragment must implement AddMealFromDialogListener and SaveMealFromDialogListener")
        }

    }

    interface AddMealFromDialogListener {
        fun addMealInDialogClicked(name: String, startTime: String, endTime: String)
    }

    interface SaveMealFromDialogListener {
        fun saveMealInDialogClicked(id: Int, name: String, startTime: String, endTime: String)
    }
}
