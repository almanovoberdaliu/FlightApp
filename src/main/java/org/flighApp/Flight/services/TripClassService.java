package org.flighApp.Flight.services;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.flighApp.Flight.models.TripClass;
import org.flighApp.Flight.repo.FlightRepository;
import org.flighApp.Flight.repo.TripClassRepository;
import org.flighApp.mongoDb.MongoDbUtil;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
public class TripClassService {

    @Inject
    TripClassRepository tripClassRepository;

    public Uni<List<TripClass>> findAll() {
        return tripClassRepository.findAll();
    }

    public Uni<Void> insert(TripClass tripClass) {
        return tripClassRepository.insert(tripClass);
    }
}
