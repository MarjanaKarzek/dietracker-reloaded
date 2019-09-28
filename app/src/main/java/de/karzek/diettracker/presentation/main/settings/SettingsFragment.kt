package de.karzek.diettracker.presentation.main.settings

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.domain.interactor.manager.managerInterface.SharedPreferencesManager
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseFragment
import de.karzek.diettracker.presentation.main.settings.adapter.SettingsMealListAdapter
import de.karzek.diettracker.presentation.main.settings.dialog.editAllergen.EditAllergensDialog
import de.karzek.diettracker.presentation.main.settings.dialog.manipulateMeal.ManipulateMealDialog
import de.karzek.diettracker.presentation.model.AllergenDisplayModel
import de.karzek.diettracker.presentation.model.MealDisplayModel
import de.karzek.diettracker.presentation.util.SharedPreferencesUtil.*
import de.karzek.diettracker.presentation.util.StringUtils
import de.karzek.diettracker.presentation.util.ValidationUtil
import kotlinx.android.synthetic.main.fragment_settings.*
import net.cachapa.expandablelayout.ExpandableLayout
import java.util.*
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 12.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 12.05.2018
 */
class SettingsFragment : BaseFragment(), SettingsContract.View {

    @Inject
    lateinit var presenter: SettingsContract.Presenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addDoneListenerToEditTextViews()
        setupClickListeners()

