package com.service.shop.controller

import com.service.shop.controller.formatter.GeocodingAddressFormatter
import com.service.shop.controller.req.AddressReq
import com.service.shop.controller.req.ShopReq
import com.service.shop.converter.ShopToAndFromConverter
import com.service.shop.geo.GeoResponse
import com.service.shop.geo.GeoService
import com.service.shop.geo.Geometry
import com.service.shop.geo.Location
import com.service.shop.geo.Result
import com.service.shop.mongo.ShopRepository
import com.service.shop.mongo.document.Address
import com.service.shop.mongo.document.Shop
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.ResponseEntity
import spock.lang.Ignore
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

    def "should add shop and return 201 response if shop is already not present with that name"() {
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

    }
}
