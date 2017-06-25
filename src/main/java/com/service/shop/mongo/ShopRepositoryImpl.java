package com.service.shop.mongo;

import com.service.shop.mongo.document.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class ShopRepositoryImpl implements  FindAndModifiable<Shop,String>{
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Optional<Shop> findAndModify(Shop shop, String name) {
        Query findByName = Query.query(where("name").is(name));
        Update updateAddress = new Update().set("address", shop.getAddress());
        FindAndModifyOptions modifyOptions = new FindAndModifyOptions().returnNew(false).upsert(true);
        Shop modifiedObject = mongoTemplate.findAndModify(findByName, updateAddress, modifyOptions,  Shop.class);
        return Optional.ofNullable(modifiedObject);
    }
}
