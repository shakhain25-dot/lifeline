package com.lifeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LifeLineDispatchApplication {

	public static void main(String[] args) {
		System.out.println("===========================================");
		System.out.println("🚑 LifeLine Dispatch System Starting...");
		System.out.println("===========================================");

		SpringApplication.run(LifeLineDispatchApplication.class, args);

		System.out.println("===========================================");
		System.out.println("✅ LifeLine System Started Successfully!");
		System.out.println("📍 Backend API: http://localhost:8080/api");
		System.out.println("📊 Health Check: http://localhost:8080/api/health");
		System.out.println("===========================================");
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
						.allowedOrigins("http://localhost:3000")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*")
						.allowCredentials(true)
						.maxAge(3600);
			}
		};
	}
}
