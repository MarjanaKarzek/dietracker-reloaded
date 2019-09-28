package de.karzek.diettracker.presentation.main.cookbook.recipeDetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseActivity
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.RecipeDetailsViewListAdapter
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.adapter.itemWrapper.RecipeDetailsViewItemWrapper
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.RecipeManipulationActivity
import de.karzek.diettracker.presentation.model.RecipeDisplayModel
import de.karzek.diettracker.presentation.util.SharedPreferencesUtil.VALUE_SETTING_NUTRITION_DETAILS_CALORIES_AND_MACROS
import de.karzek.diettracker.presentation.util.SharedPreferencesUtil.VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.snippet_loading_view.*
import java.util.*
import javax.inject.Inject

class RecipeDetailsActivity : BaseActivity(), RecipeDetailsContract.View {

    private var menu: Menu? = null
    private var recipeId: Int = 0

    @Inject
    lateinit var presenter: RecipeDetailsContract.Presenter

    override fun setupActivityComponents() {
        TrackerApplication.get(this).createRecipeDetailsComponent().inject(this)
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
        setupSupportActionBar()
        setupRecyclerView()

        presenter.setView(this)
        presenter.setRecipeId(recipeId)
        presenter.start()
    }

    private fun setupSupportActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_back_arrow_white, null))
        }
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = RecipeDetailsViewListAdapter(presenter)
    }

    @SuppressLint("StringFormatInvalid")
    override fun setupViewsInRecyclerView(displayModel: RecipeDisplayModel, nutritionDetails: String, detailsExpanded: Boolean, maxValues: HashMap<String, Long>, values: HashMap<String, Float>) {
        supportActionBar?.title = displayModel.title

        val views = ArrayList<RecipeDetailsViewItemWrapper>()
        if (displayModel.photo != null) {
            views.add(RecipeDetailsViewItemWrapper(RecipeDetailsViewItemWrapper.ItemType.PHOTO_VIEW, BitmapFactory.decodeByteArray(displayModel.photo, 0, displayModel.photo.size)))
        }

        views.add(RecipeDetailsViewItemWrapper(RecipeDetailsViewItemWrapper.ItemType.INGREDIENTS_TITLE_VIEW, getString(R.string.recipe_details_ingredients_title, StringUtils.formatFloat(displayModel.portions))))

        if (nutritionDetails == VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY && detailsExpanded)
            views.add(RecipeDetailsViewItemWrapper(RecipeDetailsViewItemWrapper.ItemType.CALORY_DETAILS_VIEW, maxValues, values))
        else if (nutritionDetails == VALUE_SETTING_NUTRITION_DETAILS_CALORIES_AND_MACROS && detailsExpanded)
            views.add(RecipeDetailsViewItemWrapper(RecipeDetailsViewItemWrapper.ItemType.CALORIES_AND_MACROS_DETAILS_VIEW, maxValues, values))

        for (ingredient in displayModel.ingredients) {
            views.add(RecipeDetailsViewItemWrapper(RecipeDetailsViewItemWrapper.ItemType.INGREDIENT_VIEW, ingredient))
        }

        if (displayModel.steps.size > 0)
            views.add(RecipeDetailsViewItemWrapper(RecipeDetailsViewItemWrapper.ItemType.TITLE_VIEW, getString(R.string.recipe_preparation_steps_title)))
        for (step in displayModel.steps)
            views.add(RecipeDetailsViewItemWrapper(RecipeDetailsViewItemWrapper.ItemType.PREPARATION_STEP_VIEW, step))

        if (displayModel.meals.size > 0)
            views.add(RecipeDetailsViewItemWrapper(RecipeDetailsViewItemWrapper.ItemType.TITLE_VIEW, getString(R.string.recipe_meals_title)))
        views.add(RecipeDetailsViewItemWrapper(RecipeDetailsViewItemWrapper.ItemType.MEALS_VIEW, displayModel.meals))

        (recycler_view!!.adapter as RecipeDetailsViewListAdapter).setList(views)
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            this.finish()
        else if (item.itemId == R.id.recipe_details_favorite) {
            item.isChecked = !item.isChecked
            if (item.isChecked) {
                item.icon = getDrawable(R.drawable.ic_star_filled_white)
            } else {
                item.icon = getDrawable(R.drawable.ic_star_white)
            }
            presenter.onFavoriteRecipeClicked(item.isChecked)
        } else if (item.itemId == R.id.recipe_details_edit) {
            startActivity(RecipeManipulationActivity.newEditIntent(this, recipeId))
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.finish()
        TrackerApplication.get(this).releaseRecipeDetailsComponent()
    }

    companion object {

        fun newIntent(context: Context, id: Int): Intent {
            val intent = Intent(context, RecipeDetailsActivity::class.java)
            intent.putExtra("recipeId", id)

            return intent
        }
    }

}
