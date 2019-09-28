package de.karzek.diettracker.presentation.main.cookbook.dialog.filterOptionsDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseDialog
import de.karzek.diettracker.presentation.main.cookbook.dialog.filterOptionsDialog.adapter.RecipeFilterOptionsListAdapter
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
class RecipeFilterOptionsDialog : BaseDialog(), RecipeFilterOptionsDialogContract.View {

    @Inject
    lateinit var presenter: RecipeFilterOptionsDialogContract.Presenter

    private var dialogView: View? = null

    private var listener: FilterOptionsSelectedDialogListener? = null

    override fun setupDialogComponent() {
        TrackerApplication.get(context!!).createRecipeFilterOptionsDialogComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(R.layout.dialog_edit_checkbox_list, container, false)

        val bundle = this.arguments
        if (bundle != null)
            presenter.setFilterOptions(bundle.getStringArrayList("filterOptions"))

        dialog_title.visibility = View.VISIBLE
        dialog_title.text = getString(R.string.filter_options_dialog_title)
        setupRecyclerView()

        dialog_action_dismiss.setOnClickListener {
            dismiss()
            listener = null
        }
        reset_selection.setOnClickListener { presenter.onResetSelectionClicked() }

        dialog_action_save.setOnClickListener {
            dismiss()
            listener!!.filterOptionsSelected(presenter.selectedFilterOptions)
            listener = null
        }

        presenter.setView(this)
        presenter.start()

        return dialogView
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = RecipeFilterOptionsListAdapter(presenter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            listener = parentFragment as FilterOptionsSelectedDialogListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("fragment must implement FilterOptionsSelectedDialogListener")
        }

    }

    override fun updateRecyclerView(options: ArrayList<String>, optionStatus: HashMap<String, Boolean>) {
        (recycler_view.adapter as RecipeFilterOptionsListAdapter).setList(options, optionStatus)
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
        TrackerApplication.get(context!!).releaseRecipeFilterOptionsDialogComponent()
    }

    interface FilterOptionsSelectedDialogListener {
        fun filterOptionsSelected(filterOptions: ArrayList<String>)
    }
}
