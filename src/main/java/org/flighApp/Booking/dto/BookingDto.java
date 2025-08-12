package org.flighApp.Booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.flighApp.Booking.models.Passenger;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class BookingDto {
  private Passenger passenger;
  private int flightId;
  private Date bookingDate;
  private int totalPassengers;
}
