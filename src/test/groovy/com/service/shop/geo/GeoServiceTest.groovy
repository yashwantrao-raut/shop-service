package com.service.shop.geo

import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class GeoServiceTest extends Specification {
    GeoService service
    def restTemplateMock=Mock(RestTemplate)
    def url="https://maps.googleapis.com/maps/api/geocode/json?address"
    void setup() {
        service = new GeoService(restTemplateMock,url)
    }

    def "should invoke geocoding api to find geo location"() {
        given:
        def formattedAddress="address line,city,state,india 1234"
        restTemplateMock

        when:
        def response = service.find(formattedAddress)

        then:

        1 * restTemplateMock.getForObject(url+formattedAddress,GeoResponse.class)
    }
}
