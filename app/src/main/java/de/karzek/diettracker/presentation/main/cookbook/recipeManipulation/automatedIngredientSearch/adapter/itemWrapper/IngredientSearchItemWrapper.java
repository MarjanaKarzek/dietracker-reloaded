package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.automatedIngredientSearch.adapter.itemWrapper;

import androidx.annotation.IntDef;

import de.karzek.diettracker.presentation.model.IngredientDisplayModel;
import lombok.Value;

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
@Value
public class IngredientSearchItemWrapper {

    @IntDef({ItemType.LOADING_INGREDIENT,
            ItemType.FOUND_INGREDIENT,
            ItemType.FAILED_INGREDIENT
    })
    public @interface ItemType {
        int LOADING_INGREDIENT = 0;
        int FOUND_INGREDIENT = 1;
        int FAILED_INGREDIENT = 2;
    }

    @ItemType
    public int type;
    public IngredientDisplayModel ingredientDisplayModel;
    public int failReason;

    public IngredientSearchItemWrapper(@ItemType int type, IngredientDisplayModel displayModel) {
        this.type = type;
        this.ingredientDisplayModel = displayModel;
        failReason = -1;
    }

    public IngredientSearchItemWrapper(@ItemType int type, IngredientDisplayModel displayModel, int failReason) {
        this.type = type;
        this.ingredientDisplayModel = displayModel;
        this.failReason = failReason;
    }

}
