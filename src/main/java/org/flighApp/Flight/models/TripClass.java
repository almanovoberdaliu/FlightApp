package org.flighApp.Flight.models;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TripClass {
  ECONOMY(1, "Economy", 0.0),
  BUSINESS(2, "Business", 100.0),
  FIRST(3, "First", 200.0);

  private final int id;
  private final String type;
  private final double price;

  public static double getPriceById(int id) {
    return Arrays.stream(values())
        .filter(t -> t.id == id)
        .findFirst()
        .map(TripClass::getPrice)
        .orElse(0.0);
  }

}
