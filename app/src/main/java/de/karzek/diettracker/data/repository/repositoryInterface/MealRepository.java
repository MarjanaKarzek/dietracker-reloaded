package de.karzek.diettracker.data.repository.repositoryInterface;

import java.util.List;

import de.karzek.diettracker.data.model.MealDataModel;
import de.karzek.diettracker.data.model.UnitDataModel;
import de.karzek.diettracker.domain.model.MealDomainModel;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by MarjanaKarzek on 27.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 27.05.2018
 */
public interface MealRepository {

    Observable<Boolean> putAllMeals(List<MealDataModel> mealDataModels);

    Observable<List<MealDataModel>> getAllMeals();

    Observable<Long> getMealCount();

    Observable<MealDataModel> getMealById(int id);

    Observable<MealDataModel> getMealByName(String meal);

    Observable<Boolean> putMeal(MealDataModel mealDataModel);

    Observable<Boolean> deleteMealById(int id);

    Observable<Boolean> updateMeal(MealDataModel meal);
}
