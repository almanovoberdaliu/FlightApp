package org.flighApp.Booking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.flighApp.Flight.models.TripClass;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class Ticket {
    private int id;
    private int bookingId;
    private Passenger passenger;
    private int flightId;
    private String seatNumber;
    private String status;
    private Date issueDate;
}
