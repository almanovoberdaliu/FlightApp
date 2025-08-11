package org.flighApp.Booking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class Booking {
    private int id;
    private Passenger passenger;
    private int flightId;
    private Date bookingDate;
    private BookingStatus status;
    private int totalPassengers;
}
