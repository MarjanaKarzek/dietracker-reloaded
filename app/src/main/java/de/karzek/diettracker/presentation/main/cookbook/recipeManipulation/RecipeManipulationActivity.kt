package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.data.cache.model.GroceryEntity
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseActivity
import de.karzek.diettracker.presentation.main.MainActivity
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.RecipeManipulationViewListAdapter
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.adapter.itemWrapper.RecipeManipulationViewItemWrapper
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.automatedIngredientSearch.AutomatedIngredientSearchActivity
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog.AddIngredientDialog
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog.AddPreparationStepDialog
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog.bottomSheet.ImageSelectorBottomSheetDialogFragment
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog.editMeals.EditMealsDialog
import de.karzek.diettracker.presentation.model.*
import de.karzek.diettracker.presentation.search.grocery.GrocerySearchActivity
import de.karzek.diettracker.presentation.search.grocery.barcodeScanner.BarcodeScannerActivity
import de.karzek.diettracker.presentation.search.grocery.groceryDetail.GroceryDetailsActivity
import de.karzek.diettracker.presentation.util.Constants.*
import kotlinx.android.synthetic.main.activity_recipe_manipulation.*
import kotlinx.android.synthetic.main.snippet_loading_view.*
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 16.06.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 16.06.2018
 */
class RecipeManipulationActivity : BaseActivity(), RecipeManipulationContract.View {

    @Inject
    lateinit var presenter: RecipeManipulationContract.Presenter

    private var mode: Int = 0

    private var units: ArrayList<UnitDisplayModel>? = null

    override fun setupActivityComponents() {
        TrackerApplication.get(this).createRecipeManipulationComponent().inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_recipe_man, menu)

