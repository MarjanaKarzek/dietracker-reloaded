package de.karzek.diettracker.presentation.dependencyInjection.module.fragmentModule;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import de.karzek.diettracker.data.repository.repositoryInterface.MealRepository;
import de.karzek.diettracker.domain.interactor.manager.managerInterface.SharedPreferencesManager;
import de.karzek.diettracker.domain.interactor.useCase.meal.GetMealByIdUseCaseImpl;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.diaryEntry.PutDiaryEntryUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.meal.GetAllMealsUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.meal.GetMealByIdUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.recipe.DeleteRecipeByIdUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.recipe.GetAllRecipesUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.recipe.GetMatchingRecipesUseCase;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.recipe.GetRecipeByIdUseCase;
import de.karzek.diettracker.domain.mapper.MealDomainMapper;
import de.karzek.diettracker.presentation.main.cookbook.CookbookContract;
import de.karzek.diettracker.presentation.main.cookbook.CookbookPresenter;
import de.karzek.diettracker.presentation.mapper.DiaryEntryUIMapper;
import de.karzek.diettracker.presentation.mapper.MealUIMapper;
import de.karzek.diettracker.presentation.mapper.RecipeUIMapper;

/**
 * Created by MarjanaKarzek on 12.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 12.05.2018
 */
@Module
public class CookbookModule {

    //presentation

    @Provides
    CookbookContract.Presenter provideCookbookPresenter(GetAllRecipesUseCase getAllRecipesUseCase,
                                                        SharedPreferencesManager sharedPreferencesManager,
                                                        Lazy<DeleteRecipeByIdUseCase> deleteRecipeByIdUseCase,
                                                        Lazy<GetAllMealsUseCase> getAllMealsUseCase,
                                                        Lazy<GetMatchingRecipesUseCase> getMatchingRecipesUseCase,
                                                        Lazy<GetRecipeByIdUseCase> getRecipeByIdUseCase,
                                                        Lazy<PutDiaryEntryUseCase> putDiaryEntryUseCase,
                                                        MealUIMapper mealMapper,
                                                        RecipeUIMapper recipeMapper,
                                                        DiaryEntryUIMapper diaryEntryMapper) {
        return new CookbookPresenter(getAllRecipesUseCase,
                sharedPreferencesManager,
                deleteRecipeByIdUseCase,
                getAllMealsUseCase,
                getMatchingRecipesUseCase,
                getRecipeByIdUseCase,
                putDiaryEntryUseCase,
                mealMapper,
                recipeMapper,
                diaryEntryMapper);
    }
}
