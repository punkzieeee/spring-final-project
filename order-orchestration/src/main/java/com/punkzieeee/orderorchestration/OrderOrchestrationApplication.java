package com.punkzieeee.orderorchestration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class OrderOrchestrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderOrchestrationApplication.class, args);
	}

}
