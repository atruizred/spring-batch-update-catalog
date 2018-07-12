package com.atruiz.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BatchConfiguration.class)
public class SpringBatchUpdateCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchUpdateCatalogApplication.class, args);
	}
}
