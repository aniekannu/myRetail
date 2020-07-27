package com.aniekanudoessien;

import com.aniekanudoessien.repository.PriceRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = PriceRepository.class)
public class MyRetailApplication {

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(MyRetailApplication.class, args);
	}
}
