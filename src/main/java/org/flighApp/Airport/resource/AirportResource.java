package org.flighApp.Airport.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.flighApp.Airport.model.Airport;
import org.flighApp.Airport.service.AirportService;

@Path("/airport")
public class AirportResource {

  @Inject AirportService airportService;

  @GET
  @Path("/get")
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<List<Airport>> getAirports() {
    return airportService.getAllAirports();
  }

  @POST
  @Path("/add")
  @Consumes(MediaType.APPLICATION_JSON)
  public Uni<Void> insertAirport(Airport airport) {
    return airportService.insert(airport);
  }
}
