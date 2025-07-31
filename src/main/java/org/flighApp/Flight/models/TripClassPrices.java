package org.flighApp.Flight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TripClassPrices {
    @BsonProperty("_id")
    private String tripClass;
    private List<Double> oneWayPrice;
    private List<Double> returnPrice;
    private Flight flight;
}