        return true
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_manipulation)

        presenter.setView(this)
        setupSupportActionBar()
        setupRecyclerView()
        setupTitleSetListener()

        mode = intent.extras!!.getInt(EXTRA_MODE)
        if (mode == RecipeManipulationContract.View.RecipeManipulationMode.MODE_ADD_RECIPE) {
            presenter.start()
        } else if (mode == RecipeManipulationContract.View.RecipeManipulationMode.MODE_EDIT_RECIPE) {
            presenter.startEditMode(intent.extras!!.getInt(EXTRA_RECIPE_ID))
        }
    }

    private fun setupTitleSetListener() {
        recipe_title.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(recipe_title.windowToken, 0)
                recipe_title.clearFocus()
                presenter.updateTitle(recipe_title.text.toString())
            }
            true
        }
        recipe_title.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> presenter.updateTitle(recipe_title.text.toString()) }
    }

    private fun setupRecyclerView() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = RecipeManipulationViewListAdapter(presenter, presenter, presenter, presenter, presenter, presenter, presenter, presenter, presenter, presenter, presenter, presenter, presenter, presenter, presenter)
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
        else if (item.itemId == R.id.recipe_manipulation_camera) {
            presenter.onCameraIconClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, GET_IMAGE_FROM_CAMERA_RESULT)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, GET_IMAGE_FROM_CAMERA_RESULT)
            } else
                Toast.makeText(this, getString(R.string.permission_grand_camera_for_recipe), Toast.LENGTH_SHORT).show()
        }
    }

    override fun openBottomSheet() {
        val imageSelector = ImageSelectorBottomSheetDialogFragment.newInstance()
        imageSelector.show(supportFragmentManager,
                TAG_IMAGE_SELECTOR)
    }

    override fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GET_IMAGE_FROM_GALLERY_RESULT)
    }

    override fun setupViewsInRecyclerView(displayModel: RecipeDisplayModel) {
        val views = ArrayList<RecipeManipulationViewItemWrapper>()
        if (displayModel.photo != null) {
            views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.PHOTO_VIEW, BitmapFactory.decodeByteArray(displayModel.photo, 0, displayModel.photo.size)))
        }
        views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.INGREDIENTS_TITLE_AND_PORTIONS_VIEW, displayModel.portions))

        for (ingredient in displayModel.ingredients) {
            if (ingredient is ManualIngredientDisplayModel)
                views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.MANUAL_INGREDIENT_ITEM, ingredient))
            else
                views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.INGREDIENT_ITEM, ingredient))
        }
        views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.INGREDIENT_ITEM_ADD_VIEW))

        views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.PREPARATION_STEPS_TITLE_VIEW))
        for (step in displayModel.steps)
            views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.PREPARATION_STEP_ITEM, step))
        views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.PREPARATION_STEP_ITEM_ADD_VIEW))

        views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.MEALS_TITLE_VIEW))
        views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.MEAL_LIST, displayModel.meals))

        views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.RECIPE_SAVE_VIEW))
        if (mode == RecipeManipulationContract.View.RecipeManipulationMode.MODE_EDIT_RECIPE)
            views.add(RecipeManipulationViewItemWrapper(RecipeManipulationViewItemWrapper.ItemType.RECIPE_DELETE_VIEW))

        (recycler_view.adapter as RecipeManipulationViewListAdapter).setList(views)
    }

    override fun startBarcodeScan() {
        startActivityForResult(BarcodeScannerActivity.newIngredientSearchIntent(this), ADD_REPLACE_INGREDIENT_INTENT_RESULT)
    }

    override fun startGrocerySearch() {
        startActivityForResult(GrocerySearchActivity.newIngredientSearchIntent(this, GroceryEntity.GroceryEntityType.TYPE_COMBINED), ADD_REPLACE_INGREDIENT_INTENT_RESULT)
    }

    override fun openAddManualIngredientDialog(units: ArrayList<UnitDisplayModel>) {
        this.units = units

        val unitStrings = ArrayList<String>()
        for (unit in units)
            unitStrings.add(unit.name)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag(TAG_DIALOG)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.addToBackStack(null)

        val dialogFragment = AddIngredientDialog()
        val bundle = Bundle()
        bundle.putInt(EXTRA_MANUAL_INGREDIENT_ID, INVALID_ENTITY_ID)
        bundle.putStringArrayList(EXTRA_UNITS, unitStrings)
        dialogFragment.arguments = bundle
        dialogFragment.show(fragmentTransaction, TAG_DIALOG)
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun showAddPreparationStepDialog() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag(TAG_DIALOG)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.addToBackStack(null)

        val dialogFragment = AddPreparationStepDialog()
        dialogFragment.show(fragmentTransaction, TAG_DIALOG)
    }

    override fun openEditMealsDialog(selectedMeals: ArrayList<MealDisplayModel>) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag(TAG_DIALOG)
        if (previous != null)
            fragmentTransaction.remove(previous)
        fragmentTransaction.addToBackStack(null)

        val dialogFragment = EditMealsDialog()
        val bundle = Bundle()
        bundle.putParcelableArrayList(EXTRA_SELECTED_MEALS, selectedMeals)
        dialogFragment.arguments = bundle
        dialogFragment.show(fragmentTransaction, TAG_DIALOG)
    }

    override fun openEditManualIngredient(id: Int, displayModel: ManualIngredientDisplayModel, units: ArrayList<UnitDisplayModel>) {
        this.units = units

        val unitStrings = ArrayList<String>()
        for (unit in units)
            unitStrings.add(unit.name)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag(TAG_DIALOG)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.addToBackStack(null)

        val dialogFragment = AddIngredientDialog()
        val bundle = Bundle()
        bundle.putInt(EXTRA_MANUAL_INGREDIENT_ID, id)
        bundle.putString(EXTRA_GROCERY_QUERY, displayModel.groceryQuery)
        bundle.putFloat(EXTRA_AMOUNT, displayModel.amount)
        bundle.putInt(EXTRA_UNIT_ID, displayModel.unit.id)
        bundle.putStringArrayList(EXTRA_UNITS, unitStrings)
        dialogFragment.arguments = bundle
        dialogFragment.show(fragmentTransaction, TAG_DIALOG)
    }

    override fun openEditIngredient(index: Int, displayModel: IngredientDisplayModel) {
        startActivityForResult(GroceryDetailsActivity.newEditIngredientIntent(this, index, displayModel), EDIT_INGREDIENT_INTENT_RESULT)
    }

    override fun showMissingTitleError() {
        recipe_title.error = getString(R.string.error_message_missing_recipe_title)
    }

    override fun showMissingIngredientsError() {
        Toast.makeText(this, getString(R.string.error_message_missing_ingredient), Toast.LENGTH_SHORT).show()
    }

    override fun finishActivity() {
        this.finish()
    }

    override fun setRecipeTitle(title: String) {
        recipe_title.setText(title)
    }

    override fun showConfirmDeletionDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.dialog_message_confirm_recipe_deletion))
        builder.setPositiveButton(getString(R.string.dialog_action_delete)) { _, _ -> presenter.onDeleteRecipeConfirmed() }
        builder.setNegativeButton(getString(R.string.dialog_action_dismiss), DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
        builder.create().show()
    }

    override fun navigateToCookbook() {
        startActivity(MainActivity.newIntentToCookbook(this))
        finish()
    }

    override fun showOnboardingScreen(onboardingTag: Int) {
        //startActivity(OnboardingActivity.newIntent(this, onboardingTag));
    }

    override fun navigateToAutomatedIngredientSearch(recipe: RecipeDisplayModel) {
        startActivityForResult(AutomatedIngredientSearchActivity.newIntent(this, recipe), CLOSE_SELF_RESULT)
    }

    override fun getRecipeTitle(): String {
        return recipe_title.text.toString()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GET_IMAGE_FROM_GALLERY_RESULT -> if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    presenter.addPhotoToRecipe(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            GET_IMAGE_FROM_CAMERA_RESULT -> if (data != null) {
                val bitmap = data.extras!!.get(EXTRA_DATA) as Bitmap?
                presenter.addPhotoToRecipe(bitmap)
            }
            ADD_REPLACE_INGREDIENT_INTENT_RESULT -> if (data != null) {
                presenter.addIngredient(data.getIntExtra(EXTRA_GROCERY_ID, 0),
                        data.getFloatExtra(EXTRA_AMOUNT, 0.0f),
                        data.getIntExtra(EXTRA_UNIT_ID, 0))
            }
            EDIT_INGREDIENT_INTENT_RESULT -> if (data != null) {
                presenter.editIngredient(
                        data.getIntExtra(EXTRA_INGREDIENT_ID, 0),
                        data.getFloatExtra(EXTRA_AMOUNT, 0.0f))
            }
            CLOSE_SELF_RESULT -> finish()
        }
    }

    override fun onAddManualIngredientClicked(amount: Float, selectedUnitId: Int, groceryQuery: String) {
        presenter.addManualIngredient(ManualIngredientDisplayModel(INVALID_ENTITY_ID, null, amount, units!![selectedUnitId], groceryQuery))
    }

    override fun onAddPreparationStepClicked(description: String) {
        presenter.addPreparationStep(description)
    }

    override fun updateMeals(selectedMeals: ArrayList<MealDisplayModel>) {
        presenter.updateMeals(selectedMeals)
    }

    override fun onSaveManualIngredientClicked(id: Int, amount: Float, selectedUnitId: Int, groceryQuery: String) {
        presenter.editManualIngredient(id, amount, units!![selectedUnitId], groceryQuery)
    }

    override fun onOpenCameraClickedInBottomSheet() {
        presenter.onOpenCameraClicked()
    }

    override fun onOpenGalleryClickedInBottomSheet() {
        presenter.onOpenGalleryClicked()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.finish()
        TrackerApplication.get(this).releaseRecipeManipulationComponent()
    }

    companion object {

        const val EXTRA_MODE = "EXTRA_MODE"
        const val EXTRA_RECIPE_ID = "EXTRA_INGREDIENT_ID"
        const val EXTRA_INGREDIENT_ID = "EXTRA_INGREDIENT_ID"
        const val EXTRA_AMOUNT = "EXTRA_AMOUNT"
        const val EXTRA_UNIT_ID = "EXTRA_UNIT_ID"
        const val EXTRA_GROCERY_ID = "EXTRA_GROCERY_ID"
        const val EXTRA_DATA = "data"
        const val EXTRA_MANUAL_INGREDIENT_ID = "EXTRA_MANUAL_INGREDIENT_ID"
        const val EXTRA_UNITS = "EXTRA_UNITS"
        const val EXTRA_GROCERY_QUERY = "EXTRA_GROCERY_QUERY"
        const val EXTRA_SELECTED_MEALS = "EXTRA_SELECTED_MEALS"

        const val TAG_DIALOG = "TAG_DIALOG"
        const val TAG_IMAGE_SELECTOR = "TAG_IMAGE_SELECTOR"

        fun newAddIntent(context: Context): Intent {
            val intent = Intent(context, RecipeManipulationActivity::class.java)
            intent.putExtra(EXTRA_MODE, RecipeManipulationContract.View.RecipeManipulationMode.MODE_ADD_RECIPE)

            return intent
        }

        fun newEditIntent(context: Context, recipeId: Int): Intent {
            val intent = Intent(context, RecipeManipulationActivity::class.java)

            intent.putExtra(EXTRA_MODE, RecipeManipulationContract.View.RecipeManipulationMode.MODE_EDIT_RECIPE)
            intent.putExtra(EXTRA_RECIPE_ID, recipeId)

            return intent
        }
    }
}
