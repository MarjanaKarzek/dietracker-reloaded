package de.karzek.diettracker.presentation.main.diary.drink

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseFragment
import de.karzek.diettracker.presentation.main.diary.meal.adapter.diaryEntryList.DiaryEntryListAdapter
import de.karzek.diettracker.presentation.main.diary.meal.viewStub.CaloryDetailsView
import de.karzek.diettracker.presentation.main.diary.meal.viewStub.CaloryMacroDetailsView
import de.karzek.diettracker.presentation.model.DiaryEntryDisplayModel
import de.karzek.diettracker.presentation.search.grocery.groceryDetail.GroceryDetailsActivity
import de.karzek.diettracker.presentation.util.Constants
import de.karzek.diettracker.presentation.util.SharedPreferencesUtil.VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY
import de.karzek.diettracker.presentation.util.StringUtils
import de.karzek.diettracker.presentation.util.ValidationUtil
import kotlinx.android.synthetic.main.fragment_generic_drink.*
import kotlinx.android.synthetic.main.snippet_drinks_status.*
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
class GenericDrinkFragment : BaseFragment(), GenericDrinkContract.View {

    @Inject
    lateinit var presenter: GenericDrinkContract.Presenter

    private var detailsView: CaloryDetailsView? = null

    private var selectedDate: String? = null
    private var maxValues: HashMap<String, Long>? = null

    override fun setupFragmentComponent() {
        TrackerApplication.get(context!!).createGenericDrinkComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_generic_drink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        circle_progress_bar_drinks_value
                .setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        circle_progress_bar_drinks_value.clearFocus()
                        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(circle_progress_bar_drinks_value.windowToken, 0)
                        if (!StringUtils.isNullOrEmpty(circle_progress_bar_drinks_value.text.toString())) {
                            if (ValidationUtil.isValid(LOWER_BOUND_WATER_INPUT, UPPER_BOUND_WATER_INPUT, java.lang.Float.valueOf(circle_progress_bar_drinks_value.text.toString()), circle_progress_bar_drinks_value, context))
                                presenter.updateAmountOfWater(java.lang.Float.valueOf(circle_progress_bar_drinks_value.text.toString()), selectedDate)
                        } else
                            circle_progress_bar_drinks_value.setText(StringUtils.formatFloat(LOWER_BOUND_WATER_INPUT))
                    }
                    true
                }
        setupClickListeners()

        selectedDate = arguments!!.getString("selectedDate")

        setupRecyclerView()

        presenter.setView(this)
        presenter.start()
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = DiaryEntryListAdapter(presenter, presenter, presenter, null)
        recycler_view.addItemDecoration(DividerItemDecoration(recycler_view.context,
                (recycler_view.layoutManager as LinearLayoutManager).orientation))
    }

    override fun showNutritionDetails(value: String) {
        detailsView = if (value == VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY) {
            CaloryDetailsView(viewstub_calory_details.inflate())
        } else {
            CaloryMacroDetailsView(viewstub_calory_macro_details.inflate())
        }
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun getSelectedDate(): String? {
        return selectedDate
    }

    override fun showRecyclerView() {
        recycler_view.visibility = View.VISIBLE
    }

    override fun hideGroceryListPlaceholder() {
        drinks_placeholder.visibility = View.GONE
    }

    override fun hideRecyclerView() {
        recycler_view.visibility = View.GONE
    }

    override fun showGroceryListPlaceholder() {
        drinks_placeholder.visibility = View.VISIBLE
    }

    override fun updateGroceryList(displayModels: ArrayList<DiaryEntryDisplayModel>) {
        (recycler_view.adapter as DiaryEntryListAdapter).setList(displayModels)
    }

    override fun hideExpandOption() {
        expandable_layout_action.visibility = View.GONE
    }

    override fun showExpandOption() {
        expandable_layout_action.visibility = View.VISIBLE
    }

    override fun setLiquidStatus(displayModel: DiaryEntryDisplayModel, waterGoal: Float) {
        circle_progress_bar_drinks_value.setText(StringUtils.formatFloat(displayModel.amount))
        circle_progress_bar_dinks_progress.progress = 100.0f / waterGoal * displayModel.amount
        liquid_max_value.text = getString(R.string.generic_drinks_max_value, StringUtils.formatFloat(waterGoal))
    }

    override fun addToLiquidStatus(displayModels: ArrayList<DiaryEntryDisplayModel>, waterGoal: Float) {
        val currentValue = java.lang.Float.valueOf(circle_progress_bar_drinks_value.text.toString())

        var additionalValue = 0.0f
        for (model in displayModels) {
            additionalValue += model.amount
        }

        circle_progress_bar_drinks_value.setText(StringUtils.formatFloat(currentValue + additionalValue))
        circle_progress_bar_dinks_progress.progress = 100.0f / waterGoal * (currentValue + additionalValue)
    }

    override fun refreshLiquidStatus() {
        presenter.updateLiquidStatus(selectedDate)
    }

    override fun setNutritionMaxValues(maxValues: HashMap<String, Long>) {
        this.maxValues = maxValues
        detailsView!!.caloryProgressBarMaxValue.text = "${maxValues[Constants.CALORIES]!!}"

        if (detailsView is CaloryMacroDetailsView) {
            (detailsView as CaloryMacroDetailsView).proteinProgressBarMaxValue.text = "von\n${maxValues[Constants.PROTEINS]}g"
            (detailsView as CaloryMacroDetailsView).carbsProgressBarMaxValue.text = "von\n${maxValues[Constants.CARBS]}g"
            (detailsView as CaloryMacroDetailsView).fatsProgressBarMaxValue.text = "von\n${maxValues[Constants.FATS]}g"
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

    override fun startEditMode(id: Int) {
        startActivity(GroceryDetailsActivity.newEditDiaryEntryIntent(context!!, id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        TrackerApplication.get(context!!).releaseGenericDrinkComponent()
    }

    private fun setupClickListeners() {
        expandable_layout_action.setOnClickListener {
            if (expandable_details.isExpanded) {
                expandable_layout_action.setImageDrawable(context!!.getDrawable(R.drawable.ic_expand_more_primary_text))
                expandable_details.collapse(true)
            } else {
                expandable_layout_action.setImageDrawable(context!!.getDrawable(R.drawable.ic_expand_less_primary_text))
                expandable_details.expand(true)
            }
        }
        add_bottle.setOnClickListener {
            presenter.addBottleWaterClicked(selectedDate)
        }
        add_glass.setOnClickListener { presenter.addGlassWaterClicked(selectedDate) }
        add_favorite_drink.setOnClickListener { presenter.addFavoriteDrinkClicked(selectedDate) }
    }

    companion object {

        private const val LOWER_BOUND_WATER_INPUT = 0.0f
        private val UPPER_BOUND_WATER_INPUT = Float.MAX_VALUE
    }
}
