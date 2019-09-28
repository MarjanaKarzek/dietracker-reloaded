package de.karzek.diettracker.presentation.search.grocery.groceryDetail

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import de.karzek.diettracker.R
import de.karzek.diettracker.data.cache.model.GroceryEntity
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseActivity
import de.karzek.diettracker.presentation.main.MainActivity
import de.karzek.diettracker.presentation.main.diary.meal.viewStub.CaloryDetailsView
import de.karzek.diettracker.presentation.main.diary.meal.viewStub.CaloryMacroDetailsView
import de.karzek.diettracker.presentation.model.*
import de.karzek.diettracker.presentation.search.grocery.groceryDetail.GroceryDetailsContract.View.DetailsMode.*
import de.karzek.diettracker.presentation.search.grocery.groceryDetail.viewStub.AllergenView
import de.karzek.diettracker.presentation.util.Constants
import de.karzek.diettracker.presentation.util.SharedPreferencesUtil.VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY
import de.karzek.diettracker.presentation.util.StringUtils
import de.karzek.diettracker.presentation.util.ValidationUtil
import kotlinx.android.synthetic.main.activity_grocery_details.*
import kotlinx.android.synthetic.main.snippet_loading_view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 02.06.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 02.06.2018
 */
class GroceryDetailsActivity : BaseActivity(), GroceryDetailsContract.View {

    @Inject
    lateinit var presenter: GroceryDetailsContract.Presenter

    private var detailsView: CaloryDetailsView? = null

    private val context = this

    private var groceryId: Int = 0
    private var selectedDate: String? = null
    private var selectedMeal: Int = 0

    private var menu: Menu? = null

    private var diaryEntryId: Int = 0
    private var mode: Int = 0
    private var ingredientIndex: Int = 0

