package com.service.shop.controller.resp;

public class AddressResp {
    private Integer number;
    private String addressLine;
    private String city;
    private String state;
    private String country;
    private String contact;
    private String postCode;
    private GeoLocationResp location;

    public AddressResp(Integer number, String addressLine, String city, String state, String country, String contact, String postCode, GeoLocationResp location) {
        this.number = number;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.country = country;
        this.contact = contact;
        this.postCode = postCode;
        this.location = location;
    }

    public AddressResp() {
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

    public GeoLocationResp getLocation() {
        return location;
    }

    public void setLocation(GeoLocationResp location) {
        this.location = location;
    }

}
