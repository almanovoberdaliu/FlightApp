package org.flighApp.Flight.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.flighApp.Flight.dto.FlightDto;
import org.flighApp.Flight.dto.TopDestinationsDto;
import org.flighApp.Flight.dto.TripClassPricesDto;
import org.flighApp.Flight.models.Flight;
import org.flighApp.Flight.models.PaginationParams;
import org.flighApp.Flight.models.SearchFlight;
import org.flighApp.Flight.services.FlightService;

@Path("/flights")
public class FlightResource {

  @Inject FlightService flightService;

  @GET
  @Path("/get")
  public Uni<List<FlightDto>> getFlights() {
    return flightService.getAllFlights();
  }

  @POST
  @Path("/add")
  @Consumes(MediaType.APPLICATION_JSON)
  public Uni<Void> addFlight(Flight flight) {
    return flightService.addFlight(flight);
  }

  @GET
  @Path("/getByOriginCode")
  public Uni<List<FlightDto>> getFlightsByOriginCode(@QueryParam("keyword") String keyword) {
    return flightService.getFlightsByOriginCode(keyword);
  }

  @GET
  @Path("/search")
  public Uni<List<FlightDto>> searchFlight(
      @QueryParam("originCode") String originCode,
      @QueryParam("destinationCode") String destinationCode) {
    return flightService.searchFlight(originCode, destinationCode);
  }

  @POST
  @Path("/find-with-pagination")
  public Uni<List<Flight>> find(PaginationParams params) {
    return flightService.find(params);
  }

  //  @POST
  //  @Path("/total")
  //  @Consumes(MediaType.APPLICATION_JSON)
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public Uni<Integer> getTotalPassengers(SearchFlight request) {
  //    return flightService.getTotalPassengers(request);
  //  }

  @POST
  @Path("/available")
  @Consumes(MediaType.APPLICATION_JSON)
  public Uni<Boolean> getAvailableSeats(SearchFlight request) {
    return flightService.getAvailableSeats(request);
  }

  //    @POST
  //    @Path("/tripType")
  //    @Consumes(MediaType.APPLICATION_JSON)
  //    @Produces(MediaType.APPLICATION_JSON)
  //    public Uni<List<Flight>> findFlightsByTripType(SearchFlight request) {
  //        return flightService.findFlightsByTripType(request);
  //    }

  //    @POST
  //    @Path("/tripType")
  //    @Consumes(MediaType.APPLICATION_JSON)
  //    @Produces(MediaType.APPLICATION_JSON)
  //    public Uni<List<SearchResultDto>> findByTripType(SearchFlight search) {
  //        return flightService.findByTripType(search);
  //    }

  @GET
  @Path("/all-flights-group-by-tripClass")
  public Uni<List<TripClassPricesDto>> allFlightsGroupByTripClass() {
    return flightService.allFlightsGroupByTripClass();
  }

  @GET
  @Path("/prices-by-trip-class")
  public Uni<List<Flight>> findPricesByTripClass(@QueryParam("tripClass") int tripClass) {
    return flightService.findPricesByTripClass(tripClass);
  }

  @GET
  @Path("/cheapest-flights")
  public Uni<List<Flight>> getCheapestFlights() {
    return flightService.getCheapestFlights();
  }

  @GET
  @Path("/get-flight-by-date")
  public Uni<List<Flight>> getFlightsByDepartureDate(@QueryParam("departureDate") String departureDate) {
    return flightService.getFlightsByDepartureDate(departureDate);
  }

  @GET
  @Path("/get-top-destinations")
  public Uni<List<TopDestinationsDto>> getTopDestinations(){
    return flightService.getTopDestinations();
  }

  @GET
  @Path("/get-flights-under-price")
  public Uni<List<Flight>> getFlightsUnderPrice(@QueryParam("price") double price){
    return flightService.getFlightsUnderPrice(price);
  }

  @POST
  @Path("/cancel-flight")
  public Uni<Void> cancelFlight(@QueryParam("flightId") ObjectId flightId,
                                @QueryParam("status") String status){
    return flightService.cancelFlight(flightId, status);
  }

  @POST
  @Path("/search-flights")
  public Uni<List<FlightDto>> searchFlight(SearchFlight request) {
    return flightService.searchFlight(request);
  }

}
