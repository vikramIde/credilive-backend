package com.hackathon.creditcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
/*********************************************************************************************
 * Credit card info app initializer												  *
 * 																					  	
 ********************************************************************************************/
@SpringBootApplication
public class CreditInfoAppInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CreditInfoAppInitializer.class);
	}

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(CreditInfoAppInitializer.class, args);
		context.getBean(ESBulkLoad.class).bulkLoad();
	}

}
