package org.flighApp.Airport.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Airport {
    private String code;
    private String name;
    private String country;
    private String location;
}
