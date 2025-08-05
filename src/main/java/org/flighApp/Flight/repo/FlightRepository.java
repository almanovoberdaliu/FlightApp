package org.flighApp.Flight.repo;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.quarkus.logging.Log;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.flighApp.Flight.dto.FlightDto;
import org.flighApp.Flight.dto.TopDestinationsDto;
import org.flighApp.Flight.dto.TripClassPricesDto;
import org.flighApp.Flight.models.*;
import org.flighApp.mongoDb.MongoDB;
import org.flighApp.mongoDb.MongoDbUtil;

@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
public class FlightRepository {

  @Inject MongoDB mongoDbRepository;

  public ReactiveMongoCollection<Flight> getFlightsCollection() {
    return mongoDbRepository.getCollection("Flight", Flight.class);
  }

  public Uni<List<FlightDto>> findAll() {
    return MongoDbUtil.find(getFlightsCollection(), FlightDto.class);
    // getFlightsCollection().find().collect().asList();
  }

  public Uni<List<Flight>> find(PaginationParams params) {
    return MongoDbUtil.find(getFlightsCollection(), params);
  }


  public Uni<Void> insert(Flight flight) {
    return MongoDbUtil.insertOne(getFlightsCollection(), flight)
        .invoke(() -> Log.info("Flight inserted successfully"))
        .onFailure()
        .invoke(error -> Log.error("Insertion failed", error));
  }

  public Uni<List<FlightDto>> searchFlightByOriginCode(String keyword) {
    List<Bson> pipeline =
        List.of(
            new Document(
                "$match",
                new Document(
                    "originCode", new Document("$regex", "^" + keyword).append("$options", "i"))));

    return MongoDbUtil.aggregate(getFlightsCollection(), pipeline, FlightDto.class);
  }

  public Uni<Boolean> getAvailableSeats(SearchFlight request) {

    Bson originCodeFilter = Filters.eq(Flight.Fields.originCode, request.getOriginCode());
    Bson destinationCodeFilter =
        Filters.eq(Flight.Fields.destinationCode, request.getDestinationCode());
    Bson departureDateFilter = Filters.eq(Flight.Fields.departureDate, request.getDepartureDate());
    Bson statusFilter = Filters.ne(Flight.Fields.status, "cancelled");

    Bson seatsFilter =
        Filters.elemMatch(
            Flight.Fields.seatsList,
            Filters.and(
                Filters.eq("tripClass", request.getTripClass()),
                Filters.gte("availableSeats", request.getPassengersCount())));

    Bson finalFilter =
        Filters.and(
            originCodeFilter,
            destinationCodeFilter,
            departureDateFilter,
            seatsFilter,
            statusFilter);

    return MongoDbUtil.exists(getFlightsCollection(), finalFilter);
  }

  public Uni<List<FlightDto>> searchFlight(SearchFlight request) {
                Bson originCodeFilter =
                    Filters.eq(Flight.Fields.originCode, request.getOriginCode());
                Bson destinationCodeFilter =
                    Filters.eq(Flight.Fields.destinationCode, request.getDestinationCode());
                Bson departureDateFilter =
                    Filters.eq(Flight.Fields.departureDate, request.getDepartureDate());
                Bson statusFilter = Filters.eq(Flight.Fields.status, "available");
                Bson seatsFilter =
                    Filters.elemMatch(
                        Flight.Fields.seatsList,
                        Filters.and(
                            Filters.eq("tripClass", request.getTripClass()),
                            Filters.gte("availableSeats", request.getPassengersCount())));

                Bson finalFilter =
                    Filters.and(
                        originCodeFilter,
                        destinationCodeFilter,
                        departureDateFilter,
                        seatsFilter,
                        statusFilter);

                return MongoDbUtil.find(getFlightsCollection(), finalFilter, FlightDto.class);
            };

  public Uni<List<TripClassPricesDto>> allFlightsGroupByTripClass() {
    return getFlightsCollection()
        .aggregate(
            List.of(
                new Document("$unwind", "$seatsList"),
                new Document(
                    "$group",
                    new Document("_id", "$seatsList.tripClass")
                        .append("flights", new Document("$addToSet", "$$ROOT"))),
                new Document(
                    "$project",
                    new Document()
                        .append("tripClass", new Document("$toString", "$_id"))
                        .append("flights", 1)
                        .append("_id", 0))),
            TripClassPricesDto.class)
        .collect()
        .asList();
  }

  public Uni<List<Flight>> findPricesByTripClass(int tripClass) {
    Bson filter = Filters.elemMatch(Flight.Fields.seatsList, Filters.eq("tripClass", tripClass));
    return MongoDbUtil.find(getFlightsCollection(), filter);
  }

  public Uni<List<FlightDto>> searchFlight(String originCode, String destinationCode) {
    Document doc = new Document();

    if (originCode != null && !originCode.isEmpty()) {
      doc.append("originCode", new Document("$regex", "^" + originCode).append("$options", "i"));
    }

    if (destinationCode != null && !destinationCode.isEmpty()) {
      doc.append(
          "destinationCode", new Document("$regex", "^" + destinationCode).append("$options", "i"));
    }

    return getFlightsCollection()
        .aggregate(List.of(new Document("$match", doc)), FlightDto.class)
        .collect()
        .asList();
  }

  public Uni<List<Flight>> getCheapestFlights() {
    List<Bson> pipeline = List.of(new Document("$sort", new Document("priceForOneWay", 1L)));
    return MongoDbUtil.aggregate(getFlightsCollection(), pipeline);
  }

  public Uni<List<Flight>> getFlightsByDepartureDate(String departureDate) {
    Bson filter = Filters.eq(Flight.Fields.departureDate, departureDate);
    return MongoDbUtil.find(getFlightsCollection(), filter);
  }

  public Uni<List<TopDestinationsDto>> getTopDestinations() {
    List<Bson> pipeline =
        List.of(
            new Document(
                "$group",
                new Document("_id", "$destinationCode").append("count", new Document("$sum", 1L))),
            new Document("$sort", new Document("count", -1L)),
            new Document("$limit", 2L));
    return MongoDbUtil.aggregate(getFlightsCollection(), pipeline, TopDestinationsDto.class);
  }

  public Uni<List<Flight>> getFlightsUnderPrice(double price) {
    Bson filter = Filters.lte(Flight.Fields.priceForOneWay, price);
    return MongoDbUtil.find(getFlightsCollection(), filter);
  }

  public Uni<Void> cancelFlight(ObjectId flightId, String status) {
    Bson filter = Filters.eq("_id", flightId);
    Bson update = Updates.set("status", status);
    return MongoDbUtil.updateOne(getFlightsCollection(), filter, update).replaceWithVoid();
  }
}
