package de.karzek.diettracker.presentation.model;

import lombok.Value;

/**
 * Created by MarjanaKarzek on 27.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 27.05.2018
 */
@Value
public class DiaryEntryDisplayModel {
    public int id;
    public MealDisplayModel meal;
    public float amount;
    public UnitDisplayModel unit;
    public GroceryDisplayModel grocery;
    public String date;

    public DiaryEntryDisplayModel(int id, MealDisplayModel meal, float amount, UnitDisplayModel unit, GroceryDisplayModel grocery, String date){
        this.id = id;
        this.meal = meal;
        this.amount = amount;
        this.unit = unit;
        this.grocery = grocery;
        this.date = date;
    }

    public DiaryEntryDisplayModel(int id, float amount, UnitDisplayModel unit, GroceryDisplayModel grocery, String date){
        this.id = id;
        meal = null;
        this.amount = amount;
        this.unit = unit;
        this.grocery = grocery;
        this.date = date;
    }
}
