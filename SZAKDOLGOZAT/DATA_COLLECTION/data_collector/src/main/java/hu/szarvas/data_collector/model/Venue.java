package hu.szarvas.data_collector.model;

import lombok.Data;

@Data
public class Venue {
    private String venueName;
    private String venueAddress;
    private String venueCity;
    private String venueCapacity;
    private String venueSurface;
}
