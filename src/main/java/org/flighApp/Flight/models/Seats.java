package org.flighApp.Flight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class Seats {
    private TripClass tripClass;
    private int availableSeats;

//    public TripClass getTripClass() {
//        return TripClass.fromId(this.tripClass);
//    }
}
