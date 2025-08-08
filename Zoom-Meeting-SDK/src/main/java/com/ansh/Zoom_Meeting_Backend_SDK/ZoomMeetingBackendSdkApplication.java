package com.ansh.Zoom_Meeting_Backend_SDK;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ZoomMeetingBackendSdkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZoomMeetingBackendSdkApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady(ApplicationReadyEvent event) {
		Environment env = event.getApplicationContext().getEnvironment();
		String port = env.getProperty("server.port", "4000");
		System.out.println("Zoom Meeting SDK Auth Endpoint Sample Spring Boot, listening on port " + port + "!");
	}
}
