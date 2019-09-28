package de.karzek.diettracker.presentation.main.home


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.getbase.floatingactionbutton.FloatingActionsMenu
import de.karzek.diettracker.R
import de.karzek.diettracker.data.cache.model.GroceryEntity
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseFragment
import de.karzek.diettracker.presentation.main.diary.meal.adapter.favoriteRecipeList.FavoriteRecipeListAdapter
import de.karzek.diettracker.presentation.model.RecipeDisplayModel
import de.karzek.diettracker.presentation.search.grocery.GrocerySearchActivity
import de.karzek.diettracker.presentation.search.recipe.RecipeSearchActivity
import de.karzek.diettracker.presentation.util.Constants
import de.karzek.diettracker.presentation.util.Constants.INVALID_ENTITY_ID
import de.karzek.diettracker.presentation.util.StringUtils
import de.karzek.diettracker.presentation.util.ValidationUtil
import de.karzek.diettracker.presentation.util.ViewUtils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.snippet_home_calory_status.*
import kotlinx.android.synthetic.main.snippet_home_drink_status.*
import kotlinx.android.synthetic.main.snippet_home_macro_status.*
import kotlinx.android.synthetic.main.snippet_loading_view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 12.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 12.05.2018
 */
class HomeFragment : BaseFragment(), HomeContract.View {

    @Inject
    lateinit var presenter: HomeContract.Presenter

    private val databaseDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY)
    private val databaseTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.GERMANY)
    private val date = Calendar.getInstance()

    override fun onResume() {
        super.onResume()
        showLoading()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        setupRecyclerView()
        floating_action_button_menu.setOnFloatingActionsMenuUpdateListener(object : FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {
            override fun onMenuExpanded() {
                fab_overlay.visibility = View.VISIBLE
            }

            override fun onMenuCollapsed() {
                fab_overlay.visibility = View.GONE
            }
        })
        ViewUtils.addElevationToFABMenuLabels(context, floating_action_button_menu)
        circle_progress_bar_drinks_value.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                circle_progress_bar_drinks_value.clearFocus()
                val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(circle_progress_bar_drinks_value.windowToken, 0)

                if (!StringUtils.isNullOrEmpty(circle_progress_bar_drinks_value.text.toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_WATER_INPUT,
                                    UPPER_BOUND_WATER_INPUT,
                                    java.lang.Float.valueOf(circle_progress_bar_drinks_value.text.toString()),
                                    circle_progress_bar_drinks_value,
                                    context))
                        presenter.updateAmountOfWater(java.lang.Float.valueOf(circle_progress_bar_drinks_value.text.toString()))
                } else
                    circle_progress_bar_drinks_value.setText(StringUtils.formatFloat(LOWER_BOUND_WATER_INPUT))
            }
            true
        }


        add_food.setOnClickListener { presenter.onAddFoodClicked() }
        add_drink.setOnClickListener { presenter.onAddDrinkClicked() }
        add_recipe.setOnClickListener { presenter.onAddRecipeClicked() }
        fab_overlay.setOnClickListener { presenter.onFabOverlayClicked() }
        add_bottle.setOnClickListener { presenter.addBottleWaterClicked() }
        add_glass.setOnClickListener { presenter.addGlassWaterClicked() }


        presenter.setView(this)
        presenter.setCurrentDate(databaseDateFormat.format(date.time))
        presenter.setCurrentTime(databaseTimeFormat.format(date.time))
        presenter.start()
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        recycler_view.layoutManager = manager
        recycler_view.adapter = FavoriteRecipeListAdapter(presenter)
    }

    override fun setupFragmentComponent() {
        TrackerApplication.get(context!!).createHomeComponent().inject(this)
    }

    override fun startFoodSearchActivity(currentMealId: Int) {
        startActivity(GrocerySearchActivity.newGrocerySearchIntent(context!!, GroceryEntity.GroceryEntityType.TYPE_FOOD, databaseDateFormat.format(date.time), currentMealId))
    }

    override fun startDrinkSearchActivity(currentMealId: Int) {
        startActivity(GrocerySearchActivity.newGrocerySearchIntent(context!!, GroceryEntity.GroceryEntityType.TYPE_DRINK, databaseDateFormat.format(date.time), INVALID_ENTITY_ID))
    }

    override fun startRecipeSearchActivity(currentMealId: Int) {
        startActivity(RecipeSearchActivity.newIntent(context!!, databaseDateFormat.format(date.time), currentMealId))
    }

    override fun closeFabMenu() {
        floating_action_button_menu.collapse()
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun showRecipeAddedInfo() {
        Toast.makeText(context, getString(R.string.success_message_portion_added), Toast.LENGTH_SHORT).show()
    }

    override fun updateRecyclerView(recipeDisplayModels: ArrayList<RecipeDisplayModel>) {
        (recycler_view.adapter as FavoriteRecipeListAdapter).setList(recipeDisplayModels)
    }

    override fun showRecyclerView() {
        recycler_view.visibility = View.VISIBLE
    }

    override fun hideRecyclerView() {
        recycler_view.visibility = View.GONE
    }

    override fun setCaloryState(sum: Float, maxValue: Int) {
        circle_progress_bar_calories.progress = 100.0f / maxValue * sum
        circle_progress_bar_calories_value.text = StringUtils.formatFloat(maxValue - sum)
    }

    override fun showFavoriteText(name: String) {
        favorite_recipe_title.visibility = View.VISIBLE
        favorite_recipe_title.text = getString(R.string.favorite_recipes_home_title, name)
    }

    override fun hideFavoriteText() {
        favorite_recipe_title.visibility = View.GONE
    }

    override fun setNutritionState(sums: HashMap<String, Float>, caloriesGoal: Int, proteinsGoal: Int, carbsGoal: Int, fatsGoal: Int) {
        circle_progress_bar_calories.progress = 100.0f / caloriesGoal * sums[Constants.CALORIES]!!
        circle_progress_bar_calories_value.text = StringUtils.formatFloat(caloriesGoal - sums[Constants.CALORIES]!!)

        circle_progress_bar_protein.progress = 100.0f / proteinsGoal * sums[Constants.PROTEINS]!!
        circle_progress_bar_protein_value.text = StringUtils.formatFloat(proteinsGoal - sums[Constants.PROTEINS]!!)

        circle_progress_bar_carbs.progress = 100.0f / carbsGoal * sums[Constants.CARBS]!!
        circle_progress_bar_carbs_value.text = StringUtils.formatFloat(carbsGoal - sums[Constants.CARBS]!!)

        circle_progress_bar_fats.progress = 100.0f / fatsGoal * sums[Constants.FATS]!!
        circle_progress_bar_fats_value.text = StringUtils.formatFloat(fatsGoal - sums[Constants.FATS]!!)
    }

    override fun hideNutritionState() {
        nutrition_state.visibility = View.GONE
    }

    override fun hideDrinksSection() {
        drinks_section.visibility = View.GONE
    }

    override fun setLiquidStatus(sum: Float, liquidGoal: Float) {
        circle_progress_bar_dinks_progress.progress = 100.0f / liquidGoal * sum
        circle_progress_bar_drinks_value.setText(StringUtils.formatFloat(sum))
        drinks_max_value.text = getString(R.string.generic_drinks_max_value, StringUtils.formatFloat(liquidGoal))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        TrackerApplication.get(context!!).releaseHomeComponent()
    }

    companion object {

        private const val LOWER_BOUND_WATER_INPUT = 0.0f
        private val UPPER_BOUND_WATER_INPUT = Float.MAX_VALUE
    }
}
