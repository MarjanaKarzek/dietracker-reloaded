package de.karzek.diettracker.presentation.search.grocery

import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.data.cache.model.GroceryEntity
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseActivity
import de.karzek.diettracker.presentation.model.GroceryDisplayModel
import de.karzek.diettracker.presentation.search.grocery.adapter.GrocerySearchResultListAdapter
import de.karzek.diettracker.presentation.search.grocery.adapter.itemWrapper.GrocerySearchResultItemWrapper
import de.karzek.diettracker.presentation.search.grocery.barcodeScanner.BarcodeScannerActivity
import de.karzek.diettracker.presentation.search.grocery.groceryDetail.GroceryDetailsActivity
import de.karzek.diettracker.presentation.util.Constants
import de.karzek.diettracker.presentation.util.Constants.ZXING_CAMERA_PERMISSION
import kotlinx.android.synthetic.main.activity_grocery_search.*
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
class GrocerySearchActivity : BaseActivity(), GrocerySearchContract.View {

    @Inject
    lateinit var presenter: GrocerySearchContract.Presenter

    private var groceryType: Int = 0
    private var noResultsPlaceholder: String? = null
    private var noFavoritesPlaceholder: String? = null
    private var selectedDate: String? = null
    private var selectedMeal: Int = 0
    private var ingredientIndex: Int = 0
    private var mode: Int = 0

    override fun onResume() {
        super.onResume()
        presenter.getFavoriteGroceries(groceryType)
    }

