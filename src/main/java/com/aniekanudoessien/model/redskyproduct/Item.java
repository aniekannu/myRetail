package com.aniekanudoessien.model.redskyproduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {

    @JsonProperty("tcin")
    private String productId;

    @JsonProperty("product_description")
    private ProductDescription productDescription;

    public Item(){}

    public Item(String productId, ProductDescription productDescription) {
        this.productId = productId;
        this.productDescription = productDescription;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ProductDescription getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(ProductDescription productDescription) {
        this.productDescription = productDescription;
    }
}
