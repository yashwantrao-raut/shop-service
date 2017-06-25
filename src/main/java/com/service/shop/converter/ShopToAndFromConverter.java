package com.service.shop.converter;

import com.service.shop.controller.req.AddressReq;
import com.service.shop.controller.req.ShopReq;
import com.service.shop.controller.resp.AddressResp;
import com.service.shop.controller.resp.GeoLocationResp;
import com.service.shop.controller.resp.ShopResp;
import com.service.shop.mongo.document.Address;
import com.service.shop.mongo.document.Shop;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

@Component
public class ShopToAndFromConverter {

    public Shop convertToShop(ShopReq shopReq, double lan, double lat) {
        GeoJsonPoint point = new GeoJsonPoint(lan, lat);
        AddressReq addressReq = shopReq.getAddress();
        Address address = new Address(addressReq.getNumber(), addressReq.getAddressLine(), addressReq.getCity(),
                addressReq.getState(), addressReq.getCountry(), addressReq.getContact(), addressReq.getPostCode(), point);
        return new Shop(shopReq.getName(), address);
    }

    public ShopResp convertFromShop(Shop shop) {
        Address address = shop.getAddress();
        double lan = address.getPoint().getCoordinates().get(0);
        double lat = address.getPoint().getCoordinates().get(1);
        GeoLocationResp geoLocationResp = new GeoLocationResp(GeoLocationResp.POINNT, lan, lat);
        AddressResp addressResp = new AddressResp(address.getNumber(), address.getAddressLine(), address.getCity(), address.getState(), address.getCountry(), address.getContact(), address.getPostCode(), geoLocationResp);
        ShopResp shopResp = new ShopResp(shop.getName(), addressResp);
        return shopResp;
    }


}
