package de.karzek.diettracker.presentation.dependencyInjection.module.activityModules;

import android.view.LayoutInflater;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import de.karzek.diettracker.data.cache.DiaryEntryCacheImpl;
import de.karzek.diettracker.data.cache.interfaces.DiaryEntryCache;
import de.karzek.diettracker.data.mapper.DiaryEntryDataMapper;
import de.karzek.diettracker.data.mapper.MealDataMapper;
import de.karzek.diettracker.data.repository.DiaryEntryRepositoryImpl;
import de.karzek.diettracker.data.repository.FavoriteGroceryRepositoryImpl;
import de.karzek.diettracker.data.repository.GroceryRepositoryImpl;
import de.karzek.diettracker.data.repository.MealRepositoryImpl;
import de.karzek.diettracker.data.repository.UnitRepositoryImpl;
import de.karzek.diettracker.data.repository.repositoryInterface.DiaryEntryRepository;
import de.karzek.diettracker.data.repository.repositoryInterface.FavoriteGroceryRepository;
import de.karzek.diettracker.data.repository.repositoryInterface.GroceryRepository;
import de.karzek.diettracker.data.repository.repositoryInterface.MealRepository;
import de.karzek.diettracker.data.repository.repositoryInterface.UnitRepository;
import de.karzek.diettracker.domain.interactor.manager.NutritionManagerImpl;
import de.karzek.diettracker.domain.interactor.manager.managerInterface.NutritionManager;
import de.karzek.diettracker.domain.interactor.useCase.diaryEntry.DeleteDiaryEntryUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.diaryEntry.GetDiaryEntryByIdUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.diaryEntry.PutDiaryEntryUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.favoriteGrocery.GetFavoriteStateForGroceryIdUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.favoriteGrocery.PutFavoriteGroceryUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.favoriteGrocery.RemoveFavoriteGroceryByNameUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.grocery.GetGroceryByIdUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.meal.GetAllMealsUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.meal.GetMealCountUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.unit.GetAllDefaultUnitsUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.allergen.GetMatchingAllergensUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.diaryEntry.DeleteDiaryEntryUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.diaryEntry.GetDiaryEntryByIdUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.diaryEntry.PutDiaryEntryUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.favoriteGrocery.GetFavoriteStateForGroceryIdUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.favoriteGrocery.PutFavoriteGroceryUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.favoriteGrocery.RemoveFavoriteGroceryByNameUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.grocery.GetGroceryByIdUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.meal.GetAllMealsUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.meal.GetMealCountUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.unit.GetAllDefaultUnitsUseCase;
import de.karzek.diettracker.domain.mapper.DiaryEntryDomainMapper;
import de.karzek.diettracker.domain.mapper.FavoriteGroceryDomainMapper;
import de.karzek.diettracker.domain.mapper.GroceryDomainMapper;
import de.karzek.diettracker.domain.mapper.MealDomainMapper;
import de.karzek.diettracker.domain.mapper.UnitDomainMapper;
import de.karzek.diettracker.presentation.mapper.AllergenUIMapper;
import de.karzek.diettracker.presentation.mapper.DiaryEntryUIMapper;
import de.karzek.diettracker.presentation.mapper.GroceryUIMapper;
import de.karzek.diettracker.presentation.mapper.MealUIMapper;
import de.karzek.diettracker.presentation.mapper.UnitUIMapper;
import de.karzek.diettracker.presentation.search.grocery.groceryDetail.GroceryDetailsContract;
import de.karzek.diettracker.presentation.search.grocery.groceryDetail.GroceryDetailsPresenter;
import de.karzek.diettracker.presentation.util.SharedPreferencesUtil;

/**
 * Created by MarjanaKarzek on 29.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 29.05.2018
 */
@Module
public class GroceryDetailsModule {

    //presentation

    @Provides
    GroceryDetailsContract.Presenter provideFoodDetailsPresenter(SharedPreferencesUtil sharedPreferencesUtil,
                                                                 GetGroceryByIdUseCase getGroceryByIdUseCase,
                                                                 GetAllDefaultUnitsUseCase getAllDefaultUnitsUseCase,
                                                                 GetAllMealsUseCase getAllMealsUseCase,
                                                                 GetMealCountUseCase getMealCountUseCase,
                                                                 GetFavoriteStateForGroceryIdUseCase getFavoriteStateForGroceryIdUseCase,
                                                                 Lazy<GetDiaryEntryByIdUseCase> getDiaryEntryByIdUseCase,
                                                                 Lazy<PutDiaryEntryUseCase> putDiaryEntryUseCase,
                                                                 Lazy<PutFavoriteGroceryUseCase> putFavoriteGroceryUseCase,
                                                                 Lazy<DeleteDiaryEntryUseCase> deleteDiaryEntryUseCase,
                                                                 Lazy<RemoveFavoriteGroceryByNameUseCase> removeFavoriteGroceryByNameUseCase,
                                                                 Lazy<GetMatchingAllergensUseCase> getMatchingAllergensUseCase,
                                                                 GroceryUIMapper groceryMapper,
                                                                 UnitUIMapper unitMapper,
                                                                 MealUIMapper mealMapper,
                                                                 DiaryEntryUIMapper diaryEntryMapper,
                                                                 AllergenUIMapper allergenMapper,
                                                                 NutritionManager nutritionManager) {
        return new GroceryDetailsPresenter(sharedPreferencesUtil,
                getGroceryByIdUseCase,
                getAllDefaultUnitsUseCase,
                getAllMealsUseCase,
                getMealCountUseCase,
                getFavoriteStateForGroceryIdUseCase,
                getDiaryEntryByIdUseCase,
                putDiaryEntryUseCase,
                putFavoriteGroceryUseCase,
                deleteDiaryEntryUseCase,
                removeFavoriteGroceryByNameUseCase,
                getMatchingAllergensUseCase,
                groceryMapper,
                unitMapper,
                mealMapper,
                diaryEntryMapper,
                allergenMapper,
                nutritionManager);
    }
}
