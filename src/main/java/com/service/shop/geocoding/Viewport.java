
package com.service.shop.geocoding;

import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "northeast",
    "southwest"
})
public class Viewport {

    @JsonProperty("northeast")
    @Valid
    private Northeast northeast;
    @JsonProperty("southwest")
    @Valid
    private Southwest southwest;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("northeast")
    public Northeast getNortheast() {
        return northeast;
    }

    @JsonProperty("northeast")
    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    @JsonProperty("southwest")
    public Southwest getSouthwest() {
        return southwest;
    }

    @JsonProperty("southwest")
    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
