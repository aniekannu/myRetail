package com.aniekanudoessien.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Config {

    // this field is linked to the Redsky
    // Product Catalog and it's loaded from
    // application.properties in src/main/resources
    @Value("${redsky.url}")
    private String redskyUrl;
}
