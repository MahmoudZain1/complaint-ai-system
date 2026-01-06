package com.ecommerce.complaints;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableRabbit
@EnableAspectJAutoProxy
public class ComplaintsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComplaintsApplication.class, args);
	}

}
