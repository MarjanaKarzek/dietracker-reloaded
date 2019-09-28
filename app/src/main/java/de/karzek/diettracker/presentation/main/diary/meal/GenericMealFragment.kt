package de.karzek.diettracker.presentation.main.diary.meal

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseFragment
import de.karzek.diettracker.presentation.main.diary.DiaryFragment
import de.karzek.diettracker.presentation.main.diary.meal.adapter.diaryEntryList.DiaryEntryListAdapter
import de.karzek.diettracker.presentation.main.diary.meal.adapter.favoriteRecipeList.FavoriteRecipeListAdapter
import de.karzek.diettracker.presentation.main.diary.meal.dialog.MealSelectorDialog
import de.karzek.diettracker.presentation.main.diary.meal.viewStub.CaloryDetailsView
import de.karzek.diettracker.presentation.main.diary.meal.viewStub.CaloryMacroDetailsView
import de.karzek.diettracker.presentation.model.DiaryEntryDisplayModel
import de.karzek.diettracker.presentation.model.MealDisplayModel
import de.karzek.diettracker.presentation.model.RecipeDisplayModel
import de.karzek.diettracker.presentation.search.grocery.groceryDetail.GroceryDetailsActivity
import de.karzek.diettracker.presentation.util.Constants
import de.karzek.diettracker.presentation.util.SharedPreferencesUtil.VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.synthetic.main.fragment_generic_meal.*
import kotlinx.android.synthetic.main.snippet_loading_view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 28.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 28.05.2018
 */
class GenericMealFragment : BaseFragment(), GenericMealContract.View {

    @Inject
    lateinit var presenter: GenericMealContract.Presenter

    private var callback: OnRefreshViewPagerNeededListener? = null

    private var detailsView: CaloryDetailsView? = null

    private var meal: String? = null
    private var selectedDate: String? = null

    private var maxValues: HashMap<String, Long>? = null

    override fun setupFragmentComponent() {
        TrackerApplication.get(context!!).createGenericMealComponent().inject(this)
    }

    interface OnRefreshViewPagerNeededListener {
        fun onRefreshViewPagerNeeded()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            callback = context as OnRefreshViewPagerNeededListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnRefreshViewPagerNeededListener")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_generic_meal, container, false)

        meal = arguments!!.getString("meal")
        selectedDate = (activity!!.supportFragmentManager.findFragmentByTag("DiaryFragment") as DiaryFragment).selectedDate

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGroceryRecyclerView()
        setupRecipeRecyclerView()

        presenter.setView(this)
        presenter.setMeal(meal)

        presenter.start()
    }

    private fun setupGroceryRecyclerView() {
        recycler_view_groceries.setHasFixedSize(true)
        recycler_view_groceries.layoutManager = LinearLayoutManager(context)
        recycler_view_groceries.adapter = DiaryEntryListAdapter(presenter, presenter, presenter, presenter)
        recycler_view_groceries.addItemDecoration(DividerItemDecoration(recycler_view_groceries.context,
                (recycler_view_groceries.layoutManager as LinearLayoutManager).orientation))
    }

