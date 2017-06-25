package com.service.shop.controller.formatter;

import com.service.shop.controller.req.AddressReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class GeocodingAddressFormatter {
    private static final String COMMA_SEPARATOR=",";
   public String format(AddressReq addressReq){
        StringBuilder builder = new StringBuilder();
        builder.append(addressReq.getAddressLine());
        builder.append(COMMA_SEPARATOR);
        builder.append(addressReq.getCity());
        if(StringUtils.isNotBlank(addressReq.getState())) {
            builder.append(COMMA_SEPARATOR);
            builder.append(addressReq.getState());
        }
        builder.append(COMMA_SEPARATOR);
        builder.append(addressReq.getCountry());
        builder.append(" ");
        builder.append(addressReq.getPostCode());
        return builder.toString();

    }
}
