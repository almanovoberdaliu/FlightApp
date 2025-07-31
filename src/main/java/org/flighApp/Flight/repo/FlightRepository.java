package org.flighApp.Flight.repo;

import com.mongodb.client.model.Filters;
import io.quarkus.logging.Log;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.flighApp.Flight.dto.FlightDto;
import org.flighApp.Flight.models.*;
import org.flighApp.mongoDb.MongoDB;
import org.flighApp.mongoDb.MongoDbUtil;
import org.flighApp.utils.Utils;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
public class FlightRepository {

  @Inject MongoDB mongoDbRepository;

  public ReactiveMongoCollection<Flight> getFlightsCollection() {
    return mongoDbRepository.getCollection("Flight", Flight.class);
  }

  public Uni<List<Flight>> findAll() {
    return getFlightsCollection().find().collect().asList();
  }

  public Uni<Void> insert(FlightDto flight) {
    return getFlightsCollection()
        .insertOne(flight)
        .onFailure()
        .invoke(
            t -> {
              Log.info("Gabim gjate insertimit: " + t.getMessage());
            })
        .replaceWithVoid();
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
    Bson availableSeatsFilter =
        Filters.gte(Flight.Fields.availableSeats, request.getPassengersCount());
    Bson finalFilter =
        Filters.and(
            originCodeFilter, destinationCodeFilter, departureDateFilter, availableSeatsFilter);

    return MongoDbUtil.exists(getFlightsCollection(),finalFilter);
  }

  public Uni<List<Flight>> findPricesByTripClass() {
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
                new Document("$project", new Document("flights", 1L))))
        .collect()
        .asList();
  }

  //    public Uni<List<Flight>> findFlightsByTripType(SearchFlight request) {
  //
  //        List<Document> departureDoc = Arrays.asList(
  //                new Document("$match",
  //                new Document("originCode", request.getOriginCode())
  //                        .append("destinationCode", request.getDestinationCode())
  //                        .append("departureDate", request.getDepartureDate())));
  //
  //        if(request.getTicketType().equalsIgnoreCase("return")) {
  //            List<Document> returnDoc = Arrays.asList(
  //                    new Document("$match",
  //                            new Document("originCode", request.getDestinationCode())
  //                                    .append("destinationCode", request.getOriginCode())
  //                                    .append("departureDate", request.getDepartureDate())
  //                                    .append("returnDate", request.getReturnDate()))
  //            );
  //
  //            Uni<List<Flight>> departureFlights = getFlightsCollection()
  //                    .aggregate(departureDoc)
  //                    .collect()
  //                    .asList();
  //
  //            Uni<List<Flight>> returnFlights = getFlightsCollection()
  //                    .aggregate(returnDoc)
  //                    .collect()
  //                    .asList();
  //
  //            return Uni.combine().all().unis(departureFlights, returnFlights)
  //                    .with((dep, ret) -> {
  //                        List<Flight> all = new ArrayList<>();
  //                        all.addAll(dep);
  //                        all.addAll(ret);
  //                        return all;
  //                    });
  //        }
  //        else{
  //            return getFlightsCollection()
  //                    .aggregate(departureDoc)
  //                    .collect()
  //                    .asList();
  //        }
  //
  //    }

  //    public Uni<List<SearchResultDto>> findByTripType(SearchFlight search) {
  //        Document departureFilter = new Document();
  //
  //        departureFilter.append("originCode", search.getOriginCode());
  //        departureFilter.append("destinationCode", search.getDestinationCode());
  //        departureFilter.append("departureDate", search.getDepartureDate());
  //
  //        Uni<List<Flight>> departureFlights = getFlightsCollection()
  //                .find(departureFilter)
  //                .collect()
  //                .asList();
  //
  //        if ("return".equalsIgnoreCase(search.getTicketType())) {
  //            Document returnFilter = new Document();
  //
  //            returnFilter.append("originCode", search.getDestinationCode());
  //            returnFilter.append("destinationCode", search.getOriginCode());
  //            returnFilter.append("departureDate", search.getDepartureDate());
  //
  //            Uni<List<Flight>> returnFlights = getFlightsCollection()
  //                    .find(returnFilter)
  //                    .collect()
  //                    .asList();
  //
  //
  //            return Uni.combine().all().unis(departureFlights, returnFlights)
  //                    .with((dep, ret) -> {
  //                        SearchResultDto result = new SearchResultDto();
  //                        result.setDepartureFlights(dep);
  //                        result.setReturnFlights(ret);
  //                        return List.of(result);
  //                    });
  //        } else {
  //            return departureFlights.map(flights -> {
  //                SearchResultDto result = new SearchResultDto();
  //                result.setDepartureFlights(flights);
  //                result.setReturnFlights(Collections.emptyList());
  //                return List.of(result);
  //            });
  //        }
  //    }

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
}
