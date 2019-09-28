package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog.editMeals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseDialog
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog.editMeals.adapter.EditMealsListAdapter
import de.karzek.diettracker.presentation.model.MealDisplayModel
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
class EditMealsDialog : BaseDialog(), EditMealsDialogContract.View {

    @Inject
    lateinit var presenter: EditMealsDialogContract.Presenter

    private var dialogView: View? = null

    private var mealList: ArrayList<MealDisplayModel>? = ArrayList()

    private var listener: SaveMealsSelectionDialogListener? = null

    override fun setupDialogComponent() {
        TrackerApplication.get(context!!).createEditMealsDialogComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(R.layout.dialog_edit_checkbox_list, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            mealList = bundle.getParcelableArrayList(EXTRA_SELECTED_MEALS)
        }
        presenter.setSelectedMealList(mealList)

        setupRecyclerView()

        dialog_action_dismiss.setOnClickListener {
            dismiss()
            listener = null
        }

        dialog_action_save.setOnClickListener {
            dismiss()
            listener!!.updateMeals(presenter.selectedMeals)
            listener = null
        }

        reset_selection.setOnClickListener { presenter.onResetSelectionClicked() }

        presenter.setView(this)
        presenter.start()

        return dialogView
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = EditMealsListAdapter(presenter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            listener = activity as SaveMealsSelectionDialogListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("fragment must implement SaveMealsSelectionDialogListener")
        }

    }

    override fun updateRecyclerView(meals: ArrayList<MealDisplayModel>, mealStatus: HashMap<Int, Boolean>) {
        (recycler_view.adapter as EditMealsListAdapter).setMeals(meals, mealStatus)
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
        TrackerApplication.get(context!!).releaseEditMealsDialogComponent()
    }

    interface SaveMealsSelectionDialogListener {
        fun updateMeals(selectedMeals: ArrayList<MealDisplayModel>)
    }

    companion object {

        const val EXTRA_SELECTED_MEALS = "EXTRA_SELECTED_MEALS"
    }
}
