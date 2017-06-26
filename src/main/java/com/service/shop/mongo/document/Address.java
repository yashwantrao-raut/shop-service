package com.service.shop.mongo.document;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

public class Address {
    private Integer number;
    private String addressLine;
    private String city;
    private String state;
    private String country;
    private String contact;
    private String postCode;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint point;
    public Address() {
    }
    public Address(Integer number, String addressLine, String city, String state, String country, String contact, String postCode, GeoJsonPoint point) {
        this.number = number;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.country = country;
        this.contact = contact;
        this.postCode = postCode;
        this.point = point;
    }


    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public GeoJsonPoint getPoint() {
        return point;
    }

    public void setPoint(GeoJsonPoint point) {
        this.point = point;
    }


}
