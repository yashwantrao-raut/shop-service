package com.service.shop.geo;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class GeoService {
    RestTemplate restTemplate;
    final String uri = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    public GeoService() {
        restTemplate = new RestTemplate();
    }

    public GeoResponse find(String  formattedAddress) {
         return restTemplate.getForObject(uri +formattedAddress , GeoResponse.class);
    }
}
