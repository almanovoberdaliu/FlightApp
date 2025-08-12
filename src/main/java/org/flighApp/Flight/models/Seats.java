package org.flighApp.Flight.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
public class Seats {
  @BsonProperty("tripClass")
//  @BsonRepresentation(BsonType.STRING)
  private TripClass tripClass;
  private int availableSeats;
}