    private fun setupRecipeRecyclerView() {
        recycler_view_favorites.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        recycler_view_favorites.layoutManager = manager
        recycler_view_favorites.adapter = FavoriteRecipeListAdapter(presenter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        TrackerApplication.get(context!!).releaseGenericMealComponent()
        callback = null
    }

    override fun showGroceryListPlaceholder() {
        grocery_list_placeholder.visibility = View.VISIBLE
    }

    override fun hideGroceryListPlaceholder() {
        grocery_list_placeholder.visibility = View.GONE
    }

    override fun showNutritionDetails(value: String) {
        detailsView = if (value == VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY) {
            CaloryDetailsView(viewstub_calory_details.inflate())
        } else {
            CaloryMacroDetailsView(viewstub_calory_macro_details.inflate())
        }
    }

    override fun setNutritionMaxValues(values: HashMap<String, Long>) {
        maxValues = values
        detailsView!!.caloryProgressBarMaxValue.text = "${values[Constants.CALORIES]!!}"

        if (detailsView is CaloryMacroDetailsView) {
            (detailsView as CaloryMacroDetailsView).proteinProgressBarMaxValue.text = "von\n${values[Constants.PROTEINS]}g"
            (detailsView as CaloryMacroDetailsView).carbsProgressBarMaxValue.text = "von\n${values[Constants.CARBS]}g"
            (detailsView as CaloryMacroDetailsView).fatsProgressBarMaxValue.text = "von\n${values[Constants.FATS]}g"
        }
    }

    override fun updateNutritionDetails(values: HashMap<String, Float>) {
        detailsView!!.caloryProgressBar.progress = 100.0f / maxValues!![Constants.CALORIES]!! * values[Constants.CALORIES]!!
        detailsView!!.caloryProgressBarValue.text = "${values[Constants.CALORIES]!!.toFloat().toInt()}"

        if (detailsView is CaloryMacroDetailsView) {
            (detailsView as CaloryMacroDetailsView).proteinProgressBar.progress = 100.0f / maxValues!![Constants.PROTEINS]!! * values[Constants.PROTEINS]!!
            (detailsView as CaloryMacroDetailsView).proteinProgressBarValue.text = StringUtils.formatFloat(values[Constants.PROTEINS]!!)

            (detailsView as CaloryMacroDetailsView).carbsProgressBar.progress = 100.0f / maxValues!![Constants.CARBS]!! * values[Constants.CARBS]!!
            (detailsView as CaloryMacroDetailsView).carbsProgressBarValue.text = StringUtils.formatFloat(values[Constants.CARBS]!!)

            (detailsView as CaloryMacroDetailsView).fatsProgressBar.progress = 100.0f / maxValues!![Constants.FATS]!! * values[Constants.FATS]!!
            (detailsView as CaloryMacroDetailsView).fatsProgressBarValue.text = StringUtils.formatFloat(values[Constants.FATS]!!)
        }
    }

    override fun updateGroceryList(diaryEntries: ArrayList<DiaryEntryDisplayModel>) {
        (recycler_view_groceries.adapter as DiaryEntryListAdapter).setList(diaryEntries)
    }

    override fun updateRecipeList(recipes: ArrayList<RecipeDisplayModel>) {
        (recycler_view_favorites.adapter as FavoriteRecipeListAdapter).setList(recipes)
    }

    override fun getSelectedDate(): String? {
        return selectedDate
    }

    override fun setSelectedDate(date: String) {
        selectedDate = date
        presenter.updateListItems(selectedDate)
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun refreshRecyclerView() {
        presenter.updateListItems(selectedDate)
    }

    override fun showMoveDiaryEntryDialog(id: Int, meals: ArrayList<MealDisplayModel>) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val previous = fragmentManager!!.findFragmentByTag("dialog")
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.addToBackStack(null)

        val dialogFragment = MealSelectorDialog()
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putParcelableArrayList("meals", meals)
        dialogFragment.arguments = bundle
        dialogFragment.show(fragmentTransaction, "dialog")
    }

    override fun startEditMode(id: Int) {
        startActivity(GroceryDetailsActivity.newEditDiaryEntryIntent(context!!, id))
    }

    override fun showRecipeRecyclerView() {
        recycler_view_favorites.visibility = View.VISIBLE
    }

    override fun hideRecipeRecyclerView() {
        recycler_view_favorites.visibility = View.GONE
    }

    override fun showGroceryRecyclerView() {
        recycler_view_groceries.visibility = View.VISIBLE
    }

    override fun hideGroceryRecyclerView() {
        recycler_view_groceries.visibility = View.GONE
    }

    override fun mealSelectedInDialog(diaryEntryId: Int, meal: MealDisplayModel) {
        presenter.moveDiaryItemToMeal(diaryEntryId, meal)
        callback!!.onRefreshViewPagerNeeded()
    }
}
