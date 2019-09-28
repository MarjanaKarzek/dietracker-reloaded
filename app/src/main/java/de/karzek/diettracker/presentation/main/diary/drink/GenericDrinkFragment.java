package de.karzek.diettracker.presentation.main.diary.drink;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.karzek.diettracker.R;
import de.karzek.diettracker.presentation.TrackerApplication;
import de.karzek.diettracker.presentation.common.BaseFragment;
import de.karzek.diettracker.presentation.main.diary.meal.adapter.diaryEntryList.DiaryEntryListAdapter;
import de.karzek.diettracker.presentation.main.diary.meal.viewStub.CaloryDetailsView;
import de.karzek.diettracker.presentation.main.diary.meal.viewStub.CaloryMacroDetailsView;
import de.karzek.diettracker.presentation.model.DiaryEntryDisplayModel;
import de.karzek.diettracker.presentation.search.grocery.groceryDetail.GroceryDetailsActivity;
import de.karzek.diettracker.presentation.util.Constants;
import de.karzek.diettracker.presentation.util.StringUtils;
import de.karzek.diettracker.presentation.util.ValidationUtil;

import static de.karzek.diettracker.presentation.util.SharedPreferencesUtil.VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY;

/**
 * Created by MarjanaKarzek on 28.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 28.05.2018
 */
public class GenericDrinkFragment extends BaseFragment implements GenericDrinkContract.View {

    private static final int LOWER_BOUND_WATER_INPUT = 0;
    private static final int UPPER_BOUND_WATER_INPUT = Integer.MAX_VALUE;

    @Override
    protected void setupFragmentComponent() {
        TrackerApplication.get(getContext()).createGenericDrinkComponent().inject(this);
    }

