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
import org.bson.types.ObjectId;
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

  // duhet me kontrollu nese availableSeat eshte me e madhe se 0, per me kriju booking
  public Uni<Void> createBooking(Booking booking, ObjectId flightId) {
    Bson filter = Filters.eq("_id", flightId);
    return MongoDbUtil.findFirst(getFlightsCollection(), filter)
        .onItem()
        .ifNull()
        .fail()
        .replaceWith("No flight found!!")
        .onItem()
        .transformToUni(
            flight ->
                MongoDbUtil.insertOne(getBookingsCollection(), booking)
                    .chain(
                        () -> {
                          Bson seatFilter =
                              Filters.and(
                                  Filters.eq("_id", flightId),
                                  Filters.eq("seatsList.tripClass", booking.getTripClass()));
                          Bson seatUpdate =
                              Updates.inc(
                                  "seatsList.$.availableSeats", -booking.getTotalPassengers());

                          return getFlightsCollection()
                              .updateOne(seatFilter, seatUpdate)
                              .replaceWithVoid();
                        }));
  }

  // duhet me kontrollu nese available seat eshte me e madhe se 0, per me leju me bo cancel
    public Uni<String> cancelBooking(int id) {
        Bson filter = Filters.eq("_id", id);
        return MongoDbUtil.findFirst(getBookingsCollection(), filter)
                .onItem()
                .ifNotNull()
                .transformToUni(
                        booking -> {
                            if (booking.getStatus() != BookingStatus.COMPLETED) {
                                Bson update = Updates.set(Booking.Fields.status, BookingStatus.CANCELLED);
                                return MongoDbUtil.updateOne(getBookingsCollection(), filter, update)
                                        .chain(
                                                () -> {
                                                    Bson seatFilter =
                                                            Filters.and(
                                                                    Filters.eq("_id", booking.getFlightId()),
                                                                    Filters.eq("seatsList.tripClass", booking.getTripClass()));

                                                    Bson updateSeats =
                                                            Updates.inc(
                                                                    "seatsList.$.availableSeats", booking.getTotalPassengers());
                                                    return MongoDbUtil.updateOne(getFlightsCollection(), seatFilter, updateSeats);
                                                })
                                        .replaceWith("Booking cancelled successfully.");
                            }
                            return Uni.createFrom().item("Booking is already completed, you cannot cancel it!!");
                        })
                .onItem()
                .ifNull()
                .continueWith("Booking not found");
    }


  public Uni<Void> confirmBooking(int id) {
    Bson filter = Filters.eq("_id", id);
    Bson update = Updates.set(Booking.Fields.status, BookingStatus.CONFIRMED);
    return MongoDbUtil.updateOne(getBookingsCollection(), filter, update).replaceWithVoid();
  }

  public Uni<Void> changeStatus(BookingStatus newStatus, int id) {
    Bson filter = Filters.eq("_id", id);
    Bson update = Updates.set(Booking.Fields.status, newStatus);

    return MongoDbUtil.updateOne(getBookingsCollection(), filter, update).replaceWithVoid();
  }

}
