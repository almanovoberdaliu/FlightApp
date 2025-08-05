package org.flighApp.Flight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {
    private String ticketType;
    private String originCode;
    private String destinationCode;
    private Double priceForOneWay;
    private Date departureDate;
    private Double priceForReturn;
    private Date returnDate;
    private String duration;
}
