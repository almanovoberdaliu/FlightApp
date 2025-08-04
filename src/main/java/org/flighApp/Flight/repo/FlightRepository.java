package org.flighApp.Flight.repo;

import com.mongodb.client.model.Filters;
import io.quarkus.logging.Log;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
//import org.flighApp.Flight.dto.FlightDto;
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

  public Uni<List<Flight>> findAll() {
    return MongoDbUtil.find(getFlightsCollection());
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

  public Uni<List<Flight>> searchFlightByOriginCode(String keyword) {
    List<Bson> pipeline =
        List.of(
            new Document(
                "$match",
                new Document(
                    "originCode", new Document("$regex", "^" + keyword).append("$options", "i"))));

    return MongoDbUtil.aggregate(getFlightsCollection(), pipeline);
  }

  public Uni<Boolean> getAvailableSeats(SearchFlight request) {

    Bson originCodeFilter = Filters.eq(Flight.Fields.originCode, request.getOriginCode());
    Bson destinationCodeFilter =
        Filters.eq(Flight.Fields.destinationCode, request.getDestinationCode());
    Bson departureDateFilter = Filters.eq(Flight.Fields.departureDate, request.getDepartureDate());
    Bson seatsFilter = Filters.elemMatch(
            Flight.Fields.seatsList,
            Filters.and(
                    Filters.eq("tripClass", request.getTripClass()),
                    Filters.gte("availableSeats", request.getPassengersCount())
            )
    );
    Bson finalFilter =
        Filters.and(
            originCodeFilter, destinationCodeFilter, departureDateFilter,seatsFilter);

    return MongoDbUtil.exists(getFlightsCollection(), finalFilter);
  }

  public Uni<List<TripClassPricesDto>> allFlightsGroupByTripClass() {
    return getFlightsCollection()
        .aggregate(
            List.of(
                new Document(
                    "$group",
                    new Document("_id", "$tripClass")
                        .append("oneWayPrices", new Document("$push", "$priceForOneWay"))
                        .append("returnPrices", new Document("$push", "$priceForReturn"))),
                new Document(
                    "$lookup",
                    new Document("from", "Flight")
                        .append("localField", "_id")
                        .append("foreignField", "tripClass")
                        .append("as", "flights")),
                new Document(
                    "$project",
                    new Document("tripClass", "$_id")
                        .append("flights", "$flights")
                        .append("_id", 0))),
            TripClassPricesDto.class)
        .collect()
        .asList();
  }

  public Uni<List<Flight>> findPricesByTripClass(String tripClass) {
    Bson filter =
        Filters.elemMatch(Flight.Fields.seatsList, Filters.eq("tripClass", tripClass));
    return MongoDbUtil.find(getFlightsCollection(), filter);
  }

  public Uni<List<Flight>> searchFlight(String originCode, String destinationCode) {
    Document doc = new Document();

    if (originCode != null && !originCode.isEmpty()) {
      doc.append("originCode", new Document("$regex", "^" + originCode).append("$options", "i"));
    }

    if (destinationCode != null && !destinationCode.isEmpty()) {
      doc.append(
          "destinationCode", new Document("$regex", "^" + destinationCode).append("$options", "i"));
    }

    return getFlightsCollection()
        .aggregate(List.of(new Document("$match", doc)))
        .collect()
        .asList();
  }

  public Uni<List<Flight>> getCheapestFlights() {
    List<Bson> pipeline = List.of(new Document("$sort", new Document("priceForOneWay", 1L)));
    return MongoDbUtil.aggregate(getFlightsCollection(), pipeline);
  }

}
