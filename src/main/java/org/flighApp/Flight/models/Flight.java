package org.flighApp.Flight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class Flight {
  // this is built behind the scenes via @FieldsNameAndConstants
  //    public static final class Fields{
  //        public static final String originCode = "originCode";
  //    }
  @BsonId
  @BsonProperty("_id")
  private int _id;
  private String ticketType;
  private String originCode;
  private String destinationCode;
  private Double priceForOneWay;
  private Date departureDate;
  private Double priceForReturn;
  private Date returnDate;
  private String duration;
  private String status;
  public List<Seats> seatsList;
  public List<Aircraft> aircrafts;

  //private String originForReturn;
  //private String destinationForReturn;
}
