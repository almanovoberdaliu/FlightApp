package org.flighApp.Flight.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
//import org.flighApp.Flight.dto.FlightDto;
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
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<List<Flight>> getFlights() {
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
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<List<Flight>> getFlightsByOriginCode(@QueryParam("keyword") String keyword) {
    return flightService.getFlightsByOriginCode(keyword);
  }

  @GET
  @Path("/search")
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<List<Flight>> searchFlight(
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
  @Produces(MediaType.APPLICATION_JSON)
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
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<List<TripClassPricesDto>> allFlightsGroupByTripClass() {
    return flightService.allFlightsGroupByTripClass();
  }

  @GET
  @Path("/prices-by-trip-class")
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<List<Flight>> findPricesByTripClass(@QueryParam("tripClass") String tripClass) {
    return flightService.findPricesByTripClass(tripClass);
  }

  @GET
  @Path("/cheapest-flights")
  public Uni<List<Flight>> getCheapestFlights() {
    return flightService.getCheapestFlights();
  }
}
