package de.karzek.diettracker.presentation.common;

import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import de.karzek.diettracker.presentation.TrackerApplication;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by MarjanaKarzek on 12.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 12.05.2018
 */
public abstract class BaseFragment extends Fragment {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFragmentComponent();
    }

    protected abstract void setupFragmentComponent();

    @Override public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }

        TrackerApplication.get(getActivity()).watch(this);
    }
}
