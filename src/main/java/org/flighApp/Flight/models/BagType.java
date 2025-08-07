package org.flighApp.Flight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BagType {
    HAND(30.0),
    CHECKED(50.0);

    private final double price;
}
