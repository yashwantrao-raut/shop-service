package com.service.shop.mongo;

import com.service.shop.mongo.document.Shop;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ShopRepository extends MongoRepository<Shop,String>,FindAndModifiable<Shop,String> {
    Shop findByAddressPointNear(GeoJsonPoint p);
}
