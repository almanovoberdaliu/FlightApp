package org.flighApp.Airport.repo;


import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.flighApp.Airport.model.Airport;
import org.flighApp.mongoDb.MongoDB;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
public class AirportRepository {

    @Inject
    MongoDB mongoDbRepository;

    public ReactiveMongoCollection<Airport> getAirportCollection() {
        return mongoDbRepository.getCollection("Airport", Airport.class);
    }

    public Uni<List<Airport>> findAll() {
        return getAirportCollection()
                .find()
                .collect()
                .asList();
    }

    public Uni<Void> insert(Airport airport) {
        return getAirportCollection()
                .insertOne(airport)
                .onFailure().invoke(t -> {
                    System.out.println("Gabim gjate insertimit: " + t.getMessage());
                })
                .replaceWithVoid();
    }
}
