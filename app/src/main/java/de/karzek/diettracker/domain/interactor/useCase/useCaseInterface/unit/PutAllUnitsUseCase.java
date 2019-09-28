package de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.unit;

import android.support.annotation.IntDef;

import java.util.List;

import de.karzek.diettracker.domain.common.BaseObservableUseCase;
import de.karzek.diettracker.domain.common.BaseUseCase;
import de.karzek.diettracker.domain.model.GroceryDomainModel;
import de.karzek.diettracker.domain.model.UnitDomainModel;
import de.karzek.diettracker.presentation.model.UnitDisplayModel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by MarjanaKarzek on 27.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 27.05.2018
 */
public interface PutAllUnitsUseCase extends BaseObservableUseCase<PutAllUnitsUseCase.Input, PutAllUnitsUseCase.Output> {

    @AllArgsConstructor
    @Data
    class Input implements BaseUseCase.Input {

        List<UnitDomainModel> unitDomainModels;

    }

    @AllArgsConstructor
    @Data
    class Output implements BaseUseCase.Output {

        @UnitDataListStatus
        int status;
        public static final int SUCCESS = 0;
        public static final int ERROR_NO_DATA = 1;
        public static final int ERROR_NETWORK_PROBLEM = 2;
        public static final int ERROR_UNKNOWN_PROBLEM = 3;

        @IntDef({SUCCESS, ERROR_NO_DATA, ERROR_NETWORK_PROBLEM, ERROR_UNKNOWN_PROBLEM})

        private @interface UnitDataListStatus {

        }
    }
}