package org.flighApp.Flight.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.flighApp.Flight.models.TripClass;
import org.flighApp.Flight.repo.TripClassRepository;
import org.flighApp.Flight.services.TripClassService;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Path("/classes")
public class TripClassResource {

    @Inject
    TripClassService tripClassService;

    @GET
    @Path("/find")
    public Uni<List<TripClass>> findAll() {
        return tripClassService.findAll();
    }

    @POST
    @Path("/insert")
    public Uni<Void> insert(TripClass tripClass) {
        return tripClassService.insert(tripClass);
    }
}
