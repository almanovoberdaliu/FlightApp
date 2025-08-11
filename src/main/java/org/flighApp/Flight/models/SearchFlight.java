package org.flighApp.Flight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.flighApp.utils.Utils;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SearchFlight {
  private String ticketType;
  private String originCode;
  private String destinationCode;
  private Date departureDate;
  private Date returnDate;
  private TripClass tripClass;
  private List<PassengersByType> passengerList;
  private List<Bag> bags;


  public Integer getPassengersCount() {
    return Utils.nullSafe(passengerList).stream().mapToInt(PassengersByType::getNumber).sum();
  }



}
