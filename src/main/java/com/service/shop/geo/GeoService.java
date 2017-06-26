package com.service.shop.geo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class GeoService {
    private RestTemplate restTemplate;
    private String uri;

    @Autowired
    public GeoService(RestTemplate restTemplate, @Value("${geocoding.service.url}")String uri) {
        this.restTemplate = restTemplate;
        this.uri = uri;
    }

    public GeoResponse find(String  formattedAddress) {
         return restTemplate.getForObject(uri +formattedAddress , GeoResponse.class);
    }
}
