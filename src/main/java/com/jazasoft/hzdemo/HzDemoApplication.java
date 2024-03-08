package com.jazasoft.hzdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;

@SpringBootApplication(exclude = HazelcastAutoConfiguration.class)
public class HzDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HzDemoApplication.class, args);
	}

}
