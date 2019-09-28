package de.karzek.diettracker.presentation.search.grocery.barcodeScanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.zxing.Result
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseActivity
import de.karzek.diettracker.presentation.search.grocery.groceryDetail.GroceryDetailsActivity
import de.karzek.diettracker.presentation.util.Constants
import kotlinx.android.synthetic.main.activity_barcode_scanner.*
import kotlinx.android.synthetic.main.snippet_loading_view.*
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 08.06.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 08.06.2018
 */
class BarcodeScannerActivity : BaseActivity(), BarcodeScannerContract.View {

    @Inject
    lateinit var presenter: BarcodeScannerContract.Presenter

    private var selectedDate: String? = null
    private var selectedMeal: Int = 0
    private var mode: Int = 0
    private var ingredientIndex: Int = 0

    override fun setupActivityComponents() {
        TrackerApplication.get(this).createBarcodeScannerComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)

        mode = intent.extras!!.getInt(EXTRA_MODE)
        when (mode) {
            BarcodeScannerContract.View.SearchMode.MODE_GROCERY_SEARCH -> {
                selectedDate = intent.extras!!.getString(EXTRA_SELECTED_DATE)
                selectedMeal = intent.extras!!.getInt(EXTRA_SELECTED_MEAL)
            }
            BarcodeScannerContract.View.SearchMode.MODE_INGREDIENT_SEARCH -> {
            }
            BarcodeScannerContract.View.SearchMode.MODE_REPLACE_INGREDIENT_SEARCH -> ingredientIndex = intent.extras!!.getInt(EXTRA_INGREDIENT_INDEX)
        }

        barcode_scanner_view.setResultHandler(this)
        barcode_scanner_view.startCamera()

        presenter.setView(this)
        presenter.start()
    }

    override fun handleResult(result: Result) {
        presenter.checkResult(result)
    }

    override fun startDetailsActivity(id: Int) {
        when (mode) {
            BarcodeScannerContract.View.SearchMode.MODE_GROCERY_SEARCH -> startActivity(GroceryDetailsActivity.newGrocerySearchIntent(this, id, selectedDate!!, selectedMeal))
            BarcodeScannerContract.View.SearchMode.MODE_INGREDIENT_SEARCH -> startActivityForResult(GroceryDetailsActivity.newIngredientSearchIntent(this, id), Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT)
            BarcodeScannerContract.View.SearchMode.MODE_REPLACE_INGREDIENT_SEARCH -> startActivityForResult(GroceryDetailsActivity.newReplaceIngredientSearchIntent(this, id, ingredientIndex), Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT)
        }
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun showNoResultsDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.barcode_search_no_result))
        builder.setPositiveButton(getString(R.string.barcode_search_no_result_accept)) { _, _ -> finish() }
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        barcode_scanner_view.stopCamera()
        presenter.finish()
        TrackerApplication.get(this).releaseBarcodeScannerComponent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Constants.ADD_REPLACE_INGREDIENT_INTENT_RESULT) {
            val intent = Intent()
            intent.putExtra(EXTRA_INGREDIENT_INDEX, data!!.getIntExtra(EXTRA_INGREDIENT_INDEX, Constants.INVALID_ENTITY_ID))
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
        const val EXTRA_INGREDIENT_INDEX = "EXTRA_INGREDIENT_ID"
        const val EXTRA_AMOUNT = "EXTRA_AMOUNT"
        const val EXTRA_GROCERY_ID = "EXTRA_GROCERY_ID"
        const val EXTRA_UNIT_ID = "EXTRA_UNIT_ID"

        fun newGrocerySearchIntent(context: Context, selectedDate: String, selectedMeal: Int): Intent {
            val intent = Intent(context, BarcodeScannerActivity::class.java)

            intent.putExtra(EXTRA_MODE, BarcodeScannerContract.View.SearchMode.MODE_GROCERY_SEARCH)
            intent.putExtra(EXTRA_SELECTED_DATE, selectedDate)
            intent.putExtra(EXTRA_SELECTED_MEAL, selectedMeal)

            return intent
        }

        fun newIngredientSearchIntent(context: Context): Intent {
            val intent = Intent(context, BarcodeScannerActivity::class.java)
            intent.putExtra(EXTRA_MODE, BarcodeScannerContract.View.SearchMode.MODE_INGREDIENT_SEARCH)
            return intent
        }

        fun newReplaceIngredientSearchIntent(context: Context, ingredientIndex: Int): Intent {
            val intent = Intent(context, BarcodeScannerActivity::class.java)
            intent.putExtra(EXTRA_MODE, BarcodeScannerContract.View.SearchMode.MODE_REPLACE_INGREDIENT_SEARCH)
            intent.putExtra(EXTRA_INGREDIENT_INDEX, ingredientIndex)
            return intent
        }
    }
}
