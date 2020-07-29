package com.aniekanudoessien.integration;

import com.aniekanudoessien.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteProductIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    PriceRepository repo;

    @Test
    public void setPriceData_Verify201_Created() throws Exception{


    }

}
