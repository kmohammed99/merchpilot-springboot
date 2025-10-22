package com.merchpilot.merchpilot;

import org.springframework.boot.SpringApplication;

public class TestMerchpilotApplication {

	public static void main(String[] args) {
		SpringApplication.from(MerchpilotApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
