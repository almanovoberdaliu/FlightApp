package org.flighApp.Airport.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import org.flighApp.Airport.model.Airport;
import org.flighApp.Airport.repo.AirportRepository;

@ApplicationScoped
public class AirportService {

  @Inject AirportRepository airportRepository;

  public Uni<List<Airport>> getAllAirports() {
    return airportRepository.findAll();
  }

  public Uni<Void> insert(Airport airport) {
    return airportRepository.insert(airport);
  }
}
