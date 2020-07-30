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
public class GetProductIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PriceRepository repo;

    @Test
    public void Verify200StatusForGetEndpoint() throws Exception{
        PriceChange mockCreatedProduct = TestResourceLoader
                .loadFromRelativePath("ValidPayloadForGet.json", new TypeReference<PriceChange>() {});
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PriceChange> entity = new HttpEntity<>(mockCreatedProduct, headers);
        ResponseEntity<ProductInfo> responseEntity = restTemplate.exchange(MyRetailPath.CREATE_RESOURCE_PRICES,
                HttpMethod.POST, entity, ProductInfo.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        String getUrl = MyRetailPath.MYRETAIL_BASE_PATH + "/products/" + mockCreatedProduct.getProductId();
        ResponseEntity<ProductInfo> getResponseEntity = restTemplate.exchange(getUrl, HttpMethod.GET, null, ProductInfo.class);
        Assert.assertEquals(HttpStatus.OK, getResponseEntity.getStatusCode());

        repo.deleteByProductId(mockCreatedProduct.getProductId());
    }

}
