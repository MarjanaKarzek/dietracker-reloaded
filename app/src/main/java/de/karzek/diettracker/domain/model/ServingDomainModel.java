package de.karzek.diettracker.domain.model;

import lombok.Value;

/**
 * Created by MarjanaKarzek on 27.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 27.05.2018
 */
@Value
public class ServingDomainModel {
    private int id;
    private String description;
    private int amount;
    private UnitDomainModel unit;
}
