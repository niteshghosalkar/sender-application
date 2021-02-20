package com.sender.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sender.app.service.SenderService;

@SpringBootApplication
public class SenderApp implements CommandLineRunner{

	@Autowired
	SenderService senderService;

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(SenderApp.class);
		springApplication.setWebApplicationType(WebApplicationType.NONE);
		springApplication.run(args);

	}

	public void run(String... arg) throws Exception {
		senderService.process();
	}

}