        presenter.setView(this)
        presenter.start()
    }

    private fun addDoneListenerToEditTextViews() {
        edit_text_amount_calories.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(edit_text_amount_calories)
                if (!StringUtils.isNullOrEmpty(edit_text_amount_calories.text.toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_CALORIES, UPPER_BOUND_CALORIES, Integer.valueOf(edit_text_amount_calories.text.toString()), edit_text_amount_calories, context))
                        presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_CALORIES_DAILY, Integer.valueOf(edit_text_amount_calories.text.toString()))
                } else
                    edit_text_amount_calories.setText(StringUtils.formatInt(LOWER_BOUND_CALORIES))
            }
            true
        }
        edit_text_amount_calories.setOnFocusChangeListener { _, _ ->
            if (!StringUtils.isNullOrEmpty(edit_text_amount_calories.text.toString())) {
                if (ValidationUtil.isValid(LOWER_BOUND_CALORIES, UPPER_BOUND_CALORIES, Integer.valueOf(edit_text_amount_calories.text.toString()), edit_text_amount_calories, context))
                    presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_CALORIES_DAILY, Integer.valueOf(edit_text_amount_calories.text.toString()))
            } else
                edit_text_amount_calories.setText(StringUtils.formatInt(LOWER_BOUND_CALORIES))
        }

        edit_text_amount_proteins.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(edit_text_amount_proteins)
                if (!StringUtils.isNullOrEmpty(edit_text_amount_proteins.text.toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_PROTEINS, UPPER_BOUND_PROTEINS, Integer.valueOf(edit_text_amount_proteins.text.toString()), edit_text_amount_proteins, context))
                        presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_PROTEINS_DAILY, Integer.valueOf(edit_text_amount_proteins.text.toString()))
                } else
                    edit_text_amount_proteins.setText(StringUtils.formatInt(LOWER_BOUND_PROTEINS))
            }
            true
        }
        edit_text_amount_proteins.setOnFocusChangeListener { _, _ ->
            if (!StringUtils.isNullOrEmpty(edit_text_amount_proteins.text.toString())) {
                if (ValidationUtil.isValid(LOWER_BOUND_PROTEINS, UPPER_BOUND_PROTEINS, Integer.valueOf(edit_text_amount_proteins.text.toString()), edit_text_amount_proteins, context))
                    presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_PROTEINS_DAILY, Integer.valueOf(edit_text_amount_proteins.text.toString()))
            } else
                edit_text_amount_proteins.setText(StringUtils.formatInt(LOWER_BOUND_PROTEINS))
        }

        edit_text_amount_carbs.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(edit_text_amount_carbs)
                if (!StringUtils.isNullOrEmpty(edit_text_amount_carbs.text.toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_CARBS, UPPER_BOUND_CARBS, Integer.valueOf(edit_text_amount_carbs.text.toString()), edit_text_amount_carbs, context))
                        presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_CARBS_DAILY, Integer.valueOf(edit_text_amount_carbs.text.toString()))
                } else
                    edit_text_amount_carbs.setText(StringUtils.formatInt(LOWER_BOUND_CARBS))
            }
            true
        }
        edit_text_amount_carbs.setOnFocusChangeListener { _, _ ->
            if (!StringUtils.isNullOrEmpty(edit_text_amount_carbs.text.toString())) {
                if (ValidationUtil.isValid(LOWER_BOUND_CARBS, UPPER_BOUND_CARBS, Integer.valueOf(edit_text_amount_carbs.text.toString()), edit_text_amount_carbs, context))
                    presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_CARBS_DAILY, Integer.valueOf(edit_text_amount_carbs.text.toString()))
            } else
                edit_text_amount_carbs.setText(StringUtils.formatInt(LOWER_BOUND_CARBS))
        }

        edit_text_amount_fats.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(edit_text_amount_fats)
                if (!StringUtils.isNullOrEmpty(edit_text_amount_fats.text.toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_FATS, UPPER_BOUND_FATS, Integer.valueOf(edit_text_amount_fats.text.toString()), edit_text_amount_fats, context))
                        presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_FATS_DAILY, Integer.valueOf(edit_text_amount_fats.text.toString()))
                } else
                    edit_text_amount_fats.setText(StringUtils.formatInt(LOWER_BOUND_FATS))
            }
            true
        }
        edit_text_amount_fats.setOnFocusChangeListener { _, _ ->
            if (!StringUtils.isNullOrEmpty(edit_text_amount_fats.text.toString())) {
                if (ValidationUtil.isValid(LOWER_BOUND_FATS, UPPER_BOUND_FATS, Integer.valueOf(edit_text_amount_fats.text.toString()), edit_text_amount_fats, context))
                    presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_FATS_DAILY, Integer.valueOf(edit_text_amount_fats.text.toString()))
            } else
                edit_text_amount_fats.setText(StringUtils.formatInt(LOWER_BOUND_FATS))
        }

        edit_text_amount_liquids.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(edit_text_amount_liquids)
                if (!StringUtils.isNullOrEmpty(edit_text_amount_liquids.text.toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_LIQUIDS, UPPER_BOUND_LIQUIDS, Integer.valueOf(edit_text_amount_liquids.text.toString()), edit_text_amount_liquids, context))
                        presenter.updateSharedPreferenceFloatValue(KEY_REQUIREMENT_LIQUID_DAILY, java.lang.Float.valueOf(edit_text_amount_liquids.text.toString()))
                } else
                    edit_text_amount_liquids.setText(StringUtils.formatFloat(LOWER_BOUND_LIQUIDS.toFloat()))
            }
            true
        }
        edit_text_amount_liquids.setOnFocusChangeListener { _, _ ->
            if (!StringUtils.isNullOrEmpty(edit_text_amount_liquids.text.toString())) {
                if (ValidationUtil.isValid(LOWER_BOUND_LIQUIDS, UPPER_BOUND_LIQUIDS, Integer.valueOf(edit_text_amount_liquids.text.toString()), edit_text_amount_liquids, context))
                    presenter.updateSharedPreferenceFloatValue(KEY_REQUIREMENT_LIQUID_DAILY, java.lang.Float.valueOf(edit_text_amount_liquids.text.toString()))
            } else
                edit_text_amount_liquids.setText(StringUtils.formatFloat(LOWER_BOUND_LIQUIDS.toFloat()))
        }

        edit_text_volume_bottle.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(edit_text_volume_bottle)
                if (!StringUtils.isNullOrEmpty(edit_text_volume_bottle.text.toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_BOTTLE, UPPER_BOUND_BOTTLE, Integer.valueOf(edit_text_volume_bottle.text.toString()), edit_text_volume_bottle, context))
                        presenter.updateSharedPreferenceFloatValue(KEY_BOTTLE_VOLUME, java.lang.Float.valueOf(edit_text_volume_bottle.text.toString()))
                } else
                    edit_text_volume_bottle.setText(StringUtils.formatInt(LOWER_BOUND_BOTTLE))
            }
            true
        }
        edit_text_volume_bottle.setOnFocusChangeListener { _, _ ->
            if (!StringUtils.isNullOrEmpty(edit_text_volume_bottle.text.toString())) {
                if (ValidationUtil.isValid(LOWER_BOUND_BOTTLE, UPPER_BOUND_BOTTLE, Integer.valueOf(edit_text_volume_bottle.text.toString()), edit_text_volume_bottle, context))
                    presenter.updateSharedPreferenceFloatValue(KEY_BOTTLE_VOLUME, java.lang.Float.valueOf(edit_text_volume_bottle.text.toString()))
            } else
                edit_text_volume_bottle.setText(StringUtils.formatInt(LOWER_BOUND_BOTTLE))
        }

        edit_text_volume_glasses.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocusOfView(edit_text_volume_glasses)
                if (!StringUtils.isNullOrEmpty(edit_text_volume_glasses.text.toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_GLASS, UPPER_BOUND_GLASS, Integer.valueOf(edit_text_volume_glasses.text.toString()), edit_text_volume_glasses, context))
                        presenter.updateSharedPreferenceFloatValue(KEY_GLASS_VOLUME, java.lang.Float.valueOf(edit_text_volume_glasses.text.toString()))
                } else
                    edit_text_volume_glasses.setText(StringUtils.formatInt(LOWER_BOUND_GLASS))
            }
            true
        }
        edit_text_volume_glasses.setOnFocusChangeListener { _, _ ->
            if (!StringUtils.isNullOrEmpty(edit_text_volume_glasses.text.toString())) {
                if (ValidationUtil.isValid(LOWER_BOUND_GLASS, UPPER_BOUND_GLASS, Integer.valueOf(edit_text_volume_glasses.text.toString()), edit_text_volume_glasses, context))
                    presenter.updateSharedPreferenceFloatValue(KEY_GLASS_VOLUME, java.lang.Float.valueOf(edit_text_volume_glasses.text.toString()))
            } else
                edit_text_volume_glasses.setText(StringUtils.formatInt(LOWER_BOUND_GLASS))
        }

    }

    override fun clearFocusOfView(view: EditText) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    override fun setupMealRecyclerView(meals: ArrayList<MealDisplayModel>) {
        val adapter = SettingsMealListAdapter(presenter, presenter)
        adapter.setList(meals)
        settings_meal_recyclerview.adapter = adapter
        settings_meal_recyclerview.layoutManager = LinearLayoutManager(context)
    }

    override fun setupAllergenTextView(allergens: ArrayList<AllergenDisplayModel>) {
        var allergenDescription = ""
        for (i in allergens.indices) {
            allergenDescription += allergens[i].name
            if (i < allergens.size - 1)
                allergenDescription += ", "
        }

        if (allergens.size == 0)
            allergenDescription = getString(R.string.no_allergens_set_placeholder)

        text_allergies.text = allergenDescription
    }

    override fun setupFragmentComponent() {
        TrackerApplication.get(context!!).createSettingsComponent().inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        TrackerApplication.get(context!!).releaseSettingsComponent()
    }

    override fun fillSettingsOptions(sharedPreferencesManager: SharedPreferencesManager) {
        edit_text_amount_calories.setText(StringUtils.formatInt(sharedPreferencesManager.getInt(KEY_REQUIREMENT_CALORIES_DAILY, VALUE_REQUIREMENT_CALORIES_DAILY)))
        edit_text_amount_proteins.setText(StringUtils.formatInt(sharedPreferencesManager.getInt(KEY_REQUIREMENT_PROTEINS_DAILY, VALUE_REQUIREMENT_PROTEINS_DAILY)))
        edit_text_amount_carbs.setText(StringUtils.formatInt(sharedPreferencesManager.getInt(KEY_REQUIREMENT_CARBS_DAILY, VALUE_REQUIREMENT_CARBS_DAILY)))
        edit_text_amount_fats.setText(StringUtils.formatInt(sharedPreferencesManager.getInt(KEY_REQUIREMENT_FATS_DAILY, VALUE_REQUIREMENT_FATS_DAILY)))

        edit_text_amount_liquids.setText(StringUtils.formatFloat(sharedPreferencesManager.getFloat(KEY_REQUIREMENT_LIQUID_DAILY, VALUE_REQUIREMENT_LIQUID_DAILY)))
        edit_text_volume_bottle.setText(StringUtils.formatFloat(sharedPreferencesManager.getFloat(KEY_BOTTLE_VOLUME, VALUE_BOTTLE_VOLUME)))
        edit_text_volume_glasses.setText(StringUtils.formatFloat(sharedPreferencesManager.getFloat(KEY_GLASS_VOLUME, VALUE_GLASS_VOLUME)))

        settings_data_macros.isChecked = sharedPreferencesManager.nutritionDetailsSetting == VALUE_SETTING_NUTRITION_DETAILS_CALORIES_AND_MACROS

        settings_start_screen_recipes.isChecked = sharedPreferencesManager.getBoolean(KEY_START_SCREEN_RECIPE, VALUE_TRUE)
        settings_start_screen_liquids.isChecked = sharedPreferencesManager.getBoolean(KEY_START_SCREEN_LIQUIDS, VALUE_TRUE)
    }

    override fun showEditMealDialog(mealDisplayModel: MealDisplayModel) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val previous = fragmentManager!!.findFragmentByTag("dialog")
        if (previous != null)
            fragmentTransaction.remove(previous)
        fragmentTransaction.addToBackStack(null)

        val dialogFragment = ManipulateMealDialog()
        val bundle = Bundle()
        bundle.putInt("mealId", mealDisplayModel.id)
        bundle.putString("mealTitle", mealDisplayModel.name)
        bundle.putString("startTime", mealDisplayModel.startTime)
        bundle.putString("endTime", mealDisplayModel.endTime)
        dialogFragment.arguments = bundle
        dialogFragment.show(fragmentTransaction, "dialog")
    }

    override fun showEditAllergenDialog() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val previous = fragmentManager!!.findFragmentByTag("dialog")
        if (previous != null)
            fragmentTransaction.remove(previous)
        fragmentTransaction.addToBackStack(null)

        val dialogFragment = EditAllergensDialog()
        dialogFragment.show(fragmentTransaction, "dialog")
    }

    override fun updateRecyclerView() {
        settings_meal_recyclerview.adapter?.notifyDataSetChanged()
    }

    override fun setupCheckboxListeners() {
        settings_data_macros.setOnCheckedChangeListener { _, checked -> presenter.setNutritionDetailsSetting(checked) }

        settings_start_screen_recipes.setOnCheckedChangeListener { _, checked -> presenter.setStartScreenRecipesSetting(checked) }

        settings_start_screen_liquids.setOnCheckedChangeListener { _, checked -> presenter.setStartScreenLiquidsSetting(checked) }
    }

    override fun showOnboardingScreen(onboardingTag: Int) {
        //startActivity(OnboardingActivity.newIntent(getContext(), onboardingTag));
    }

    override fun showDeleteMealConfirmDialog(id: Int) {
        val builder = AlertDialog.Builder(context!!)

        builder.setMessage(getString(R.string.dialog_message_confirm_meal_deletion))
        builder.setPositiveButton(getString(R.string.dialog_action_delete)) { _, _ -> presenter.onMealItemDeleteConfirmed(id) }
        builder.setNegativeButton(getString(R.string.dialog_action_dismiss), DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
        builder.create().show()
    }

    private fun setupClickListeners() {
        expandable_diet_action.setOnClickListener {
            if (expandable_diet_details.isExpanded) {
                collapseExpandableLayout(expandable_diet_details, expandable_diet_action)
            } else {
                expandExpandableLayout(expandable_diet_details, expandable_diet_action)
            }
        }
        expandable_liquids_action.setOnClickListener {
            if (expandable_liquid_details.isExpanded) {
                collapseExpandableLayout(expandable_liquid_details, expandable_liquids_action)
            } else {
                expandExpandableLayout(expandable_liquid_details, expandable_liquids_action)
            }
        }

        expandable_meals_action.setOnClickListener {
            if (expandable_meal_details.isExpanded) {
                collapseExpandableLayout(expandable_meal_details, expandable_meals_action)
            } else {
                expandExpandableLayout(expandable_meal_details, expandable_meals_action)
            }
        }
        expandable_allergies_action.setOnClickListener {
            if (expandable_allergies_details.isExpanded) {
                collapseExpandableLayout(expandable_allergies_details, expandable_allergies_action)
            } else {
                expandExpandableLayout(expandable_allergies_details, expandable_allergies_action)
            }
        }
        expandable_data_action.setOnClickListener {
            if (expandable_data_details.isExpanded) {
                collapseExpandableLayout(expandable_data_details, expandable_data_action)
            } else {
                expandExpandableLayout(expandable_data_details, expandable_data_action)
            }
        }
        expandable_start_screen_action.setOnClickListener {
            if (expandable_start_screen_details.isExpanded) {
                collapseExpandableLayout(expandable_start_screen_details, expandable_start_screen_action)
            } else {
                expandExpandableLayout(expandable_start_screen_details, expandable_start_screen_action)
            }
        }
        add_meal.setOnClickListener {
            if (settings_meal_recyclerview.adapter?.itemCount!! >= 10)
                Toast.makeText(context, R.string.error_message_only_ten_supported_meals, Toast.LENGTH_LONG).show()
            else {
                val fragmentTransaction = childFragmentManager.beginTransaction()
                val previous = fragmentManager!!.findFragmentByTag("dialog")
                if (previous != null)
                    fragmentTransaction.remove(previous)
                fragmentTransaction.addToBackStack(null)

                val dialogFragment = ManipulateMealDialog()
                dialogFragment.show(fragmentTransaction, "dialog")
            }
        }
        text_allergies.setOnClickListener {
            presenter.onEditAllergensClicked()
        }
    }

    private fun collapseExpandableLayout(layout: ExpandableLayout, action: ImageButton) {
        action.setImageDrawable(context!!.getDrawable(R.drawable.ic_expand_more_primary_text))
        layout.collapse(true)
    }

    private fun expandExpandableLayout(layout: ExpandableLayout, action: ImageButton) {
        action.setImageDrawable(context!!.getDrawable(R.drawable.ic_expand_less_primary_text))
        layout.expand(true)
    }

    override fun updateAllergens() {
        presenter.updateAllergens()
    }

    override fun addMealInDialogClicked(name: String, startTime: String, endTime: String) {
        presenter.onAddMealInDialogClicked(name, startTime, endTime)
    }

    override fun saveMealInDialogClicked(id: Int, name: String, startTime: String, endTime: String) {
        presenter.onSaveMealInDialogClicked(id, name, startTime, endTime)
    }

    companion object {

        private const val LOWER_BOUND_CALORIES = 0
        private const val UPPER_BOUND_CALORIES = 5000
        private const val LOWER_BOUND_PROTEINS = 0
        private const val UPPER_BOUND_PROTEINS = 500
        private const val LOWER_BOUND_CARBS = 0
        private const val UPPER_BOUND_CARBS = 500
        private const val LOWER_BOUND_FATS = 0
        private const val UPPER_BOUND_FATS = 500
        private const val LOWER_BOUND_LIQUIDS = 0
        private const val UPPER_BOUND_LIQUIDS = 10000
        private const val LOWER_BOUND_BOTTLE = 0
        private const val UPPER_BOUND_BOTTLE = 5000
        private const val LOWER_BOUND_GLASS = 0
        private const val UPPER_BOUND_GLASS = 1000
    }
}