    @Inject
    GenericDrinkContract.Presenter presenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.viewstub_calory_details)
    ViewStub caloryDetails;
    @BindView(R.id.viewstub_calory_macro_details)
    ViewStub caloryMacroDetails;
    @BindView(R.id.circle_progress_bar_dinks_progress)
    CircularProgressBar liquidProgressBar;
    @BindView(R.id.circle_progress_bar_drinks_value)
    EditText drinkStatus;
    @BindView(R.id.expandable_details)
    ExpandableLayout expandableDetails;
    @BindView(R.id.expandable_layout_action)
    ImageButton expandableLayoutAction;
    @BindView(R.id.loading_view)
    FrameLayout loadingView;
    @BindView(R.id.drinks_placeholder)
    TextView placeholder;
    @BindView(R.id.liquid_max_value)
    TextView liquidMaxValue;

    private Unbinder unbinder;

    private CaloryDetailsView detailsView;

    private String selectedDate;
    private HashMap<String, Long> maxValues;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generic_drink, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drinkStatus
                .setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            drinkStatus.clearFocus();
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(drinkStatus.getWindowToken(), 0);
                            if (!StringUtils.isNullOrEmpty(drinkStatus.getText().toString())) {
                                if (ValidationUtil.isValid(LOWER_BOUND_WATER_INPUT, UPPER_BOUND_WATER_INPUT, Float.valueOf(drinkStatus.getText().toString()), drinkStatus, getContext()))
                                    presenter.updateAmountOfWater(Float.valueOf(drinkStatus.getText().toString()), selectedDate);
                            } else
                                drinkStatus.setText(StringUtils.formatFloat(LOWER_BOUND_WATER_INPUT));
                        }
                        return true;
                    }
                });

        selectedDate = getArguments().getString("selectedDate");

        setupRecyclerView();

        presenter.setView(this);
        presenter.start();
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DiaryEntryListAdapter(presenter, presenter, presenter, null));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));
    }

    @Override
    public void showNutritionDetails(String value) {
        if (value.equals(VALUE_SETTING_NUTRITION_DETAILS_CALORIES_ONLY)) {
            detailsView = new CaloryDetailsView(caloryDetails.inflate());
        } else {
            detailsView = new CaloryMacroDetailsView(caloryMacroDetails.inflate());
        }
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public String getSelectedDate() {
        return selectedDate;
    }

    @Override
    public void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGroceryListPlaceholder() {
        placeholder.setVisibility(View.GONE);
    }

    @Override
    public void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showGroceryListPlaceholder() {
        placeholder.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateGroceryList(ArrayList<DiaryEntryDisplayModel> displayModels) {
        ((DiaryEntryListAdapter) recyclerView.getAdapter()).setList(displayModels);
    }

    @Override
    public void hideExpandOption() {
        expandableLayoutAction.setVisibility(View.GONE);
    }

    @Override
    public void showExpandOption() {
        expandableLayoutAction.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLiquidStatus(DiaryEntryDisplayModel displayModel, float waterGoal) {
        drinkStatus.setText(StringUtils.formatFloat(displayModel.getAmount()));
        liquidProgressBar.setProgress(100.0f / waterGoal * displayModel.getAmount());
        liquidMaxValue.setText(getString(R.string.generic_drinks_max_value, StringUtils.formatFloat(waterGoal)));
    }

    @Override
    public void addToLiquidStatus(ArrayList<DiaryEntryDisplayModel> displayModels, float waterGoal) {
        float currentValue = Float.valueOf(drinkStatus.getText().toString());

        float additionalValue = 0.0f;
        for (DiaryEntryDisplayModel model : displayModels) {
            additionalValue += model.getAmount();
        }

        drinkStatus.setText(StringUtils.formatFloat(currentValue + additionalValue));
        liquidProgressBar.setProgress(100.0f / waterGoal * (currentValue + additionalValue));
    }

    @Override
    public void refreshLiquidStatus() {
        presenter.updateLiquidStatus(selectedDate);
    }

    @Override
    public void setNutritionMaxValues(HashMap<String, Long> maxValues) {
        this.maxValues = maxValues;
        detailsView.getCaloryProgressBarMaxValue().setText("" + maxValues.get(Constants.CALORIES));

        if (detailsView instanceof CaloryMacroDetailsView) {
            ((CaloryMacroDetailsView) detailsView).getProteinProgressBarMaxValue().setText("von\n" + maxValues.get(Constants.PROTEINS) + "g");
            ((CaloryMacroDetailsView) detailsView).getCarbsProgressBarMaxValue().setText("von\n" + maxValues.get(Constants.CARBS) + "g");
            ((CaloryMacroDetailsView) detailsView).getFatsProgressBarMaxValue().setText("von\n" + maxValues.get(Constants.FATS) + "g");
        }
    }

    @Override
    public void updateNutritionDetails(HashMap<String, Float> values) {
        detailsView.getCaloryProgressBar().setProgress(100.0f / maxValues.get(Constants.CALORIES) * values.get(Constants.CALORIES));
        detailsView.getCaloryProgressBarValue().setText("" + (int) values.get(Constants.CALORIES).floatValue());

        if (detailsView instanceof CaloryMacroDetailsView) {
            ((CaloryMacroDetailsView) detailsView).getProteinProgressBar().setProgress(100.0f / maxValues.get(Constants.PROTEINS) * values.get(Constants.PROTEINS));
            ((CaloryMacroDetailsView) detailsView).getProteinProgressBarValue().setText("" + StringUtils.formatFloat(values.get(Constants.PROTEINS)));

            ((CaloryMacroDetailsView) detailsView).getCarbsProgressBar().setProgress(100.0f / maxValues.get(Constants.CARBS) * values.get(Constants.CARBS));
            ((CaloryMacroDetailsView) detailsView).getCarbsProgressBarValue().setText("" + StringUtils.formatFloat(values.get(Constants.CARBS)));

            ((CaloryMacroDetailsView) detailsView).getFatsProgressBar().setProgress(100.0f / maxValues.get(Constants.FATS) * values.get(Constants.FATS));
            ((CaloryMacroDetailsView) detailsView).getFatsProgressBarValue().setText("" + StringUtils.formatFloat(values.get(Constants.FATS)));
        }
    }

    @Override
    public void startEditMode(int id) {
        startActivity(GroceryDetailsActivity.newEditDiaryEntryIntent(getContext(), id));
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
        TrackerApplication.get(getContext()).releaseGenericDrinkComponent();
    }

    @OnClick(R.id.expandable_layout_action)
    public void onExpandDetailsClicked() {
        if (expandableDetails.isExpanded()) {
            expandableLayoutAction.setImageDrawable(getContext().getDrawable(R.drawable.ic_expand_more_primary_text));
            expandableDetails.collapse(true);
        } else {
            expandableLayoutAction.setImageDrawable(getContext().getDrawable(R.drawable.ic_expand_less_primary_text));
            expandableDetails.expand(true);
        }
    }

    @OnClick(R.id.add_bottle)
    public void onAddBottleWaterClicked() {
        presenter.addBottleWaterClicked(selectedDate);
    }

    @OnClick(R.id.add_glass)
    public void onAddGlassWaterClicked() {
        presenter.addGlassWaterClicked(selectedDate);
    }

    @OnClick(R.id.add_favorite_drink)
    public void onAddFavoriteDrinkClicked() {
        presenter.addFavoriteDrinkClicked(selectedDate);
    }
}
