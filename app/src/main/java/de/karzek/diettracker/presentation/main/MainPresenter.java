package de.karzek.diettracker.presentation.main;

import de.karzek.diettracker.domain.interactor.manager.managerInterface.SharedPreferencesManager;
import io.reactivex.disposables.CompositeDisposable;

import static de.karzek.diettracker.presentation.util.Constants.ONBOARDING_WELCOME;

/**
 * Created by MarjanaKarzek on 25.04.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 25.04.2018
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    private SharedPreferencesManager sharedPreferencesManager;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainPresenter(SharedPreferencesManager sharedPreferencesManager) {
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public void start() {
        if (!sharedPreferencesManager.getOnboardingViewed(ONBOARDING_WELCOME))
            view.showOnboardingScreen();
    }

    @Override
    public void setView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void finish() {
        compositeDisposable.clear();
    }

}
