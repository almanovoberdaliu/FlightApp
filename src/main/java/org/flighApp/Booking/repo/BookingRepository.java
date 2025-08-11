package org.flighApp.Booking.repo;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.conversions.Bson;
import org.flighApp.Booking.models.Booking;
import org.flighApp.Booking.models.BookingStatus;
import org.flighApp.Flight.models.Flight;
import org.flighApp.mongoDb.MongoDB;
import org.flighApp.mongoDb.MongoDbUtil;

@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingRepository {

  @Inject MongoDB mongoRepository;

  public ReactiveMongoCollection<Booking> getBookingsCollection() {
    return mongoRepository.getCollection("Booking", Booking.class);
  }

  public ReactiveMongoCollection<Flight> getFlightsCollection() {
    return mongoRepository.getCollection("Flight", Flight.class);
  }

  public Uni<Void> createBooking(Booking booking, int flightId) {
    Bson filter = Filters.eq("_id", flightId);
    return MongoDbUtil.findFirst(getFlightsCollection(), filter)
        .onItem()
        .ifNull()
        .fail()
        .replaceWith("No flight found!!")
        .onItem()
        .transformToUni(flight -> MongoDbUtil.insertOne(getBookingsCollection(), booking));
  }

  public Uni<Void> cancelBooking(int id) {
    Bson filter = Filters.eq("id", id);
    return MongoDbUtil.findFirst(getBookingsCollection(), filter)
        .onItem()
        .ifNotNull()
        .transform(
            booking -> {
              if (booking.getStatus() != BookingStatus.COMPLETED) {
                Bson update = Updates.set(Booking.Fields.status, BookingStatus.CANCELLED);
                return MongoDbUtil.updateOne(getBookingsCollection(), filter, update);
              }
              return Uni.createFrom().voidItem();
            })
        .replaceWithVoid();
  }


}
