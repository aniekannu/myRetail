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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateProductIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PriceRepository repo;


    // FAILING
    @Test
    public void Verify200_ProductPriceUpdate() throws Exception{


        // acquire mock to create product
        PriceChange mockCreatedProduct = TestResourceLoader
                .loadFromRelativePath("ValidPayloadUpdate.json", new TypeReference<PriceChange>() {});
        repo.deleteByProductId(mockCreatedProduct.getProductId());


        // create new product and assert equals for status code 201
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PriceChange> entity = new HttpEntity<>(mockCreatedProduct, headers);
        ResponseEntity<ProductInfo> responseEntity = restTemplate.exchange(MyRetailPath.CREATE_RESOURCE_PRICES,
                HttpMethod.POST, entity, ProductInfo.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        // acquire mock to update product
        ProductInfo mockedUpdatedPayload = TestResourceLoader
                .loadFromRelativePath("ValidUpdatedPayloadForUpdate.json", new TypeReference<ProductInfo>() {});


        String url = MyRetailPath.MYRETAIL_BASE_PATH + "/products/" + mockedUpdatedPayload.getId();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductInfo> updatedEntity = new HttpEntity<>(mockedUpdatedPayload, headers);
        ResponseEntity<ProductInfo> updatedResponseEntity = restTemplate.exchange(url, HttpMethod.PUT,
                updatedEntity, ProductInfo.class);
        Assert.assertEquals(HttpStatus.OK, updatedResponseEntity.getStatusCode());

        repo.deleteByProductId(mockedUpdatedPayload.getId());
    }
}
