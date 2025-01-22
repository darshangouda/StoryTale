package com.storytale.storytaleAI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class StorytaleAiApplication {

	public static void main(String[] args) {
		// Load the .env file
		Dotenv dotenv = Dotenv.load();
		System.setProperty("OPENAI_KEY", dotenv.get("OPENAI_KEY"));

		SpringApplication.run(StorytaleAiApplication.class, args);
	}
}
