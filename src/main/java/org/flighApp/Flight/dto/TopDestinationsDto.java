package org.flighApp.Flight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopDestinationsDto {
    @BsonProperty("_id")
    private String destinationCode;

    @BsonProperty("count")
    private int count;
}
