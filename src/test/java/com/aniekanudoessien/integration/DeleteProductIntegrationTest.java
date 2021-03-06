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
public class DeleteProductIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void deleteProduct_Verify200_StatusCode() throws Exception{

        // create mock for new product
        PriceChange mockCreatedProduct = TestResourceLoader
                .loadFromRelativePath("ValidDeletePayload.json", new TypeReference<PriceChange>() {});
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PriceChange> entity = new HttpEntity<>(mockCreatedProduct, headers);
        ResponseEntity<ProductInfo> responseEntity = restTemplate.exchange(MyRetailPath.CREATE_RESOURCE_PRICES,
                HttpMethod.POST, entity, ProductInfo.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        // delete the product
        String deleteUrl = MyRetailPath.MYRETAIL_BASE_PATH + "/products/" + mockCreatedProduct.getProductId();
        ResponseEntity<String> deletedResponseEntity = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, String.class);
        Assert.assertNotNull(deletedResponseEntity.getBody());

        // attempt to fetch the product
        String getUrl = MyRetailPath.MYRETAIL_BASE_PATH + "/products/" + mockCreatedProduct.getProductId();
        ResponseEntity<ProductInfo> getResponseEntity = restTemplate.exchange(getUrl, HttpMethod.GET, null, ProductInfo.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, getResponseEntity.getStatusCode());
    }

}
