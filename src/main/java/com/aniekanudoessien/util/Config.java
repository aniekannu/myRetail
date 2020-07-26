package com.aniekanudoessien.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Config {

    @Value("${redsky.url}")
    private String redskyUrl;
}
