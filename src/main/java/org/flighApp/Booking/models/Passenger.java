package org.flighApp.Booking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.flighApp.Flight.models.PassengerType;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class Passenger {
  private int id;
  private String firstName;
  private String lastName;
  private String passportNumber;
  private Date dateOfBirth;
  private PassengerType type;
}
