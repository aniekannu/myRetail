package com.aniekanudoessien.integration;

import com.aniekanudoessien.model.PriceChange;
import com.aniekanudoessien.model.responseproduct.ProductInfo;
import com.aniekanudoessien.repository.PriceRepository;
import com.aniekanudoessien.util.MyRetailPath;
import com.aniekanudoessien.utils.TestResourceLoader;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateProductIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PriceRepository repo;

    @Test
    public void setPriceData_Verify201_Created() throws Exception{
        PriceChange mockCreatedProduct = TestResourceLoader
                .loadFromRelativePath("ValidProductPricePayload.json", new TypeReference<PriceChange>() {});
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PriceChange> entity = new HttpEntity<>(mockCreatedProduct, headers);
        ResponseEntity<ProductInfo> responseEntity = restTemplate.exchange(MyRetailPath.CREATE_RESOURCE_PRICES,
                HttpMethod.POST, entity, ProductInfo.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        repo.deleteByProductId(mockCreatedProduct.getProductId());
    }

    @Test
    public void setPriceData_Verify409_409ConflictWithExistingDocument() throws Exception{
        PriceChange mockCreatedProduct = TestResourceLoader
                .loadFromRelativePath("ValidPayloadConflict.json", new TypeReference<PriceChange>() {});

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PriceChange> entity = new HttpEntity<>(mockCreatedProduct, headers);
        ResponseEntity<ProductInfo> responseEntity = restTemplate.exchange(MyRetailPath.CREATE_RESOURCE_PRICES,
                HttpMethod.POST, entity, ProductInfo.class);

        ResponseEntity<ProductInfo> newResponseEntity = restTemplate.exchange(MyRetailPath.CREATE_RESOURCE_PRICES,
                HttpMethod.POST, entity, ProductInfo.class);
        Assert.assertEquals(HttpStatus.CONFLICT, newResponseEntity.getStatusCode());

        repo.deleteByProductId(mockCreatedProduct.getProductId());
    }

    @Test
    public void setPriceData_400InvalidProductId() throws Exception{
        PriceChange mockCreatedProduct = TestResourceLoader
                .loadFromRelativePath("ValidProductPricePayload.json", new TypeReference<PriceChange>() {});

        mockCreatedProduct.setProductId((long) (new Random().nextInt(100000000) + 10000000));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PriceChange> entity = new HttpEntity<>(mockCreatedProduct, headers);
        ResponseEntity<ProductInfo> responseEntity = restTemplate.exchange(MyRetailPath.CREATE_RESOURCE_PRICES,
                HttpMethod.POST, entity, ProductInfo.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}