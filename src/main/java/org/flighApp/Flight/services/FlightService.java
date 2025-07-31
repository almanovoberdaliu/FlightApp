package org.flighApp.Flight.services;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import org.flighApp.Flight.dto.FlightDto;
import org.flighApp.Flight.dto.TripClassPricesDto;
import org.flighApp.Flight.models.*;
import org.flighApp.Flight.repo.FlightRepository;

@ApplicationScoped
public class FlightService {

  @Inject FlightRepository flightRepository;

  public Uni<List<Flight>> getAllFlights() {
    return flightRepository.findAll();
  }

  public Uni<Void> addFlight(FlightDto flight) {
    return flightRepository.insert(flight);
  }

  public Uni<List<Flight>> getFlightsByOriginCode(String keyword) {
    return flightRepository.searchFlightByOriginCode(keyword);
  }

  public Uni<List<Flight>> searchFlight(String originCode, String destinationCode) {
    return flightRepository.searchFlight(originCode, destinationCode);
  }


  public Uni<Boolean> getAvailableSeats(SearchFlight request) {
    return flightRepository.getAvailableSeats(request);
  }


  public Uni<List<TripClassPricesDto>> allFlightsGroupByTripClass() {
    return flightRepository.allFlightsGroupByTripClass();
  }

  public Uni<List<Flight>> findPricesByTripClass(String tripClass) {
    return flightRepository.findPricesByTripClass(tripClass);
  }

  public Uni<List<Flight>> getCheapestFlights() {
    return flightRepository.getCheapestFlights();
  }
}
