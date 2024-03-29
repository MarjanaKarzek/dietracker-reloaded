package de.karzek.diettracker.presentation.search.grocery.barcodeScanner;

import androidx.annotation.IntDef;

import com.google.zxing.Result;

import de.karzek.diettracker.presentation.common.BasePresenter;
import de.karzek.diettracker.presentation.common.BaseView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by MarjanaKarzek on 12.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 12.05.2018
 */
public interface BarcodeScannerContract {

    interface View extends BaseView<Presenter>,
            ZXingScannerView.ResultHandler {

        void startDetailsActivity(int id);

        void showLoading();

        void hideLoading();

        void showNoResultsDialog();

        @IntDef({SearchMode.MODE_GROCERY_SEARCH,
                SearchMode.MODE_INGREDIENT_SEARCH,
                SearchMode.MODE_REPLACE_INGREDIENT_SEARCH})
        @interface SearchMode {
            int MODE_GROCERY_SEARCH = 0;
            int MODE_INGREDIENT_SEARCH = 1;
            int MODE_REPLACE_INGREDIENT_SEARCH = 2;
        }
    }

    interface Presenter extends BasePresenter<View> {

        void checkResult(Result result);

    }
}
