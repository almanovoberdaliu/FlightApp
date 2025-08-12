package org.flighApp.Booking.resources;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.flighApp.Booking.models.Booking;
import org.flighApp.Booking.models.BookingStatus;
import org.flighApp.Booking.services.BookingService;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Path("/booking")
public class BookingResource {
  @Inject BookingService bookingService;

  @POST
  @Path("/create-booking")
  public Uni<Void> createBooking(Booking booking, @QueryParam("flightId") String flightId) {
    ObjectId objectId = new ObjectId(flightId);
    return bookingService.createBooking(booking, objectId);
  }

  @POST
  @Path("/cancel-booking")
  public Uni<String> cancelBooking(@QueryParam("id") int id) {
    return bookingService.cancelBooking(id);
  }

  @GET
  @Path("/confirm-booking")
  public Uni<Void> confirmBooking(@QueryParam("id") int id) {
    return bookingService.confirmBooking(id);
  }

  @GET
  @Path("/status")
  public Uni<Void> changeStatus(@QueryParam("newStatus") BookingStatus newStatus, @QueryParam("id") int id) {
    return bookingService.changeStatus(newStatus, id);
  }
}
