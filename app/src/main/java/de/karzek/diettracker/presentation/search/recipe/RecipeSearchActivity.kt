package de.karzek.diettracker.presentation.search.recipe

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseActivity
import de.karzek.diettracker.presentation.model.RecipeDisplayModel
import de.karzek.diettracker.presentation.search.recipe.adapter.RecipeSearchResultListAdapter
import de.karzek.diettracker.presentation.search.recipe.recipeEditDetails.RecipeEditDetailsActivity
import kotlinx.android.synthetic.main.activity_recipe_search.*
import kotlinx.android.synthetic.main.snippet_loading_view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 29.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 29.05.2018
 */
class RecipeSearchActivity : BaseActivity(), RecipeSearchContract.View {

    @Inject
    lateinit var presenter: RecipeSearchContract.Presenter

    private var noResultsPlaceholder: String? = null
    private var noFavoritesPlaceholder: String? = null
    private var selectedDate: String? = null
    private var selectedMeal: Int = 0

    override fun onResume() {
        super.onResume()
        presenter.getFavoriteRecipes(selectedMeal)
    }

    override fun setupActivityComponents() {
        TrackerApplication.get(this).createRecipeSearchComponent().inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.recipe_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.recipe_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.isIconified = true
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query != "")
                    presenter.getRecipesMatchingQuery(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query != "")
                    presenter.getRecipesMatchingQuery(query)
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_search)

        selectedDate = intent.extras!!.getString(EXTRA_SELECTED_DATE, "")
        selectedMeal = intent.extras!!.getInt(EXTRA_SELECTED_MEAL, 0)

        initializePlaceholders()

        setupSupportActionBar()
        setupRecyclerView()

        presenter.setView(this)
        presenter.start()
        presenter.getFavoriteRecipes(selectedMeal)
    }

    private fun initializePlaceholders() {
        noResultsPlaceholder = getString(R.string.recipe_search_query_without_result_placeholder)
        noFavoritesPlaceholder = getString(R.string.recipe_search_placeholder)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.finish()
        TrackerApplication.get(this).releaseRecipeSearchComponent()
    }

    override fun showRecipeDetails(id: Int) {
        startActivity(RecipeEditDetailsActivity.newIntent(this, id, selectedMeal, selectedDate!!))
    }

    override fun updateRecipeSearchResultList(recipes: ArrayList<RecipeDisplayModel>) {
        (recycler_view.adapter as RecipeSearchResultListAdapter).setList(recipes)
    }

    override fun showPlaceholder() {
        recipe_search_placeholder.text = noFavoritesPlaceholder
        recipe_search_placeholder.visibility = View.VISIBLE
    }

    override fun hidePlaceholder() {
        recipe_search_placeholder.visibility = View.GONE
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun showQueryWithoutResultPlaceholder() {
        recipe_search_placeholder.text = noResultsPlaceholder
        recipe_search_placeholder.visibility = View.VISIBLE
    }

    override fun hideQueryWithoutResultPlaceholder() {
        recipe_search_placeholder.visibility = View.GONE
    }

    override fun hideRecyclerView() {
        recycler_view.visibility = View.GONE
    }

    override fun showRecyclerView() {
        recycler_view.visibility = View.VISIBLE
    }

    override fun finishActivity() {
        this.finish()
    }

    override fun getSelectedDate(): String? {
        return selectedDate
    }

    override fun getSelectedMeal(): Int {
        return selectedMeal
    }

    private fun setupSupportActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_back_arrow_white, null))
        supportActionBar!!.setTitle(R.string.recipe_search_title)
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = RecipeSearchResultListAdapter(presenter, presenter)
    }

    companion object {

        var EXTRA_SELECTED_DATE = "EXTRA_SELECTED_DATE"
        var EXTRA_SELECTED_MEAL = "EXTRA_SELECTED_MEAL"

        fun newIntent(context: Context, selectedDate: String, selectedMeal: Int): Intent {
            val intent = Intent(context, RecipeSearchActivity::class.java)
            intent.putExtra(EXTRA_SELECTED_DATE, selectedDate)
            intent.putExtra(EXTRA_SELECTED_MEAL, selectedMeal)

            return intent
        }
    }
}
