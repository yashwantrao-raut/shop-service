package com.service.shop.conf;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
@Configuration
public class FongoConfig extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "shop-service";
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        return new Fongo("In Memory Mongo DB Fongo").getMongo();
    }
}
