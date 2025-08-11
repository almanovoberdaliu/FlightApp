package org.flighApp.Flight.repo;

import io.quarkus.logging.Log;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.flighApp.Flight.models.Aircraft;
import org.flighApp.Flight.models.Flight;
import org.flighApp.Flight.models.Seats;
import org.flighApp.Flight.models.TripClass;
import org.flighApp.mongoDb.MongoDB;
import org.flighApp.mongoDb.MongoDbUtil;

import javax.swing.text.Document;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RandomFlightRepository {

    @Inject
    MongoDB mongoDbRepository;

    public ReactiveMongoCollection<Flight> getFlightsCollection() {
        return mongoDbRepository.getCollection("Flight", Flight.class);
    }

    private io.smallrye.mutiny.subscription.Cancellable cancellable;

    private final Random random = new Random();
    private final List<String> airports = Arrays.asList("TGD", "IST", "VIE", "FRA", "LHR");


    @PostConstruct
    public void init() {
        Multi.createFrom()
                .ticks()
                .every(Duration.ofSeconds(5))
                .subscribe()
                .with(tick -> {
                    Flight flight = generateRandomFlight();

                    MongoDbUtil.insertOne(getFlightsCollection(), flight);

                    Log.info("Flight added: " + flight);
                });
    }

    public Uni<List<Flight>> getAllRandomFlights() {
        return MongoDbUtil.find(getFlightsCollection(), Flight.class);
    }


//    @PreDestroy
//    public void destroy() {
//        cancellable.cancel();
//        flights.clear();
//    }

//    public Uni<List<Flight>> getRandomFlights() {
//
//    }

    private static AtomicInteger counter = new AtomicInteger(1);

    private Flight generateRandomFlight() {
        int id = counter.getAndIncrement();

        String origin = airports.get(random.nextInt(airports.size()));
        String destination;
        do {
            destination = airports.get(random.nextInt(airports.size()));
        } while (destination.equals(origin));

        double priceOneWay = 50 + random.nextInt(200);
        double priceReturn = 50 + random.nextInt(200);

        Flight flight = new Flight();
        flight.set_id(id);
        flight.setTicketType("ROUND_TRIP");
        flight.setOriginCode(origin);
        flight.setDestinationCode(destination);
        flight.setPriceForOneWay(priceOneWay);
        flight.setPriceForReturn(priceReturn);
        flight.setDepartureDate(Date.from(Instant.now().plus(random.nextInt(30), ChronoUnit.DAYS)));
        flight.setReturnDate(Date.from(Instant.now().plus(random.nextInt(60), ChronoUnit.DAYS)));
        flight.setDuration("2h 30m");
        flight.setStatus("available");

        List<Seats> seatsList = new ArrayList<>();
        seatsList.add(new Seats(TripClass.ECONOMY, random.nextInt(50) + 1));
        seatsList.add(new Seats(TripClass.BUSINESS, random.nextInt(20) + 1));
        seatsList.add(new Seats(TripClass.FIRST, random.nextInt(10) + 1));
        flight.setSeatsList(seatsList);

        List<Aircraft> aircrafts = new ArrayList<>();
        aircrafts.add(new Aircraft(101, "Airbus A320", "4O-AOB"));
        aircrafts.add(new Aircraft(102, "Boeing 737", "4O-BOE"));
        flight.setAircrafts(aircrafts);

        return flight;
    }

}
