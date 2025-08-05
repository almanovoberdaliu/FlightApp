package org.flighApp.Flight.services;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.flighApp.Flight.dto.AvailableTripClassesDto;
import org.flighApp.Flight.dto.FlightDto;
import org.flighApp.Flight.dto.TopDestinationsDto;
import org.flighApp.Flight.dto.TripClassPricesDto;
import org.flighApp.Flight.models.*;
import org.flighApp.Flight.repo.FlightRepository;

@ApplicationScoped
public class FlightService {

  @Inject FlightRepository flightRepository;

  public Uni<List<FlightDto>> getAllFlights() {
    return flightRepository.findAll();
  }

  public Uni<Void> addFlight(Flight flight) {
    return flightRepository.insert(flight);
  }

  public Uni<List<FlightDto>> getFlightsByOriginCode(String keyword) {
    return flightRepository.searchFlightByOriginCode(keyword);
  }

  public Uni<List<FlightDto>> searchFlight(String originCode, String destinationCode) {
    return flightRepository.searchFlight(originCode, destinationCode);
  }

  public Uni<Boolean> getAvailableSeats(SearchFlight request) {
    return flightRepository.getAvailableSeats(request);
  }

  public Uni<List<TripClassPricesDto>> allFlightsGroupByTripClass() {
    return flightRepository.allFlightsGroupByTripClass();
  }

  public Uni<List<Flight>> findPricesByTripClass(int tripClass) {
    return flightRepository.findPricesByTripClass(tripClass);
  }

  public Uni<List<Flight>> getCheapestFlights() {
    return flightRepository.getCheapestFlights();
  }

  public Uni<List<Flight>> find(PaginationParams params) {
    return flightRepository.find(params);
  }

  public Uni<List<Flight>> getFlightsByDepartureDate(String departureDate) {
    return flightRepository.getFlightsByDepartureDate(departureDate);
  }

  public Uni<List<TopDestinationsDto>> getTopDestinations(){
    return flightRepository.getTopDestinations();
  }

  public Uni<List<Flight>> getFlightsUnderPrice(double price){
    return flightRepository.getFlightsUnderPrice(price);
  }

  public Uni<Void> cancelFlight(ObjectId flightId, String status){
    return flightRepository.cancelFlight(flightId, status);
  }

  public Uni<List<FlightDto>> searchFlight(SearchFlight request) {
    return flightRepository.searchFlight(request);
  }

}
