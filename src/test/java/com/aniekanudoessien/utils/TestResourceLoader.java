package com.aniekanudoessien.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class TestResourceLoader {

    private static final String BASE_TEST_PATH = "src/test/resources/";
    public static final ObjectMapper mapper = new ObjectMapper();

    public TestResourceLoader(){}

    public static <T> T loadFromRelativePath(String relativePath, TypeReference<T> typeReference) throws Exception{
        return mapper.readValue(new File(BASE_TEST_PATH + relativePath), typeReference);
    }
}
