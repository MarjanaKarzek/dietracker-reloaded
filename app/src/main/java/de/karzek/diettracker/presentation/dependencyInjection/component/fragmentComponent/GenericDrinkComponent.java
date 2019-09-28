package de.karzek.diettracker.presentation.dependencyInjection.component.fragmentComponent;

import dagger.Subcomponent;
import de.karzek.diettracker.presentation.dependencyInjection.module.fragmentModule.GenericDrinkModule;
import de.karzek.diettracker.presentation.main.diary.drink.GenericDrinkFragment;

/**
 * Created by MarjanaKarzek on 29.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 29.05.2018
 */
@Subcomponent(modules = {GenericDrinkModule.class})
public interface GenericDrinkComponent {

    void inject(GenericDrinkFragment fragment);
}
