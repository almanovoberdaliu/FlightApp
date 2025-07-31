package org.flighApp.Flight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class Flight {
    //this is built behind the scenes via @FieldsNameAndConstants
//    public static final class Fields{
//        public static final String originCode = "originCode";

//    }
    private String ticketType;
    private String originCode;
    private String destinationCode;
    private Double priceForOneWay;
    private Date departureDate;
//    private String originForReturn;
//    private String destinationForReturn;
    private Double priceForReturn;
    private String tripClass;
    private Date returnDate;
    private String duration;
    private int availableSeats;

}
