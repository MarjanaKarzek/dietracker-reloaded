package de.karzek.diettracker.presentation.dependencyInjection.component.fragmentComponent;

import dagger.Subcomponent;
import de.karzek.diettracker.presentation.dependencyInjection.module.fragmentModule.SettingsModule;
import de.karzek.diettracker.presentation.dependencyInjection.module.featureModule.AllergenModule;
import de.karzek.diettracker.presentation.main.settings.SettingsFragment;

/**
 * Created by MarjanaKarzek on 12.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 12.05.2018
 */
@Subcomponent(modules = {SettingsModule.class, AllergenModule.class})
public interface SettingsComponent {

    void inject(SettingsFragment fragment);
}
