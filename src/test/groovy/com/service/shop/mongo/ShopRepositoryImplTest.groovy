package com.service.shop.mongo

import com.mongodb.BasicDBObject
import com.service.shop.mongo.document.Address
import com.service.shop.mongo.document.Shop
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@DataMongoTest
class ShopRepositoryImplTest extends Specification {
    @Autowired
    ShopRepository repository;
    @Autowired
    MongoTemplate mongoTemplate;
    void setup() {
       mongoTemplate.getCollection("shops").remove(new BasicDBObject())
    }
    def "should insert new document if already is not present"() {
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
        repository.findAndModify(shop,shop.name);

        then:
        mongoTemplate.findAll(Shop).size()==1
    }

    def "should update already exisitng document and return old document for exisitng document"() {
        given:
        def lan = 11.22
        def lat = 22.33
        def point1 = new GeoJsonPoint(lan, lat)
        def address1 = new Address(
                addressLine: "address line",
                number: 123,
                city: "city",
                state: "state",
                country: "india",
                postCode: "1234",
                point: point1
        )
        def shop1 = new Shop(name: "shop1", address: address1)
        mongoTemplate.insert(shop1);
        def address2 = new Address(
                addressLine: "address line 2",
                number: 123,
                city: "city 2",
                state: "state 2",
                country: "india",
                postCode: "1234",
                point: point1
        )
        def shop2 = new Shop(name: "shop1", address: address2)
        when:
        def modified = repository.findAndModify(shop2, "shop1");

        then:
        def shops = mongoTemplate.findAll(Shop)
        shops.size()==1
        def shop = shops.first()
        shop.name == shop2.name
        shop.address.addressLine== address2.addressLine
        shop.address.state== address2.state
        shop.address.city== address2.city

        def oldShop = modified.get()
        oldShop.address.addressLine == address1.addressLine
        oldShop.address.city == address1.city
        oldShop.address.state == address1.state
    }

    def "should find nearest shop from given location "() {
        given:
        def lan1 = 73.95764789999998
        def lat1 = 18.4808374
        def point1 = new GeoJsonPoint(lan1, lat1)
        def address1 = new Address(
                addressLine: "green county, hadapsar",
                number: 123,
                city: "pune",
                state: "mh",
                country: "india",
                postCode: "1234",
                point: point1
        )
        def shop1 = new Shop(name: "shop 1", address: address1)
        mongoTemplate.insert(shop1);
        def lan2 = 73.92716440000004
        def lat2 = 18.5158057
        def point2 = new GeoJsonPoint(lan2, lat2)
        def address2 = new Address(
                addressLine: "Magarpatta City",
                number: 123,
                city: "pune",
                state: "mh",
                country: "india",
                postCode: "1234",
                point: point2
        )
        def shop2 = new Shop(name: "shop 2", address: address2)
        mongoTemplate.insert(shop2);
        def userlan=73.95075429999997
        def userlat=18.5514828
        def userPoint = new GeoJsonPoint(userlan, userlat)

        when:
        def shop = repository.findByAddressPointNear(userPoint);

        then:
        shop.name == shop2.name
        shop.address.addressLine == shop2.address.addressLine
        shop.address.point.coordinates == shop2.address.point.coordinates

    }
}
