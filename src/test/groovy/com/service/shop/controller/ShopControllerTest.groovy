package com.service.shop.controller

import com.service.shop.controller.formatter.GeocodingAddressFormatter
import com.service.shop.controller.req.AddressReq
import com.service.shop.controller.req.ShopReq
import com.service.shop.converter.ShopToAndFromConverter
import com.service.shop.geo.*
import com.service.shop.mongo.ShopRepository
import com.service.shop.mongo.document.Address
import com.service.shop.mongo.document.Shop
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import spock.lang.Specification

class ShopControllerTest extends Specification {
    ShopController controller
    def shopRepositoryMock
    def geoServiceMock
    def geocodingAddressFormatterMock
    def shopToAndFromConverterMock

    public void setup(){
        geocodingAddressFormatterMock=Mock(GeocodingAddressFormatter)
        shopToAndFromConverterMock=Mock(ShopToAndFromConverter)
        geoServiceMock=Mock(GeoService)
        shopRepositoryMock=Mock(ShopRepository)
        controller = new ShopController(geocodingAddressFormatterMock,shopToAndFromConverterMock,geoServiceMock,shopRepositoryMock)

    }

    def "should add shop and return  ResponseEntity with 201 if shop already not exist with same name"() {
        given:
        def addressReq = new AddressReq(addressLine: "address line", number: 123, city: "city", state: "state", country: "india", postCode: "1234")
        def shopReq = new ShopReq(name: "shop 1", address: addressReq)
        def formattedAddress = "address line,city,state,india 1234"
        geocodingAddressFormatterMock.format(addressReq)>> formattedAddress
        def location = new Location(lat: 11, lng: 23)
        def results=[new Result(geometry: new Geometry(location: location))]
        def geoResponse=new GeoResponse(status: "OK",results: results)
        geoServiceMock.find(formattedAddress)>>geoResponse
        def point = new GeoJsonPoint(location.lng, location.lat)
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
        shopToAndFromConverterMock.convertToShop(shopReq,location.lng,location.lat)>>shop
        shopRepositoryMock.findAndModify(shop,shop.name) >>Optional.ofNullable(null)
        when:

        def entity = controller.addShop(shopReq)

        then:
        entity.statusCode.value() ==201

    }

    def "should return near shop from customer lat , lan"() {
        given:
        def lat=12.99
        def lan=34.11
        def point = new GeoJsonPoint(lan,lat)
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
        def shopRepoStub= Stub(ShopRepository)
        shopRepoStub.findByAddressPointNear(_) >> {GeoJsonPoint p-> return  shop}
        def controller= new ShopController(geocodingAddressFormatterMock,shopToAndFromConverterMock,geoServiceMock,shopRepoStub)

        when:
        def response=controller.findNear(lan,lat)

        then:
        response.statusCode.value() ==200
    }
    def "should return 404 if near shop does not exit from customer lat , lan"() {
        given:
        def lat=12.99
        def lan=34.11
        def shopRepoStub= Stub(ShopRepository)
        shopRepoStub.findByAddressPointNear(_) >> {GeoJsonPoint p-> return  null}
        def controller= new ShopController(geocodingAddressFormatterMock,shopToAndFromConverterMock,geoServiceMock,shopRepoStub)

        when:
        def response=controller.findNear(lan,lat)

        then:
        response.statusCode.value() ==404
    }
}
