package com.service.shop.controller.formatter

import com.service.shop.controller.req.AddressReq
import spock.lang.Specification


class GeocodingAddressFormatterTest extends Specification {
    GeocodingAddressFormatter formatter;
    void setup() {
        formatter= new GeocodingAddressFormatter()
    }

    def "should format given address in geocoding api needed parameter format"() {
        given:
        def addressReq=new AddressReq(addressLine: "address line",number: 123,city: "city",state: "state",country: "india",postCode: "1234")
        when:
        def result=formatter.format(addressReq)

        then:
        result=="address line,city,state,india 1234"
    }
}