    override fun setupActivityComponents() {
        TrackerApplication.get(this).createGrocerySearchComponent().inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.grocery_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.grocery_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.isIconified = true
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query != "")
                    presenter.getGroceriesMatchingQuery(query, groceryType)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query != "")
                    presenter.getGroceriesMatchingQuery(query, groceryType)
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.grocery_search_barcode) {
            presenter.onBarcodeScannerClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_search)

        mode = intent.extras!!.getInt(EXTRA_MODE, GrocerySearchContract.View.SearchMode.MODE_GROCERY_SEARCH)

        when (mode) {
            GrocerySearchContract.View.SearchMode.MODE_GROCERY_SEARCH -> {
                selectedDate = intent.extras!!.getString(EXTRA_SELECTED_DATE)
                selectedMeal = intent.extras!!.getInt(EXTRA_SELECTED_MEAL)
                groceryType = intent.extras!!.getInt(EXTRA_GROCERY_TYPE)
            }
            GrocerySearchContract.View.SearchMode.MODE_INGREDIENT_SEARCH -> groceryType = intent.extras!!.getInt(EXTRA_GROCERY_TYPE)
            GrocerySearchContract.View.SearchMode.MODE_REPLACE_INGREDIENT_SEARCH -> {
                groceryType = intent.extras!!.getInt(EXTRA_GROCERY_TYPE)
                ingredientIndex = intent.extras!!.getInt(EXTRA_INGREDIENT_INDEX)
            }
        }

        initializePlaceholders(groceryType)

        setupSupportActionBar()
        setupRecyclerView()

        presenter.setView(this)
        presenter.start()
        presenter.getFavoriteGroceries(groceryType)
    }

    private fun initializePlaceholders(type: Int) {
        when (type) {
            GroceryEntity.GroceryEntityType.TYPE_FOOD -> {
                noResultsPlaceholder = getString(R.string.food_search_query_without_result_placeholder)
                noFavoritesPlaceholder = getString(R.string.food_search_placeholder)
            }
            GroceryEntity.GroceryEntityType.TYPE_DRINK -> {
                noResultsPlaceholder = getString(R.string.drink_search_query_without_result_placeholder)
                noFavoritesPlaceholder = getString(R.string.drink_search_placeholder)
            }
            else -> {
                noResultsPlaceholder = getString(R.string.product_search_query_without_result_placeholder)
                noFavoritesPlaceholder = getString(R.string.product_search_placeholder)
            }
        }
    }

    override fun onDestroy() {
        presenter.finish()
        super.onDestroy()
        TrackerApplication.get(this).releaseGrocerySearchComponent()
    }

    override fun showGroceryDetails(id: Int) {
        when (mode) {
            GrocerySearchContract.View.SearchMode.MODE_GROCERY_SEARCH -> startActivity(GroceryDetailsActivity.newGrocerySearchIntent(this, id, selectedDate!!, selectedMeal))
            GrocerySearchContract.View.SearchMode.MODE_INGREDIENT_SEARCH -> startActivityForResult(GroceryDetailsActivity.newIngredientSearchIntent(this, id), Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT)
            GrocerySearchContract.View.SearchMode.MODE_REPLACE_INGREDIENT_SEARCH -> startActivityForResult(GroceryDetailsActivity.newReplaceIngredientSearchIntent(this, id, ingredientIndex), Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT)
        }
    }

    override fun updateFoodSearchResultList(foods: ArrayList<GroceryDisplayModel>) {
        val wrappedFoods = ArrayList<GrocerySearchResultItemWrapper>()

        for (food in foods) {
            wrappedFoods.add(GrocerySearchResultItemWrapper(food.type, food))
        }

        (recycler_view.adapter as GrocerySearchResultListAdapter).setList(wrappedFoods)
    }

    override fun showPlaceholder() {
        food_search_placeholder.text = noFavoritesPlaceholder
        food_search_placeholder.visibility = View.VISIBLE
    }

    override fun hidePlaceholder() {
        food_search_placeholder.visibility = View.GONE
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun showQueryWithoutResultPlaceholder() {
        food_search_placeholder.text = noResultsPlaceholder
        food_search_placeholder.visibility = View.VISIBLE
    }

    override fun hideQueryWithoutResultPlaceholder() {
        food_search_placeholder.visibility = View.GONE
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

    override fun startBarcodeScannerActivity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), ZXING_CAMERA_PERMISSION)
        } else {
            when (mode) {
                GrocerySearchContract.View.SearchMode.MODE_GROCERY_SEARCH -> startActivity(BarcodeScannerActivity.newGrocerySearchIntent(this, selectedDate!!, selectedMeal))
                GrocerySearchContract.View.SearchMode.MODE_INGREDIENT_SEARCH -> startActivityForResult(BarcodeScannerActivity.newIngredientSearchIntent(this), Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT)
                GrocerySearchContract.View.SearchMode.MODE_REPLACE_INGREDIENT_SEARCH -> startActivityForResult(BarcodeScannerActivity.newReplaceIngredientSearchIntent(this, ingredientIndex), Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT)
            }
        }
    }

    private fun setupSupportActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_back_arrow_white, null))
        when (groceryType) {
            GroceryEntity.GroceryEntityType.TYPE_FOOD -> supportActionBar!!.setTitle(getString(R.string.food_search_title))
            GroceryEntity.GroceryEntityType.TYPE_DRINK -> supportActionBar!!.setTitle(getString(R.string.drink_search_title))
            else -> supportActionBar!!.setTitle(R.string.product_search_title)
        }
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = GrocerySearchResultListAdapter(presenter, presenter, presenter)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ZXING_CAMERA_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                when (mode) {
                    GrocerySearchContract.View.SearchMode.MODE_GROCERY_SEARCH -> startActivity(BarcodeScannerActivity.newGrocerySearchIntent(this, selectedDate!!, selectedMeal))
                    GrocerySearchContract.View.SearchMode.MODE_INGREDIENT_SEARCH -> startActivityForResult(BarcodeScannerActivity.newIngredientSearchIntent(this), Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT)
                    GrocerySearchContract.View.SearchMode.MODE_REPLACE_INGREDIENT_SEARCH -> startActivityForResult(BarcodeScannerActivity.newReplaceIngredientSearchIntent(this, ingredientIndex), Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT)
                }
            else
                Toast.makeText(this, getString(R.string.permission_grand_camera_for_barcode_scanner), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT) {
            val intent = Intent()
            intent.putExtra(EXTRA_INDEX, data!!.getIntExtra(EXTRA_INDEX, Constants.INVALID_ENTITY_ID))
            intent.putExtra(EXTRA_GROCERY_ID, data.getIntExtra(EXTRA_GROCERY_ID, 0))
            intent.putExtra(EXTRA_AMOUNT, data.getFloatExtra(EXTRA_AMOUNT, 0.0f))
            intent.putExtra(EXTRA_UNIT_ID, data.getIntExtra(EXTRA_UNIT_ID, 0))
            setResult(Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT, intent)
            finish()
        }
    }

    companion object {

        const val EXTRA_MODE = "EXTRA_MODE"
        const val EXTRA_SELECTED_DATE = "EXTRA_SELECTED_DATE"
        const val EXTRA_SELECTED_MEAL = "EXTRA_SELECTED_MEAL"
        const val EXTRA_GROCERY_TYPE = "EXTRA_GROCERY_TYPE"
        const val EXTRA_INGREDIENT_INDEX = "EXTRA_INGREDIENT_ID"
        const val EXTRA_GROCERY_ID = "EXTRA_GROCERY_ID"
        const val EXTRA_AMOUNT = "EXTRA_AMOUNT"
        const val EXTRA_INDEX = "EXTRA_INDEX"
        const val EXTRA_UNIT_ID = "EXTRA_UNIT_ID"

        fun newGrocerySearchIntent(context: Context, groceryType: Int, selectedDate: String, selectedMeal: Int): Intent {
            val intent = Intent(context, GrocerySearchActivity::class.java)

            intent.putExtra(EXTRA_MODE, GrocerySearchContract.View.SearchMode.MODE_GROCERY_SEARCH)
            intent.putExtra(EXTRA_SELECTED_DATE, selectedDate)
            intent.putExtra(EXTRA_SELECTED_MEAL, selectedMeal)
            intent.putExtra(EXTRA_GROCERY_TYPE, groceryType)

            return intent
        }

        fun newIngredientSearchIntent(context: Context, groceryType: Int): Intent {
            val intent = Intent(context, GrocerySearchActivity::class.java)
            intent.putExtra(EXTRA_MODE, GrocerySearchContract.View.SearchMode.MODE_INGREDIENT_SEARCH)
            intent.putExtra(EXTRA_GROCERY_TYPE, groceryType)
            return intent
        }

        fun newIngredientReplaceIntent(context: Context, groceryType: Int, ingredientIndex: Int): Intent {
            val intent = Intent(context, GrocerySearchActivity::class.java)
            intent.putExtra(EXTRA_MODE, GrocerySearchContract.View.SearchMode.MODE_REPLACE_INGREDIENT_SEARCH)
            intent.putExtra(EXTRA_GROCERY_TYPE, groceryType)
            intent.putExtra(EXTRA_INGREDIENT_INDEX, ingredientIndex)
            return intent
        }
    }
}
