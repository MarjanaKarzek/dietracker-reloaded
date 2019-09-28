package de.karzek.diettracker.presentation.main.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.karzek.diettracker.R;
import de.karzek.diettracker.domain.interactor.manager.managerInterface.SharedPreferencesManager;
import de.karzek.diettracker.presentation.TrackerApplication;
import de.karzek.diettracker.presentation.common.BaseFragment;
import de.karzek.diettracker.presentation.main.settings.adapter.SettingsMealListAdapter;
import de.karzek.diettracker.presentation.main.settings.dialog.manipulateMeal.ManipulateMealDialog;
import de.karzek.diettracker.presentation.main.settings.dialog.editAllergen.EditAllergensDialog;
import de.karzek.diettracker.presentation.model.AllergenDisplayModel;
import de.karzek.diettracker.presentation.model.MealDisplayModel;
import de.karzek.diettracker.presentation.onboarding.OnboardingActivity;
import de.karzek.diettracker.presentation.util.SharedPreferencesUtil;
import de.karzek.diettracker.presentation.util.StringUtils;
import de.karzek.diettracker.presentation.util.ValidationUtil;

import static de.karzek.diettracker.presentation.util.SharedPreferencesUtil.KEY_BOTTLE_VOLUME;
import static de.karzek.diettracker.presentation.util.SharedPreferencesUtil.KEY_GLASS_VOLUME;
import static de.karzek.diettracker.presentation.util.SharedPreferencesUtil.KEY_REQUIREMENT_CALORIES_DAILY;
import static de.karzek.diettracker.presentation.util.SharedPreferencesUtil.KEY_REQUIREMENT_CARBS_DAILY;
import static de.karzek.diettracker.presentation.util.SharedPreferencesUtil.KEY_REQUIREMENT_FATS_DAILY;
import static de.karzek.diettracker.presentation.util.SharedPreferencesUtil.KEY_REQUIREMENT_LIQUID_DAILY;
import static de.karzek.diettracker.presentation.util.SharedPreferencesUtil.KEY_REQUIREMENT_PROTEINS_DAILY;

/**
 * Created by MarjanaKarzek on 12.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 12.05.2018
 */
public class SettingsFragment extends BaseFragment implements SettingsContract.View {

    private static final int LOWER_BOUND_CALORIES = 0;
    private static final int UPPER_BOUND_CALORIES = 5000;
    private static final int LOWER_BOUND_PROTEINS = 0;
    private static final int UPPER_BOUND_PROTEINS = 500;
    private static final int LOWER_BOUND_CARBS = 0;
    private static final int UPPER_BOUND_CARBS = 500;
    private static final int LOWER_BOUND_FATS = 0;
    private static final int UPPER_BOUND_FATS = 500;
    private static final int LOWER_BOUND_LIQUIDS = 0;
    private static final int UPPER_BOUND_LIQUIDS = 10000;
    private static final int LOWER_BOUND_BOTTLE = 0;
    private static final int UPPER_BOUND_BOTTLE = 5000;
    private static final int LOWER_BOUND_GLASS = 0;
    private static final int UPPER_BOUND_GLASS = 1000;

    @Inject
    SettingsContract.Presenter presenter;

    @BindView(R.id.expandable_diet_action)
    ImageButton expandableDietLayoutAction;
    @BindView(R.id.expandable_diet_details)
    ExpandableLayout expandableDietLayout;

    @BindView(R.id.edit_text_amount_calories)
    EditText amountCalories;
    @BindView(R.id.edit_text_amount_proteins)
    EditText amountProteins;
    @BindView(R.id.edit_text_amount_carbs)
    EditText amountCarbs;
    @BindView(R.id.edit_text_amount_fats)
    EditText amountFats;

    @BindView(R.id.expandable_liquids_action)
    ImageButton expandableLiquidLayoutAction;
    @BindView(R.id.expandable_liquid_details)
    ExpandableLayout expandableLiquidLayout;

    @BindView(R.id.edit_text_amount_liquids)
    EditText amountLiquids;
    @BindView(R.id.edit_text_volume_bottle)
    EditText volumeBottle;
    @BindView(R.id.edit_text_volume_glasses)
    EditText volumeGlass;

    @BindView(R.id.expandable_meals_action)
    ImageButton expandableMealsLayoutAction;
    @BindView(R.id.expandable_meal_details)
    ExpandableLayout expandableMealsLayout;

    @BindView(R.id.settings_meal_recyclerview)
    RecyclerView recyclerViewMeals;

    @BindView(R.id.expandable_allergies_action)
    ImageButton expandableAllergiesLayoutAction;
    @BindView(R.id.expandable_allergies_details)
    ExpandableLayout expandableAllergiesLayout;

