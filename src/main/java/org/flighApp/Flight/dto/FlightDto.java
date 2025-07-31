package org.flighApp.Flight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.flighApp.Flight.models.Flight;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto extends Flight {
    private int availableSeats;
}



