package org.flighApp.Flight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.flighApp.Flight.models.Flight;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripClassPricesDto {
    private String tripClass;
    private List<Flight> flights;
}
