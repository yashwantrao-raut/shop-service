package com.service.shop.controller.resp;

public class GeoLocationResp {
    public  static  String POINNT="point";
    private String type;
    private double lan;
    private double lat;
    public GeoLocationResp(String type, double lan, double lat) {
        this.type = type;
        this.lan = lan;
        this.lat = lat;
    }

    public GeoLocationResp() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
