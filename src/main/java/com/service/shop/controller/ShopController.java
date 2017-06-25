package com.service.shop.controller;

import com.service.shop.controller.formatter.GeocodingAddressFormatter;
import com.service.shop.controller.req.ShopReq;
import com.service.shop.converter.ShopToAndFromConverter;
import com.service.shop.geo.GeoResponse;
import com.service.shop.geo.GeoService;
import com.service.shop.geo.Location;
import com.service.shop.mongo.ShopRepository;
import com.service.shop.mongo.document.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Predicate;

@RestController
@RequestMapping("shops")
public class ShopController {

    @Autowired
    private GeocodingAddressFormatter geocodingAddressFormatter;
    @Autowired
    private ShopToAndFromConverter shopToAndFromConverter;
    @Autowired
    private GeoService geoService;
    @Autowired
    private ShopRepository shopRepository;

    Predicate<GeoResponse> isValidGeoResponse = response -> "OK".equals(response.getStatus()) && response.getResults().size() == 1;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addShop(@RequestBody ShopReq shopReq) {
        String formattedAddress = geocodingAddressFormatter.format(shopReq.getAddress());
        GeoResponse geoResponse = geoService.find(formattedAddress);
        if (isValidGeoResponse.test(geoResponse)) {
            Location location = geoResponse.getResults().get(0).getGeometry().getLocation();
            Shop shop = shopToAndFromConverter.convertToShop(shopReq, location.getLng(), location.getLat());
            Optional<Shop> anyModified = shopRepository.findAndModify(shop, shop.getName());
            if(anyModified.isPresent()){
                return ResponseEntity.ok(shopToAndFromConverter.convertFromShop(anyModified.get()));
            }
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(409).body("Multiple geolocation founds");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getNear(@RequestParam Double lan, @RequestParam Double lat) {
        Shop near = shopRepository.findByAddressPointNear(new GeoJsonPoint(lan, lat));
        if(near!=null) {
            return ResponseEntity.ok(shopToAndFromConverter.convertFromShop(near));
        }
        return ResponseEntity.status(404).build();
    }

}
