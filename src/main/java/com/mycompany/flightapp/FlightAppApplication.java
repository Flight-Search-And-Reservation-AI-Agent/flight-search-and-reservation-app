package com.mycompany.flightapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class FlightAppApplication {

	public static void main(String[] args) {
		String env = System.getenv("GAE_ENV"); // App Engine sets this env var when deployed
		if (env == null || !env.equals("standard")) {
			// Local dev: try to load .env, but don't fail if missing
			try {
				io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.configure()
						.ignoreIfMissing()
						.load();
				System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL", ""));
				System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME", ""));
				System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD", ""));
			} catch (Exception e) {
				// Log or ignore, as .env is optional in some environments
			}
		} else {
			// On App Engine
			System.setProperty("SPRING_DATASOURCE_URL", System.getenv("SPRING_DATASOURCE_URL"));
			System.setProperty("SPRING_DATASOURCE_USERNAME", System.getenv("SPRING_DATASOURCE_USERNAME"));
			System.setProperty("SPRING_DATASOURCE_PASSWORD", System.getenv("SPRING_DATASOURCE_PASSWORD"));
		}
		SpringApplication.run(FlightAppApplication.class, args);
	}
}
