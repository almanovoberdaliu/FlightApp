package org.flighApp.Booking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.bson.types.ObjectId;
import org.flighApp.Flight.models.TripClass;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class Booking {
  private int id;
  private Passenger passenger;
  private ObjectId flightId;
  private Date bookingDate;
  private BookingStatus status;
  private int totalPassengers;
  private TripClass tripClass;
}