    @BindView(R.id.text_allergies)
    TextView allergenText;

    @BindView(R.id.expandable_data_action)
    ImageButton expandableDataLayoutAction;
    @BindView(R.id.expandable_data_details)
    ExpandableLayout expandableDataLayout;

    @BindView(R.id.settings_data_macros)
    CheckBox dataDisplayMacro;

    @BindView(R.id.expandable_start_screen_action)
    ImageButton expandableStartScreenLayoutAction;
    @BindView(R.id.expandable_start_screen_details)
    ExpandableLayout expandableStartScreenLayout;

    @BindView(R.id.settings_start_screen_recipes)
    CheckBox startScreenRecipes;
    @BindView(R.id.settings_start_screen_liquids)
    CheckBox startScreenLiquids;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addDoneListenerToEditTextViews();
        presenter.setView(this);
        presenter.start();
    }

    private void addDoneListenerToEditTextViews() {
        amountCalories.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocusOfView(amountCalories);
                    if (!StringUtils.isNullOrEmpty(amountCalories.getText().toString())) {
                        if (ValidationUtil.isValid(LOWER_BOUND_CALORIES, UPPER_BOUND_CALORIES, Integer.valueOf(amountCalories.getText().toString()), amountCalories, getContext()))
                            presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_CALORIES_DAILY, Integer.valueOf(amountCalories.getText().toString()));
                    } else
                        amountCalories.setText(StringUtils.formatInt(LOWER_BOUND_CALORIES));
                }
                return true;
            }
        });
        amountCalories.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!StringUtils.isNullOrEmpty(amountCalories.getText().toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_CALORIES, UPPER_BOUND_CALORIES, Integer.valueOf(amountCalories.getText().toString()), amountCalories, getContext()))
                        presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_CALORIES_DAILY, Integer.valueOf(amountCalories.getText().toString()));
                } else
                    amountCalories.setText(StringUtils.formatInt(LOWER_BOUND_CALORIES));
            }
        });

        amountProteins.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocusOfView(amountProteins);
                    if (!StringUtils.isNullOrEmpty(amountProteins.getText().toString())) {
                        if (ValidationUtil.isValid(LOWER_BOUND_PROTEINS, UPPER_BOUND_PROTEINS, Integer.valueOf(amountProteins.getText().toString()), amountProteins, getContext()))
                            presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_PROTEINS_DAILY, Integer.valueOf(amountProteins.getText().toString()));
                    } else
                        amountProteins.setText(StringUtils.formatInt(LOWER_BOUND_PROTEINS));
                }
                return true;
            }
        });
        amountProteins.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!StringUtils.isNullOrEmpty(amountProteins.getText().toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_PROTEINS, UPPER_BOUND_PROTEINS, Integer.valueOf(amountProteins.getText().toString()), amountProteins, getContext()))
                        presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_PROTEINS_DAILY, Integer.valueOf(amountProteins.getText().toString()));
                } else
                    amountProteins.setText(StringUtils.formatInt(LOWER_BOUND_PROTEINS));
            }
        });

        amountCarbs.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocusOfView(amountCarbs);
                    if (!StringUtils.isNullOrEmpty(amountCarbs.getText().toString())) {
                        if (ValidationUtil.isValid(LOWER_BOUND_CARBS, UPPER_BOUND_CARBS, Integer.valueOf(amountCarbs.getText().toString()), amountCarbs, getContext()))
                            presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_CARBS_DAILY, Integer.valueOf(amountCarbs.getText().toString()));
                    } else
                        amountCarbs.setText(StringUtils.formatInt(LOWER_BOUND_CARBS));
                }
                return true;
            }
        });
        amountCarbs.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!StringUtils.isNullOrEmpty(amountCarbs.getText().toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_CARBS, UPPER_BOUND_CARBS, Integer.valueOf(amountCarbs.getText().toString()), amountCarbs, getContext()))
                        presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_CARBS_DAILY, Integer.valueOf(amountCarbs.getText().toString()));
                } else
                    amountCarbs.setText(StringUtils.formatInt(LOWER_BOUND_CARBS));
            }
        });

        amountFats.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocusOfView(amountFats);
                    if (!StringUtils.isNullOrEmpty(amountFats.getText().toString())) {
                        if (ValidationUtil.isValid(LOWER_BOUND_FATS, UPPER_BOUND_FATS, Integer.valueOf(amountFats.getText().toString()), amountFats, getContext()))
                            presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_FATS_DAILY, Integer.valueOf(amountFats.getText().toString()));
                    } else
                        amountFats.setText(StringUtils.formatInt(LOWER_BOUND_FATS));
                }
                return true;
            }
        });
        amountFats.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!StringUtils.isNullOrEmpty(amountFats.getText().toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_FATS, UPPER_BOUND_FATS, Integer.valueOf(amountFats.getText().toString()), amountFats, getContext()))
                        presenter.updateSharedPreferenceIntValue(KEY_REQUIREMENT_FATS_DAILY, Integer.valueOf(amountFats.getText().toString()));
                } else
                    amountFats.setText(StringUtils.formatInt(LOWER_BOUND_FATS));
            }
        });

        amountLiquids.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocusOfView(amountLiquids);
                    if (!StringUtils.isNullOrEmpty(amountLiquids.getText().toString())) {
                        if (ValidationUtil.isValid(LOWER_BOUND_LIQUIDS, UPPER_BOUND_LIQUIDS, Integer.valueOf(amountLiquids.getText().toString()), amountLiquids, getContext()))
                            presenter.updateSharedPreferenceFloatValue(KEY_REQUIREMENT_LIQUID_DAILY, Float.valueOf(amountLiquids.getText().toString()));
                    } else
                        amountLiquids.setText(StringUtils.formatFloat(LOWER_BOUND_LIQUIDS));
                }
                return true;
            }
        });
        amountLiquids.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!StringUtils.isNullOrEmpty(amountLiquids.getText().toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_LIQUIDS, UPPER_BOUND_LIQUIDS, Integer.valueOf(amountLiquids.getText().toString()), amountLiquids, getContext()))
                        presenter.updateSharedPreferenceFloatValue(KEY_REQUIREMENT_LIQUID_DAILY, Float.valueOf(amountLiquids.getText().toString()));
                } else
                    amountLiquids.setText(StringUtils.formatFloat(LOWER_BOUND_LIQUIDS));
            }
        });

        volumeBottle.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocusOfView(volumeBottle);
                    if (!StringUtils.isNullOrEmpty(volumeBottle.getText().toString())) {
                        if (ValidationUtil.isValid(LOWER_BOUND_BOTTLE, UPPER_BOUND_BOTTLE, Integer.valueOf(volumeBottle.getText().toString()), volumeBottle, getContext()))
                            presenter.updateSharedPreferenceFloatValue(KEY_BOTTLE_VOLUME, Float.valueOf(volumeBottle.getText().toString()));
                    } else
                        volumeBottle.setText(StringUtils.formatInt(LOWER_BOUND_BOTTLE));
                }
                return true;
            }
        });
        volumeBottle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!StringUtils.isNullOrEmpty(volumeBottle.getText().toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_BOTTLE, UPPER_BOUND_BOTTLE, Integer.valueOf(volumeBottle.getText().toString()), volumeBottle, getContext()))
                        presenter.updateSharedPreferenceFloatValue(KEY_BOTTLE_VOLUME, Float.valueOf(volumeBottle.getText().toString()));
                } else
                    volumeBottle.setText(StringUtils.formatInt(LOWER_BOUND_BOTTLE));
            }
        });

        volumeGlass.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocusOfView(volumeGlass);
                    if (!StringUtils.isNullOrEmpty(volumeGlass.getText().toString())) {
                        if (ValidationUtil.isValid(LOWER_BOUND_GLASS, UPPER_BOUND_GLASS, Integer.valueOf(volumeGlass.getText().toString()), volumeGlass, getContext()))
                            presenter.updateSharedPreferenceFloatValue(KEY_GLASS_VOLUME, Float.valueOf(volumeGlass.getText().toString()));
                    } else
                        volumeGlass.setText(StringUtils.formatInt(LOWER_BOUND_GLASS));
                }
                return true;
            }
        });
        volumeGlass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!StringUtils.isNullOrEmpty(volumeGlass.getText().toString())) {
                    if (ValidationUtil.isValid(LOWER_BOUND_GLASS, UPPER_BOUND_GLASS, Integer.valueOf(volumeGlass.getText().toString()), volumeGlass, getContext()))
                        presenter.updateSharedPreferenceFloatValue(KEY_GLASS_VOLUME, Float.valueOf(volumeGlass.getText().toString()));
                } else
                    volumeGlass.setText(StringUtils.formatInt(LOWER_BOUND_GLASS));
            }
        });

    }

    @Override
    public void clearFocusOfView(EditText view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    @Override
    public void setupMealRecyclerView(ArrayList<MealDisplayModel> meals) {
        SettingsMealListAdapter adapter = new SettingsMealListAdapter(presenter, presenter);
        adapter.setList(meals);
        recyclerViewMeals.setAdapter(adapter);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void setupAllergenTextView(ArrayList<AllergenDisplayModel> allergens) {
        String allergenDescription = "";
        for (int i = 0; i < allergens.size(); i++) {
            allergenDescription += allergens.get(i).getName();
            if (i < allergens.size() - 1)
                allergenDescription += ", ";
        }

        if (allergens.size() == 0)
            allergenDescription = getString(R.string.no_allergens_set_placeholder);

        allergenText.setText(allergenDescription);
    }

    @Override
    protected void setupFragmentComponent() {
        TrackerApplication.get(getContext()).createSettingsComponent().inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.finish();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TrackerApplication.get(getContext()).releaseSettingsComponent();
    }

    @Override
    public void fillSettingsOptions(SharedPreferencesManager sharedPreferencesManager) {
        amountCalories.setText(StringUtils.formatInt(sharedPreferencesManager.getInt(KEY_REQUIREMENT_CALORIES_DAILY, SharedPreferencesUtil.VALUE_REQUIREMENT_CALORIES_DAILY)));
        amountProteins.setText(StringUtils.formatInt(sharedPreferencesManager.getInt(SharedPreferencesUtil.KEY_REQUIREMENT_PROTEINS_DAILY, SharedPreferencesUtil.VALUE_REQUIREMENT_PROTEINS_DAILY)));
        amountCarbs.setText(StringUtils.formatInt(sharedPreferencesManager.getInt(SharedPreferencesUtil.KEY_REQUIREMENT_CARBS_DAILY, SharedPreferencesUtil.VALUE_REQUIREMENT_CARBS_DAILY)));
        amountFats.setText(StringUtils.formatInt(sharedPreferencesManager.getInt(SharedPreferencesUtil.KEY_REQUIREMENT_FATS_DAILY, SharedPreferencesUtil.VALUE_REQUIREMENT_FATS_DAILY)));

        amountLiquids.setText(StringUtils.formatFloat(sharedPreferencesManager.getFloat(SharedPreferencesUtil.KEY_REQUIREMENT_LIQUID_DAILY, SharedPreferencesUtil.VALUE_REQUIREMENT_LIQUID_DAILY)));
        volumeBottle.setText(StringUtils.formatFloat(sharedPreferencesManager.getFloat(SharedPreferencesUtil.KEY_BOTTLE_VOLUME, SharedPreferencesUtil.VALUE_BOTTLE_VOLUME)));
        volumeGlass.setText(StringUtils.formatFloat(sharedPreferencesManager.getFloat(SharedPreferencesUtil.KEY_GLASS_VOLUME, SharedPreferencesUtil.VALUE_GLASS_VOLUME)));

        if (sharedPreferencesManager.getNutritionDetailsSetting().equals(SharedPreferencesUtil.VALUE_SETTING_NUTRITION_DETAILS_CALORIES_AND_MACROS))
            dataDisplayMacro.setChecked(true);
        else
            dataDisplayMacro.setChecked(false);

        startScreenRecipes.setChecked(sharedPreferencesManager.getBoolean(SharedPreferencesUtil.KEY_START_SCREEN_RECIPE, SharedPreferencesUtil.VALUE_TRUE));
        startScreenLiquids.setChecked(sharedPreferencesManager.getBoolean(SharedPreferencesUtil.KEY_START_SCREEN_LIQUIDS, SharedPreferencesUtil.VALUE_TRUE));
    }

    @Override
    public void showEditMealDialog(MealDisplayModel mealDisplayModel) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        Fragment previous = getFragmentManager().findFragmentByTag("dialog");
        if (previous != null)
            fragmentTransaction.remove(previous);
        fragmentTransaction.addToBackStack(null);

        AppCompatDialogFragment dialogFragment = new ManipulateMealDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("mealId", mealDisplayModel.getId());
        bundle.putString("mealTitle", mealDisplayModel.getName());
        bundle.putString("startTime", mealDisplayModel.getStartTime());
        bundle.putString("endTime", mealDisplayModel.getEndTime());
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentTransaction, "dialog");
    }

    @Override
    public void showEditAllergenDialog() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        Fragment previous = getFragmentManager().findFragmentByTag("dialog");
        if (previous != null)
            fragmentTransaction.remove(previous);
        fragmentTransaction.addToBackStack(null);

        AppCompatDialogFragment dialogFragment = new EditAllergensDialog();
        dialogFragment.show(fragmentTransaction, "dialog");
    }

    @Override
    public void updateRecyclerView() {
        recyclerViewMeals.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void setupCheckboxListeners() {
        dataDisplayMacro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                presenter.setNutritionDetailsSetting(checked);
            }
        });

        startScreenRecipes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                presenter.setStartScreenRecipesSetting(checked);
            }
        });

        startScreenLiquids.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                presenter.setStartScreenLiquidsSetting(checked);
            }
        });
    }

    @Override
    public void showOnboardingScreen(int onboardingTag) {
        startActivity(OnboardingActivity.newIntent(getContext(), onboardingTag));
    }

    @Override
    public void showDeleteMealConfirmDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(getString(R.string.dialog_message_confirm_meal_deletion));
        builder.setPositiveButton(getString(R.string.dialog_action_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.onMealItemDeleteConfirmed(id);
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_action_dismiss), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.expandable_diet_action)
    public void onExpandDietLayoutClicked() {
        if (expandableDietLayout.isExpanded()) {
            collapseExpandableLayout(expandableDietLayout, expandableDietLayoutAction);
        } else {
            expandExpandableLayout(expandableDietLayout, expandableDietLayoutAction);
        }
    }

    @OnClick(R.id.expandable_liquids_action)
    public void onExpandLiquidsLayoutClicked() {
        if (expandableLiquidLayout.isExpanded()) {
            collapseExpandableLayout(expandableLiquidLayout, expandableLiquidLayoutAction);
        } else {
            expandExpandableLayout(expandableLiquidLayout, expandableLiquidLayoutAction);
        }
    }

    @OnClick(R.id.expandable_meals_action)
    public void onExpandMealLayoutClicked() {
        if (expandableMealsLayout.isExpanded()) {
            collapseExpandableLayout(expandableMealsLayout, expandableMealsLayoutAction);
        } else {
            expandExpandableLayout(expandableMealsLayout, expandableMealsLayoutAction);
        }
    }

    @OnClick(R.id.expandable_allergies_action)
    public void onExpandAllergiesLayoutClicked() {
        if (expandableAllergiesLayout.isExpanded()) {
            collapseExpandableLayout(expandableAllergiesLayout, expandableAllergiesLayoutAction);
        } else {
            expandExpandableLayout(expandableAllergiesLayout, expandableAllergiesLayoutAction);
        }
    }

    @OnClick(R.id.expandable_data_action)
    public void onExpandDataLayoutClicked() {
        if (expandableDataLayout.isExpanded()) {
            collapseExpandableLayout(expandableDataLayout, expandableDataLayoutAction);
        } else {
            expandExpandableLayout(expandableDataLayout, expandableDataLayoutAction);
        }
    }

    @OnClick(R.id.expandable_start_screen_action)
    public void onExpandStartScreenLayoutClicked() {
        if (expandableStartScreenLayout.isExpanded()) {
            collapseExpandableLayout(expandableStartScreenLayout, expandableStartScreenLayoutAction);
        } else {
            expandExpandableLayout(expandableStartScreenLayout, expandableStartScreenLayoutAction);
        }
    }

    @OnClick(R.id.add_meal)
    public void onAddMealClicked() {
        if (recyclerViewMeals.getAdapter().getItemCount() >= 10)
            Toast.makeText(getContext(), R.string.error_message_only_ten_supported_meals, Toast.LENGTH_LONG).show();
        else {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            Fragment previous = getFragmentManager().findFragmentByTag("dialog");
            if (previous != null)
                fragmentTransaction.remove(previous);
            fragmentTransaction.addToBackStack(null);

            AppCompatDialogFragment dialogFragment = new ManipulateMealDialog();
            dialogFragment.show(fragmentTransaction, "dialog");
        }
    }

    @OnClick(R.id.text_allergies)
    public void onEditAllergensClicked() {
        presenter.onEditAllergensClicked();
    }

    private void collapseExpandableLayout(ExpandableLayout layout, ImageButton action) {
        action.setImageDrawable(getContext().getDrawable(R.drawable.ic_expand_more_primary_text));
        layout.collapse(true);
    }

    private void expandExpandableLayout(ExpandableLayout layout, ImageButton action) {
        action.setImageDrawable(getContext().getDrawable(R.drawable.ic_expand_less_primary_text));
        layout.expand(true);
    }

    @Override
    public void updateAllergens() {
        presenter.updateAllergens();
    }

    @Override
    public void addMealInDialogClicked(String name, String startTime, String endTime) {
        presenter.onAddMealInDialogClicked(name, startTime, endTime);
    }

    @Override
    public void saveMealInDialogClicked(int id, String name, String startTime, String endTime) {
        presenter.onSaveMealInDialogClicked(id, name, startTime, endTime);
    }
}
