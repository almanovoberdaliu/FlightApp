package org.flighApp.Flight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassengerType {
  CHILD("3-17"),
  ADULT("18-64"),
  INFANT("0-2");

  private final String ageRange;
}
