package org.flighApp.Booking.services;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.bson.types.ObjectId;
import org.flighApp.Booking.models.Booking;
import org.flighApp.Booking.models.BookingStatus;
import org.flighApp.Booking.repo.BookingRepository;

@ApplicationScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class BookingService {

  @Inject BookingRepository bookingRepository;

  public Uni<Void> createBooking(Booking booking, ObjectId flightId) {
    return bookingRepository.createBooking(booking, flightId);
  }
  public Uni<String> cancelBooking(int id) {
    return bookingRepository.cancelBooking(id);
  }
  public Uni<Void> confirmBooking(int id) {
    return bookingRepository.confirmBooking(id);
  }
  public Uni<Void> changeStatus(BookingStatus newStatus, int id){
    return bookingRepository.changeStatus(newStatus, id);
  }
}
