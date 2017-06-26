package com.service.shop.controller

import com.service.shop.controller.formatter.GeocodingAddressFormatter
import com.service.shop.controller.req.AddressReq
import com.service.shop.controller.req.ShopReq
import com.service.shop.converter.ShopToAndFromConverter
import com.service.shop.geo.GeoService
import com.service.shop.mongo.ShopRepository
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

    @Ignore
    def "should add shop"() {
        given:
        def addressReq = new AddressReq(addressLine: "address line", number: 123, city: "city", state: "state", country: "india", postCode: "1234")
        def shopReq = new ShopReq(name: "shop 1", address: addressReq)
        when:
        def entity = controller.addShop(shopReq)
        then:
        entity.statusCode ==201

    }

    def "should return near shop from customer lat , lan"() {

    }
}
