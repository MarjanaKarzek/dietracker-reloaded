package de.karzek.diettracker.presentation.main.cookbook.dialog.sortOptionsDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import de.karzek.diettracker.R
import kotlinx.android.synthetic.main.dialog_edit_recipe_sort_option.*

/**
 * Created by MarjanaKarzek on 07.06.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 07.06.2018
 */
class RecipeSortOptionsDialog : AppCompatDialogFragment() {

    private var dialogView: View? = null

    private var selectedOption: String? = null
    private var asc: Boolean = false

    private var listener: SortOptionSelectedDialogListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(R.layout.dialog_edit_recipe_sort_option, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            selectedOption = bundle.getString("sortOption")
            asc = bundle.getBoolean("asc")
            when (selectedOption) {
                "title" -> if (asc)
                    title_asc.isChecked = true
                else
                    title_desc.isChecked = true
                "portions" -> if (asc)
                    portions_asc.isChecked = true
                else
                    portions_desc.isChecked = true
            }
        } else
            title_asc.isSelected = true

        radio_group.setOnCheckedChangeListener { _, viewId ->
            when (viewId) {
                R.id.title_asc -> {
                    selectedOption = "title"
                    asc = true
                }
                R.id.title_desc -> {
                    selectedOption = "title"
                    asc = false
                }
                R.id.portions_asc -> {
                    selectedOption = "portions"
                    asc = true
                }
                R.id.portions_desc -> {
                    selectedOption = "portions"
                    asc = false
                }
            }
        }

        dialog_action_dismiss.setOnClickListener {
            dismiss()
            listener = null
        }

        dialog_action_save.setOnClickListener {
            dismiss()
            listener!!.sortOptionSelected(selectedOption, asc)
            listener = null
        }

        return dialogView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            listener = parentFragment as SortOptionSelectedDialogListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("fragment must implement SortOptionSelectedDialogListener")
        }

    }

    interface SortOptionSelectedDialogListener {
        fun sortOptionSelected(sortOption: String?, asc: Boolean)
    }
}
