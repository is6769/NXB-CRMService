package org.example.crmservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CrmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmServiceApplication.class, args);
	}

}
