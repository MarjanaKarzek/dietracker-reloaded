package de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.diaryEntry;

import androidx.annotation.IntDef;

import de.karzek.diettracker.domain.common.BaseObservableUseCase;
import de.karzek.diettracker.domain.common.BaseUseCase;
import de.karzek.diettracker.domain.model.MealDomainModel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by MarjanaKarzek on 27.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 27.05.2018
 */
public interface UpdateMealOfDiaryEntryUseCase extends BaseObservableUseCase<UpdateMealOfDiaryEntryUseCase.Input, UpdateMealOfDiaryEntryUseCase.Output> {

    @AllArgsConstructor
    @Data
    class Input implements BaseUseCase.Input {
        int id;
        MealDomainModel meal;
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