    private val selectedDateCalendar = Calendar.getInstance()
    private val simpleDateFormat = SimpleDateFormat("d. MMM yyyy", Locale.GERMANY)
    private val databaseDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY)
    private var dateSetListener: DatePickerDialog.OnDateSetListener? = null

    private var groceryDisplayModel: GroceryDisplayModel? = null
    private var defaultUnits: ArrayList<UnitDisplayModel>? = null
    private var servings: ArrayList<ServingDisplayModel>? = null
    private var meals: ArrayList<MealDisplayModel>? = null
    private var maxValues: HashMap<String, Long>? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.grocery_search_result_details, menu)

        this.menu = menu

        presenter.checkFavoriteState(groceryId)

        return true
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_details)

        presenter.setView(this)
        setupSupportActionBar()
        edittext_amount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                refreshNutritionDetails()
            }

            override fun afterTextChanged(editable: Editable) {
                if (!StringUtils.isNullOrEmpty(edittext_amount.text.toString())) {
                    ValidationUtil.isValid(LOWER_BOUND_AMOUNT, UPPER_BOUND_AMOUNT, java.lang.Float.valueOf(edittext_amount.text.toString()), edittext_amount, context)
                    refreshNutritionDetails()
                }
            }
        })

        setupClickListeners()

        mode = intent.extras!!.getInt(EXTRA_MODE)
        when (mode) {
            MODE_GROCERY_SEARCH -> {
                groceryId = intent.extras!!.getInt(EXTRA_GROCERY_ID)
                selectedDate = intent.extras!!.getString(EXTRA_SELECTED_DATE)
                selectedMeal = intent.extras!!.getInt(EXTRA_SELECTED_MEAL)

                try {
                    val currentSelectedDate = databaseDateFormat.parse(selectedDate!!)
                    date_label.text = simpleDateFormat.format(currentSelectedDate!!)
                    selectedDateCalendar.time = currentSelectedDate
                } catch (e: ParseException) {
                    e.printStackTrace()
                    date_label.text = simpleDateFormat.format(Calendar.getInstance().time)
                }

                presenter.setGroceryId(groceryId)
                presenter.start()
            }
            MODE_INGREDIENT_SEARCH -> {
                groceryId = intent.extras!!.getInt(EXTRA_GROCERY_ID)
                presenter.startAddIngredientMode(groceryId)
            }
            MODE_REPLACE_INGREDIENT_SEARCH -> {
                ingredientIndex = intent.extras!!.getInt(EXTRA_INGREDIENT_ID)
                groceryId = intent.extras!!.getInt(EXTRA_GROCERY_ID)
                presenter.startAddIngredientMode(groceryId)
            }
            MODE_EDIT_DIARY_ENTRY -> {
                diaryEntryId = intent.extras!!.getInt(EXTRA_DIARY_ENTRY_ID)
                presenter.startEditDiaryEntryMode(diaryEntryId)
            }
            MODE_EDIT_INGREDIENT -> {
                ingredientIndex = intent.extras!!.getInt(EXTRA_INGREDIENT_ID)
                groceryId = intent.extras!!.getInt(EXTRA_GROCERY_ID)
                presenter.setGroceryId(groceryId)
                presenter.startEditIngredientMode(intent.extras!!.getFloat(EXTRA_AMOUNT))
            }
        }
    }

    private fun setupClickListeners() {
        date_label.setOnClickListener { presenter.onDateLabelClicked() }
        delete_diary_entry.setOnClickListener {
            showLoading()
            presenter.onDeleteDiaryEntryClicked(diaryEntryId)
        }
        add_grocery.setOnClickListener {
            var amount = 1f
            val servingPosition = spinner_serving.selectedItemPosition

            val multiplier: Int
            if (servingPosition >= defaultUnits!!.size) {
                amount = servings!![servingPosition - defaultUnits!!.size].amount.toFloat()
                multiplier = servings!![servingPosition - defaultUnits!!.size].unit.multiplier
            } else {
                multiplier = defaultUnits!![servingPosition].multiplier
            }

            val editTextValue: Float = if (!edittext_amount.text.toString().equals(""))
                java.lang.Float.valueOf(edittext_amount.text.toString())
            else
                java.lang.Float.valueOf(edittext_amount.hint.toString())
            amount *= multiplier * editTextValue

            var id = -1
            if (mode == MODE_EDIT_DIARY_ENTRY)
                id = diaryEntryId

            if (mode == MODE_INGREDIENT_SEARCH || mode == MODE_REPLACE_INGREDIENT_SEARCH) {
                val intent = Intent()
                intent.putExtra(EXTRA_INDEX, ingredientIndex)
                intent.putExtra(EXTRA_GROCERY_ID, groceryDisplayModel!!.id)
                intent.putExtra(EXTRA_AMOUNT, amount)
                intent.putExtra(EXTRA_UNIT_ID, defaultUnits!![0].id)
                setResult(Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT, intent)
                finish()
            } else if (mode == MODE_EDIT_INGREDIENT) {
                val intent = Intent()
                intent.putExtra(EXTRA_INGREDIENT_ID, ingredientIndex)
                intent.putExtra(EXTRA_AMOUNT, amount)
                setResult(Constants.EDIT_INGREDIENT_INTENT_RESULT, intent)
                finish()
            } else {
                if (groceryDisplayModel!!.type == GroceryEntity.GroceryEntityType.TYPE_FOOD) {
                    presenter.addFood(DiaryEntryDisplayModel(id,
                            meals!![spinner_meal.selectedItemPosition],
                            amount,
                            defaultUnits!![0],
                            groceryDisplayModel,
                            databaseDateFormat.format(selectedDateCalendar.time)))
                } else {
                    presenter.addDrink(DiaryEntryDisplayModel(id,
                            amount,
                            defaultUnits!![0],
                            groceryDisplayModel,
                            databaseDateFormat.format(selectedDateCalendar.time)))
                }
            }
        }
    }

    override fun refreshNutritionDetails() {
        var amount = 1f
        val servingPosition = spinner_serving.selectedItemPosition
        var multiplier = 0
        if (servingPosition >= defaultUnits!!.size) {
            amount = servings!![servingPosition - defaultUnits!!.size].amount.toFloat()
            multiplier = servings!![servingPosition - defaultUnits!!.size].unit.multiplier
        } else {
            multiplier = defaultUnits!![servingPosition].multiplier
        }
        val editTextValue = if (edittext_amount.text.toString() != "")
            java.lang.Float.valueOf(edittext_amount.text.toString())
        else
            java.lang.Float.valueOf(edittext_amount.hint.toString())
        amount *= multiplier * editTextValue

        presenter.updateNutritionDetails(groceryDisplayModel, amount)
    }

    override fun setupActivityComponents() {
        TrackerApplication.get(this).createGroceryDetailsComponent().inject(this)
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun showNutritionDetails(value: String) {
        if (value == VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY) {
            detailsView = CaloryDetailsView(viewstub_calory_details.inflate())
        } else {
            detailsView = CaloryMacroDetailsView(viewstub_calory_macro_details.inflate())
        }
    }

    override fun setupAllergenWarning(allergenDisplayModels: ArrayList<AllergenDisplayModel>) {
        val allergenView = AllergenView(viewstub_allergen_info.inflate())

        var warning = getString(R.string.allergen_warning) + " "

        for (i in allergenDisplayModels.indices) {
            warning += allergenDisplayModels[i].name
            if (i < allergenDisplayModels.size - 1)
                warning += ", "
        }

        allergenView.allergenWarning.text = warning
    }

    override fun setNutritionMaxValues(values: HashMap<String, Long>) {
        maxValues = values
        detailsView!!.caloryProgressBarMaxValue.text = "" + values[Constants.CALORIES]!!

        if (detailsView is CaloryMacroDetailsView) {
            (detailsView as CaloryMacroDetailsView).proteinProgressBarMaxValue.text = "von\n" + values[Constants.PROTEINS] + "g"
            (detailsView as CaloryMacroDetailsView).carbsProgressBarMaxValue.text = "von\n" + values[Constants.CARBS] + "g"
            (detailsView as CaloryMacroDetailsView).fatsProgressBarMaxValue.text = "von\n" + values[Constants.FATS] + "g"
        }
    }

    override fun updateNutritionDetails(values: HashMap<String, Float>) {
        detailsView!!.caloryProgressBar.progress = 100.0f / maxValues!![Constants.CALORIES]!! * values[Constants.CALORIES]!!
        detailsView!!.caloryProgressBarValue.text = "" + values[Constants.CALORIES]!!.toFloat().toInt()

        if (detailsView is CaloryMacroDetailsView) {
            (detailsView as CaloryMacroDetailsView).proteinProgressBar.progress = 100.0f / maxValues!![Constants.PROTEINS]!! * values[Constants.PROTEINS]!!
            (detailsView as CaloryMacroDetailsView).proteinProgressBarValue.text = "" + StringUtils.formatFloat(values[Constants.PROTEINS]!!)

            (detailsView as CaloryMacroDetailsView).carbsProgressBar.progress = 100.0f / maxValues!![Constants.CARBS]!! * values[Constants.CARBS]!!
            (detailsView as CaloryMacroDetailsView).carbsProgressBarValue.text = "" + StringUtils.formatFloat(values[Constants.CARBS]!!)

            (detailsView as CaloryMacroDetailsView).fatsProgressBar.progress = 100.0f / maxValues!![Constants.FATS]!! * values[Constants.FATS]!!
            (detailsView as CaloryMacroDetailsView).fatsProgressBarValue.text = "" + StringUtils.formatFloat(values[Constants.FATS]!!)
        }
    }

    override fun fillGroceryDetails(grocery: GroceryDisplayModel) {
        groceryDisplayModel = grocery
        supportActionBar!!.title = grocery.name
    }

    override fun initializeServingsSpinner(defaultUnits: ArrayList<UnitDisplayModel>, servings: ArrayList<ServingDisplayModel>) {
        this.defaultUnits = defaultUnits
        val servingLabels = ArrayList<String>()
        for (unit in defaultUnits)
            servingLabels.add(unit.name)

        this.servings = servings
        for (serving in servings)
            servingLabels.add(formatServing(serving))

        val servingAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, servingLabels)
        servingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_serving.adapter = servingAdapter
        spinner_serving.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                if (i >= defaultUnits.size)
                    edittext_amount.setHint(R.string.value_one)
                else
                    edittext_amount.setHint(R.string.value_one_hundret)

                refreshNutritionDetails()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                return
            }
        }
    }

    override fun initializeMealSpinner(mealDisplayModels: ArrayList<MealDisplayModel>) {
        if (groceryDisplayModel!!.type == GroceryEntity.GroceryEntityType.TYPE_DRINK) {
            spinner_meal.visibility = View.GONE
            return
        }
        this.meals = mealDisplayModels

        val mealNames = ArrayList<String>()
        var selectedMealIndex = 0
        for (meal in mealDisplayModels) {
            mealNames.add(meal.name)
            if (meal.id == selectedMeal)
                selectedMealIndex = meals!!.indexOf(meal)
        }

        val mealAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mealNames)
        mealAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_meal.adapter = mealAdapter
        spinner_meal.setSelection(selectedMealIndex)
    }

    override fun navigateToDiaryFragment() {
        startActivity(MainActivity.newIntentToDiary(this))
        finish()
    }

    private fun formatServing(serving: ServingDisplayModel): String {
        return serving.description + " (" + serving.amount + " " + serving.unit.name + ")"
    }

    private fun setupSupportActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_back_arrow_white, null))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        else if (item.itemId == R.id.food_detail_favorite) {
            item.isChecked = !item.isChecked
            if (item.isChecked) {
                item.icon = getDrawable(R.drawable.ic_star_filled_white)
            } else {
                item.icon = getDrawable(R.drawable.ic_star_white)
            }
            presenter.onFavoriteGroceryClicked(item.isChecked, groceryDisplayModel)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun openDatePicker() {
        if (dateSetListener == null) {
            dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                selectedDateCalendar.set(Calendar.YEAR, year)
                selectedDateCalendar.set(Calendar.MONTH, monthOfYear)
                selectedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                date_label.text = simpleDateFormat.format(selectedDateCalendar.time)
            }
        }

        val dialog = DatePickerDialog(this, dateSetListener, selectedDateCalendar.get(Calendar.YEAR), selectedDateCalendar.get(Calendar.MONTH), selectedDateCalendar.get(Calendar.DAY_OF_MONTH))
        dialog.datePicker.maxDate = Calendar.getInstance().time.time + Constants.weekInMilliS
        dialog.show()
    }

    override fun setFavoriteIconCheckedState(checked: Boolean) {
        val item = menu!!.findItem(R.id.food_detail_favorite).setChecked(checked)
        if (item.isChecked) {
            item.icon = getDrawable(R.drawable.ic_star_filled_white)
        } else {
            item.icon = getDrawable(R.drawable.ic_star_white)
        }
    }

    override fun prepareEditMode(diaryEntry: DiaryEntryDisplayModel) {
        selectedDate = diaryEntry.date
        try {
            val currentSelectedDate = databaseDateFormat.parse(selectedDate!!)
            date_label.text = simpleDateFormat.format(currentSelectedDate!!)
            selectedDateCalendar.time = currentSelectedDate
        } catch (e: ParseException) {
            e.printStackTrace()
            date_label.text = simpleDateFormat.format(Calendar.getInstance().time)
        }

        edittext_amount.setText(StringUtils.formatFloat(diaryEntry.amount))

        if (diaryEntry.grocery.type == GroceryEntity.GroceryEntityType.TYPE_FOOD)
            spinner_meal.setSelection(meals!!.indexOf(diaryEntry.meal))
        spinner_serving.setSelection(defaultUnits!!.indexOf(diaryEntry.unit))

        add_grocery.text = getString(R.string.save_button)
        delete_diary_entry.visibility = View.VISIBLE
    }

    override fun prepareAddIngredientMode() {
        date_label.visibility = View.GONE
        spinner_meal.visibility = View.GONE
    }

    override fun finishView() {
        this.finish()
    }

    override fun prepareEditIngredientMode(amount: Float) {
        spinner_meal.visibility = View.GONE
        edittext_amount.setText(StringUtils.formatFloat(amount))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.finish()
        TrackerApplication.get(this).releaseGroceryDetailsComponent()
        dateSetListener = null
    }

    companion object {

        private const val LOWER_BOUND_AMOUNT = 1.0f
        private const val UPPER_BOUND_AMOUNT = 2000.0f

        const val EXTRA_MODE = "EXTRA_MODE"
        const val EXTRA_SELECTED_DATE = "EXTRA_SELECTED_DATE"
        const val EXTRA_SELECTED_MEAL = "EXTRA_SELECTED_MEAL"
        const val EXTRA_INGREDIENT_ID = "EXTRA_INGREDIENT_ID"
        const val EXTRA_GROCERY_ID = "EXTRA_GROCERY_ID"
        const val EXTRA_DIARY_ENTRY_ID = "EXTRA_DIARY_ENTRY_ID"
        const val EXTRA_AMOUNT = "EXTRA_AMOUNT"
        const val EXTRA_INDEX = "EXTRA_INDEX"
        const val EXTRA_UNIT_ID = "EXTRA_UNIT_ID"

        fun newGrocerySearchIntent(context: Context, groceryId: Int, selectedDate: String, selectedMeal: Int): Intent {
            val intent = Intent(context, GroceryDetailsActivity::class.java)

            intent.putExtra(EXTRA_MODE, MODE_GROCERY_SEARCH)
            intent.putExtra(EXTRA_GROCERY_ID, groceryId)
            intent.putExtra(EXTRA_SELECTED_DATE, selectedDate)
            intent.putExtra(EXTRA_SELECTED_MEAL, selectedMeal)

            return intent
        }

        fun newIngredientSearchIntent(context: Context, groceryId: Int): Intent {
            val intent = Intent(context, GroceryDetailsActivity::class.java)

            intent.putExtra(EXTRA_MODE, MODE_INGREDIENT_SEARCH)
            intent.putExtra(EXTRA_GROCERY_ID, groceryId)

            return intent
        }

        fun newReplaceIngredientSearchIntent(context: Context, groceryId: Int, ingredientIndex: Int): Intent {
            val intent = Intent(context, GroceryDetailsActivity::class.java)

            intent.putExtra(EXTRA_MODE, MODE_REPLACE_INGREDIENT_SEARCH)
            intent.putExtra(EXTRA_GROCERY_ID, groceryId)
            intent.putExtra(EXTRA_INGREDIENT_ID, ingredientIndex)

            return intent
        }

        fun newEditDiaryEntryIntent(context: Context, diaryEntryId: Int): Intent {
            val intent = Intent(context, GroceryDetailsActivity::class.java)

            intent.putExtra(EXTRA_MODE, MODE_EDIT_DIARY_ENTRY)
            intent.putExtra(EXTRA_DIARY_ENTRY_ID, diaryEntryId)

            return intent
        }

        fun newEditIngredientIntent(context: Context, ingredientIndex: Int, ingredient: IngredientDisplayModel): Intent {
            val intent = Intent(context, GroceryDetailsActivity::class.java)

            intent.putExtra(EXTRA_MODE, MODE_EDIT_INGREDIENT)
            intent.putExtra(EXTRA_INGREDIENT_ID, ingredientIndex)
            intent.putExtra(EXTRA_GROCERY_ID, ingredient.grocery.id)
            intent.putExtra(EXTRA_AMOUNT, ingredient.amount)

            return intent
        }
    }
}
