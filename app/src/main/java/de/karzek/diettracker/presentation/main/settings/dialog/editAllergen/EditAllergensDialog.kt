package de.karzek.diettracker.presentation.main.settings.dialog.editAllergen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseDialog
import de.karzek.diettracker.presentation.main.settings.dialog.editAllergen.adapter.EditAllergensListAdapter
import de.karzek.diettracker.presentation.model.AllergenDisplayModel
import kotlinx.android.synthetic.main.dialog_edit_checkbox_list.*
import kotlinx.android.synthetic.main.snippet_loading_view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 07.06.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 07.06.2018
 */
class EditAllergensDialog : BaseDialog(), EditAllergensDialogContract.View {

    @Inject
    lateinit var presenter: EditAllergensDialogContract.Presenter

    private var currentView: View? = null

    private var listener: SaveAllergenSelectionDialogListener? = null

    override fun setupDialogComponent() {
        TrackerApplication.get(context!!).createEditAllergensDialogComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.dialog_edit_checkbox_list, container, false)

        setupRecyclerView()

        dialog_action_dismiss.setOnClickListener {
            dismiss()
            listener = null
        }

        dialog_action_save.setOnClickListener {
            dismiss()
            presenter.saveAllergenSelection()
            listener!!.updateAllergens()
            listener = null
        }

        reset_selection.setOnClickListener { presenter.onResetSelectionClicked() }

        presenter.setView(this)
        presenter.start()

        return currentView
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = EditAllergensListAdapter(presenter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            listener = parentFragment as SaveAllergenSelectionDialogListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("fragment must implement SaveAllergenSelectionDialogListener")
        }

    }

    override fun updateRecyclerView(allergens: ArrayList<AllergenDisplayModel>, allergenStatus: HashMap<Int, Boolean>) {
        (recycler_view.adapter as EditAllergensListAdapter).setAllergens(allergens, allergenStatus)
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.finish()
        TrackerApplication.get(context!!).releaseEditAllergensDialogComponent()
    }

    interface SaveAllergenSelectionDialogListener {
        fun updateAllergens()
    }
}
