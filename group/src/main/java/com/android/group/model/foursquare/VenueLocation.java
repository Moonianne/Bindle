package com.android.group.model.foursquare;

import java.util.List;

public class VenueLocation {

    private String address;
    private String crossStreet;
    private double lat;
    private double lng;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private List formattedAddress;

    public VenueLocation(final String address,
                         final String crossStreet,
                         final double lat,
                         final double lng,
                         final String postalCode,
                         final String city, String state,
                         final String country,
                         final List formattedAddress) {
        this.address = address;
        this.crossStreet = crossStreet;
        this.lat = lat;
        this.lng = lng;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.formattedAddress = formattedAddress;
    }


    public String getAddress() {
        return address;
    }

    public String getCrossStreet() {
        return crossStreet;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public List getFormattedAddress() {
        return formattedAddress;
    }
}
