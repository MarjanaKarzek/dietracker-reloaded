package de.karzek.diettracker.data.model;

import lombok.Value;

/**
 * Created by MarjanaKarzek on 27.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 27.05.2018
 */
@Value
public class MealDataModel {
    private int id;
    private String name;
    private String startTime;
    private String endTime;
}
