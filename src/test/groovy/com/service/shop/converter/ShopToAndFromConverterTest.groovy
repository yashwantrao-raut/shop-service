package com.service.shop.converter

import com.service.shop.controller.req.AddressReq
import com.service.shop.controller.req.ShopReq
import com.service.shop.controller.resp.ShopResp
import com.service.shop.mongo.document.Address
import com.service.shop.mongo.document.Shop
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import spock.lang.Specification

class ShopToAndFromConverterTest extends Specification {
    ShopToAndFromConverter converter

    void setup() {
        converter = new ShopToAndFromConverter()
    }

    def "should convert to Shop object from shopReq"() {
        given:
        def addressReq = new AddressReq(addressLine: "address line", number: 123, city: "city", state: "state", country: "india", postCode: "1234")
        def shopReq = new ShopReq(name: "shop 1", address: addressReq)
        def lan = 11.22
        def lat = 22.33
        when:
        def result = converter.convertToShop(shopReq, lan, lat)

        then:
        result.class == Shop
        result.name == shopReq.name
        result.address.addressLine == addressReq.addressLine
        result.address.number == addressReq.number
        result.address.city == addressReq.city
        result.address.contact == addressReq.contact
        result.address.state == addressReq.state
        result.address.country == addressReq.country
        result.address.postCode == addressReq.postCode
        result.address.point.class == GeoJsonPoint
        def coordinates = result.address.point.coordinates
        coordinates.first() == lan
        coordinates.last() == lat
    }

    def "should convert from Shop to ShopResp"() {

        given:
        def lan = 11.22
        def lat = 22.33
        def point = new GeoJsonPoint(lan, lat)
        def address = new Address(
                addressLine: "address line",
                number: 123,
                city: "city",
                state: "state",
                country: "india",
                postCode: "1234",
                point: point
        )

        def shop = new Shop(name: "shop 1", address: address)

        when:
        def result = converter.convertFromShop(shop)

        then:
        result.class == ShopResp
        result.name == shop.name
        result.address.addressLine == address.addressLine
        result.address.number == address.number
        result.address.city == address.city
        result.address.contact == address.contact
        result.address.state == address.state
        result.address.country == address.country
        result.address.postCode == address.postCode
        result.address.location.type == result.address.location.POINNT
        result.address.location.lan == lan
        result.address.location.lat == lat

    }
}
