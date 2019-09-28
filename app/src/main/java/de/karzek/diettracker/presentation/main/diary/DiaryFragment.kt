package de.karzek.diettracker.presentation.main.diary

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.getbase.floatingactionbutton.FloatingActionsMenu
import de.karzek.diettracker.R
import de.karzek.diettracker.data.cache.model.GroceryEntity
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseFragment
import de.karzek.diettracker.presentation.main.diary.adapter.DiaryViewPagerAdapter
import de.karzek.diettracker.presentation.main.diary.drink.GenericDrinkFragment
import de.karzek.diettracker.presentation.main.diary.meal.GenericMealFragment
import de.karzek.diettracker.presentation.model.MealDisplayModel
import de.karzek.diettracker.presentation.search.grocery.GrocerySearchActivity
import de.karzek.diettracker.presentation.search.recipe.RecipeSearchActivity
import de.karzek.diettracker.presentation.util.Constants
import de.karzek.diettracker.presentation.util.Constants.INVALID_ENTITY_ID
import de.karzek.diettracker.presentation.util.ViewUtils
import kotlinx.android.synthetic.main.fragment_diary.*
import kotlinx.android.synthetic.main.snippet_loading_view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 12.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.1
 * @date 29.05.2018
 */
class DiaryFragment : BaseFragment(), DiaryContract.View {

    @Inject
    lateinit var presenter: DiaryContract.Presenter

    private val simpleDateFormat = SimpleDateFormat("d. MMM yyyy", Locale.GERMANY)
    private val databaseDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY)
    private var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val datePickerCalendar = Calendar.getInstance()

    private var callback: OnDateSelectedListener? = null

    private var meals: ArrayList<MealDisplayModel>? = null

    interface OnDateSelectedListener {
        fun onDateSelected(databaseDateFormat: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            callback = context as OnDateSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnHeadlineSelectedListener")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_diary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()
        tab_layout.setupWithViewPager(view_pager)

        date_label.text = simpleDateFormat.format(Calendar.getInstance().time)

        floating_action_button_menu.setOnFloatingActionsMenuUpdateListener(object : FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {
            override fun onMenuExpanded() {
                fab_overlay.visibility = View.VISIBLE
            }

            override fun onMenuCollapsed() {
                fab_overlay.visibility = View.GONE
            }
        })
        ViewUtils.addElevationToFABMenuLabels(context, floating_action_button_menu)
        setupClickListeners()

        presenter.setView(this)
        presenter.start()
    }

    override fun setupFragmentComponent() {
        TrackerApplication.get(context!!).createDiaryComponent().inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        TrackerApplication.get(context!!).releaseDiaryComponent()
        dateSetListener = null
        callback = null
    }

    private fun setupClickListeners() {
        add_food.setOnClickListener { presenter.onAddFoodClicked() }
        add_drink.setOnClickListener { presenter.onAddDrinkClicked() }
        add_recipe.setOnClickListener { presenter.onAddRecipeClicked() }
        fab_overlay.setOnClickListener { presenter.onFabOverlayClicked() }
        date_label.setOnClickListener { presenter.onDateLabelClicked() }
        previous_date.setOnClickListener { presenter.onPreviousDateClicked() }
        next_date.setOnClickListener { presenter.onNextDateClicked() }
    }

    override fun setupViewPager(meals: ArrayList<MealDisplayModel>) {
        this.meals = meals
        val adapter = DiaryViewPagerAdapter(fragmentManager)

        for (meal in meals) {
            val bundle = Bundle()
            bundle.putString("meal", meal.name)
            val fragment = GenericMealFragment()
            fragment.arguments = bundle
            adapter.addFragment(fragment, meal.name)
        }

        val bundle = Bundle()
        bundle.putString("selectedDate", selectedDate)
        val fragment = GenericDrinkFragment()
        fragment.arguments = bundle
        adapter.addFragment(fragment, "Getränke")

        view_pager.adapter = adapter
    }

    override fun onDateSelected(selectedDate: String) {
        callback!!.onDateSelected(selectedDate)
    }

    override fun getSelectedDate(): String {
        return databaseDateFormat.format(datePickerCalendar.time)
    }

    override fun startFoodSearchActivity() {
        if (view_pager.currentItem < meals!!.size)
            startActivity(GrocerySearchActivity.newGrocerySearchIntent(context!!, GroceryEntity.GroceryEntityType.TYPE_FOOD, databaseDateFormat.format(datePickerCalendar.time), meals!![view_pager.currentItem].id))
        else
            startActivity(GrocerySearchActivity.newGrocerySearchIntent(context!!, GroceryEntity.GroceryEntityType.TYPE_FOOD, databaseDateFormat.format(datePickerCalendar.time), meals!![0].id))
    }

    override fun startDrinkSearchActivity() {
        startActivity(GrocerySearchActivity.newGrocerySearchIntent(context!!, GroceryEntity.GroceryEntityType.TYPE_DRINK, databaseDateFormat.format(datePickerCalendar.time), INVALID_ENTITY_ID))
    }

    override fun startRecipeSearchActivity() {
        startActivity(RecipeSearchActivity.newIntent(context!!, databaseDateFormat.format(datePickerCalendar.time), view_pager.currentItem))
    }

    override fun closeFabMenu() {
        floating_action_button_menu.collapse()
    }

    override fun openDatePicker() {
        if (dateSetListener == null) {
            dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                datePickerCalendar.set(Calendar.YEAR, year)
                datePickerCalendar.set(Calendar.MONTH, monthOfYear)
                datePickerCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateSelectedDate(datePickerCalendar.time)
            }
        }

        val dialog = DatePickerDialog(activity!!, dateSetListener, datePickerCalendar.get(Calendar.YEAR), datePickerCalendar.get(Calendar.MONTH), datePickerCalendar.get(Calendar.DAY_OF_MONTH))
        dialog.datePicker.maxDate = Calendar.getInstance().time.time + Constants.weekInMilliS
        dialog.show()
    }

    override fun showPreviousDate() {
        datePickerCalendar.set(Calendar.DAY_OF_MONTH, datePickerCalendar.get(Calendar.DAY_OF_MONTH) - 1)
        updateSelectedDate(datePickerCalendar.time)
    }

    override fun showNextDate() {
        datePickerCalendar.set(Calendar.DAY_OF_MONTH, datePickerCalendar.get(Calendar.DAY_OF_MONTH) + 1)
        updateSelectedDate(datePickerCalendar.time)
    }

    private fun updateSelectedDate(date: Date) {
        date_label.text = simpleDateFormat.format(date)
        presenter.onDateSelected(databaseDateFormat.format(date))
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun refreshViewPager() {
        view_pager.adapter?.notifyDataSetChanged()
    }

    override fun showOnboardingScreen(onboardingTag: Int) {
        //startActivity(OnboardingActivity.newIntent(getContext(), onboardingTag));
    }
}
