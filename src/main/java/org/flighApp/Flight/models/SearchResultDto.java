package org.flighApp.Flight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto {
    private List<Flight> departureFlights;
    private List<Flight> returnFlights;
}
