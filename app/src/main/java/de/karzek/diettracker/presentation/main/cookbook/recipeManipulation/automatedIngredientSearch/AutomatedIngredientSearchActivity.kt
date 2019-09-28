package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.automatedIngredientSearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.data.cache.model.GroceryEntity
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseActivity
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.automatedIngredientSearch.adapter.IngredientSearchListAdapter
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.automatedIngredientSearch.adapter.itemWrapper.IngredientSearchItemWrapper
import de.karzek.diettracker.presentation.model.IngredientDisplayModel
import de.karzek.diettracker.presentation.model.ManualIngredientDisplayModel
import de.karzek.diettracker.presentation.model.RecipeDisplayModel
import de.karzek.diettracker.presentation.search.grocery.GrocerySearchActivity
import de.karzek.diettracker.presentation.search.grocery.barcodeScanner.BarcodeScannerActivity
import de.karzek.diettracker.presentation.util.Constants
import de.karzek.diettracker.presentation.util.Constants.INVALID_ENTITY_ID
import kotlinx.android.synthetic.main.activity_automated_ingredient_search.*
import kotlinx.android.synthetic.main.snippet_semi_transparent_loading_view.*
import java.util.*
import javax.inject.Inject

class AutomatedIngredientSearchActivity : BaseActivity(), AutomatedIngredientSearchContract.View {

    @Inject
    lateinit var presenter: AutomatedIngredientSearchContract.Presenter

    override fun setupActivityComponents() {
        TrackerApplication.get(this).createAutomatedIngredientSearchComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_automated_ingredient_search)

        setupSupportActionBar()
        setupRecyclerView()
        disableSaveButton()

        presenter.setView(this)
        val recipeDisplayModel = intent.extras!!.getParcelable<RecipeDisplayModel>(EXTRA_RECIPE)
        presenter.setRecipe(recipeDisplayModel)
        if (recipeDisplayModel!!.id != INVALID_ENTITY_ID)
            presenter.startEdit()
        else
            presenter.start()

        save_button.setOnClickListener { presenter.onSaveButtonClicked() }
    }

    private fun disableSaveButton() {
        save_button.alpha = .5f
        save_button.isClickable = false
    }

    override fun enableSaveButton() {
        save_button.alpha = 1f
        save_button.isClickable = true
    }

    private fun setupSupportActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_back_arrow_white, null))
        supportActionBar!!.title = getString(R.string.automated_ingredient_search_title)
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = IngredientSearchListAdapter(presenter, presenter, presenter)
    }

    override fun setupViewsInRecyclerView(ingredients: ArrayList<IngredientDisplayModel>) {
        val views = ArrayList<IngredientSearchItemWrapper>()

        for (ingredient in ingredients) {
            if (ingredient is ManualIngredientDisplayModel) {
                views.add(IngredientSearchItemWrapper(IngredientSearchItemWrapper.ItemType.LOADING_INGREDIENT, ingredient))
            } else {
                views.add(IngredientSearchItemWrapper(IngredientSearchItemWrapper.ItemType.FOUND_INGREDIENT, ingredient))
            }
        }

        (recycler_view.adapter as IngredientSearchListAdapter).setList(views)
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun startGrocerySearch(index: Int) {
        startActivityForResult(GrocerySearchActivity.newIngredientReplaceIntent(this, GroceryEntity.GroceryEntityType.TYPE_COMBINED, index), Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT)
    }

    override fun startBarcodeScan(index: Int) {
        startActivityForResult(BarcodeScannerActivity.newReplaceIngredientSearchIntent(this, index), Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT)
    }

    override fun showSuccessfulSearchDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.dialog_message_automated_ingredient_search_success))
        builder.setPositiveButton(getString(R.string.dialog_action_ok)) { _, _ -> presenter.onSuccessfulSearchDialogOKClicked() }
        builder.setCancelable(false)
        builder.create().show()
    }

    override fun setupViewsInRecyclerViewForAdaption(ingredients: ArrayList<IngredientDisplayModel>, failReasons: HashMap<Int, Int>) {
        val views = ArrayList<IngredientSearchItemWrapper>()

        for (i in ingredients.indices) {
            if (ingredients[i] is ManualIngredientDisplayModel) {
                views.add(IngredientSearchItemWrapper(IngredientSearchItemWrapper.ItemType.FAILED_INGREDIENT,
                        ingredients[i],
                        failReasons[i]!!))
            } else {
                views.add(IngredientSearchItemWrapper(IngredientSearchItemWrapper.ItemType.FOUND_INGREDIENT, ingredients[i]))
            }
        }

        (recycler_view.adapter as IngredientSearchListAdapter).setList(views)
    }

    override fun showErrorWhileSavingRecipe() {
        Toast.makeText(this, getString(R.string.error_message_unknown_error), Toast.LENGTH_SHORT).show()
    }

    override fun finishActivity() {
        finish()
        setResult(Constants.CLOSE_SELF_RESULT, intent)
    }

    override fun showSaveButton() {
        save_button.visibility = View.VISIBLE
    }

    override fun showUnsuccessfulSearchDiaog() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.dialog_message_automated_ingredient_search_unsuccess))
        builder.setPositiveButton(getString(R.string.dialog_action_ok)) { _, _ -> }
        builder.setCancelable(false)
        builder.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            this.finish()

        return super.onOptionsItemSelected(item)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT -> presenter.replaceIngredient(data.getIntExtra(EXTRA_INDEX, 0),
                        data.getIntExtra(EXTRA_GROCERY_ID, 0),
                        data.getFloatExtra(EXTRA_AMOUNT, 0.0f),
                        data.getIntExtra(EXTRA_UNIT_ID, 0))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.finish()
        TrackerApplication.get(this).releaseAutomatedIngredientSearchComponent()
    }

    companion object {

        const val EXTRA_INDEX = "EXTRA_INDEX"
        const val EXTRA_AMOUNT = "EXTRA_AMOUNT"
        const val EXTRA_UNIT_ID = "EXTRA_UNIT_ID"
        const val EXTRA_GROCERY_ID = "EXTRA_GROCERY_ID"
        const val EXTRA_RECIPE = "EXTRA_RECIPE"

        fun newIntent(context: Context, recipe: RecipeDisplayModel): Intent {
            val intent = Intent(context, AutomatedIngredientSearchActivity::class.java)
            intent.putExtra(EXTRA_RECIPE, recipe)

            return intent
        }
    }

}
