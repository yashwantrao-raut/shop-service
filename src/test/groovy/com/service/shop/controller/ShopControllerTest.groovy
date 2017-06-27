package com.service.shop.controller

import com.service.shop.controller.formatter.GeocodingAddressFormatter
import com.service.shop.controller.req.AddressReq
import com.service.shop.controller.req.ShopReq
import com.service.shop.controller.resp.AddressResp
import com.service.shop.controller.resp.GeoLocationResp
import com.service.shop.controller.resp.ShopResp
import com.service.shop.converter.ShopToAndFromConverter
import com.service.shop.geo.*
import com.service.shop.mongo.ShopRepository
import com.service.shop.mongo.document.Address
import com.service.shop.mongo.document.Shop
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class ShopControllerTest extends Specification {
    ShopController controller
    def shopRepositoryMock
    def geoServiceMock
    def geocodingAddressFormatterMock
    def shopToAndFromConverterMock
    def mockMvc


    public void setup(){
        geocodingAddressFormatterMock=Mock(GeocodingAddressFormatter)
        shopToAndFromConverterMock=Mock(ShopToAndFromConverter)
        geoServiceMock=Mock(GeoService)
        shopRepositoryMock=Mock(ShopRepository)
        controller = new ShopController(geocodingAddressFormatterMock,shopToAndFromConverterMock,geoServiceMock,shopRepositoryMock)
        mockMvc=MockMvcBuilders.standaloneSetup(controller).build()

    }

    def "should add shop and return  ResponseEntity with 201 if shop already not exist with same name"() {
        given:
        def addressReq = new AddressReq(addressLine: "address line", number: 123, city: "city", state: "state", country: "india", postCode: "1234")
        def shopReq = new ShopReq(name: "shop 1", address: addressReq)
        def formattedAddress = "address line,city,state,india 1234"
        geocodingAddressFormatterMock.format(_)>> formattedAddress
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
        shopToAndFromConverterMock.convertToShop(_,location.lng,location.lat)>>shop
        shopRepositoryMock.findAndModify(shop,shop.name) >>Optional.ofNullable(null)
        def reqBody= JsonOutput.toJson(shopReq)
        when:
        def response = mockMvc.perform(post('/shops').contentType(APPLICATION_JSON).content(reqBody)).andReturn().response


        then:
        response.status == CREATED.value()

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
        shopRepositoryMock.findByAddressPointNear(_) >> shop
        def geoLocationResp = new GeoLocationResp("point",lan,lat)
        def addressResp = new AddressResp(
                addressLine: "address line",
                number: 123,
                city: "city",
                state: "state",
                country: "india",
                postCode: "1234",
                location: geoLocationResp
        )
        def shopResp = new ShopResp(name: "shop 1", address: addressResp)
        shopToAndFromConverterMock.convertFromShop(shop)>>shopResp

        when:
        def response = mockMvc.perform(get('/shops').param("lan","12.99").param("lat","34.22")).andReturn().response

        then:
        response.status == OK.value()
        def content = new JsonSlurper().parseText(response.contentAsString)
        content.name == shop.name
        content.address.addressLine == shop.address.addressLine


    }
    def "should return 404 if near shop does not exit from customer lat , lan"() {
        given:
        shopRepositoryMock.findByAddressPointNear(_) >> null

        when:

        def response = mockMvc.perform(get('/shops').param("lan","12.99").param("lat","34.22")).andReturn().response

        then:
        response.status == NOT_FOUND.value()
    }
}
