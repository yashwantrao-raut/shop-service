package com.service.shop.controller;

import com.service.shop.controller.formatter.GeocodingAddressFormatter;
import com.service.shop.controller.req.ShopReq;
import com.service.shop.controller.resp.ConflictResponse;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("shops")
public class ShopController {
    private GeocodingAddressFormatter geocodingAddressFormatter;
    private ShopToAndFromConverter shopToAndFromConverter;
    private GeoService geoService;
    private ShopRepository shopRepository;
    private Predicate<GeoResponse> isValidGeoResponse = response -> "OK".equals(response.getStatus());
    private  Predicate<GeoResponse> isZeoResult = response -> "ZERO_RESULTS".equals(response.getStatus());
    private Predicate<GeoResponse> isOnlyoneResultFound = response -> response.getResults().size() == 1;

    @Autowired
    public ShopController(GeocodingAddressFormatter geocodingAddressFormatter, ShopToAndFromConverter shopToAndFromConverter, GeoService geoService, ShopRepository shopRepository) {
        this.geocodingAddressFormatter = geocodingAddressFormatter;
        this.shopToAndFromConverter = shopToAndFromConverter;
        this.geoService = geoService;
        this.shopRepository = shopRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addShop(@RequestBody ShopReq shopReq) {
        String formattedAddress = geocodingAddressFormatter.format(shopReq.getAddress());
        GeoResponse geoResponse = geoService.find(formattedAddress);
        if (isValidGeoResponse.test(geoResponse)) {
            if(isOnlyoneResultFound.test(geoResponse)){
                Location location = geoResponse.getResults().get(0).getGeometry().getLocation();
                Shop shop = shopToAndFromConverter.convertToShop(shopReq, location.getLng(), location.getLat());
                Optional<Shop> anyModified = shopRepository.findAndModify(shop, shop.getName());
                if (anyModified.isPresent()) {
                    return ResponseEntity.ok(shopToAndFromConverter.convertFromShop(anyModified.get()));
                }
                return ResponseEntity.status(201).build();
            }
            List<String> addresses = geoResponse.getResults().stream().map(result -> result.getFormattedAddress()).collect(Collectors.toList());
            ConflictResponse<String,String> conflictResponse= new ConflictResponse<>(Arrays.asList("Multiple matching address found"),addresses);
            return ResponseEntity.status(409).body(conflictResponse);

        }
        else if(isZeoResult.test(geoResponse)){
            return ResponseEntity.status(400).body("No matching geolocation found for address");
        }
        return ResponseEntity.status(502).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findNear(@RequestParam Double lan, @RequestParam Double lat) {
        Shop near = shopRepository.findByAddressPointNear(new GeoJsonPoint(lan, lat));
        if(near!=null) {
            return ResponseEntity.ok(shopToAndFromConverter.convertFromShop(near));
        }
        return ResponseEntity.status(404).build();
    }

}
