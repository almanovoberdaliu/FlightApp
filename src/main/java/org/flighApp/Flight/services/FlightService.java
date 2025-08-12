package org.flighApp.Flight.services;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.flighApp.Flight.dto.FlightDto;
import org.flighApp.Flight.dto.TopDestinationsDto;
import org.flighApp.Flight.dto.TripClassPricesDto; 
import org.flighApp.Flight.models.*;
import org.flighApp.Flight.repo.FlightRepository;
import org.flighApp.utils.Utils;

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

  public Uni<List<Flight>> findPricesByTripClass(String tripClass) {
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

  public Uni<List<TopDestinationsDto>> getTopDestinations() {
    return flightRepository.getTopDestinations();
  }

  public Uni<List<Flight>> getFlightsUnderPrice(double price) {
    return flightRepository.getFlightsUnderPrice(price);
  }

  public Uni<Void> cancelFlight(ObjectId flightId, String status) {
    return flightRepository.cancelFlight(flightId, status);
  }

  public Uni<List<FlightDto>> searchFlight(SearchFlight request) {
    return flightRepository.searchFlight(request);
  }

  public Uni<List<FlightDto>> searchAndCalculateTotalPrice(SearchFlight request) {
    return flightRepository
        .searchFlight(request)
        .onItem()
        .transform(
            flights -> {
              if (Utils.isNull(flights) || flights.isEmpty()) {
                throw new NotFoundException("Flight not found");
              }

              return flights.stream()
                  .peek(
                      flight -> {
                        double ticketPrice =
                            "return".equalsIgnoreCase(request.getTicketType())
                                ? flight.getPriceForReturn() + flight.getPriceForOneWay()
                                : flight.getPriceForOneWay();

                        int passengerCount = request.getPassengersCount();

                        double bagTotal = request.getBags().stream()
                                .mapToDouble(Bag::getTotalPrice)
                                .sum();

                        double total = (ticketPrice * passengerCount) + bagTotal;
                        flight.setTotalPrice(total);
                      })

                  .collect(Collectors.toList());
            });
  }

}
