package org.flighApp.Flight.repo;

import io.quarkus.logging.Log;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.flighApp.Flight.models.Flight;
import org.flighApp.Flight.models.TripClass;
import org.flighApp.mongoDb.MongoDB;
import org.flighApp.mongoDb.MongoDbUtil;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
public class TripClassRepository {
    @Inject
    MongoDB mongoDbRepository;

    public ReactiveMongoCollection<TripClass> getTripClassCollection() {
        return mongoDbRepository.getCollection("TripClass", TripClass.class);
    }

    public Uni<List<TripClass>> findAll() {
        return MongoDbUtil.find(getTripClassCollection());
    }

    public Uni<Void> insert(TripClass tripClass) {
        return MongoDbUtil.insertOne(getTripClassCollection(), tripClass)
                .invoke(() -> Log.info("Trip Class inserted successfully"))
                .onFailure()
                .invoke(error -> Log.error("Insertion failed", error));
    }
}
