package org.flighApp.Booking.services;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.flighApp.Booking.models.Booking;
import org.flighApp.Booking.repo.BookingRepository;

public class BookingService {

    @Inject
    BookingRepository bookingRepository;


}
