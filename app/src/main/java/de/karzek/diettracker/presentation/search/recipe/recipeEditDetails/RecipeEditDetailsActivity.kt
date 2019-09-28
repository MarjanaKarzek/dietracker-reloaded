package de.karzek.diettracker.presentation.search.recipe.recipeEditDetails

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseActivity
import de.karzek.diettracker.presentation.main.MainActivity
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.RecipeManipulationActivity
import de.karzek.diettracker.presentation.model.MealDisplayModel
import de.karzek.diettracker.presentation.model.RecipeDisplayModel
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.RecipeEditDetailsViewListAdapter
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.adapter.itemWrapper.RecipeEditDetailsViewItemWrapper
import de.karzek.diettracker.presentation.util.Constants
import de.karzek.diettracker.presentation.util.SharedPreferencesUtil.VALUE_SETTING_NUTRITION_DETAILS_CALORIES_AND_MACROS
import de.karzek.diettracker.presentation.util.SharedPreferencesUtil.VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.snippet_loading_view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RecipeEditDetailsActivity : BaseActivity(), RecipeEditDetailsContract.View {

    private var menu: Menu? = null
    private var recipeId: Int = 0
    private var selectedMeal: Int = 0
    private var selectedDate: String? = null

    private val selectedDateCalendar = Calendar.getInstance()
    private val databaseDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY)
    private var dateSetListener: DatePickerDialog.OnDateSetListener? = null

    @Inject
    lateinit var presenter: RecipeEditDetailsContract.Presenter

    override fun setupActivityComponents() {
        TrackerApplication.get(this).createRecipeEditDetailsComponent().inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.recipe_details, menu)

        this.menu = menu
        presenter.checkFavoriteState(recipeId)

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        recipeId = intent.extras!!.getInt("recipeId")
        selectedMeal = intent.extras!!.getInt("selectedMeal")
        selectedDate = intent.extras!!.getString("selectedDate")

        setupSupportActionBar()
        setupRecyclerView()

        presenter.setView(this)
        presenter.setRecipeId(recipeId)
        presenter.setSelectedMeal(selectedMeal)
        presenter.setSelectedDate(selectedDate)
        presenter.start()
    }

    private fun setupSupportActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_back_arrow_white, null))
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = RecipeEditDetailsViewListAdapter(presenter, presenter, presenter, presenter, presenter, presenter)
    }

    @SuppressLint("StringFormatInvalid")
    override fun setupViewsInRecyclerView(displayModel: RecipeDisplayModel, selectedPortions: Float, nutritionDetails: String, detailsExpanded: Boolean, maxValues: HashMap<String, Long>, values: HashMap<String, Float>, meals: ArrayList<MealDisplayModel>) {
        supportActionBar!!.title = displayModel.title

        val views = ArrayList<RecipeEditDetailsViewItemWrapper>()
        if (displayModel.photo != null) {
            views.add(RecipeEditDetailsViewItemWrapper(RecipeEditDetailsViewItemWrapper.ItemType.PHOTO_VIEW, BitmapFactory.decodeByteArray(displayModel.photo, 0, displayModel.photo.size)))
        }

        views.add(RecipeEditDetailsViewItemWrapper(RecipeEditDetailsViewItemWrapper.ItemType.INGREDIENTS_TITLE_VIEW, selectedPortions))

        if (nutritionDetails == VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY && detailsExpanded)
            views.add(RecipeEditDetailsViewItemWrapper(RecipeEditDetailsViewItemWrapper.ItemType.CALORY_DETAILS_VIEW, maxValues, values))
        else if (nutritionDetails == VALUE_SETTING_NUTRITION_DETAILS_CALORIES_AND_MACROS && detailsExpanded)
            views.add(RecipeEditDetailsViewItemWrapper(RecipeEditDetailsViewItemWrapper.ItemType.CALORIES_AND_MACROS_DETAILS_VIEW, maxValues, values))

        for (ingredient in displayModel.ingredients)
            views.add(RecipeEditDetailsViewItemWrapper(RecipeEditDetailsViewItemWrapper.ItemType.INGREDIENT_VIEW, ingredient, selectedPortions))

        views.add(RecipeEditDetailsViewItemWrapper(RecipeEditDetailsViewItemWrapper.ItemType.MEAL_SELECTOR_VIEW, meals, selectedMeal))
        views.add(RecipeEditDetailsViewItemWrapper(RecipeEditDetailsViewItemWrapper.ItemType.DATE_SELECTOR_VIEW, selectedDate))

        views.add(RecipeEditDetailsViewItemWrapper(RecipeEditDetailsViewItemWrapper.ItemType.ADD_VIEW))

        (recycler_view.adapter as RecipeEditDetailsViewListAdapter).setList(views)
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == android.R.id.home -> this.finish()
            item.itemId == R.id.recipe_details_favorite -> {
                item.isChecked = !item.isChecked
                if (item.isChecked) {
                    item.icon = getDrawable(R.drawable.ic_star_filled_white)
                } else {
                    item.icon = getDrawable(R.drawable.ic_star_white)
                }
                presenter.onFavoriteRecipeClicked(item.isChecked)
            }
            item.itemId == R.id.recipe_details_edit -> startActivity(RecipeManipulationActivity.newEditIntent(this, recipeId))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setFavoriteIconCheckedState(checked: Boolean) {
        val item = menu!!.findItem(R.id.recipe_details_favorite).setChecked(checked)
        if (item.isChecked) {
            item.icon = getDrawable(R.drawable.ic_star_filled_white)
        } else {
            item.icon = getDrawable(R.drawable.ic_star_white)
        }
    }

    override fun showErrorNotEnoughIngredientsLeft() {
        Toast.makeText(this, getString(R.string.error_message_not_enough_ingredients_left), Toast.LENGTH_SHORT).show()
    }

    override fun navigateToDiary() {
        startActivity(MainActivity.newIntentToDiary(this))
        finish()
    }

    override fun openDateSelectorDialog() {
        if (dateSetListener == null) {
            dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                selectedDateCalendar.set(Calendar.YEAR, year)
                selectedDateCalendar.set(Calendar.MONTH, monthOfYear)
                selectedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                selectedDate = databaseDateFormat.format(selectedDateCalendar.time)
                presenter.setSelectedDateFromDialog(selectedDate)
            }
        }

        val dialog = DatePickerDialog(this, dateSetListener, selectedDateCalendar.get(Calendar.YEAR), selectedDateCalendar.get(Calendar.MONTH), selectedDateCalendar.get(Calendar.DAY_OF_MONTH))
        dialog.datePicker.maxDate = Calendar.getInstance().time.time + Constants.weekInMilliS
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.finish()
        TrackerApplication.get(this).releaseRecipeEditDetailsComponent()
        dateSetListener = null
    }

    companion object {

        fun newIntent(context: Context, id: Int, selectedMeal: Int, selectedDate: String): Intent {
            val intent = Intent(context, RecipeEditDetailsActivity::class.java)
            intent.putExtra("recipeId", id)
            intent.putExtra("selectedMeal", selectedMeal)
            intent.putExtra("selectedDate", selectedDate)

            return intent
        }
    }

}
